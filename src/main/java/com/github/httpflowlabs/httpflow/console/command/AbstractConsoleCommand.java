package com.github.httpflowlabs.httpflow.console.command;

import com.github.httpflowlabs.httpflow.console.io.ConsolePrinter;
import com.github.httpflowlabs.httpflow.console.session.ConsoleSession;

public abstract class AbstractConsoleCommand {

    protected ConsolePrinter consolePrinter = ConsolePrinter.INSTANCE;
    protected ConsoleSession session = ConsoleSession.INSTANCE;

    public abstract AbstractConsoleCommand execute();

}
