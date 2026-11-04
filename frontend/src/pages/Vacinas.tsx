import { useEffect, useState } from "react";
import api from "../services/api";

interface Vacina {
  id: number;
  nome: string;
  dataAplicacao: string;
  proximaDose: string;
  petId: number;
}

export default function Vacinas() {
  const [vacinas, setVacinas] = useState<Vacina[]>([]);
  const [nome, setNome] = useState("");
  const [dataAplicacao, setDataAplicacao] = useState("");
  const [proximaDose, setProximaDose] = useState("");
  const [petId, setPetId] = useState("");
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState("");

  async function carregarVacinas() {
    try {
      const response = await api.get("/vacinas");

      const dados = response.data;

      if (Array.isArray(dados)) {
        setVacinas(dados);
      } else if (dados.content) {
        setVacinas(dados.content);
      } else {
        setVacinas([]);
      }
    } catch {
      setErro("Não foi possível carregar as vacinas.");
    }
  }

  useEffect(() => {
    carregarVacinas();
  }, []);

  async function cadastrarVacina(event: React.FormEvent) {
    event.preventDefault();

    setErro("");
    setCarregando(true);

    try {
      await api.post("/vacinas", {
        nome,
        dataAplicacao,
        proximaDose,
        petId: Number(petId),
      });

      setNome("");
      setDataAplicacao("");
      setProximaDose("");
      setPetId("");

      await carregarVacinas();
    } catch {
      setErro("Não foi possível cadastrar a vacina.");
    } finally {
      setCarregando(false);
    }
  }

  async function excluirVacina(id: number) {
    const confirmar = window.confirm(
      "Tem certeza que deseja excluir esta vacina?"
    );

    if (!confirmar) {
      return;
    }

    try {
      await api.delete(`/vacinas/${id}`);
      await carregarVacinas();
    } catch {
      setErro("Não foi possível excluir a vacina.");
    }
  }

  function formatarData(data: string) {
    if (!data) return "-";

    const partes = data.split("-");

    if (partes.length !== 3) {
      return data;
    }

    return `${partes[2]}/${partes[1]}/${partes[0]}`;
  }

  return (
    <div className="page-container">
      <div className="page-header">
        <div>
          <h1>💉 Vacinas</h1>
          <p>Controle de vacinação dos pets</p>
        </div>
      </div>

      <div className="form-card">
        <h2>Registrar vacina</h2>

        <form onSubmit={cadastrarVacina}>
          <div className="form-group">
            <label>Nome da vacina</label>

            <input
              type="text"
              placeholder="Ex: V10"
              value={nome}
              onChange={(event) => setNome(event.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Data de aplicação</label>

            <input
              type="date"
              value={dataAplicacao}
              onChange={(event) => setDataAplicacao(event.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Próxima dose</label>

            <input
              type="date"
              value={proximaDose}
              onChange={(event) => setProximaDose(event.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>ID do pet</label>

            <input
              type="number"
              min="1"
              placeholder="Ex: 1"
              value={petId}
              onChange={(event) => setPetId(event.target.value)}
              required
            />
          </div>

          {erro && <p className="error-message">{erro}</p>}

          <button type="submit" disabled={carregando}>
            {carregando ? "Salvando..." : "Registrar vacina"}
          </button>
        </form>
      </div>

      <div className="list-card">
        <div className="list-header">
          <h2>Vacinas cadastradas</h2>
          <span>{vacinas.length} registro(s)</span>
        </div>

        {vacinas.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">💉</div>
            <h3>Nenhuma vacina cadastrada</h3>
            <p>Cadastre a primeira vacina do seu pet.</p>
          </div>
        ) : (
          <div className="items-list">
            {vacinas.map((vacina) => (
              <div className="item-card" key={vacina.id}>
                <div className="item-icon">💉</div>

                <div className="item-info">
                  <h3>{vacina.nome}</h3>

                  <p>
                    <strong>Aplicação:</strong>{" "}
                    {formatarData(vacina.dataAplicacao)}
                  </p>

                  <p>
                    <strong>Próxima dose:</strong>{" "}
                    {formatarData(vacina.proximaDose)}
                  </p>

                  <p>
                    <strong>Pet:</strong> #{vacina.petId}
                  </p>
                </div>

                <button
                  className="delete-button"
                  onClick={() => excluirVacina(vacina.id)}
                >
                  Excluir
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}