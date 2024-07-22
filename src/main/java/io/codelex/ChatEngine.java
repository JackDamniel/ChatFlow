package io.codelex;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatEngine {
    private BufferedReader reader;
    private BufferedWriter writer;
    private List<ChatDialog> dialogs;
    private Map<String, String> context;

    public ChatEngine(InputStream in, OutputStream out) {
        this.reader = new BufferedReader(new InputStreamReader(in));
        this.writer = new BufferedWriter(new OutputStreamWriter(out));
        this.dialogs = new ArrayList<>();
        this.context = new HashMap<>();
    }

    public void addDialog(ChatDialog dialog) {
        dialogs.add(dialog);
    }

    public void start() throws IOException {
        for (ChatDialog dialog : dialogs) {
            MessageTemplate template = dialog.getTemplate();
            InputType inputType = dialog.getInputType();
            String message = template.render(context);

            writeOutput(message);

            if (dialog.isAwaitingResponse()) {
                String userInput = readInput();
                if (inputType == InputType.SINGLE_CHOICE) {
                    userInput = validateSingleChoice(dialog, userInput);
                }
                UserResponse response = new UserResponse(userInput, template.getPlaceholder());
                dialog.handleResponse(response);
            }
        }
    }

    private String validateSingleChoice(ChatDialog dialog, String userInput) throws IOException {
        List<String> validChoices = dialog.getValidChoices();
        while (validChoices != null && !validChoices.contains(userInput.trim())) {
            writeOutput("Invalid choice, please select from: " + String.join(", ", validChoices));
            userInput = readInput();
        }
        return userInput;
    }

    public void writeOutput(String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    public String readInput() throws IOException {
        return reader.readLine();
    }

    public Map<String, String> getContext() {
        return context;
    }
}
