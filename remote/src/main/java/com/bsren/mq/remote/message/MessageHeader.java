package com.bsren.mq.remote.message;

import com.bsren.mq.remote.protocol.CommandHeader;
import lombok.Data;

@Data
public class MessageHeader extends CommandHeader {

    String topic;
    int count;

}
