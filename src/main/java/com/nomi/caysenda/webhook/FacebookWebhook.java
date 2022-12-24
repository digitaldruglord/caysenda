package com.nomi.caysenda.webhook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/facebook/messager")
public class FacebookWebhook {
    @GetMapping("")
    ResponseEntity<String> messengerHook(@RequestParam Map<String,String> allRequestParams){
        String challenge = allRequestParams.get("hub.challenge");
        if (challenge!=null) System.out.println(challenge);
       return ResponseEntity.ok(challenge);
    }
    @PostMapping("")
    ResponseEntity<String> handleEvent(@RequestBody Map params){
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("hadle event messager");
        try {
           String s =  mapper.writeValueAsString(params);
           if (s!=null){
               System.out.println(s);
           }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("EVENT_RECEIVED");
    }
}
