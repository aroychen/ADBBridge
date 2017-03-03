package com.tencent.weread.bridge.rpc;

import android.util.Log;

import com.tencent.weread.bridge.command.CommandExecutor;

import java.util.Arrays;

/**
 * Created by roylkchen on 8/3/16.
 */
public class RPCServiceImpl implements RPCService {
    private static final String TAG = "RPCServiceImpl";

    private static final String CMD_HELP = "help";
    private final CommandExecutor executor;

    public RPCServiceImpl(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public String hello() {
        Log.d(TAG, "hello method involved");

        return "Hello world!";
    }

    @Override
    public RPCResult execute(String cmd) {
        if (cmd == null || cmd.equals("")) {
            return RPCResult.failed("Empty command");
        }

        String[] args = cmd.split(" ");
        Log.d(TAG, "execute: " + args[0]);

        String cmdKey = args[0].toLowerCase();

        if (cmdKey.equals(CMD_HELP)) {
            String helpMsg = executor.help();
            return RPCResult.ok(helpMsg);
        } else {
            return executor.execute(cmdKey, Arrays.copyOfRange(args, 1, args.length));
        }
    }
}
