package com.github.httpflowlabs.httpflow.console.command.newfile.postprocess;

public interface BodyPostProcessor {

    PostProcessResult postProcess(String method, String bodyString);

}
