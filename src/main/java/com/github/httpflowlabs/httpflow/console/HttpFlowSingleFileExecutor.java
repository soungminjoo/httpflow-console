package com.github.httpflowlabs.httpflow.console;

import com.github.httpflowlabs.httpflow.HttpFlow;
import com.github.httpflowlabs.httpflow.console.io.ConsolePrinter;
import com.github.httpflowlabs.httpflow.resource.impl.FileHttpFlowResource;
import com.github.httpflowlabs.httpflow.support.easyread.regex.EasyReadRegexBuilder;
import com.github.httpflowlabs.httpflow.support.easyread.regex.split.TwoSplitToken;

import java.io.File;

public class HttpFlowSingleFileExecutor {

    private ConsolePrinter consolePrinter = ConsolePrinter.INSTANCE;

    public void start(String[] args) {
        if (!new File(args[0]).exists()) {
            consolePrinter.println("File not found : " + args[0]);
            return;
        }

        HttpFlow httpFlow = new HttpFlow();
        if (args.length > 1) {
            addExternalParamFromArgs(httpFlow, args);
        }

        httpFlow.setResponseListener(new HttpFlowSingleFileExecutorListener());

        httpFlow.execute(FileHttpFlowResource.of(new File(args[0])));
    }

    private void addExternalParamFromArgs(HttpFlow httpFlow, String[] args) {
        for (int i = 1; i < args.length; i++) {
            TwoSplitToken split = EasyReadRegexBuilder.splitIntoTwo(args[i], "=");
            httpFlow.getContext().addExternalParam(split.getLeft(), split.getRight());
        }
    }

}
