package com.bsren.mq.remote;

public class Server {

    public static void main(String[] args) {
        NettyRemoteServer server = new NettyRemoteServer();
        server.start();
    }
}
