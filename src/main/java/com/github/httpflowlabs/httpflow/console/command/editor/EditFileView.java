package com.github.httpflowlabs.httpflow.console.command.editor;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleView;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class EditFileView extends AbstractConsoleView {

    public void print(List<String> lineList) {
        consolePrinter.horizontalLine();
        consolePrinter.println("  File Editor");
        consolePrinter.horizontalLine();
        consolePrinter.println("  " + session.getFileViewerPath().getAbsolutePath());
        consolePrinter.horizontalLine();

        String indent = "";
        int maxLineNumLength = String.valueOf(lineList.size()).length();

        for (int i = 0; i < lineList.size(); i++) {
            consolePrinter.print(StringUtils.leftPad(String.valueOf(i + 1), maxLineNumLength, " ") + " | ");
            consolePrinter.println((lineList.get(i) == null) ? "" : indent + lineList.get(i));
        }
    }

}
