package br.com.lifepet.service;

import br.com.lifepet.entity.Pet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetServiceTest {

    @Test
    void deveCriarPet(){

        Pet pet = new Pet();

        pet.setNome("Thor");

        assertEquals(
                "Thor",
                pet.getNome()
        );

    }

}
