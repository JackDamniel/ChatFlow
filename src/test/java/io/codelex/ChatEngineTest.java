package io.codelex;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatEngineTest {

    @Test
    public void testExample1() throws IOException {
        String simulatedUserInput = "John\nDoe\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ChatEngine engine = new ChatEngine(in, out);

        MessageTemplate template1 = new MessageTemplate("Hi there, I'm Jeff\nYour new best friend for finding great loan offers!\nFirst things first - let's get your account set up\nWhat is your first name?");
        Consumer<UserResponse> responseHandler1 = response -> {
            engine.getContext().put("firstName", response.getResponse());
        };
        ChatDialog dialog1 = new ChatDialog(template1, InputType.TEXT, responseHandler1, true);

        MessageTemplate template2 = new MessageTemplate("And what is your last name?");
        Consumer<UserResponse> responseHandler2 = response -> {
            engine.getContext().put("lastName", response.getResponse());
        };
        ChatDialog dialog2 = new ChatDialog(template2, InputType.TEXT, responseHandler2, true);

        MessageTemplate template3 = new MessageTemplate("Nice to meet you, ${firstName} ${lastName}!");
        ChatDialog dialog3 = new ChatDialog(template3, InputType.TEXT, response -> {
        }, false);

        engine.addDialog(dialog1);
        engine.addDialog(dialog2);
        engine.addDialog(dialog3);

        engine.start();

        String output = out.toString();
        assertTrue(output.contains("Hi there, I'm Jeff"));
        assertTrue(output.contains("What is your first name?"));
        assertTrue(output.contains("And what is your last name?"));
        assertTrue(output.contains("Nice to meet you, John Doe!"));

        Map<String, String> context = engine.getContext();
        assertEquals("John", context.get("firstName"));
        assertEquals("Doe", context.get("lastName"));
    }

    @Test
    public void testExample2() throws IOException {
        String simulatedUserInput = "john.doe@example.com\n30\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ChatEngine engine = new ChatEngine(in, out);
        engine.getContext().put("firstName", "John");

        MessageTemplate template1 = new MessageTemplate("${firstName}, what's your email address?");
        Consumer<UserResponse> responseHandler1 = response -> {
            engine.getContext().put("email", response.getResponse());
        };
        ChatDialog dialog1 = new ChatDialog(template1, InputType.TEXT, responseHandler1, true);

        MessageTemplate template2 = new MessageTemplate("Fantastic. We are 70% done with the setup!\nYour age is another important value for finding the best offers. Please enter your age:");
        Consumer<UserResponse> responseHandler2 = response -> {
            engine.getContext().put("age", response.getResponse());
        };
        ChatDialog dialog2 = new ChatDialog(template2, InputType.TEXT, responseHandler2, true);

        engine.addDialog(dialog1);
        engine.addDialog(dialog2);

        engine.start();

        String output = out.toString();
        assertTrue(output.contains("John, what's your email address?"));
        assertTrue(output.contains("Fantastic. We are 70% done with the setup!"));

        Map<String, String> context = engine.getContext();
        assertEquals("john.doe@example.com", context.get("email"));
        assertEquals("30", context.get("age"));
    }

    @Test
    public void testExample3() throws IOException {
        String simulatedUserInput = "Home\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ChatEngine engine = new ChatEngine(in, out);

        MessageTemplate template1 = new MessageTemplate("And what do you need the money for?\n<single-choice-input=['Home','Car','Holidays','Big Event']>");
        Consumer<UserResponse> responseHandler1 = response -> {
            engine.getContext().put("purpose", response.getResponse());
        };
        ChatDialog dialog1 = new ChatDialog(template1, InputType.SINGLE_CHOICE, responseHandler1, true);

        MessageTemplate template2 = new MessageTemplate("Nice, I already have some options for you.");
        ChatDialog dialog2 = new ChatDialog(template2, InputType.TEXT, response -> {
        }, false);

        engine.addDialog(dialog1);
        engine.addDialog(dialog2);

        engine.start();

        String output = out.toString();
        assertTrue(output.contains("And what do you need the money for?"));
        assertTrue(output.contains("Nice, I already have some options for you."));

        Map<String, String> context = engine.getContext();
        assertEquals("Home", context.get("purpose"));
    }

    @Test
    public void testMultiStepConversation() throws IOException {
        String simulatedUserInput = "John\nDoe\njohn.doe@example.com\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ChatEngine engine = new ChatEngine(in, out);

        MessageTemplate firstNameTemplate = new MessageTemplate("Hi there, what's your first name?");
        Consumer<UserResponse> responseHandler1 = response -> {
            engine.getContext().put("firstName", response.getResponse());
        };
        ChatDialog firstNameDialog = new ChatDialog(firstNameTemplate, InputType.TEXT, responseHandler1, true);

        MessageTemplate lastNameTemplate = new MessageTemplate("Great, now what's your last name?");
        Consumer<UserResponse> responseHandler2 = response -> {
            engine.getContext().put("lastName", response.getResponse());
        };
        ChatDialog lastNameDialog = new ChatDialog(lastNameTemplate, InputType.TEXT, responseHandler2, true);

        MessageTemplate emailTemplate = new MessageTemplate("Thanks! Finally, what's your email address?");
        Consumer<UserResponse> responseHandler3 = response -> {
            engine.getContext().put("email", response.getResponse());
        };
        ChatDialog emailDialog = new ChatDialog(emailTemplate, InputType.TEXT, responseHandler3, true);

        MessageTemplate finalTemplate = new MessageTemplate("Thank you for your input, ${firstName}");
        ChatDialog finalDialog = new ChatDialog(finalTemplate, InputType.TEXT, response -> {
        }, false);

        engine.addDialog(firstNameDialog);
        engine.addDialog(lastNameDialog);
        engine.addDialog(emailDialog);
        engine.addDialog(finalDialog);

        engine.start();

        String expectedOutput = "Hi there, what's your first name?\n" +
                "Great, now what's your last name?\n" +
                "Thanks! Finally, what's your email address?\n" +
                "Thank you for your input, John";

        String actualOutput = out.toString().trim();

        assertEquals(expectedOutput.replaceAll("\\s+", " "), actualOutput.replaceAll("\\s+", " "));
    }
    @Test
    public void testValidationSingleChoice() throws IOException {
        String simulatedUserInput = "Invalid\nCar\n";
        ByteArrayInputStream in = new ByteArrayInputStream(simulatedUserInput.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        ChatEngine engine = new ChatEngine(in, out);

        MessageTemplate template = new MessageTemplate("And what do you need the money for?\n<single-choice-input=['Home','Car','Holidays','Big Event']>");
        Consumer<UserResponse> responseHandler = response -> {
            engine.getContext().put("purpose", response.getResponse());
        };
        List<String> validChoices = Arrays.asList("Home", "Car", "Holidays", "Big Event");
        ChatDialog dialog = new ChatDialog(template, InputType.SINGLE_CHOICE, responseHandler, true, validChoices);

        engine.addDialog(dialog);

        engine.start();

        String output = out.toString();
        System.out.println(output);

        assertTrue(output.contains("Invalid choice, please select from: Home, Car, Holidays, Big Event"));
        assertTrue(output.contains("And what do you need the money for?"));

        Map<String, String> context = engine.getContext();
        assertEquals("Car", context.get("purpose"));
    }
}