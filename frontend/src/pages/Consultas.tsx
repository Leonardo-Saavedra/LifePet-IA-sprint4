
import { useEffect, useState } from "react";
import api from "../services/api";

interface Pet {
  id: number;
  nome: string;
}

interface Consulta {
  id: number;
  data: string;
  veterinario: string;
  observacoes: string;
  status: "AGENDADA" | "CONFIRMADA" | "REALIZADA" | "CANCELADA";
  pet?: Pet;
}

interface PaginaConsultas {
  content: Consulta[];
  totalElements: number;
  totalPages: number;
  number: number;
}

export default function Consultas() {
  const [consultas, setConsultas] = useState<Consulta[]>([]);
  const [pets, setPets] = useState<Pet[]>([]);

  const [data, setData] = useState("");
  const [veterinario, setVeterinario] = useState("");
  const [observacoes, setObservacoes] = useState("");
  const [petId, setPetId] = useState("");

  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");
  const [carregando, setCarregando] = useState(false);

  async function carregarConsultas() {
    try {
      const response = await api.get<PaginaConsultas>("/consultas");

      setConsultas(response.data.content);
    } catch (error) {
      console.error(error);
      setErro("Não foi possível carregar as consultas.");
    }
  }

  async function carregarPets() {
    try {
      const response = await api.get("/pets");

      setPets(response.data.content);
    } catch (error) {
      console.error(error);
    }
  }

  useEffect(() => {
    carregarConsultas();
    carregarPets();
  }, []);

  async function agendarConsulta(event: React.FormEvent) {
    event.preventDefault();

    setErro("");
    setSucesso("");
    setCarregando(true);

    try {
      await api.post("/consultas", {
        data,
        veterinario,
        observacoes,
        pet: {
          id: Number(petId),
        },
      });

      setSucesso("Consulta agendada com sucesso!");

      setData("");
      setVeterinario("");
      setObservacoes("");
      setPetId("");

      await carregarConsultas();
    } catch (error: any) {
      console.error(error);

      if (error.response?.data?.mensagem) {
        setErro(error.response.data.mensagem);
      } else {
        setErro("Não foi possível agendar a consulta.");
      }
    } finally {
      setCarregando(false);
    }
  }

  async function confirmarConsulta(id: number) {
    try {
      await api.put(`/consultas/${id}/confirmar`);

      await carregarConsultas();
    } catch (error) {
      console.error(error);
      setErro("Não foi possível confirmar a consulta.");
    }
  }

  async function realizarConsulta(id: number) {
    try {
      await api.put(`/consultas/${id}/realizar`);

      await carregarConsultas();
    } catch (error) {
      console.error(error);
      setErro("Não foi possível realizar a consulta.");
    }
  }

  async function cancelarConsulta(id: number) {
    try {
      await api.put(`/consultas/${id}/cancelar`);

      await carregarConsultas();
    } catch (error) {
      console.error(error);
      setErro("Não foi possível cancelar a consulta.");
    }
  }

  function corStatus(status: Consulta["status"]) {
    switch (status) {
      case "AGENDADA":
        return "status agendada";

      case "CONFIRMADA":
        return "status confirmada";

      case "REALIZADA":
        return "status realizada";

      case "CANCELADA":
        return "status cancelada";

      default:
        return "status";
    }
  }

  return (
    <div className="consultas-container">

      <div className="consultas-header">
        <div>
          <h1>📅 Consultas</h1>
          <p>Gerencie as consultas veterinárias dos pets.</p>
        </div>
      </div>

      {erro && (
        <div className="message error-message">
          {erro}
        </div>
      )}

      {sucesso && (
        <div className="message success-message">
          {sucesso}
        </div>
      )}

      <div className="consulta-form-card">

        <h2>Agendar consulta</h2>

        <form onSubmit={agendarConsulta}>

          <div className="form-group">

            <label>Pet</label>

            <select
              value={petId}
              onChange={(event) => setPetId(event.target.value)}
              required
            >
              <option value="">
                Selecione o pet
              </option>

              {pets.map((pet) => (
                <option
                  key={pet.id}
                  value={pet.id}
                >
                  {pet.nome}
                </option>
              ))}
            </select>

          </div>

          <div className="form-group">

            <label>Data</label>

            <input
              type="date"
              value={data}
              onChange={(event) => setData(event.target.value)}
              required
            />

          </div>

          <div className="form-group">

            <label>Veterinário</label>

            <input
              type="text"
              placeholder="Nome do veterinário"
              value={veterinario}
              onChange={(event) =>
                setVeterinario(event.target.value)
              }
              required
            />

          </div>

          <div className="form-group">

            <label>Observações</label>

            <textarea
              placeholder="Digite as observações da consulta"
              value={observacoes}
              onChange={(event) =>
                setObservacoes(event.target.value)
              }
              required
            />

          </div>

          <button
            type="submit"
            disabled={carregando}
          >
            {carregando
              ? "Agendando..."
              : "Agendar consulta"}
          </button>

        </form>

      </div>

      <div className="consultas-list">

        <h2>Consultas cadastradas</h2>

        {consultas.length === 0 ? (
          <div className="empty-message">
            Nenhuma consulta cadastrada.
          </div>
        ) : (

          <div className="consulta-grid">

            {consultas.map((consulta) => (

              <div
                className="consulta-card"
                key={consulta.id}
              >

                <div className="consulta-card-header">

                  <h3>
                    Consulta #{consulta.id}
                  </h3>

                  <span className={corStatus(consulta.status)}>
                    {consulta.status}
                  </span>

                </div>

                <div className="consulta-info">

                  <p>
                    <strong>🐾 Pet:</strong>{" "}
                    {consulta.pet?.nome ?? "Pet não informado"}
                  </p>

                  <p>
                    <strong>📅 Data:</strong>{" "}
                    {new Date(
                      consulta.data + "T00:00:00"
                    ).toLocaleDateString("pt-BR")}
                  </p>

                  <p>
                    <strong>👨‍⚕️ Veterinário:</strong>{" "}
                    {consulta.veterinario}
                  </p>

                  <p>
                    <strong>📝 Observações:</strong>{" "}
                    {consulta.observacoes}
                  </p>

                </div>

                <div className="consulta-actions">

                  {consulta.status === "AGENDADA" && (
                    <button
                      onClick={() =>
                        confirmarConsulta(consulta.id)
                      }
                    >
                      Confirmar
                    </button>
                  )}

                  {consulta.status === "CONFIRMADA" && (
                    <button
                      onClick={() =>
                        realizarConsulta(consulta.id)
                      }
                    >
                      Realizar
                    </button>
                  )}

                  {consulta.status !== "REALIZADA" &&
                    consulta.status !== "CANCELADA" && (
                      <button
                        onClick={() =>
                          cancelarConsulta(consulta.id)
                        }
                      >
                        Cancelar
                      </button>
                    )}

                </div>

              </div>

            ))}

          </div>

        )}

      </div>

    </div>
  );
}


