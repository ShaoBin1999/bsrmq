package com.bsren.mq.remote;

import com.bsren.mq.remote.message.Decoder;
import com.bsren.mq.remote.message.Encoder;
import com.bsren.mq.remote.protocol.RemotingCommand;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyRemoteServer {

    public void start(){
        new ServerBootstrap()
                .group(new NioEventLoopGroup(),new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new Encoder());
                        ch.pipeline().addLast(new Decoder());
                        ch.pipeline().addLast(new NettyServerHandler());
                    }
                })
                .bind(8080);
    }


    private class NettyServerHandler extends SimpleChannelInboundHandler<RemotingCommand> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
        }
    }
}
