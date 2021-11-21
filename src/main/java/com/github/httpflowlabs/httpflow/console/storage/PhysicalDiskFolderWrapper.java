package com.github.httpflowlabs.httpflow.console.storage;

import com.github.httpflowlabs.httpflow.console.io.UserInputReader;
import com.github.httpflowlabs.httpflow.console.utils.HttpFlowConsoleUtils;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class PhysicalDiskFolderWrapper implements FolderWrapper {

    private static final File NAVIGATOR_ROOT_FOLDER = new File(".").getAbsoluteFile();

    private File file;
    private int depth;

    public PhysicalDiskFolderWrapper() {
        this(new File(".").getAbsoluteFile());
    }

    public PhysicalDiskFolderWrapper(File file) {
        this.file = file;

        File currFolder = file;
        int depth = 0;
        while (currFolder != null && !NAVIGATOR_ROOT_FOLDER.equals(currFolder)) {
            depth++;
            currFolder = currFolder.getParentFile();
        }
        this.depth = depth;
    }

    @Override
    public String getFolderName() {
        return file.getName();
    }

    @Override
    public List<FileWrapper> getFiles() {
        List<FileWrapper> list = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null) {
            List<File> fileList = Stream.of(files).sorted(Comparator.comparing(File::getName)).collect(Collectors.toList());
            for (File f : fileList) {
                if (f.isFile() && f.getName().toLowerCase().endsWith(".hfd")) {
                    list.add(new FileWrapper(f));
                }
            }
        }
        return list;
    }

    @Override
    public boolean isValidFileName(String name) {
        // TODO - Check allowed special character according to OS
        for (char ch : name.toCharArray()) {
            if (!Character.isLetterOrDigit(ch) && ch != '.' && ch != '_') {
                return false;
            }
        }
        return true;
    }

    @Override
    public FolderWrapper getParent() {
        if (NAVIGATOR_ROOT_FOLDER.equals(file.getParentFile())) {
            return new PhysicalDiskFolderWrapper();
        }
        return new PhysicalDiskFolderWrapper(file.getParentFile());
    }

    @Override
    public boolean isRootFolder() {
        return NAVIGATOR_ROOT_FOLDER.equals(file);
    }

    @Override
    public void mkdirs(String name) {
        new File(file, name).mkdirs();
    }

    @Override
    public boolean delete() {
        if (file.listFiles() == null || file.listFiles().length == 0) {
            file.delete();
            return true;
        }

        UserInputReader.INSTANCE.printMessageAndWait("ERROR : Can't delete current folder. It's not empty.");
        return false;
    }

    @Override
    public boolean existsFile(String name) {
        for (File f : file.listFiles()) {
            if (f.isFile() && f.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public FileWrapper createFile(String name, String contents) {
        File file = new File(this.file, name);
        HttpFlowConsoleUtils.writeFile(file, contents);
        return new FileWrapper(file);
    }

}
