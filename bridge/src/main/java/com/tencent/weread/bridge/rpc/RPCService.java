package com.tencent.weread.bridge.rpc;

/**
 * Created by roylkchen on 8/3/16.
 */
public interface RPCService {
    String hello();

    RPCResult execute(String cmd);
}
