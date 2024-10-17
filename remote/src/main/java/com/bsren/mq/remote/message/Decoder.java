package com.bsren.mq.remote.message;

import com.bsren.mq.remote.protocol.RemotingCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


public class Decoder extends LengthFieldBasedFrameDecoder {

    public Decoder() {
        super(16777216, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        RemotingCommand remotingCommand = RemotingCommand.decode(in);
        System.out.println(remotingCommand);
        return remotingCommand;
    }
}
