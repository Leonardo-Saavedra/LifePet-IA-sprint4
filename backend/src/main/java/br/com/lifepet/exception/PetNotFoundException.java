package br.com.lifepet.exception;

public class PetNotFoundException extends RuntimeException {

    public PetNotFoundException(Long id) {
        super("Pet não encontrado. ID: " + id);
    }

}