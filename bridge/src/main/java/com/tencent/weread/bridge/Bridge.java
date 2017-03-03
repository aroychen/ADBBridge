package com.tencent.weread.bridge;

import android.util.Log;

import com.googlecode.jsonrpc4j.JsonRpcBasicServer;
import com.googlecode.jsonrpc4j.StreamServer;
import com.tencent.weread.bridge.command.CommandExecutor;
import com.tencent.weread.bridge.rpc.RPCService;
import com.tencent.weread.bridge.rpc.RPCServiceImpl;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by roylkchen on 10/26/16.
 */
public class Bridge {
    private static final String TAG = "Bridge";

    private static final int PORT = 38300;
    private static final int MAX_THREAD = 2;

    private StreamServer streamServer;
    private RPCServiceImpl serviceImpl;

    private volatile static Bridge sInstance;

    public static Bridge instance() {
        if (sInstance == null) {
            synchronized (Bridge.class) {
                sInstance = new Bridge();
            }
        }

        return sInstance;
    }

    private Bridge() {}

    public boolean connect(ClassLoader classLoader, String executorClassPath) {
        //close older connection.
        close();

        CommandExecutor executor = createExecutor(classLoader, executorClassPath);

        if (executor == null) {
            Log.e(TAG, "Failed to connect bridge");
            return false;
        }

        serviceImpl = new RPCServiceImpl(executor);

        // create the jsonRpcServer
        JsonRpcBasicServer jsonRpcServer = new JsonRpcBasicServer(serviceImpl, RPCService.class);
        // create the stream server
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            streamServer = new StreamServer(jsonRpcServer, MAX_THREAD, serverSocket);

            // start it, this method doesn't block
            streamServer.start();
        } catch (IOException e) {
            Log.d(TAG, "failed to start RPC server:" + e);
            return false;
        }

        return true;
    }

    /**
     * Create an instance of {@link CommandExecutor} by given class path.
     * @param classLoader
     * @param executorClassPath
     * @return
     */
    private CommandExecutor createExecutor(ClassLoader classLoader, String executorClassPath) {
        try {
            Class executorClz = classLoader.loadClass(executorClassPath);
            CommandExecutor executor = (CommandExecutor) executorClz.newInstance();
            return executor;
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "Failed to create instance of executor:" + e);
        } catch (InstantiationException e) {
            Log.e(TAG, "Failed to create instance of executor:" + e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Failed to create instance of executor:" + e);
        }

        return null;
    }

    private void close() {
        StreamServer server = streamServer;
        streamServer = null;
        serviceImpl = null;

        if (server != null) {
            try {
                server.stop();
            } catch (InterruptedException e) {
            }
        }
    }
}
