package com.devsenior.cdiaz.bibliokeep.service;

public interface NotificationService {

    void enviarMensaje(String topic, String mensaje);
}