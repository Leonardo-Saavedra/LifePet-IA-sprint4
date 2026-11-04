package br.com.lifepet.exception;

public class HistoricoNotFoundException extends RuntimeException {

    public HistoricoNotFoundException(Long id) {
        super("Histórico não encontrado. ID: " + id);
    }

}