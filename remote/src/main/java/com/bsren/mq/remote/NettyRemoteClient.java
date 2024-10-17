package com.bsren.mq.remote;

import com.bsren.mq.remote.message.Decoder;
import com.bsren.mq.remote.message.Encoder;
import com.bsren.mq.remote.protocol.RemotingCommand;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.EventExecutorGroup;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class NettyRemoteClient {

    private final Bootstrap bootstrap = new Bootstrap();

    private Channel channel;

    public NettyRemoteClient(){}


    public void start() throws InterruptedException {
        this.channel = this.bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new Encoder());
                        pipeline.addLast(new Decoder());
                        pipeline.addLast(new NettyClientHandler());
                    }
                })
                .connect(new InetSocketAddress("localhost", 8080))
                .sync()
                .channel();
    }

    public void sendMsg(String str){
        RemotingCommand command = new RemotingCommand();
        command.setCode(1);
        command.setVersion(2);
        command.setOpaque(3);
        Map<String,String> map = new HashMap<>();
        map.put("kr","邝睿");
        command.setExtFields(map);
        command.setBody(str.getBytes(StandardCharsets.UTF_8));
        System.out.println(command);
        this.channel.writeAndFlush(command);
    }

    private class NettyClientHandler extends SimpleChannelInboundHandler<RemotingCommand> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand cmd) throws Exception {

        }
    }
}
