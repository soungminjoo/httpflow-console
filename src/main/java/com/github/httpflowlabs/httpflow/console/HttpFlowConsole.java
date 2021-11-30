package com.github.httpflowlabs.httpflow.console;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleCommand;
import com.github.httpflowlabs.httpflow.console.command.navigator.NavigatorCommand;
import com.github.httpflowlabs.httpflow.console.command.run.HttpFlowBuilder;
import com.github.httpflowlabs.httpflow.console.exception.SilentStopSignalException;
import com.github.httpflowlabs.httpflow.console.io.ConsolePrinter;
import com.github.httpflowlabs.httpflow.console.session.ConsoleSession;

public class HttpFlowConsole {

    private void start() {
        ConsoleSession.INSTANCE.setHttpFlow(new HttpFlowBuilder().build());
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
            if (isLastArgsVersionOptioin(args)) {
                showHttpFlowVersioin();
            } else {
                new HttpFlowSingleFileExecutor().start(args);
            }

        } else {
            try {
                new HttpFlowConsole().start();
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    private static boolean isLastArgsVersionOptioin(String[] args) {
        String lastArgs = args[args.length - 1];
        return "-v".equals(lastArgs) || "--version".equals(lastArgs);
    }

    private static void showHttpFlowVersioin() {
        ConsolePrinter.INSTANCE.println("Httpflow 0.0.1");
    }

    private static void handleException(Exception e) {
        if (e instanceof SilentStopSignalException) {

        } else {
            e.printStackTrace();
        }
    }

}
