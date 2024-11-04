package com.bsren.mq.broker;

import com.bsren.mq.common.protocol.GetMessageRequestHeader;
import com.bsren.mq.common.protocol.SendMessageRequestHeader;
import com.bsren.mq.remote.CommandHeader;
import com.bsren.mq.remote.message.Decoder;
import com.bsren.mq.remote.message.Encoder;
import com.bsren.mq.remote.protocol.RemotingCommand;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Broker {

    //topic
    private Map<String, List<RemotingCommand>> map = new HashMap<>();

    private Channel channel;

    public Broker(){
        ChannelFuture server = new ServerBootstrap().group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new Encoder());
                        ch.pipeline().addLast(new Decoder());
                        ch.pipeline().addLast(new BrokerHandler());
                    }
                }).bind(8988);
    }

    public static void main(String[] args) {
        Broker broker = new Broker();
    }


    public RemotingCommand receiveTopicMessage(RemotingCommand command){
        CommandHeader commandHeader = command.getCommandHeader();
        SendMessageRequestHeader header = (SendMessageRequestHeader) commandHeader;
        String topic = header.getTopic();
        map.computeIfAbsent(topic, k -> new ArrayList<>());
        map.get(topic).add(command);
        RemotingCommand remotingCommand = new RemotingCommand();
        remotingCommand.setCode(1);
        return remotingCommand;
    }

    public RemotingCommand getTopicMessage(RemotingCommand command){
        CommandHeader commandHeader = command.getCommandHeader();
        GetMessageRequestHeader header = (GetMessageRequestHeader) commandHeader;
        String topic = header.getTopic();
        List<RemotingCommand> commandList = map.get(topic);
        if(commandList==null || commandList.isEmpty()){
            return null;
        }
        return commandList.remove(0);
    }


    private class BrokerHandler extends SimpleChannelInboundHandler<RemotingCommand> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand command) throws Exception {
            RemotingCommand remotingCommand = handleRequest(command);
            ctx.writeAndFlush(remotingCommand);
        }
    }

    public RemotingCommand handleRequest(RemotingCommand command){
        if(command.getCommandHeader().getClass()==GetMessageRequestHeader.class){
            RemotingCommand topicMessage = getTopicMessage(command);
            System.out.println(topicMessage);
            return topicMessage;
        }else if(command.getCommandHeader().getClass()==SendMessageRequestHeader.class){
            RemotingCommand message = receiveTopicMessage(command);
            System.out.println(message);
            return message;
        }
        return null;
    }
}
