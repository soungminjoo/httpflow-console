package com.github.httpflowlabs.httpflow.console.command.run;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleView;

public class RunHttpFlowView extends AbstractConsoleView {

    public void printStart() {
        consolePrinter.horizontalLine();
        consolePrinter.println("  Running httpflow... [ " + session.getFileViewerPath().getAbsolutePath() + " ]");
    }

    public void printResponse(ResponseListener listener) {
        consolePrinter.horizontalLine();
        consolePrinter.println("  Response : " + listener.getCode() + " " + listener.getReasonPhrase());
        consolePrinter.horizontalLine();
        consolePrinter.println(listener.getBody());
        consolePrinter.horizontalLine();
    }
}
