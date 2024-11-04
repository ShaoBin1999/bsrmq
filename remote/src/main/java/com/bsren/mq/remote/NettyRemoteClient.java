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
import java.util.Random;
import java.util.UUID;

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

    public void invokeOneway(Channel channel,RemotingCommand command){
        this.channel.writeAndFlush(command);
    }

    private class NettyClientHandler extends SimpleChannelInboundHandler<RemotingCommand> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand cmd) throws Exception {

        }
    }

    public RemotingCommand genRandomRemotingCommand(){
        Random random = new Random();
        RemotingCommand cmd = new RemotingCommand();
        cmd.setOpaque(random.nextInt());
        cmd.setVersion(1);
        cmd.setCode(random.nextInt(100));
        cmd.setBody(UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
        return cmd;
    }


}
