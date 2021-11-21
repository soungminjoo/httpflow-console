package com.github.httpflowlabs.httpflow.console.session;

import com.github.httpflowlabs.httpflow.HttpFlow;
import com.github.httpflowlabs.httpflow.console.storage.FileWrapper;
import com.github.httpflowlabs.httpflow.console.storage.LogicalFolderWrapper;
import com.github.httpflowlabs.httpflow.console.storage.PhysicalDiskFolderWrapper;
import com.github.httpflowlabs.httpflow.console.storage.FolderWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsoleSession {

    public static final ConsoleSession INSTANCE = new ConsoleSession();

    private HttpFlow httpFlow;

    private boolean isPhysicalDiskMode;
    private FolderWrapper navigatorPath;
    private FileWrapper fileViewerPath;

    private ConsoleSession() {
        setPhysicalDiskMode(false);
    }

    public void setPhysicalDiskMode(boolean isPhysicalDiskMode) {
        this.isPhysicalDiskMode = isPhysicalDiskMode;
        if (isPhysicalDiskMode) {
            navigatorPath = new PhysicalDiskFolderWrapper();
        } else {
            navigatorPath = new LogicalFolderWrapper();
        }
    }

    public boolean isNavigatorInRootFolder() {
        return navigatorPath.isRootFolder();
    }

}
