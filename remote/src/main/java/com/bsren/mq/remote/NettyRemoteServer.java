package com.bsren.mq.remote;

import com.bsren.mq.remote.protocol.RemotingCommand;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class NettyRemoteServer {

    public void start(){
        new ServerBootstrap()
                .group(new NioEventLoopGroup(),new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //添加具体的handler,将ByteBuf转化为字符串
                        ch.pipeline().addLast(new StringDecoder());
                        //自定义handler,连接到读事件，打印字符串
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                .bind(8080);
    }


    private class NettyServerHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            System.out.println(msg);
//            final RemotingCommand cmd = msg;
//            if(cmd==null){
//                return;
//            }
//            if(cmd.getRequestType()){
//                System.out.println("服务端收到请求");
//            }else {
//                System.out.println("服务端收到回复");
//            }
        }
    }
}
