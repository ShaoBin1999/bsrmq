package com.bsren.mq.common.protocol;

import com.bsren.mq.remote.CommandHeader;

public class SendMessageRequestHeader implements CommandHeader {

    private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
