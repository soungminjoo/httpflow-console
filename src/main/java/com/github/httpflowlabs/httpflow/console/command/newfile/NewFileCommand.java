package com.github.httpflowlabs.httpflow.console.command.newfile;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleCommand;
import com.github.httpflowlabs.httpflow.console.command.navigator.NavigatorCommand;
import com.github.httpflowlabs.httpflow.console.command.viewer.FileViewerCommand;
import com.github.httpflowlabs.httpflow.console.storage.FileWrapper;
import com.github.httpflowlabs.httpflow.console.exception.UserCancelException;
import com.github.httpflowlabs.httpflow.console.io.UserInputReader;

public class NewFileCommand extends AbstractConsoleCommand {

    @Override
    public AbstractConsoleCommand execute() {
        consolePrinter.horizontalLine();
        consolePrinter.println(getDialogTitle());
        consolePrinter.horizontalLine();
        consolePrinter.print(getFileNameMsg());

        String name = UserInputReader.INSTANCE.readLine().trim();
        if (!name.toLowerCase().endsWith(".hfd")) {
            name += ".hfd";
        }

        if (session.getNavigatorPath().existsFile(name)) {
            UserInputReader.INSTANCE.printMessageAndWait("ERROR : Already existing file name.");
            if (retryCurrentCommand()) {
                return this;
            }

        } else {
            if (!"".equals(name)) {
                try {
                    String contents = getFileContents();
                    FileWrapper createdFile = session.getNavigatorPath().createFile(name, contents);
                    session.setFileViewerPath(createdFile);
                    return new FileViewerCommand();

                } catch (UserCancelException e) {
                }
            }
            UserInputReader.INSTANCE.printMessageAndWait("httpflow> Canceled.");
        }

        return new NavigatorCommand();
    }

    protected boolean retryCurrentCommand() {
        return false;
    }

    protected String getDialogTitle() {
        return "New file dialog";
    }

    protected String getFileNameMsg() {
        return "httpflow> Enter file name : ";
    }

    protected String getFileContents() {
        consolePrinter.println("httpflow> Enter http flow definition below. (Last line must be 'EOF' command)");
        return UserInputReader.INSTANCE.readMultiLine();
    }

}
