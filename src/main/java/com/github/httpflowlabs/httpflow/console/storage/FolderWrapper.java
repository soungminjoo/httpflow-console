package com.github.httpflowlabs.httpflow.console.storage;

import java.util.List;

public interface FolderWrapper {

    boolean isRootFolder();
    FolderWrapper getParent();
    int getDepth();
    String getFolderName();
    List<FileWrapper> getFiles();

    boolean isValidFileName(String name);
    void mkdirs(String name);
    boolean delete();

    boolean existsFile(String name);
    FileWrapper createFile(String name, String contents);

}
