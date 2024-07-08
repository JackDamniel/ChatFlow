package io.codelex;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatEngineTest {

    @Test
    public void testStart() {
        String simulatedUserInput = "Test\n";
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        ChatDialog mockDialog = new ChatDialog(
                new MessageTemplate("Enter your response: ${lastResponse}"),
                InputType.TEXT,
                response -> {
                });

        ChatEngine chatEngine = new ChatEngine();
        chatEngine.addDialog(mockDialog);
        chatEngine.start();

        assertEquals("Test", chatEngine.getContext().get("lastResponse"));
    }
}
