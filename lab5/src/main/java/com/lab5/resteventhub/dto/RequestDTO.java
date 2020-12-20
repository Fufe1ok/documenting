package com.lab5.resteventhub.dto;

public class RequestDTO {

    private String url;

    private String strategy;

    public RequestDTO(String url, String strategy) {
        this.url = url;
        this.strategy = strategy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }
}