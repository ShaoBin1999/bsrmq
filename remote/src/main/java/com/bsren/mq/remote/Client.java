package com.bsren.mq.remote;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        NettyRemoteClient client = new NettyRemoteClient();
        client.start();
        client.sendMsg("111");
    }
}
