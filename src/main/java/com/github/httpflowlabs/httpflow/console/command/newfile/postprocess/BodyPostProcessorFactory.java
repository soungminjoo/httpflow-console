package com.github.httpflowlabs.httpflow.console.command.newfile.postprocess;

import com.github.httpflowlabs.httpflow.console.command.newfile.postprocess.impl.FormUrlEncodedBodyPostProcessor;
import com.github.httpflowlabs.httpflow.console.command.newfile.postprocess.impl.JsonBodyPostProcessor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class BodyPostProcessorFactory {

    public static final BodyPostProcessorFactory INSTANCE = new BodyPostProcessorFactory();

    private Map<String, BodyPostProcessor> processorMap = new HashMap<>();

    private BodyPostProcessorFactory() {
        processorMap.put("application/x-www-form-urlencoded", new FormUrlEncodedBodyPostProcessor());
        processorMap.put("application/json", new JsonBodyPostProcessor());
    }

    public BodyPostProcessor get(String contentType) {
        if (StringUtils.isEmpty(contentType)) {
            return null;
        }

        for (String key : processorMap.keySet()) {
            if (contentType.startsWith(key+";")) {
                return processorMap.get(key);
            }
        }
        return processorMap.get(contentType);
    }

}
