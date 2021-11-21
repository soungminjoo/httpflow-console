package com.github.httpflowlabs.httpflow.console.exception;

public class SilentStopSignalException extends RuntimeException {

    public SilentStopSignalException(Exception e) {
        super(e);
    }

}
