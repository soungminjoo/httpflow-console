package com.github.httpflowlabs.httpflow.console.command.run;

import com.github.httpflowlabs.httpflow.HttpFlow;
import com.github.httpflowlabs.httpflow.core.context.externalparam.ExternalParamSupplier;
import com.github.httpflowlabs.httpflow.support.HttpFlowUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class HttpFlowBuilder {

    public HttpFlow build() {
        HttpFlow httpFlow = new HttpFlow();
        processExternalParamSupplier(httpFlow);
        return httpFlow;
    }

    private void processExternalParamSupplier(HttpFlow httpFlow) {
        String externalParamSupplierClass = System.getProperty("httpflow.external.param.supplier");
        if (!StringUtils.isEmpty(externalParamSupplierClass)) {
            ExternalParamSupplier externalParamSupplier = HttpFlowUtils.newInstance(externalParamSupplierClass);
            Map<String, Object> params = externalParamSupplier.getExternalParams();
            if (params != null) {
                for (String key : params.keySet()) {
                    httpFlow.getContext().addExternalParam(key, params.get(key));
                }
            }
        }
    }

}
