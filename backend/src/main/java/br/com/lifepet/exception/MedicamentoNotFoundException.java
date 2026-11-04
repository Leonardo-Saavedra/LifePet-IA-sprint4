package br.com.lifepet.exception;

public class MedicamentoNotFoundException extends RuntimeException {

    public MedicamentoNotFoundException(Long id) {
        super("Medicamento não encontrado. ID: " + id);
    }

}