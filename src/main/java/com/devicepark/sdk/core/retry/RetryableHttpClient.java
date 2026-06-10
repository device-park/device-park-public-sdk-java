package com.devicepark.sdk.core.retry;

import com.devicepark.sdk.core.exception.SdkClientException;
import com.devicepark.sdk.core.http.SdkHttpClient;
import com.devicepark.sdk.core.http.SdkHttpRequest;
import com.devicepark.sdk.core.http.SdkHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Bir {@link SdkHttpClient}'ı saran ve retry'lı çalıştıran decorator.
 *
 * <p>Idempotent veya sunucu hatalarında tekrar denenebilir istekler için kullanılır.
 * Backoff: {@code min(baseDelay * 2^(attempt-1), maxDelay)} + jitter.</p>
 */
public final class RetryableHttpClient implements SdkHttpClient {

    private static final Logger LOG = LoggerFactory.getLogger(RetryableHttpClient.class);

    private final SdkHttpClient delegate;
    private final RetryPolicy policy;
    private final Random random;

    public RetryableHttpClient(SdkHttpClient delegate, RetryPolicy policy) {
        this(delegate, policy, new SecureRandom());
    }

    RetryableHttpClient(SdkHttpClient delegate, RetryPolicy policy, Random random) {
        this.delegate = delegate;
        this.policy = policy;
        this.random = random;
    }

    @Override
    public SdkHttpResponse execute(SdkHttpRequest request) {
        SdkClientException lastException = null;
        SdkHttpResponse lastResponse = null;

        for (int attempt = 1; attempt <= policy.maxAttempts(); attempt++) {
            try {
                SdkHttpResponse response = delegate.execute(request);
                if (!policy.isRetryableStatus(response.statusCode())) {
                    return response;
                }
                lastResponse = response;
                LOG.debug("Status {} retry'lanabilir (deneme {}/{})",
                        response.statusCode(), attempt, policy.maxAttempts());
            } catch (SdkClientException e) {
                lastException = e;
                LOG.debug("İstek hatası (deneme {}/{}): {}", attempt, policy.maxAttempts(), e.getMessage());
            }

            if (attempt < policy.maxAttempts()) {
                sleepBackoff(attempt);
            }
        }

        if (lastResponse != null) return lastResponse;
        throw lastException != null ? lastException
                : new SdkClientException("Retry sonrası başarılı yanıt alınamadı");
    }

    private void sleepBackoff(int attempt) {
        long base = policy.baseDelay().toMillis();
        long max = policy.maxDelay().toMillis();
        long exp = (long) (base * Math.pow(2, attempt - 1));
        long capped = Math.min(exp, max);
        // Java 8 uyumlu: Random.nextLong(bound) JDK 17+ olduğu için modulo ile jitter üret.
        long jitter = capped > 0 ? Math.abs(random.nextLong()) % capped : 0L;
        try {
            Thread.sleep(jitter);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SdkClientException("Retry sırasında thread interrupt oldu", e);
        }
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}

