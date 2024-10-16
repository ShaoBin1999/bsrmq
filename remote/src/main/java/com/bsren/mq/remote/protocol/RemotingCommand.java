package com.bsren.mq.remote.protocol;

import java.nio.ByteBuffer;

public class RemotingCommand {

    private CommandHeader commandHeader;

    private byte[] body;

    private boolean requestType;

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

    public ByteBuffer encodeHeader(){
        return encodeHeader(this.body!=null?this.body.length:0);
    }

    public boolean getRequestType(){
        return requestType;
    }

    public ByteBuffer encodeHeader(int length){
        ByteBuffer result = ByteBuffer.allocate(4);
        result.putInt(length);
        result.flip();
        return result;
    }
}
