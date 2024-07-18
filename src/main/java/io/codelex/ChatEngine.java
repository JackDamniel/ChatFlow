package io.codelex;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

public class ChatEngine {
    private List<ChatDialog> dialogs = new ArrayList<>();
    private Map<String, String> context = new HashMap<>();
    private Scanner scanner;
    private PrintStream out;

    public ChatEngine(InputStream in, OutputStream out) {
        this.scanner = new Scanner(in);
        this.out = new PrintStream(out);
    }

    public void addDialog(ChatDialog dialog) {
        dialogs.add(dialog);
    }

    public void start() {
        for (ChatDialog dialog : dialogs) {
            String message = dialog.getMessage(context);
            out.println(message);

            if (dialog.requiresInput()) {
                if (scanner.hasNextLine()) {
                    String userInput = scanner.nextLine().trim();
                    UserResponse response = new UserResponse(userInput, dialog.getInputType().toString());
                    dialog.handleResponse(response);
                    updateContext(response);
                } else {
                    System.err.println("No input available for dialog: " + message);
                    break;
                }
            }
        }
    }

    private void updateContext(UserResponse response) {
        context.put(response.getKey(), response.getResponse());
    }

    public Map<String, String> getContext() {
        return context;
    }
}
