package com.tencent.weread.bridge.rpc;

/**
 * Created by roylkchen on 10/28/16.
 */
public class RPCResult {
    private static final int OK = 0;
    private static final int FAILED = 1;

    private int status;
    private String prompt;

    private RPCResult(int status, String prompt) {
        this.status = status;
        this.prompt = prompt;
    }

    public int getStatus() {
        return status;
    }

    public String getPrompt() {
        return prompt;
    }

    @Override
    public String toString() {
        return (status == OK ? "[OK]" : "[FAILED]") + prompt;
    }

    public static RPCResult ok(String result) {
        return new RPCResult(OK, result);
    }

    public static RPCResult failed(String prompt) {
        return new RPCResult(FAILED, prompt);
    }
}
