package br.com.lifepet.exception;

public class VacinaNotFoundException extends RuntimeException {

    public VacinaNotFoundException(Long id) {
        super("Vacina não encontrada. ID: " + id);
    }

}