package com.github.httpflowlabs.httpflow.console.command.viewer;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleCommand;
import com.github.httpflowlabs.httpflow.console.command.CommandInfo;
import com.github.httpflowlabs.httpflow.console.command.deletefile.DeleteFileCommand;
import com.github.httpflowlabs.httpflow.console.command.editor.EditFileCommand;
import com.github.httpflowlabs.httpflow.console.command.navigator.NavigatorCommand;
import com.github.httpflowlabs.httpflow.console.command.newfile.FileSaveAsCommand;
import com.github.httpflowlabs.httpflow.console.command.run.RunHttpFlowCommand;
import com.github.httpflowlabs.httpflow.console.io.UserInputReader;

public class FileViewerCommand extends AbstractConsoleCommand {

    private static final String RUN = "Run";
    private static final String EDIT = "Edit";
    private static final String DELETE = "Delete";
    private static final String SAVE_AS = "Save As";
    private static final String CLOSE = "Close";

    private FileViewerView fileViewerView = new FileViewerView();

    @Override
    public AbstractConsoleCommand execute() {
        CommandInfo commandInfo = new CommandInfo();
        fileViewerView.print(commandInfo);

        while (true) {
            String[] commands = new String[]{RUN, EDIT, DELETE, SAVE_AS, CLOSE};
            String infoMsg = "httpflow> Choose command ";

            String input = UserInputReader.INSTANCE.chooseCommand(infoMsg, commands);
            if (CLOSE.equalsIgnoreCase(input)) {
                break;
            }

            AbstractConsoleCommand command = resolveCommand(input);
            if (command != null) {
                return command;
            }
        }
        return new NavigatorCommand();
    }

    private AbstractConsoleCommand resolveCommand(String input) {
        if (EDIT.equalsIgnoreCase(input)) {
            return new EditFileCommand();
        }
        if (DELETE.equalsIgnoreCase(input)) {
            return new DeleteFileCommand();
        }
        if (RUN.equalsIgnoreCase(input)) {
            return new RunHttpFlowCommand();
        }
        if (SAVE_AS.equalsIgnoreCase(input)) {
            return new FileSaveAsCommand();
        }
        return null;
    }

}
