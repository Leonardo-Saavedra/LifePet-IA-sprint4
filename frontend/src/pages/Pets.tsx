import { useEffect, useState } from "react";
import api from "../services/api";

interface Pet {
  id: number;
  nome: string;
  especie: string;
  raca: string;
  idade: number;
  peso: number;
  tutorId: number;
}

export default function Pets() {
  const [pets, setPets] = useState<Pet[]>([]);
  const [carregando, setCarregando] = useState(true);

  const [nome, setNome] = useState("");
  const [especie, setEspecie] = useState("");
  const [raca, setRaca] = useState("");
  const [idade, setIdade] = useState("");
  const [peso, setPeso] = useState("");

  const perfil = localStorage.getItem("perfil");

  async function carregarPets() {
    try {
      const response = await api.get("/pets");

      setPets(response.data.content);
    } catch (error) {
      console.error("Erro ao carregar pets:", error);
    } finally {
      setCarregando(false);
    }
  }

  useEffect(() => {
    carregarPets();
  }, []);

  async function cadastrarPet(event: React.FormEvent) {
    event.preventDefault();

    try {
      await api.post("/pets", {
        nome,
        especie,
        raca,
        idade: Number(idade),
        peso: Number(peso),
        tutorId: 1,
      });

      alert("Pet cadastrado com sucesso!");

      setNome("");
      setEspecie("");
      setRaca("");
      setIdade("");
      setPeso("");

      carregarPets();
    } catch (error) {
      console.error("Erro ao cadastrar pet:", error);
      alert("Não foi possível cadastrar o pet.");
    }
  }

  return (
    <div className="pets-page">

      <div className="pets-header">
        <div>
          <h1>🐾 Meus Pets</h1>
          <p>Gerencie os pets cadastrados no LifePet.</p>
        </div>
      </div>

      {perfil === "TUTOR" && (
        <div className="pet-form-card">
          <h2>Adicionar novo pet</h2>

          <form onSubmit={cadastrarPet}>

            <div>
              <label>Nome</label>
              <input
                type="text"
                placeholder="Ex: Rex"
                value={nome}
                onChange={(e) => setNome(e.target.value)}
                required
              />
            </div>

            <div>
              <label>Espécie</label>
              <input
                type="text"
                placeholder="Ex: Cachorro"
                value={especie}
                onChange={(e) => setEspecie(e.target.value)}
                required
              />
            </div>

            <div>
              <label>Raça</label>
              <input
                type="text"
                placeholder="Ex: Golden Retriever"
                value={raca}
                onChange={(e) => setRaca(e.target.value)}
                required
              />
            </div>

            <div>
              <label>Idade</label>
              <input
                type="number"
                min="0"
                placeholder="Ex: 3"
                value={idade}
                onChange={(e) => setIdade(e.target.value)}
                required
              />
            </div>

            <div>
              <label>Peso (kg)</label>
              <input
                type="number"
                step="0.1"
                min="0.1"
                placeholder="Ex: 25.5"
                value={peso}
                onChange={(e) => setPeso(e.target.value)}
                required
              />
            </div>

            <button type="submit">
              Cadastrar Pet
            </button>

          </form>
        </div>
      )}

      <div className="pets-list">

        <h2>Pets cadastrados</h2>

        {carregando ? (
          <p>Carregando pets...</p>
        ) : pets.length === 0 ? (
          <p>Nenhum pet cadastrado.</p>
        ) : (
          <div className="pets-grid">

            {pets.map((pet) => (
              <div className="pet-card" key={pet.id}>

                <div className="pet-icon">
                  🐶
                </div>

                <h3>{pet.nome}</h3>

                <p>
                  <strong>Espécie:</strong> {pet.especie}
                </p>

                <p>
                  <strong>Raça:</strong> {pet.raca}
                </p>

                <p>
                  <strong>Idade:</strong> {pet.idade} anos
                </p>

                <p>
                  <strong>Peso:</strong> {pet.peso} kg
                </p>

              </div>
            ))}

          </div>
        )}

      </div>

    </div>
  );
}