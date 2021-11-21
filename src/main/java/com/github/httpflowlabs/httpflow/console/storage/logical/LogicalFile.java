package com.github.httpflowlabs.httpflow.console.storage.logical;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LogicalFile extends LogicalFileCommon implements Serializable {

    private static final long serialVersionUID = 1024128L;

    private String contents;

    public LogicalFile() {
    }

    public LogicalFile(String name, LogicalFolder parent) {
        super(name, parent);
    }

}
