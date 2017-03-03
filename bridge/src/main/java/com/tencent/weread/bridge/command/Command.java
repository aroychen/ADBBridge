package com.tencent.weread.bridge.command;

import com.tencent.weread.bridge.rpc.RPCResult;

/**
 * Created by roylkchen on 10/31/16.
 */
public interface Command {

    String description();

    String commandKey();

    RPCResult execute(String[] args);
}
