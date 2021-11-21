package com.github.httpflowlabs.httpflow.console.command.deletefile;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleCommand;
import com.github.httpflowlabs.httpflow.console.command.navigator.NavigatorCommand;
import com.github.httpflowlabs.httpflow.console.command.viewer.FileViewerCommand;
import com.github.httpflowlabs.httpflow.console.io.UserInputReader;

public class DeleteFileCommand extends AbstractConsoleCommand {

    @Override
    public AbstractConsoleCommand execute() {
        consolePrinter.horizontalLine();
        consolePrinter.println("Confirm to delete file");
        consolePrinter.horizontalLine();
        consolePrinter.println("  - file : " + session.getFileViewerPath().getAbsolutePath());
        consolePrinter.horizontalLine();

        String answer = UserInputReader.INSTANCE.confirmYn("httpflow> Are you sure you want to delete this file?(Y/N) : ");
        if ("Y".equalsIgnoreCase(answer)) {
            session.getFileViewerPath().delete();
            session.setFileViewerPath(null);
        } else {
            return new FileViewerCommand();
        }

        return new NavigatorCommand();
    }

}
