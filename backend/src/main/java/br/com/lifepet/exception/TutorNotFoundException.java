package br.com.lifepet.exception;

public class TutorNotFoundException extends RuntimeException {

    public TutorNotFoundException(Long id) {
        super("Tutor não encontrado. ID: " + id);
    }

}