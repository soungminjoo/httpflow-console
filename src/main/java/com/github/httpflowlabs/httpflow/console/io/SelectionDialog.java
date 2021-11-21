package com.github.httpflowlabs.httpflow.console.io;

import com.github.httpflowlabs.httpflow.support.easyread.regex.EasyReadRegexBuilder;
import com.github.httpflowlabs.httpflow.support.easyread.regex.split.TwoSplitToken;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.map.CaseInsensitiveMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class SelectionDialog {

    private String title;
    private Map<String, String> optionMap = new CaseInsensitiveMap<>();
    private List<String> optionNames = new ArrayList<>();

    @Setter
    private boolean allowNoSelection;

    private ConsolePrinter consolePrinter = ConsolePrinter.INSTANCE;

    public SelectionDialog(String title, String... options) {
        this.title = title;
        for (String option : options) {
            TwoSplitToken tokens = EasyReadRegexBuilder.splitIntoTwo(option, ":");
            addOption(tokens.getLeft().trim(), tokens.getRight().trim());
        }
    }

    public void addOption(String key, String label) {
        optionMap.put(key, label);
        optionNames.add(key);
    }

    public String open() {
        return open("");
    }

    public String open(String infoMsg) {
        consolePrinter.horizontalLine();
        consolePrinter.println(title);
        consolePrinter.horizontalLine();

        for (String key : optionNames) {
            consolePrinter.println("  " + key.toUpperCase() + " : " + optionMap.get(key));
        }
        consolePrinter.horizontalLine();

        while (true) {
            consolePrinter.print(infoMsg);

            String line = UserInputReader.INSTANCE.readLine();
            if (allowNoSelection && "".equals(line.trim())) {
                return "";
            }

            if (optionMap.containsKey(line.trim())) {
                return line.trim();
            }
        }
    }

}
