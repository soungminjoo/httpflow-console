package com.github.httpflowlabs.httpflow.console.command.newfile;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleCommand;
import com.github.httpflowlabs.httpflow.console.io.SelectionDialog;

public class NewTypeChooseCommand extends AbstractConsoleCommand {

    @Override
    public AbstractConsoleCommand execute() {
        SelectionDialog dialog = new SelectionDialog("Resource Types", "D: New Folder", "F: New file", "W: New file wizard");
        String type = dialog.open("httpflow> Choose resource type : ");

        return handleUserInput(type);
    }

    public AbstractConsoleCommand handleUserInput(String type) {
        if (type.equalsIgnoreCase("F")) {
            return new NewFileCommand();
        } else if (type.equalsIgnoreCase("W")) {
            return new NewFileWizardCommand();
        } else {
            return new NewFolderCommand();
        }
    }

}
