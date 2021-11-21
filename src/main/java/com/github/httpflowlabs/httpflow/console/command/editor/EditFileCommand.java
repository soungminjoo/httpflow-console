package com.github.httpflowlabs.httpflow.console.command.editor;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleCommand;
import com.github.httpflowlabs.httpflow.console.command.CommandInfo;
import com.github.httpflowlabs.httpflow.console.command.viewer.FileViewerCommand;
import com.github.httpflowlabs.httpflow.console.io.UserInputReader;
import com.github.httpflowlabs.httpflow.console.utils.HttpFlowConsoleUtils;
import com.github.httpflowlabs.httpflow.support.HttpFlowUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EditFileCommand extends AbstractConsoleCommand {

    private static final String EDIT = "Edit line";
    private static final String DELETE = "Delete line";
    private static final String INSERT = "Insert line";
    private static final String SAVE = "Save";
    private static final String QUIT = "Quit";
    private static final String SAVE_QUIT = "SQ";

    private EditFileView editFileView = new EditFileView();

    @Override
    public AbstractConsoleCommand execute() {
        String[] lines = HttpFlowUtils.splithttpFlowContents(session.getFileViewerPath().getFileContents());
        List<String> lineList = Stream.of(lines).collect(Collectors.toList());

        while (true) {
            editFileView.print(lineList);

            String[] commands = new String[]{EDIT, DELETE, INSERT, SAVE, QUIT, SAVE_QUIT};
            Set<String> compositionCommands = new HashSet<>();
            compositionCommands.add(SAVE_QUIT);
            String input = UserInputReader.INSTANCE.chooseCommand("httpflow> Choose command ", commands, compositionCommands);
            if (QUIT.equalsIgnoreCase(input)) {
                break;
            }

            processEditorCommand(lineList, input);
            if (SAVE_QUIT.equalsIgnoreCase(input)) {
                break;
            }
        }

        return new FileViewerCommand();
    }

    private void processEditorCommand(List<String> lineList, String input) {
        if (SAVE.equalsIgnoreCase(input) || SAVE_QUIT.equalsIgnoreCase(input)) {
            String contents = lineList.stream().reduce((l1, l2) -> l1 + "\n" + l2).get();
            session.getFileViewerPath().saveFileContents(contents);
            return;
        }

        int index = chooseLineNumber(lineList, input) - 1;

        if (EDIT.equalsIgnoreCase(input)) {
            consolePrinter.print("httpflow> Enter text to replace (current text is copied to clipboard) : ");
            HttpFlowConsoleUtils.copyToClipboard(lineList.get(index));
            lineList.set(index, UserInputReader.INSTANCE.readLine());
        }

        if (DELETE.equalsIgnoreCase(input)) {
            lineList.remove(index);
        }

        if (INSERT.equalsIgnoreCase(input)) {
            consolePrinter.print("httpflow> Enter text to insert in new line : ");
            lineList.add(index, UserInputReader.INSTANCE.readLine());
        }
    }

    private int chooseLineNumber(List<String> lineList, String input) {
        CommandInfo commandInfo = new CommandInfo();
        commandInfo.setMaxNumber(lineList.size());

        String infoMsg = "httpflow> Choose number to " + input.toLowerCase() + " : ";
        String numberStr = UserInputReader.INSTANCE.chooseNumber(infoMsg, commandInfo);
        return Integer.parseInt(numberStr);
    }

}
