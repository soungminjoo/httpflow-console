package com.github.httpflowlabs.httpflow.console.command.navigator;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleView;
import com.github.httpflowlabs.httpflow.console.command.CommandInfo;
import com.github.httpflowlabs.httpflow.console.session.ConsoleSession;
import com.github.httpflowlabs.httpflow.console.storage.LogicalFolderWrapper;
import com.github.httpflowlabs.httpflow.console.storage.PhysicalDiskFolderWrapper;
import com.github.httpflowlabs.httpflow.console.storage.FolderWrapper;
import com.github.httpflowlabs.httpflow.console.storage.logical.LogicalFile;
import com.github.httpflowlabs.httpflow.console.storage.logical.LogicalFolder;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NavigatorView extends AbstractConsoleView {

    public void print(CommandInfo commandInfo) {
        printTitleLine();
        printFolderTree(commandInfo);
        printFileAndFolder(commandInfo);
    }

    private void printTitleLine() {
        consolePrinter.horizontalLine();
        consolePrinter.print("  File navigator");
        if (ConsoleSession.INSTANCE.isPhysicalDiskMode()) {
            consolePrinter.println(" [ Mode : Physical disk ]");
        } else {
            consolePrinter.println(" [ Mode : Logical storage ]");
        }
        consolePrinter.horizontalLine();
    }

    private void printFolderTree(CommandInfo commandInfo) {
        StringBuilder builder = new StringBuilder();
        if (session.isNavigatorInRootFolder()) {
            printRootFolderTree(commandInfo, builder);
        } else {
            printChildrenFolderTree(commandInfo, builder);
        }
        consolePrinter.println(builder.toString());
        consolePrinter.horizontalLine();
    }

    private void printRootFolderTree(CommandInfo commandInfo, StringBuilder builder) {
        builder.append("    /   (Current folder is root)\n");
        builder.append("    N : (New file or folder)");
        commandInfo.addExtraKeys("N");
    }

    private void printChildrenFolderTree(CommandInfo commandInfo, StringBuilder builder) {
        FolderWrapper folderWrapper = session.getNavigatorPath();
        int depth = folderWrapper.getDepth();

        insertCommandLines(commandInfo, builder);
        while (!folderWrapper.isRootFolder()) {
            String indent = buildIndent(depth--);
            builder.insert(0, "\n").insert(0, indent + folderWrapper.getFolderName());
            folderWrapper = folderWrapper.getParent();
        }

        builder.insert(0, "\n").insert(0,  "    /");
        builder.deleteCharAt(builder.length() - 1);
    }

    private String buildIndent(int depth) {
        String indent = "    ";
        for (int i = 0; i < depth; i++) {
            indent += "  ";
        }
        if (depth > 0) {
            indent += "+ ";
        }
        return indent;
    }

    private void insertCommandLines(CommandInfo commandInfo, StringBuilder builder) {
        builder.insert(0, "\n").insert(0, "    D : (Delete current folder)");
        builder.insert(0, "\n").insert(0, "    N : (New file or folder)");
        commandInfo.addExtraKeys("D", "N");
    }

    private void printFileAndFolder(CommandInfo commandInfo) {
        commandInfo.setMaxNumber(10);
        if (!session.isNavigatorInRootFolder()) {
            consolePrinter.println("    P : (Go to parent)");
            commandInfo.addExtraKeys("P");
        }

        int index = 0;
        if (session.isPhysicalDiskMode()) {
            index = printPhysicalFileAndFolder(commandInfo);
        } else {
            index = printLogicalFileAndFolder(commandInfo);
        }
        commandInfo.setMaxNumber(index);
        if (index == 0) {
            consolePrinter.println("    No files in this folder.");
        }

        consolePrinter.horizontalLine();
    }

    private int printPhysicalFileAndFolder(CommandInfo commandInfo) {
        int index = 1;

        PhysicalDiskFolderWrapper physicalPath = (PhysicalDiskFolderWrapper) session.getNavigatorPath();
        File[] files = physicalPath.getFile().listFiles();
        if (files != null) {
            List<File> fileList = Stream.of(files).sorted(Comparator.comparing(File::getName)).collect(Collectors.toList());

            for (File folder : fileList) {
                if (folder.isDirectory() && !folder.getName().startsWith(".")) {
                    consolePrinter.println("    " + index + " : [" + folder.getName() + "]");
                    commandInfo.addIndexedCommand(index++, folder);
                }
            }
            for (File file : fileList) {
                if (!file.isDirectory() && file.getName().toLowerCase().endsWith(".hfd")) {
                    consolePrinter.println("    " + index + " : " + file.getName());
                    commandInfo.addIndexedCommand(index++, file);
                }
            }
        }

        return index - 1;
    }

    private int printLogicalFileAndFolder(CommandInfo commandInfo) {
        int index = 1;
        LogicalFolderWrapper storageWrapper = (LogicalFolderWrapper) session.getNavigatorPath();

        for (LogicalFolder folder : storageWrapper.getFolder().getFolders()) {
            consolePrinter.println("    " + index + " : [" + folder.getName() + "]");
            commandInfo.addIndexedCommand(index++, folder);
        }
        for (LogicalFile file : storageWrapper.getFolder().getFiles()) {
            consolePrinter.println("    " + index + " : " + file.getName());
            commandInfo.addIndexedCommand(index++, file);
        }

        return index - 1;
    }

}
