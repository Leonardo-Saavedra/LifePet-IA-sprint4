package br.com.lifepet.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do pet é obrigatório")
    private String nome;

    @NotNull(message = "Idade é obrigatória")
    @Min(value = 0, message = "Idade não pode ser negativa")
    private Integer idade;

    @NotBlank(message = "Raça é obrigatória")
    private String raca;

    @NotBlank(message = "Espécie é obrigatória")
    private String especie;

    @NotNull(message = "Peso é obrigatório")
    @Positive(message = "Peso deve ser maior que zero")
    private Double peso;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    @JsonBackReference
    private Tutor tutor;

    @OneToMany(mappedBy = "pet")
    @JsonManagedReference
    private List<Vacina> vacinas;

    @OneToMany(mappedBy = "pet")
    @JsonManagedReference
    private List<Consulta> consultas;

    @OneToMany(mappedBy = "pet")
    @JsonManagedReference
    private List<Medicamento> medicamentos;

    @OneToMany(mappedBy = "pet")
    @JsonManagedReference
    private List<Historico> historicos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public List<Vacina> getVacinas() {
        return vacinas;
    }

    public void setVacinas(List<Vacina> vacinas) {
        this.vacinas = vacinas;
    }

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }

    public List<Historico> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(List<Historico> historicos) {
        this.historicos = historicos;
    }
}
