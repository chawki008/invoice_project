package com.cheikh.invoice.web.websocket;

import static com.cheikh.invoice.config.WebsocketConfiguration.IP_ADDRESS;

import com.cheikh.invoice.domain.TempsTravail;
import com.cheikh.invoice.domain.User;
import com.cheikh.invoice.repository.TempsTravailRepository;
import com.cheikh.invoice.repository.UserRepository;
import com.cheikh.invoice.web.websocket.dto.ActivityDTO;

import java.security.Principal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class ActivityService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;
    private Map<String, TempsTravail> sessionIdToTempsTravail = new HashMap<>();
    private Map<String, ActivityDTO> connectedUsers = new HashMap<>();
    @Autowired
    private TempsTravailRepository tempsTravailRepository;
    @Autowired
    private UserRepository userRepository;

    public ActivityService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/topic/activity")
    @SendTo("/topic/tracker")
    public ActivityDTO sendActivity(@Payload ActivityDTO activityDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
        activityDTO.setUserLogin(principal.getName());
        activityDTO.setSessionId(stompHeaderAccessor.getSessionId());
        System.out.println("SessionID sendactivity: " +stompHeaderAccessor.getSessionId());
        if (sessionIdToTempsTravail.get(stompHeaderAccessor.getSessionId()) == null) {
            TempsTravail tempsTravail = new TempsTravail();
            Optional<User> user = userRepository.findOneByLogin(principal.getName());
            tempsTravail.setUser(user.get());
            tempsTravail.startDate(Instant.now());
            sessionIdToTempsTravail.put(stompHeaderAccessor.getSessionId(), tempsTravail);
        }
        activityDTO.setIpAddress(stompHeaderAccessor.getSessionAttributes().get(IP_ADDRESS).toString());
        activityDTO.setTime(Instant.now());
        if (connectedUsers.get(stompHeaderAccessor.getSessionId()) == null){
            connectedUsers.put(stompHeaderAccessor.getSessionId(), activityDTO);
        }
        System.out.println(connectedUsers.values());
        log.debug("Sending user tracking data {}", activityDTO);
        return activityDTO;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        ActivityDTO activityDTO = new ActivityDTO();
        System.out.println("SessionID activityService: " + event.getSessionId());
        connectedUsers.remove(event.getSessionId());
        TempsTravail tempsTravail = sessionIdToTempsTravail.get(event.getSessionId());
        tempsTravail.setEndDate(Instant.now());
        tempsTravailRepository.save(tempsTravail);
        System.out.println("StartDate: " +tempsTravail.getStartDate().toString());
        activityDTO.setSessionId(event.getSessionId());
        activityDTO.setPage("logout");
        messagingTemplate.convertAndSend("/topic/tracker", activityDTO);
    }
}
