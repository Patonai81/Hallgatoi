package hu.webuni.nyilvantarto.ws;

import lombok.Data;

@Data
public class MessageDTO {

    private Long courseId;
    private String messageTxt;
    private String senderName;
}
