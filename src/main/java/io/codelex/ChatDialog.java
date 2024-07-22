package io.codelex;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ChatDialog {
    private MessageTemplate template;
    private InputType inputType;
    private Consumer<UserResponse> responseHandler;
    private boolean awaitingResponse;
    private List<String> validChoices;

    public ChatDialog(MessageTemplate template, InputType inputType, Consumer<UserResponse> responseHandler, boolean awaitingResponse) {
        this.template = template;
        this.inputType = inputType;
        this.responseHandler = responseHandler;
        this.awaitingResponse = awaitingResponse;
    }

    public ChatDialog(MessageTemplate template, InputType inputType, Consumer<UserResponse> responseHandler, boolean awaitingResponse, List<String> validChoices) {
        this(template, inputType, responseHandler, awaitingResponse);
        this.validChoices = validChoices;
    }

    public String getMessage(Map<String, String> context) {
        return template.render(context);
    }

    public void handleResponse(UserResponse response) {
        responseHandler.accept(response);
        template.setPlaceholder(response.getPlaceholder(), response.getResponse());
    }

    public MessageTemplate getTemplate() {
        return template;
    }

    public InputType getInputType() {
        return inputType;
    }

    public boolean isAwaitingResponse() {
        return awaitingResponse;
    }

    public List<String> getValidChoices() {
        return validChoices;
    }
}
