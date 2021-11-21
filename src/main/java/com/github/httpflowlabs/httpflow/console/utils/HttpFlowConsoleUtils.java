package com.github.httpflowlabs.httpflow.console.utils;

import com.github.httpflowlabs.httpflow.console.exception.SilentStopSignalException;
import com.github.httpflowlabs.httpflow.console.io.ConsolePrinter;
import com.github.httpflowlabs.httpflow.console.io.UserInputReader;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;

public class HttpFlowConsoleUtils {

    public static void saveAsObjectOutputStream(Object object, String fileName) {
        if (!new File(fileName).getParentFile().exists()) {
            new File(fileName).getParentFile().mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(object);
        } catch (Exception e) {
            ConsolePrinter.INSTANCE.println("ERROR : " + fileName + " file cannot be saved. " + e.getMessage());
            UserInputReader.INSTANCE.printMessageAndWait("Program will be shut down.");
            throw new SilentStopSignalException(e);
        }
    }

    public static <T> T readFromObjectOutputStream(String fileName) {
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(fileName))) {
            return (T) oos.readObject();
        } catch (Exception e) {
            ConsolePrinter.INSTANCE.println("ERROR : " + fileName + " file cannot be loaded. " + e.getMessage());
            UserInputReader.INSTANCE.printMessageAndWait("Program will be shut down.");
            throw new SilentStopSignalException(e);
        }
    }

    public static void copyToClipboard(String text) {
        Clipboard clipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection data = new StringSelection(text);
        clipBoard.setContents(data, data);
    }

    public static void writeFile(File file, String contents) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(contents.getBytes());
        } catch (Exception e) {
            UserInputReader.INSTANCE.printMessageAndWait("ERROR : Failed to save file : " + e.getMessage());
        }
    }

}
