package io.codelex;

import java.util.Map;
import java.util.function.Consumer;

public class ChatDialog {
    private MessageTemplate messageTemplate;
    private InputType inputType;
    private Consumer<UserResponse> responseHandler;

    public ChatDialog(MessageTemplate messageTemplate, InputType inputType, Consumer<UserResponse> responseHandler) {
        this.messageTemplate = messageTemplate;
        this.inputType = inputType;
        this.responseHandler = responseHandler;
    }

    public String getMessage(Map<String, String> context) {
        return messageTemplate.render(context);
    }

    public InputType getInputType() {
        return inputType;
    }

    public void handleResponse(UserResponse response) {
        responseHandler.accept(response);
    }
}