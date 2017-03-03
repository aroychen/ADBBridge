package com.tencent.weread.bridge.command;

import com.tencent.weread.bridge.rpc.RPCResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by roylkchen on 10/28/16.
 */
public abstract class CommandExecutor {
    private Map<String, Command> commandMap;
    private Command[] commands;

    public CommandExecutor() {
        Command[] commands = commands();
        addCommands(commands);
    }

    /**
     * return {@link Command}s implements.
     */
    protected abstract Command[] commands();

    /**
     * Initialize commands by inherited class.
     * @param commands
     */
    private void addCommands(Command[] commands) {
        if (commands == null || commands.length == 0) {
            commandMap = Collections.EMPTY_MAP;
            return;
        }

        Map<String, Command> map = new HashMap<>();
        for (Command command : commands) {
            String key = command.commandKey();

            Command elderOne = map.put(key, command);
            if (elderOne != null) {
                throw new RuntimeException(String.format("Command %s is duplicated with [%s] and [%s]",
                        key, command.getClass().getSimpleName(), elderOne.getClass().getSimpleName()));
            }
        }

        commandMap = Collections.unmodifiableMap(map);
        this.commands = commands;
    }

    public final RPCResult execute(String cmd, String[] args) {
        Command command = commandMap.get(cmd);

        if (command == null) {
            return RPCResult.failed("Command not found:" + cmd);
        }

        RPCResult result =  command.execute(args);

        return result != null ? result : RPCResult.failed("Command executed with no result.");
    }

    public final String help() {
        if (commands == null || commands.length == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < commands.length; i++) {
            Command command = commands[i];

            result.append("[")
                    .append(command.commandKey())
                    .append("]")
                    .append("-")
                    .append(command.description())
                    .append("\n");
        }

        return result.toString();
    }
}
