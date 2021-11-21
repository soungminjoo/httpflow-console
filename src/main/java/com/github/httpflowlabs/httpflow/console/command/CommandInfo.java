package com.github.httpflowlabs.httpflow.console.command;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class CommandInfo {

    private int maxNumber;
    private Set<String> extraKeys = new HashSet<>();
    private Map<Integer, Object> indexedCommandMap = new HashMap<>();

    public void addExtraKeys(String... keys) {
        for (String key : keys) {
            extraKeys.add(key);
        }
    }

    public void addIndexedCommand(int index, Object command) {
        indexedCommandMap.put(index, command);
    }

}
