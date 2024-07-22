package io.codelex;

import java.util.Map;

public class MessageTemplate {
    private String template;
    private String placeholder;

    public MessageTemplate(String template) {
        this.template = template;
        this.placeholder = extractPlaceholder(template);
    }

    public String render(Map<String, String> context) {
        String rendered = template;
        for (Map.Entry<String, String> entry : context.entrySet()) {
            rendered = rendered.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return rendered;
    }

    public void setPlaceholder(String key, String value) {
        template = template.replace("${" + key + "}", value);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    private String extractPlaceholder(String template) {
        int start = template.indexOf("${");
        int end = template.indexOf("}", start);
        if (start != -1 && end != -1) {
            return template.substring(start + 2, end);
        }
        return null;
    }
}
