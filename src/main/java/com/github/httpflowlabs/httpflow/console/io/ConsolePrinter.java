package com.github.httpflowlabs.httpflow.console.io;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ConsolePrinter {

    public static final ConsolePrinter INSTANCE = new ConsolePrinter();

    private ConsolePrinter() {

    }

    public void print(Object msg) {
        System.out.print(msg);
    }

    public void println(Object msg) {
        System.out.println(msg);
    }

    public void println() {
        println("");
    }

    public void horizontalLine() {
        horizontalLine("-");
    }

    public void horizontalLine(String s) {
        for (int i = 0; i < 80; i++) {
            print(s);
        }
        println();
    }

    public void bottomLine() {
        horizontalLine("=");
        println();
        println();
    }

    public void printStackTrace(Throwable e) {
        e.printStackTrace(System.out);
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
