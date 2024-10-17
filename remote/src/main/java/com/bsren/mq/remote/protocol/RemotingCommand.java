package com.bsren.mq.remote.protocol;

import io.netty.buffer.ByteBuf;
import lombok.ToString;

import java.nio.ByteBuffer;

@ToString
public class RemotingCommand {

    private CommandHeader commandHeader;

    private byte[] body;

    private boolean requestType;

    public static RemotingCommand decode(ByteBuf buf) {
        int headerLength = buf.readInt();
        int bodyLength = buf.readInt();
        byte[] headerData = new byte[headerLength];
        buf.readBytes(headerData,0,32);
        RemotingCommand command = new RemotingCommand();
        byte[] body = new byte[bodyLength];
        buf.readBytes(body);
        command.setBody(body);
        return command;
    }

    public static RemotingCommand decode(ByteBuffer buffer) {
        int headerLength = buffer.getInt();
        int bodyLength = buffer.get();
        byte[] headerData = new byte[headerLength];
        buffer.get(headerData);
        RemotingCommand command = new RemotingCommand();
        byte[] body = new byte[bodyLength];
        buffer.get(body);
        command.setBody(body);
        return command;
    }

    public CommandHeader getCommandHeader() {
        return commandHeader;
    }

    public void setCommandHeader(CommandHeader commandHeader) {
        this.commandHeader = commandHeader;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public boolean isRequestType() {
        return requestType;
    }

    public void setRequestType(boolean requestType) {
        this.requestType = requestType;
    }

    public ByteBuffer encode(){
        return encode(this.body!=null?this.body.length:0);
    }

    public boolean getRequestType(){
        return requestType;
    }

    //headerLen+bodyLen+header
    public ByteBuffer encode(int bodyLength){
        int len = 8;
        byte[] headerData = new byte[32];   //先固定为32
        len+=headerData.length;
        len+=this.body==null?0:this.body.length;
        ByteBuffer result = ByteBuffer.allocate(len);
        result.putInt(headerData.length);
        result.putInt(this.body==null?0:this.body.length);
        result.put(headerData);
        if(this.body!=null) result.put(this.body);
        result.flip();
        return result;
    }
}
