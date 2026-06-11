package com.devicepark.sdk.core.http;

import com.devicepark.sdk.auth.DeviceParkAuthClient;
import com.devicepark.sdk.auth.exception.AuthenticationException;
import org.apache.hc.client5.http.classic.ExecChain;
import org.apache.hc.client5.http.classic.ExecChainHandler;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

/**
 * Apache execution-chain handler that injects bearer auth and performs one refresh-on-401 retry.
 */
public final class AuthRefreshExecChainHandler implements ExecChainHandler {

    private final DeviceParkAuthClient authClient;

    public AuthRefreshExecChainHandler(DeviceParkAuthClient authClient) {
        this.authClient = authClient;
    }

    @Override
    public ClassicHttpResponse execute(ClassicHttpRequest request,
                                       ExecChain.Scope scope,
                                       ExecChain chain) throws IOException, HttpException {
        request.setHeader("Authorization", authClient.authorizationHeader());
        ClassicHttpResponse response = chain.proceed(request, scope);

        if (response.getCode() != 401) {
            return response;
        }

        EntityUtils.consumeQuietly(response.getEntity());
        response.close();

        authClient.refreshToken();
        request.setHeader("Authorization", authClient.authorizationHeader());

        ClassicHttpResponse retried = chain.proceed(request, scope);
        if (retried.getCode() == 401) {
            EntityUtils.consumeQuietly(retried.getEntity());
            retried.close();
            throw new AuthenticationException("Received HTTP 401 after token refresh retry");
        }
        return retried;
    }
}

