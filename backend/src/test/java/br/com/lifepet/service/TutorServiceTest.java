package br.com.lifepet.service;

import br.com.lifepet.entity.Tutor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TutorServiceTest {

    @Test
    void deveCriarTutor(){

        Tutor tutor = new Tutor();

        tutor.setNome("Vitor");

        assertEquals(
                "Vitor",
                tutor.getNome()
        );

    }
}