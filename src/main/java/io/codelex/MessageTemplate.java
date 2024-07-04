package io.codelex;

import java.util.Map;

public class MessageTemplate {
    private String template;

    public MessageTemplate(String template) {
        this.template = template;
    }

    public String render(Map<String, String> context) {
        String rendered = template;
        for (Map.Entry<String, String> entry : context.entrySet()) {
            rendered = rendered.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return rendered;
    }
}