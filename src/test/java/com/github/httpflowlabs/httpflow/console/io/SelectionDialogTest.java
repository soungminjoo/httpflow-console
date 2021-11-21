package com.github.httpflowlabs.httpflow.console.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SelectionDialogTest {

    @Test
    public void test() {
        SelectionDialog dialog = new SelectionDialog("Title", "A : Option1", "B:Option2", "C");

        Assertions.assertEquals(3, dialog.getOptionMap().size());
        Assertions.assertEquals("Option1", dialog.getOptionMap().get("A"));
        Assertions.assertEquals("Option2", dialog.getOptionMap().get("B"));
        Assertions.assertEquals("", dialog.getOptionMap().get("C"));
    }

}
