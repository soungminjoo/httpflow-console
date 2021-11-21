package com.github.httpflowlabs.httpflow.console.storage.logical;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class LogicalFileCommon implements Serializable {

    private static final long serialVersionUID = 1024000L;

    private String name;
    private LogicalFolder parent;
    private String lastModified;

    public LogicalFileCommon() {
    }

    public LogicalFileCommon(String name, LogicalFolder parent) {
        this.name = name;
        this.parent = parent;
    }

    public void updateLastModified() {
        this.lastModified = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
