package com.github.httpflowlabs.httpflow.console.command.deletefile;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleCommand;
import com.github.httpflowlabs.httpflow.console.command.navigator.NavigatorCommand;
import com.github.httpflowlabs.httpflow.console.io.UserInputReader;
import com.github.httpflowlabs.httpflow.console.storage.FolderWrapper;

public class DeleteFolderCommand extends AbstractConsoleCommand {

    @Override
    public AbstractConsoleCommand execute() {
        consolePrinter.horizontalLine();
        consolePrinter.println("Confirm to delete folder");
        consolePrinter.horizontalLine();
        consolePrinter.println("  - folder : " + session.getNavigatorPath().getFolderName());
        consolePrinter.println("    (All sub-folders will be deleted.)");
        consolePrinter.horizontalLine();

        String answer = UserInputReader.INSTANCE.confirmYn("httpflow> Are you sure you want to delete this folder?(Y/N) : ");
        if ("Y".equalsIgnoreCase(answer)) {
            FolderWrapper parent = session.getNavigatorPath().getParent();
            session.getNavigatorPath().delete();
            session.setNavigatorPath(parent);
        }

        return new NavigatorCommand();
    }

}
