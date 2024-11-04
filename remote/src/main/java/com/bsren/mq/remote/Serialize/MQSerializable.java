package com.bsren.mq.remote.Serialize;

import com.bsren.mq.remote.protocol.RemotingCommand;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MQSerializable {

    public static byte[] ProtocolEncode(RemotingCommand cmd) {
        byte[] mapSerialize = mapSerialize(cmd.getExtFields());
        int len = 4+4+4+4+(mapSerialize==null?0:mapSerialize.length);

        ByteBuffer buffer = ByteBuffer.allocate(len);
        //1.code
        buffer.putInt(cmd.getCode());
        //2.version
        buffer.putInt(cmd.getVersion());
        //3.opaque
        buffer.putInt(cmd.getOpaque());
        //4.extFields
        if(cmd.getExtFields()!=null && !cmd.getExtFields().isEmpty()){
            buffer.putInt(mapSerialize.length);
            buffer.put(mapSerialize);
        }
        return buffer.array();
    }

    public static byte[] mapSerialize(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        int total_len = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key != null && value != null) {
                int kv_len = 4 + key.getBytes(StandardCharsets.UTF_8).length
                        + 4 + value.getBytes(StandardCharsets.UTF_8).length;
                total_len += kv_len;
            }
        }
        ByteBuffer content = ByteBuffer.allocate(total_len);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(key!=null && value!=null){
                content.putInt(key.getBytes(StandardCharsets.UTF_8).length);
                content.put(key.getBytes(StandardCharsets.UTF_8));
                content.putInt(value.getBytes(StandardCharsets.UTF_8).length);
                content.put(value.getBytes(StandardCharsets.UTF_8));
            }
        }
        return content.array();
    }
}
