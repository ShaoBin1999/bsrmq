package com.bsren.mq.client;

import com.bsren.mq.common.protocol.SendMessageRequestHeader;
import com.bsren.mq.remote.NettyRemoteClient;
import com.bsren.mq.remote.message.Decoder;
import com.bsren.mq.remote.message.Encoder;
import com.bsren.mq.remote.message.Message;
import com.bsren.mq.remote.protocol.RemotingCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Client {


    private final Bootstrap bootstrap = new Bootstrap();
    private Channel channel;

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        Message message = new Message();
        String topic = "topicA";
        message.setTopic(topic);
        String msg = "1234566";
        message.setBody(msg.getBytes(StandardCharsets.UTF_8));
        SendMessageRequestHeader header = new SendMessageRequestHeader();
        header.setTopic(topic);
        client.sendMessage("localhost",8988,message,header);
    }

    public Client() throws InterruptedException {
        this.channel = this.bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new Encoder());
                        pipeline.addLast(new Decoder());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8988))
                .sync()
                .channel();
    }

    public void sendMessage(String address,int port, Message message, SendMessageRequestHeader header){
        InetSocketAddress inetSocketAddress = new InetSocketAddress(address,port);
        RemotingCommand command = new RemotingCommand();
        command.setCommandHeader(header);
        command.setBody(message.getBody());
        System.out.println(command);
        this.channel.writeAndFlush(command);
    }

}
