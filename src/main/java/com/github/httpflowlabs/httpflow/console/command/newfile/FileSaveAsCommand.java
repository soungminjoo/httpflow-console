package com.github.httpflowlabs.httpflow.console.command.newfile;

public class FileSaveAsCommand extends NewFileCommand {

    protected String getDialogTitle() {
        return "Save As...";
    }

    protected String getFileNameMsg() {
        return "[Step.1] Enter file name : ";
    }

    protected String getFileContents() {
        return session.getFileViewerPath().getFileContents();
    }

    protected boolean retryCurrentCommand() {
        return true;
    }

}
