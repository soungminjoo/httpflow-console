package com.github.httpflowlabs.httpflow.console.command.run;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleView;
import com.github.httpflowlabs.httpflow.console.storage.FileWrapper;

import java.util.List;

public class RunAllFilesInFolderView extends AbstractConsoleView {

    public void printStart(List<FileWrapper> files) {
        consolePrinter.horizontalLine();
        consolePrinter.println("  Run current folder [ " + session.getNavigatorPath().getFolderName() + " ]");
        consolePrinter.horizontalLine();

        for (FileWrapper f : files) {
            consolePrinter.println("    " + f.getFileName());
        }
        consolePrinter.horizontalLine();
    }

    public void printResponse(FileWrapper file, ResponseListener listener) {
        consolePrinter.horizontalLine();
        consolePrinter.println("  " + file.getFileName() + " [ " + listener.getUrl() + " ]");
        consolePrinter.horizontalLine();
        consolePrinter.println("  Response status: " + listener.getCode() + " " + listener.getReasonPhrase());
        consolePrinter.horizontalLine();
        consolePrinter.println(listener.getBody());
        consolePrinter.horizontalLine();
    }

}
