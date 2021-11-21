package com.github.httpflowlabs.httpflow.console.command.newfile.postprocess.impl;

import com.github.httpflowlabs.httpflow.console.command.newfile.postprocess.BodyPostProcessor;
import com.github.httpflowlabs.httpflow.console.command.newfile.postprocess.PostProcessResult;
import com.google.common.net.UrlEscapers;

public class FormUrlEncodedBodyPostProcessor implements BodyPostProcessor {

    @Override
    public PostProcessResult postProcess(String method, String bodyString) {
        if (bodyString.contains("\n")) {
            StringBuilder subBuilder = new StringBuilder();
            String[] tokens = bodyString.split("\n");

            for (int i = 0; i < tokens.length; i++) {
                subBuilder.append(UrlEscapers.urlFragmentEscaper().escape(tokens[i]));
                if (i < tokens.length - 1) {
                    subBuilder.append("&");
                }
            }
            bodyString = subBuilder.toString();
        }

        PostProcessResult result = new PostProcessResult();
        result.setBodyString(bodyString);
        return result;
    }

}
