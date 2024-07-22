package io.codelex;

public class UserResponse {
    private String response;
    private String placeholder;

    public UserResponse(String response, String placeholder) {
        this.response = response;
        this.placeholder = placeholder;
    }

    public String getResponse() {
        return response;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

