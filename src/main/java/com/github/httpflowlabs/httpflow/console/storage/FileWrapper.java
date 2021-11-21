package com.github.httpflowlabs.httpflow.console.storage;

import com.github.httpflowlabs.httpflow.console.storage.logical.LogicalFile;
import com.github.httpflowlabs.httpflow.console.storage.logical.LogicalFileRepository;
import com.github.httpflowlabs.httpflow.console.utils.HttpFlowConsoleUtils;
import com.github.httpflowlabs.httpflow.support.HttpFlowUtils;
import lombok.Getter;

import java.io.File;

@Getter
public class FileWrapper {

    private File physicalFile;
    private LogicalFile logicalFile;

    public FileWrapper(Object file) {
        if (file instanceof File) {
            this.physicalFile = (File) file;
        } else {
            this.logicalFile = (LogicalFile) file;
        }
    }

    public String getFileContents() {
        if (physicalFile != null) {
            return HttpFlowUtils.readFile(physicalFile);
        } else {
            return logicalFile.getContents();
        }
    }

    public String getAbsolutePath() {
        // TODO change to real absolute path
        if (physicalFile != null) {
            return physicalFile.getAbsolutePath();
        } else {
            return logicalFile.getName();
        }
    }

    public void delete() {
        if (physicalFile != null) {
            physicalFile.delete();
        } else {
            LogicalFileRepository.INSTANCE.deleteFile(logicalFile);
        }
    }

    public void saveFileContents(String contents) {
        if (physicalFile != null) {
            HttpFlowConsoleUtils.writeFile(physicalFile, contents);
        } else {
            logicalFile.setContents(contents);
            LogicalFileRepository.INSTANCE.persist();
        }
    }

    public String getFileName() {
        if (physicalFile != null) {
            return physicalFile.getName();
        } else {
            return logicalFile.getName();
        }
    }
}
