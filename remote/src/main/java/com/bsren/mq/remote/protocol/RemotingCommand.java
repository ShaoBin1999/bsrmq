package com.bsren.mq.remote.protocol;

import com.bsren.mq.remote.CommandHeader;
import com.bsren.mq.remote.Serialize.MQSerializable;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class RemotingCommand {

    private static final AtomicInteger requestId = new AtomicInteger(0);

    private int code;

    private int version;

    private int opaque = requestId.getAndIncrement();

    private Map<String ,String > extFields = new HashMap<>();

    private int serializeType = 0;

    private CommandHeader commandHeader;

    private byte[] body;

    private boolean requestType;


    public static RemotingCommand decode(ByteBuf buf) {
        int headerLength = buf.readInt();
        int bodyLength = buf.readInt();
        byte[] headerData = new byte[headerLength];
        buf.readBytes(headerData,0,headerLength);
        RemotingCommand command = headerDecode(headerData);
        byte[] body = new byte[bodyLength];
        buf.readBytes(body);
        command.setBody(body);
        return command;
    }

    private static RemotingCommand headerDecode(byte[] headerData) {
        RemotingCommand cmd = new RemotingCommand();
        ByteBuffer buffer = ByteBuffer.wrap(headerData);
        int code = buffer.getInt();
        int version = buffer.getInt();
        int opaque = buffer.getInt();
        int extFieldsLen = buffer.getInt();
        byte[] extFields = new byte[extFieldsLen];
        buffer.get(extFields);
        cmd.setCode(code);
        cmd.setVersion(version);
        cmd.setOpaque(opaque);
        cmd.setExtFields(mapDeserialize(extFields));
        return cmd;
    }

    public static Map<String,String> mapDeserialize(byte[] bytes){
        if(bytes==null || bytes.length==0){
            return null;
        }
        HashMap<String,String> map = new HashMap<>();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int keySize,valueSize;
        byte[] keyContent,valueContent;
        while (buffer.hasRemaining()){
            keySize = buffer.getInt();
            keyContent = new byte[keySize];
            buffer.get(keyContent);

            valueSize = buffer.getInt();
            valueContent = new byte[valueSize];
            buffer.get(valueContent);
            map.put(new String(keyContent, StandardCharsets.UTF_8),new String(valueContent,StandardCharsets.UTF_8));
        }
        return map;
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



    public boolean getRequestType(){
        return requestType;
    }

    //headerLen+bodyLen+header
    public ByteBuffer encode() {
        int len = 8;
        byte[] headerData = this.headerEncoder();
        len += headerData.length;
        len += this.body == null ? 0 : this.body.length;
        ByteBuffer result = ByteBuffer.allocate(len);
        result.putInt(headerData.length);
        result.putInt(this.body == null ? 0 : this.body.length);
        result.put(headerData);
        if (this.body != null) result.put(this.body);
        result.flip();
        System.out.println(result);
        return result;
    }

    private byte[] headerEncoder() {
        return MQSerializable.ProtocolEncode(this);
    }


}
