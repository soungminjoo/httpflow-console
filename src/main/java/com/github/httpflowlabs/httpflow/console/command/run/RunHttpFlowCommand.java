package com.github.httpflowlabs.httpflow.console.command.run;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleCommand;
import com.github.httpflowlabs.httpflow.console.command.viewer.FileViewerCommand;
import com.github.httpflowlabs.httpflow.console.io.UserInputReader;
import com.github.httpflowlabs.httpflow.resource.impl.StringHttpFlowResource;

public class RunHttpFlowCommand extends AbstractConsoleCommand {

    private RunHttpFlowView runHttpFlowView = new RunHttpFlowView();
    private ResponseListener responseListener = new ResponseListener();

    @Override
    public AbstractConsoleCommand execute() {
        runHttpFlowView.printStart();

        String httpFlowStr = session.getFileViewerPath().getFileContents();

        session.getHttpFlow().setResponseListener(responseListener);
        session.getHttpFlow().execute(StringHttpFlowResource.of(httpFlowStr));

        runHttpFlowView.printResponse(responseListener);
        UserInputReader.INSTANCE.printMessageAndWait("httpflow> Request completed.");
        return new FileViewerCommand();
    }

}
