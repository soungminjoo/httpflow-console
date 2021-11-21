package com.github.httpflowlabs.httpflow.console.storage.logical;

import com.github.httpflowlabs.httpflow.console.utils.HttpFlowConsoleUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.Comparator;
import java.util.Optional;

public class LogicalFileRepository {

    public static final LogicalFileRepository INSTANCE = new LogicalFileRepository();
    public static final String REPO_FILE_NAME = ".httpflow/logical_files";

    @Getter
    private LogicalFolder rootFolder = new LogicalFolder("/", null);

    @Getter
    @Setter
    private boolean unitTestMode;

    private LogicalFileRepository() {
        if (new File(REPO_FILE_NAME).exists()) {
            this.rootFolder = HttpFlowConsoleUtils.readFromObjectOutputStream(REPO_FILE_NAME);
        }
    }

    public LogicalFolder addFolderToRoot(String name) {
        return addFolder(null, name);
    }

    public LogicalFolder addFolder(LogicalFolder parent, String name) {
        if (parent == null) {
            parent = rootFolder;
        }

        Optional<LogicalFolder> existing = parent.getFolders().stream()
                .filter(f -> f.getName().equals(name))
                .findAny();
        if (existing.isPresent()) {
            return existing.get();
        }

        LogicalFolder folder = new LogicalFolder(name, parent);
        folder.updateLastModified();

        parent.getFolders().add(folder);
        parent.getFolders().sort(Comparator.comparing(LogicalFolder::getName));

        persist();
        return folder;
    }

    public LogicalFile addFileToRoot(String name, String contents) {
        return addFile(null, name, contents);
    }

    public LogicalFile addFile(LogicalFolder parent, String name, String contents) {
        if (parent == null) {
            parent = rootFolder;
        }

        Optional<LogicalFolder> existing = parent.getFolders().stream().filter(f -> f.getName().equals(name)).findAny();
        if (existing.isPresent()) {
            return null;
            //TODO
            //throw new UserLevelMessageException("File '" + name + "' already exists.");
        }

        LogicalFile file = new LogicalFile(name, parent);
        file.setContents(contents);
        file.updateLastModified();

        parent.getFiles().add(file);
        parent.getFiles().sort(Comparator.comparing(LogicalFile::getName));

        persist();

        return file;
    }

    public void deleteFile(LogicalFile currentFile) {
        deleteFile(currentFile.getParent(), currentFile.getName());
    }

    public void deleteFile(String currentFile) {
        deleteFile(null, currentFile);
    }

    public void deleteFile(LogicalFolder parent, String currentFile) {
        if (parent == null) {
            parent = rootFolder;
        }

        Optional<LogicalFile> fileOpt = parent.getFiles().stream()
                .filter(f -> f.getName().equals(currentFile))
                .findFirst();

        if (fileOpt.isPresent()) {
            parent.getFiles().remove(fileOpt.get());
            persist();
        }
    }

    public void deleteFolder(LogicalFolder currentFolder) {
        if (currentFolder.getParent() == null) {
            return;
        }

        Optional<LogicalFolder> folderOpt = currentFolder.getParent().getFolders().stream()
                .filter(folder -> currentFolder.getName().equals(folder.getName()))
                .findFirst();

        if (folderOpt.isPresent()) {
            currentFolder.getParent().getFolders().remove(folderOpt.get());
            persist();
        }
    }

    public void persist() {
        if (!unitTestMode) {
            HttpFlowConsoleUtils.saveAsObjectOutputStream(rootFolder, REPO_FILE_NAME);
        }
    }
}
