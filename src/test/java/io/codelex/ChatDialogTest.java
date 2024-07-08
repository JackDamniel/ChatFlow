package io.codelex;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public class ChatDialogTest {

    @Test
    public void testGetMessage() {
        MessageTemplate mockTemplate = mock(MessageTemplate.class);
        when(mockTemplate.render(anyMap())).thenReturn("Mocked message");

        InputType inputType = InputType.TEXT;
        Consumer<UserResponse> responseHandler = mock(Consumer.class);

        ChatDialog dialog = new ChatDialog(mockTemplate, inputType, responseHandler);

        Map<String, String> context = new HashMap<>();
        String message = dialog.getMessage(context);

        verify(mockTemplate).render(context);
    }

    @Test
    public void testHandleResponse() {
        MessageTemplate mockTemplate = mock(MessageTemplate.class);
        InputType inputType = InputType.TEXT;

        Consumer<UserResponse> mockHandler = mock(Consumer.class);
        ChatDialog dialog = new ChatDialog(mockTemplate, inputType, mockHandler);

        UserResponse response = new UserResponse("Test response");
        dialog.handleResponse(response);

        verify(mockHandler).accept(response);
    }
}
