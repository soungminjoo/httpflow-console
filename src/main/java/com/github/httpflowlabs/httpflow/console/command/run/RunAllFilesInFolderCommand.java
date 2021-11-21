package com.github.httpflowlabs.httpflow.console.command.run;

import com.github.httpflowlabs.httpflow.console.command.AbstractConsoleCommand;
import com.github.httpflowlabs.httpflow.console.command.navigator.NavigatorCommand;
import com.github.httpflowlabs.httpflow.console.storage.FileWrapper;
import com.github.httpflowlabs.httpflow.console.io.UserInputReader;
import com.github.httpflowlabs.httpflow.resource.impl.StringHttpFlowResource;

import java.util.List;

public class RunAllFilesInFolderCommand extends AbstractConsoleCommand {

    private RunAllFilesInFolderView runAllFilesInFolderView = new RunAllFilesInFolderView();
    private ResponseListener responseListener = new ResponseListener();

    @Override
    public AbstractConsoleCommand execute() {
        List<FileWrapper> files = session.getNavigatorPath().getFiles();
        runAllFilesInFolderView.printStart(files);

        String answer = UserInputReader.INSTANCE.confirmYn("httpflow> Are you sure to run all this files?(Y/N) : ");
        if ("Y".equalsIgnoreCase(answer)) {
            session.getHttpFlow().setResponseListener(responseListener);

            boolean isForceYes = false;
            for (int i = 0; i < files.size(); i++) {
                FileWrapper f = files.get(i);
                session.getHttpFlow().execute(StringHttpFlowResource.of(f.getFileContents()));
                runAllFilesInFolderView.printResponse(f, responseListener);

                if (!isForceYes && i < files.size() - 1) {
                    answer = UserInputReader.INSTANCE.confirmYn("httpflow> Continue next file '" + files.get(i + 1).getFileName() + "'? (Y/N/All) : ", "A", "All");
                    if ("N".equalsIgnoreCase(answer)) {
                        return new NavigatorCommand();
                    } else if ("A".equalsIgnoreCase(answer) || "All".equalsIgnoreCase(answer)) {
                        isForceYes = true;
                    }
                }
            }
            UserInputReader.INSTANCE.printMessageAndWait("httpflow> Request completed.");
        }
        return new NavigatorCommand();
    }

}
