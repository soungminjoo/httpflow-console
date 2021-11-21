package com.github.httpflowlabs.httpflow.console.storage;

import com.github.httpflowlabs.httpflow.console.storage.logical.LogicalFile;
import com.github.httpflowlabs.httpflow.console.storage.logical.LogicalFileCommon;
import com.github.httpflowlabs.httpflow.console.storage.logical.LogicalFileRepository;
import com.github.httpflowlabs.httpflow.console.storage.logical.LogicalFolder;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LogicalFolderWrapper implements FolderWrapper {

    private LogicalFolder folder;

    public LogicalFolderWrapper() {
        this(LogicalFileRepository.INSTANCE.getRootFolder());
    }

    public LogicalFolderWrapper(LogicalFolder folder) {
        this.folder = folder;
    }

    @Override
    public int getDepth() {
        return folder.getDepth();
    }

    @Override
    public String getFolderName() {
        return folder.getName();
    }

    @Override
    public List<FileWrapper> getFiles() {
        return folder.getFiles().stream()
                .sorted(Comparator.comparing(LogicalFileCommon::getName))
                .map(f -> new FileWrapper(f))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValidFileName(String name) {
        return true;
    }

    @Override
    public FolderWrapper getParent() {
        return new LogicalFolderWrapper(folder.getParent());
    }

    @Override
    public boolean isRootFolder() {
        return folder.getParent() == null;
    }

    @Override
    public void mkdirs(String name) {
        String[] tokens = name.split("[/|\\\\]");
        LogicalFolder current = folder;
        for (String token : tokens) {
            current = LogicalFileRepository.INSTANCE.addFolder(current, token);
        }
    }

    @Override
    public boolean delete() {
        LogicalFileRepository.INSTANCE.deleteFolder(folder);
        return true;
    }

    @Override
    public boolean existsFile(String name) {
        return folder.getFiles().stream().filter(f -> f.getName().equalsIgnoreCase(name)).findAny().isPresent();
    }

    @Override
    public FileWrapper createFile(String name, String contents) {
        LogicalFile createdFile = LogicalFileRepository.INSTANCE.addFile(folder, name, contents);
        return new FileWrapper(createdFile);
    }

}
