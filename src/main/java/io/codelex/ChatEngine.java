package io.codelex;

import java.util.*;

public class ChatEngine {
    private List<ChatDialog> dialogs = new ArrayList<>();
    private int currentDialogIndex = 0;
    private Scanner scanner = new Scanner(System.in);
    private Map<String, String> context = new HashMap<>();

    public void addDialog(ChatDialog dialog) {
        dialogs.add(dialog);
    }

    public void start() {
        while (currentDialogIndex < dialogs.size()) {
            ChatDialog dialog = dialogs.get(currentDialogIndex);
            System.out.println(dialog.getMessage(context));
            String userInput = scanner.nextLine();
            UserResponse response = new UserResponse(userInput);
            dialog.handleResponse(response);
            updateContext(dialog, response);
            currentDialogIndex++;
        }
    }

    private void updateContext(ChatDialog dialog, UserResponse response) {
        context.put("lastResponse", response.getResponse());
    }

    public Map<String, String> getContext() {
        return context;
    }
}

