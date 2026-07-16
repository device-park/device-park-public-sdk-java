package io.testinium.devicepark.model.common;

/**
 * Server tarafındaki nested {@code sorting.X} pagination/sorting parametrelerinin
 * paylaşılan modeli. Liste request'lerinde {@code sorting} alanı olarak yer alır.
 *
 * <p>Query string'e şöyle çevrilir:
 * {@code ?sorting.page=0&sorting.size=20&sorting.sortBy=ID&sorting.direction=DESC}</p>
 */
public class Sorting {

    private Integer page = 0;
    private Integer size = 20;
    private String sortBy = "ID";
    private SortDirection direction = SortDirection.DESC;

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }

    public SortDirection getDirection() { return direction; }
    public void setDirection(SortDirection direction) { this.direction = direction; }
}

