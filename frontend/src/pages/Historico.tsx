import { useEffect, useState } from "react";
import api from "../services/api";

interface Historico {
  id: number;
  descricao: string;
  dataRegistro: string;
  observacoes: string;
  petId: number;
}

export default function Historico() {
  const [historicos, setHistoricos] = useState<Historico[]>([]);

  const [descricao, setDescricao] = useState("");
  const [dataRegistro, setDataRegistro] = useState("");
  const [observacoes, setObservacoes] = useState("");
  const [petId, setPetId] = useState("");

  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState("");

  async function carregarHistorico() {
    try {
      const response = await api.get("/historicos");

      const dados = response.data;

      if (Array.isArray(dados)) {
        setHistoricos(dados);
      } else if (dados.content) {
        setHistoricos(dados.content);
      } else {
        setHistoricos([]);
      }
    } catch {
      setErro("Não foi possível carregar o histórico.");
    }
  }

  useEffect(() => {
    carregarHistorico();
  }, []);

  async function cadastrarHistorico(event: React.FormEvent) {
    event.preventDefault();

    setErro("");
    setCarregando(true);

    try {
      await api.post("/historicos", {
        descricao,
        dataRegistro,
        observacoes,
        petId: Number(petId),
      });

      setDescricao("");
      setDataRegistro("");
      setObservacoes("");
      setPetId("");

      await carregarHistorico();
    } catch {
      setErro("Não foi possível registrar o histórico.");
    } finally {
      setCarregando(false);
    }
  }

  async function excluirHistorico(id: number) {
    const confirmar = window.confirm(
      "Tem certeza que deseja excluir este registro?"
    );

    if (!confirmar) {
      return;
    }

    try {
      await api.delete(`/historicos/${id}`);
      await carregarHistorico();
    } catch {
      setErro("Não foi possível excluir o registro.");
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
          <h1>📋 Histórico</h1>
          <p>Acompanhe o histórico de saúde dos pets</p>
        </div>
      </div>

      <div className="form-card">
        <h2>Novo registro</h2>

        <form onSubmit={cadastrarHistorico}>
          <div className="form-group">
            <label>Descrição</label>

            <input
              type="text"
              placeholder="Ex: Consulta de rotina"
              value={descricao}
              onChange={(event) => setDescricao(event.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Data do registro</label>

            <input
              type="date"
              value={dataRegistro}
              onChange={(event) => setDataRegistro(event.target.value)}
              required
            />
          </div>

          <div className="form-group">
            <label>Observações</label>

            <textarea
              placeholder="Digite as observações..."
              value={observacoes}
              onChange={(event) => setObservacoes(event.target.value)}
              rows={4}
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
            {carregando ? "Salvando..." : "Adicionar ao histórico"}
          </button>
        </form>
      </div>

      <div className="list-card">
        <div className="list-header">
          <h2>Histórico dos pets</h2>
          <span>{historicos.length} registro(s)</span>
        </div>

        {historicos.length === 0 ? (
          <div className="empty-state">
            <div className="empty-icon">📋</div>
            <h3>Nenhum registro encontrado</h3>
            <p>Os registros clínicos aparecerão aqui.</p>
          </div>
        ) : (
          <div className="items-list">
            {historicos.map((historico) => (
              <div className="item-card" key={historico.id}>
                <div className="item-icon">📋</div>

                <div className="item-info">
                  <h3>{historico.descricao}</h3>

                  <p>
                    <strong>Data:</strong>{" "}
                    {formatarData(historico.dataRegistro)}
                  </p>

                  <p>
                    <strong>Pet:</strong> #{historico.petId}
                  </p>

                  {historico.observacoes && (
                    <p>
                      <strong>Observações:</strong>{" "}
                      {historico.observacoes}
                    </p>
                  )}
                </div>

                <button
                  className="delete-button"
                  onClick={() => excluirHistorico(historico.id)}
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