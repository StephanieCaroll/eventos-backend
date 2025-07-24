package br.com.ifpe.eventos.util.exception;

public class EventoException extends RuntimeException {

    public static final String MSG_EVENTO_NAO_ENCONTRADO = "Evento não encontrado.";

    public EventoException(String msg) {
        super(msg);
    }
}