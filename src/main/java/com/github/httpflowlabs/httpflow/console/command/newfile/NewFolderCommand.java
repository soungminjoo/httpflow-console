package com.github.httpflowlabs.httpflow.console.command.newfile;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleCommand;
import com.github.httpflowlabs.httpflow.console.command.navigator.NavigatorCommand;
import com.github.httpflowlabs.httpflow.console.io.UserInputReader;

public class NewFolderCommand extends AbstractConsoleCommand {

    @Override
    public NavigatorCommand execute() {
        consolePrinter.horizontalLine();
        consolePrinter.println("New folder dialog");
        consolePrinter.horizontalLine();
        consolePrinter.print("httpflow> Enter folder name : ");

        String name = UserInputReader.INSTANCE.readLine().trim();
        if (!"".equalsIgnoreCase(name)) {
            if (session.getNavigatorPath().isValidFileName(name)) {
                session.getNavigatorPath().mkdirs(name);
            } else {
                UserInputReader.INSTANCE.printMessageAndWait("httpflow> Invalid folder name.");
            }
        } else {
            UserInputReader.INSTANCE.printMessageAndWait("httpflow> Canceled.");
        }
        return new NavigatorCommand();
    }

}
