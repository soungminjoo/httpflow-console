package com.github.httpflowlabs.httpflow.console.storage.logical;

import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
public class LogicalFolder extends LogicalFileCommon implements Serializable {

    private static final long serialVersionUID = 1024256L;

    private List<LogicalFolder> folders = new ArrayList<>();
    private List<LogicalFile> files = new ArrayList<>();

    public LogicalFolder() {
    }

    public LogicalFolder(String name, LogicalFolder parent) {
        super(name, parent);
    }

    public String getAbsolutePath() {
        StringBuilder builder = new StringBuilder();
        LogicalFolder temp = this;
        while (temp != null) {
            String name = temp.getName();
            if (temp.getParent() != null) {
                name = "/" + name;
                builder.insert(0, name);
            }
            temp = temp.getParent();
        }
        return builder.toString();
    }

    public int getDepth() {
        int depth = -1;
        LogicalFolder temp = this;
        while (temp != null) {
            depth++;
            temp = temp.getParent();
        }
        return depth;
    }

    public boolean hasFile(String name) {
        return files.stream()
                .filter(file -> file.getName().equals(name))
                .findAny().isPresent();
    }

}
