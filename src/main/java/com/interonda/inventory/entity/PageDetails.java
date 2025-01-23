package com.interonda.inventory.entity;

import java.util.function.IntFunction;

public class PageDetails {
    private int number;
    private int size;
    private int totalPages;
    private boolean hasPrevious;
    private boolean hasNext;
    private String previousPageUrl;
    private String nextPageUrl;
    private IntFunction<String> pageUrl;

    // Getters y setters

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getPreviousPageUrl() {
        return previousPageUrl;
    }

    public void setPreviousPageUrl(String previousPageUrl) {
        this.previousPageUrl = previousPageUrl;
    }

    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public IntFunction<String> getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(IntFunction<String> pageUrl) {
        this.pageUrl = pageUrl;
    }
}
