package com.github.httpflowlabs.httpflow.console.command.newfile.postprocess.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.httpflowlabs.httpflow.console.command.newfile.postprocess.BodyPostProcessor;
import com.github.httpflowlabs.httpflow.console.command.newfile.postprocess.PostProcessResult;
import com.github.httpflowlabs.httpflow.console.exception.SilentStopSignalException;
import com.github.httpflowlabs.httpflow.console.io.UserInputReader;
import com.github.httpflowlabs.httpflow.support.easyread.regex.EasyReadRegexBuilder;
import com.github.httpflowlabs.httpflow.support.easyread.regex.split.TwoSplitToken;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonBodyPostProcessor implements BodyPostProcessor {

    @Override
    public PostProcessResult postProcess(String method, String bodyString) {
        try {
            if (bodyString.trim().startsWith("{")) {
                new ObjectMapper().readValue(bodyString, Map.class);
            } else if (bodyString.trim().startsWith("[")) {
                new ObjectMapper().readValue(bodyString, List.class);
            } else {
                bodyString = convertNameValuePairs(bodyString);
            }
        } catch (JsonMappingException e) {
            bodyString = convertNameValuePairs(bodyString);
        } catch (JsonProcessingException e) {
            bodyString = convertNameValuePairs(bodyString);
        }

        PostProcessResult result = new PostProcessResult();
        result.setBodyString(bodyString);
        return result;
    }

    private String convertNameValuePairs(String bodyString) {
        if (bodyString.contains("\n")) {
            String[] tokens = bodyString.split("\n");

            Map<String, String> data = new LinkedHashMap<>();
            for (int i = 0; i < tokens.length; i++) {
                TwoSplitToken split = EasyReadRegexBuilder.splitIntoTwo(tokens[i], "=");
                data.put(split.getLeft().trim(), split.getRight());
            }

            try {
                bodyString = new ObjectMapper().writeValueAsString(data);
            } catch (JsonProcessingException e) {
                UserInputReader.INSTANCE.printMessageAndWait("ERROR : " + e.getMessage());
                throw new SilentStopSignalException(e);
            }
        }
        return bodyString;
    }

}
