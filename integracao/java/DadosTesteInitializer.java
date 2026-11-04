package br.com.lifepet.config;

import br.com.lifepet.entity.Medicamento;
import br.com.lifepet.entity.Pet;
import br.com.lifepet.entity.Tutor;
import br.com.lifepet.entity.Vacina;
import br.com.lifepet.repository.MedicamentoRepository;
import br.com.lifepet.repository.PetRepository;
import br.com.lifepet.repository.TutorRepository;
import br.com.lifepet.repository.VacinaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DadosTesteInitializer {

    @Bean
    CommandLineRunner criarPetTeste(
            TutorRepository tutorRepository,
            PetRepository petRepository,
            VacinaRepository vacinaRepository,
            MedicamentoRepository medicamentoRepository) {

        return args -> {

            if (petRepository.count() > 0) {
                return;
            }

            Tutor tutor = new Tutor();
            tutor.setNome("Vitor Mendes");
            tutor.setEmail("vitor@email.com");
            tutor.setTelefone("11999999999");
            tutor = tutorRepository.save(tutor);

            Pet pet = new Pet();
            pet.setNome("Thor");
            pet.setIdade(3);
            pet.setRaca("Golden Retriever");
            pet.setEspecie("cachorro");
            pet.setPeso(28.5);
            pet.setTutor(tutor);
            pet = petRepository.save(pet);

            Vacina v10 = new Vacina();
            v10.setNome("V10");
            v10.setDataAplicacao(LocalDate.of(2025, 3, 10));
            v10.setProximaDose(LocalDate.of(2026, 3, 10));
            v10.setPet(pet);
            vacinaRepository.save(v10);

            Vacina antirrabica = new Vacina();
            antirrabica.setNome("Antirrábica");
            antirrabica.setDataAplicacao(LocalDate.of(2026, 6, 15));
            antirrabica.setProximaDose(LocalDate.of(2027, 6, 15));
            antirrabica.setPet(pet);
            vacinaRepository.save(antirrabica);

            Medicamento vermifugo = new Medicamento();
            vermifugo.setNome("Vermífugo");
            vermifugo.setDosagem("1 comprimido");
            vermifugo.setFrequencia("Dose única");
            vermifugo.setDataInicio(LocalDate.of(2026, 10, 1));
            vermifugo.setDataFim(LocalDate.of(2026, 10, 1));
            vermifugo.setPet(pet);
            medicamentoRepository.save(vermifugo);

            System.out.println("PET DE TESTE CRIADO: Thor (id " + pet.getId() + ")");
        };
    }
}