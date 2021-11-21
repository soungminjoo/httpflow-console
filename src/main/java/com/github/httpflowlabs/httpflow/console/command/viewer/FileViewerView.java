package com.github.httpflowlabs.httpflow.console.command.viewer;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleView;
import com.github.httpflowlabs.httpflow.console.command.CommandInfo;

public class FileViewerView extends AbstractConsoleView {

    public void print(CommandInfo commandInfo) {
        consolePrinter.horizontalLine();
        consolePrinter.println("  File Viewer");
        consolePrinter.horizontalLine();
        consolePrinter.println("  " + session.getFileViewerPath().getAbsolutePath());
        consolePrinter.horizontalLine();
        printFileContents();
        consolePrinter.horizontalLine();
    }

    private void printFileContents() {
        // TODO Add paging
        String httpFlowStr = session.getFileViewerPath().getFileContents();
        consolePrinter.println(httpFlowStr);
    }

}
