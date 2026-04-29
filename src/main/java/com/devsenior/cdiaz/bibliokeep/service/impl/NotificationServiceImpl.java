package com.devsenior.cdiaz.bibliokeep.service.impl;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.devsenior.cdiaz.bibliokeep.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final SimpMessagingTemplate template;

    @Override
    public void enviarMensaje(String topic, String mensaje) {
        template.convertAndSend("/topic/" + topic, mensaje);
    }
    
}
