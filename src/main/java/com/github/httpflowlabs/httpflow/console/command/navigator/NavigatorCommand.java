package com.github.httpflowlabs.httpflow.console.command.navigator;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleCommand;
import com.github.httpflowlabs.httpflow.console.command.CommandInfo;
import com.github.httpflowlabs.httpflow.console.command.deletefile.DeleteFolderCommand;
import com.github.httpflowlabs.httpflow.console.command.newfile.NewTypeChooseCommand;
import com.github.httpflowlabs.httpflow.console.command.run.RunAllFilesInFolderCommand;
import com.github.httpflowlabs.httpflow.console.command.viewer.FileViewerCommand;
import com.github.httpflowlabs.httpflow.console.storage.FileWrapper;
import com.github.httpflowlabs.httpflow.console.io.UserInputReader;
import com.github.httpflowlabs.httpflow.console.storage.LogicalFolderWrapper;
import com.github.httpflowlabs.httpflow.console.storage.PhysicalDiskFolderWrapper;
import com.github.httpflowlabs.httpflow.console.storage.logical.LogicalFile;
import com.github.httpflowlabs.httpflow.console.storage.logical.LogicalFolder;

import java.io.File;

public class NavigatorCommand extends AbstractConsoleCommand {

    private static final String QUIT = "Quit";
    private static final String RUN = "Run";

    private NavigatorView navigatorView = new NavigatorView();

    @Override
    public AbstractConsoleCommand execute() {
        CommandInfo commandInfo = new CommandInfo();
        navigatorView.print(commandInfo);

        String infoMsg = "httpflow> Choose number of file to open (or " + QUIT + " / " + RUN + ") : ";
        commandInfo.addExtraKeys("Q", QUIT, "R", RUN, "ND", "NF", "NW", "");

        String input = UserInputReader.INSTANCE.chooseNumber(infoMsg, commandInfo).trim();
        return handleUserInput(input, commandInfo);
    }

    private AbstractConsoleCommand handleUserInput(String input, CommandInfo commandInfo) {
        if (input.equalsIgnoreCase("Q") || input.equalsIgnoreCase(QUIT)) {
            return null;
        }
        if (input.equalsIgnoreCase("R") || input.equalsIgnoreCase(RUN)) {
            return new RunAllFilesInFolderCommand();
        }
        if (input.equals("")) {
            return new NavigatorCommand();
        }

        if ("N".equalsIgnoreCase(input)) {
            return new NewTypeChooseCommand();

        } else if ("ND".equalsIgnoreCase(input) || "NF".equalsIgnoreCase(input) || "NW".equalsIgnoreCase(input)) {
            return new NewTypeChooseCommand().handleUserInput(input.substring(1));

        } else if ("D".equalsIgnoreCase(input)) {
            return new DeleteFolderCommand();

        } else if ("P".equalsIgnoreCase(input)) {
            goBackToParentFolder();

        } else {
            Object indexedCommand = commandInfo.getIndexedCommandMap().get(Integer.parseInt(input));
            return onIndexedCommandSelected(indexedCommand);
        }

        return new NavigatorCommand();
    }

    private void goBackToParentFolder() {
        session.setNavigatorPath(session.getNavigatorPath().getParent());
    }

    private AbstractConsoleCommand onIndexedCommandSelected(Object indexedCommand) {
        if (isFileType(indexedCommand)) {
            session.setFileViewerPath(new FileWrapper(indexedCommand));
            return new FileViewerCommand();
        }

        if (indexedCommand instanceof File) {
            session.setNavigatorPath(new PhysicalDiskFolderWrapper((File) indexedCommand));

        } else if (indexedCommand instanceof LogicalFolder) {
            session.setNavigatorPath(new LogicalFolderWrapper((LogicalFolder) indexedCommand));
        }
        return new NavigatorCommand();
    }

    private boolean isFileType(Object indexedCommand) {
        if (indexedCommand instanceof LogicalFile) {
            return true;
        } else if (indexedCommand instanceof LogicalFolder) {
            return false;
        }

        File file = (File) indexedCommand;
        return file.isFile();
    }

}
