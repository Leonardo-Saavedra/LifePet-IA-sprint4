package br.com.lifepet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> tratarErro(RuntimeException ex){

        Map<String,Object> erro = new HashMap<>();

        erro.put(
                "timestamp",
                LocalDateTime.now()
        );

        erro.put(
                "status",
                HttpStatus.NOT_FOUND.value()
        );

        erro.put(
                "mensagem",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erro);
    }

}