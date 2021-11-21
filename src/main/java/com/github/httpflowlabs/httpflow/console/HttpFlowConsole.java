package com.github.httpflowlabs.httpflow.console;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleCommand;
import com.github.httpflowlabs.httpflow.console.command.navigator.NavigatorCommand;
import com.github.httpflowlabs.httpflow.console.command.run.HttpFlowBuilder;
import com.github.httpflowlabs.httpflow.console.exception.SilentStopSignalException;
import com.github.httpflowlabs.httpflow.console.io.ConsolePrinter;
import com.github.httpflowlabs.httpflow.console.io.SelectionDialog;
import com.github.httpflowlabs.httpflow.console.session.ConsoleSession;

public class HttpFlowConsole {

    private void start() {
        ConsoleSession.INSTANCE.setHttpFlow(new HttpFlowBuilder().build());

        SelectionDialog storageDialog = new SelectionDialog("Storage Mode", "P : Physical disk", "L : Logical storage");
        String cmd = storageDialog.open("httpflow> Choose storage mode : ");
        ConsoleSession.INSTANCE.setPhysicalDiskMode("P".equalsIgnoreCase(cmd));
        ConsolePrinter.INSTANCE.bottomLine();

        AbstractConsoleCommand command = new NavigatorCommand();
        while (command != null) {
            ConsolePrinter.INSTANCE.clearScreen();
            command = command.execute();
            ConsolePrinter.INSTANCE.bottomLine();
        }
    }


    public static void main(String[] args) {
        if (args.length > 0) {
            new HttpFlowSingleFileExecutor().start(args);

        } else {
            try {
                new HttpFlowConsole().start();
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private static void handleException(Exception e) {
        if (e instanceof SilentStopSignalException) {

        } else {
            e.printStackTrace();
        }
    }

}
