package io.codelex;

public class UserResponse {
    private String response;
    private String key;

    public UserResponse(String response, String key) {
        this.response = response;
        this.key = key;
    }

    public String getResponse() {
        return response;
    }

    public String getKey() {
        return key;
    }
}

