package com.github.httpflowlabs.httpflow.console.io;

import com.github.httpflowlabs.httpflow.console.command.CommandInfo;
import com.github.httpflowlabs.httpflow.console.exception.SilentStopSignalException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserInputReader {

    public static final UserInputReader INSTANCE = new UserInputReader();

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private UserInputReader() {

    }

    public String readLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new SilentStopSignalException(e);
        }
    }

    public String readLineWithMultiLineOption() {
        StringBuilder builder = new StringBuilder();
        String input = readLine();

        if ("MULTILINE\"".equalsIgnoreCase(input.trim()) || "\"\"\"".equals(input.trim())) {
            ConsolePrinter.INSTANCE.println("httpflow> Multi-line mode started. Last line must be \"MULTILINE or \"\"\"");
            while (true) {
                String line = readLine();
                if ("\"MULTILINE".equalsIgnoreCase(line) || "\"\"\"".equals(line)) {
                    break;
                }
                builder.append(line).append("\n");
            }
        } else {
            builder.append(input).append("\n");
        }

        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public String readMultiLine() {
        String endCommand = "EOF";
        StringBuilder builder = new StringBuilder();
        StringBuilder line = new StringBuilder();

        while (true) {
            char ch = readOneChar();
            if (ch == '\n') {
                if (endCommand.equalsIgnoreCase(line.toString().trim())) {
                    break;
                }

                builder.append(line.toString()).append("\n");
                line = new StringBuilder();
            } else {
                line.append(ch);
            }
        }

        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }

    private char readOneChar() {
        try {
            return (char) System.in.read();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public String chooseCommand(String infoMsg, String[] commands) {
        return chooseCommand(infoMsg, commands, null);
    }

    public String chooseCommand(String infoMsg, String[] commands, Set<String> compositionCommands) {
        while (true) {
            ConsolePrinter.INSTANCE.print(infoMsg);
            for (String command : commands) {
                if (compositionCommands == null || !compositionCommands.contains(command)) {
                    ConsolePrinter.INSTANCE.print("[" + command + "] ");
                }
            }
            ConsolePrinter.INSTANCE.print(": ");
            String line = readLine().trim();

            if (compositionCommands != null && compositionCommands.contains(line)) {
                return line;
            }

            List<Predicate<String>> predicates = new ArrayList<>();
            predicates.add(command -> line.equalsIgnoreCase(command));
            predicates.add(command -> line.equalsIgnoreCase(command.substring(0, 1)));
            predicates.add(command -> !line.equals("") && command.toLowerCase().contains(line.toLowerCase()));

            for (Predicate<String> predicate : predicates) {
                List<String> matched = Stream.of(commands).filter(predicate).collect(Collectors.toList());
                if (matched.size() == 1) {
                    return matched.get(0);

                } else if (matched.size() > 1) {
                    for (String command : matched) {
                        ConsolePrinter.INSTANCE.println(command);
                    }
                    break;
                }
            }
        }
    }

    public String chooseNumber(String infoMsg, CommandInfo commandInfo) {
        while (true) {
            ConsolePrinter.INSTANCE.print(infoMsg);
            String line = readLine().trim();

            if (line.matches("\\d+")) {
                int number = Integer.parseInt(line);
                if (number >= 1 && number <= commandInfo.getMaxNumber()) {
                    return line;
                }
            }

            for (String extraKey : commandInfo.getExtraKeys()) {
                if (line.equalsIgnoreCase(extraKey)) {
                    return extraKey;
                }
            }
        }
    }

    public String confirmYn(String infoMsg, String... additionalList) {
        while (true) {
            ConsolePrinter.INSTANCE.print(infoMsg);
            String line = readLine().trim();

            if (line.equalsIgnoreCase("Y") || line.equalsIgnoreCase("N")) {
                return line.toUpperCase();
            }
            for (String additional : additionalList) {
                if (additional.equalsIgnoreCase(line)) {
                    return additional;
                }
            }
        }
    }

    public void printMessageAndWait(String msg) {
        ConsolePrinter.INSTANCE.print(msg + " Please press enter key...");
        UserInputReader.INSTANCE.readLine();
    }

}
