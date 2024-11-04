package com.bsren.mq.remote.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {

    private String topic;

    private int flag;

    public String getTopic() {
        return topic;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    private Map<String ,String > props;

    private byte[] body;

    public Message(){}

    public Message(String topic,int flag,byte[] body){
        this.topic = topic;
        this.flag = flag;
        this.body = body;
    }

    public void putProp(String key,String val){
        if(props==null){
            props = new HashMap<>();
        }
        props.put(key,val);
    }

    public String  getProp(String key){
        if(props==null){
            props = new HashMap<>();
        }
        return props.get(key);
    }

}
