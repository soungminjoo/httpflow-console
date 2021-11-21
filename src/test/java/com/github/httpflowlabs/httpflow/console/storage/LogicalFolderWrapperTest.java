package com.github.httpflowlabs.httpflow.console.storage;

import com.github.httpflowlabs.httpflow.console.storage.logical.LogicalFileRepository;
import com.github.httpflowlabs.httpflow.console.storage.logical.LogicalFolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LogicalFolderWrapperTest {

    @BeforeEach
    public void before() {
        LogicalFileRepository.INSTANCE.setUnitTestMode(true);
    }

    @Test
    public void test() {
        LogicalFolder folder = new LogicalFolder("/", null);
        LogicalFolderWrapper wrapper = new LogicalFolderWrapper(folder);
        wrapper.mkdirs("foo");

        Assertions.assertEquals("foo", folder.getFolders().get(0).getName());
        Assertions.assertEquals(0, folder.getFolders().get(0).getFolders().size());
    }

    @Test
    public void test2() {
        LogicalFolder folder = new LogicalFolder("/", null);
        LogicalFolderWrapper wrapper = new LogicalFolderWrapper(folder);
        wrapper.mkdirs("foo/bar");

        Assertions.assertEquals("foo", folder.getFolders().get(0).getName());
        Assertions.assertEquals("bar", folder.getFolders().get(0).getFolders().get(0).getName());
    }

    @Test
    public void test3() {
        LogicalFolder folder = new LogicalFolder("/", null);
        LogicalFolderWrapper wrapper = new LogicalFolderWrapper(folder);
        wrapper.mkdirs("foo\\bar");

        Assertions.assertEquals("foo", folder.getFolders().get(0).getName());
        Assertions.assertEquals("bar", folder.getFolders().get(0).getFolders().get(0).getName());
    }

}
