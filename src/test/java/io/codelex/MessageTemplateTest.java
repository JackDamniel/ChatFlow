package io.codelex;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTemplateTest {

    @Test
    public void testRender() {
        String template = "Hello ${name}, your response was ${lastResponse}.";
        MessageTemplate messageTemplate = new MessageTemplate(template);

        Map<String, String> context = new HashMap<>();
        context.put("name", "Alice");
        context.put("lastResponse", "Yes");

        String renderedMessage = messageTemplate.render(context);

        assertEquals("Hello Alice, your response was Yes.", renderedMessage);
    }

    @Test
    public void testEmail(){
        String template = "Hello ${firstname} ${lastname}, is your email: ${email}.";
        MessageTemplate messageTemplate = new MessageTemplate(template);
        Map<String, String> context = new HashMap<>();
        context.put("firstname", "Alice");
        context.put("lastname", "Gabi");
        context.put("email", "Alice@Gabi.com");

        String renderedMessage = messageTemplate.render(context);

        assertEquals("Hello Alice Gabi, is your email: Alice@Gabi.com.", renderedMessage);

    }
}
