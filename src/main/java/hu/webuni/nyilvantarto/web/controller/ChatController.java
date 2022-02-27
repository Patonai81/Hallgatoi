package hu.webuni.nyilvantarto.web.controller;


import hu.webuni.nyilvantarto.ws.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    public void onMessage(MessageDTO messageDTO) throws Exception {
            simpMessagingTemplate.convertAndSend("/topic/myChatRoom/"+messageDTO.getCourseId(),
                    messageDTO.getSenderName()+":"+messageDTO.getMessageTxt());
    }
}
