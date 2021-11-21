package com.github.httpflowlabs.httpflow.console;

import com.github.httpflowlabs.httpflow.console.io.ConsolePrinter;
import com.github.httpflowlabs.httpflow.console.utils.HttpFlowConsoleUtils;
import com.github.httpflowlabs.httpflow.core.context.HttpFlowContext;
import com.github.httpflowlabs.httpflow.core.response.listener.HttpFlowResponseListener;
import com.github.httpflowlabs.httpflow.resource.parser.model.HttpFlowConstants;
import com.github.httpflowlabs.httpflow.resource.parser.model.HttpFlowElement;
import com.github.httpflowlabs.httpflow.resource.parser.model.HttpFlowHeaderValues;
import com.github.httpflowlabs.httpflow.support.httptemplate.HttpFlowRequest;
import com.github.httpflowlabs.httpflow.support.httptemplate.model.HttpTemplateResponse;

import java.io.File;

public class HttpFlowSingleFileExecutorListener implements HttpFlowResponseListener {

    private ConsolePrinter consolePrinter = ConsolePrinter.INSTANCE;


    @Override
    public void beforeResponseProcess(HttpFlowContext context, HttpFlowElement element, HttpFlowRequest request, HttpTemplateResponse<String> response) {
        consolePrinter.println();
        consolePrinter.horizontalLine();
        consolePrinter.println("REQUEST URL : " + element.getUrl());
        if (context.getCsrfToken() != null) {
            consolePrinter.println("X-CSRF-TOKEN : " + context.getCsrfToken());
        }
    }

    @Override
    public void afterResponseProcess(HttpFlowContext context, HttpFlowElement element, HttpFlowRequest request, HttpTemplateResponse<String> response) {
        consolePrinter.horizontalLine();
        consolePrinter.println("RESPONSE STATUS : " + response.getCode() + " " + response.getReasonPhrase());
        printContextVariable(context, element);
        consolePrinter.horizontalLine();
        dumpOrPrintResponseBody(response);
    }

    private void printContextVariable(HttpFlowContext context, HttpFlowElement element) {
        HttpFlowHeaderValues printVarHeaders = element.getHeader(HttpFlowConstants.HTTP_FLOW_PRINT_VARIABLE);
        if (printVarHeaders.size() > 0) {
            consolePrinter.horizontalLine();
            consolePrinter.println(HttpFlowConstants.HTTP_FLOW_PRINT_VARIABLE);
            for (String expr : printVarHeaders.asList()) {
                consolePrinter.println("  " + expr + " : " + context.getContextVariableByOgnl(expr));
            }
        }
    }

    private void dumpOrPrintResponseBody(HttpTemplateResponse<String> response) {
        String dumpMode = System.getProperty("DumpMode");
        if ("File".equalsIgnoreCase(dumpMode)) {
            File folder = new File("dumpfile");
            if (!folder.exists()) {
                folder.mkdirs();
            }
            HttpFlowConsoleUtils.writeFile(new File(folder, "dump" + System.currentTimeMillis()), response.getBody());
        } else{
            consolePrinter.println(response.getBody());
        }
    }

}
