package io.codelex;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ChatDialogTest {

    @Test
    public void testGetMessage() {
        MessageTemplate mockTemplate = mock(MessageTemplate.class);
        when(mockTemplate.render(anyMap())).thenReturn("Mocked message");

        InputType inputType = InputType.TEXT;
        Consumer<UserResponse> responseHandler = mock(Consumer.class);

        ChatDialog dialog = new ChatDialog(mockTemplate, inputType, responseHandler, true);

        Map<String, String> context = new HashMap<>();
        String message = dialog.getMessage(context);

        verify(mockTemplate).render(context);
        assertEquals("Mocked message", message);
    }

    @Test
    public void testHandleResponse() {
        MessageTemplate mockTemplate = mock(MessageTemplate.class);
        InputType inputType = InputType.TEXT;
        Consumer<UserResponse> mockHandler = mock(Consumer.class);

        ChatDialog dialog = new ChatDialog(mockTemplate, inputType, mockHandler, true);

        UserResponse response = new UserResponse("Test response", "key");
        dialog.handleResponse(response);

        verify(mockHandler).accept(response);
        verify(mockTemplate).setPlaceholder("key", "Test response");
    }
}
