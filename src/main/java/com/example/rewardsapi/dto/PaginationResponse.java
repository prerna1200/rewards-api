package com.example.rewardsapi.dto;

import java.util.List;

/**
 * Generic response class used for paginated data.
 * It wraps list of data along with page details.
 */
public class PaginationResponse<T> {

    private List<T> data;
    private int currentPage;
    private int totalPages;
    private long totalElements;

    // default constructor
    public PaginationResponse() {}

    /**
     * creates paginated response
     *
     * @param data list of records
     * @param currentPage current page number
     * @param totalPages total number of pages
     * @param totalElements total number of records
     */
    public PaginationResponse(List<T> data, int currentPage, int totalPages, long totalElements) {
        this.data = data;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }

    // returns list of data
    public List<T> getData() { return data; }

    // returns current page number
    public int getCurrentPage() { return currentPage; }

    // returns total pages
    public int getTotalPages() { return totalPages; }

    // returns total number of records
    public long getTotalElements() { return totalElements; }
}
