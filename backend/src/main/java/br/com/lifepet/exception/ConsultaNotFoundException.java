package br.com.lifepet.exception;

public class ConsultaNotFoundException extends RuntimeException {

    public ConsultaNotFoundException(Long id) {
        super("Consulta não encontrada. ID: " + id);
    }

}