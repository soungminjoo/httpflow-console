package com.github.httpflowlabs.httpflow.console.command.run;

import com.github.httpflowlabs.httpflow.core.context.HttpFlowContext;
import com.github.httpflowlabs.httpflow.core.response.listener.HttpFlowResponseListener;
import com.github.httpflowlabs.httpflow.resource.parser.model.HttpFlowElement;
import com.github.httpflowlabs.httpflow.support.httptemplate.HttpFlowRequest;
import com.github.httpflowlabs.httpflow.support.httptemplate.model.HttpTemplateResponse;
import lombok.Getter;

@Getter
public class ResponseListener implements HttpFlowResponseListener {

    private int code;
    private String reasonPhrase;
    private String url;
    private String body;

    @Override
    public void beforeResponseProcess(HttpFlowContext context, HttpFlowElement element, HttpFlowRequest request, HttpTemplateResponse<String> response) {

    }

    @Override
    public void afterResponseProcess(HttpFlowContext context, HttpFlowElement element, HttpFlowRequest request, HttpTemplateResponse<String> response) {
        this.code = response.getCode();
        this.reasonPhrase = response.getReasonPhrase();
        this.url = request.getUrl();
        this.body = response.getBody();
    }

}
