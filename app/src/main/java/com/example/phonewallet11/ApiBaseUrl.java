package com.example.phonewallet11;

public class ApiBaseUrl {
    private final String baseUrl = "http://192.168.31.171:8081/bank/api/";

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public String assemblyUrl(String path) {
        // 这里传递的string，前面不能有/
        return this.baseUrl + path;
    }
}
