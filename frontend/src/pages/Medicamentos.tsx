
import { useEffect, useState } from "react";
import api from "../services/api";

interface Pet {
  id: number;
  nome: string;
}

interface Medicamento {
  id: number;
  nome: string;
  dosagem: string;
  frequencia: string;
  dataInicio: string;
  dataFim: string;
  pet?: Pet;
}

interface PaginaMedicamentos {
  content: Medicamento[];
  totalElements: number;
}

interface PaginaPets {
  content: Pet[];
  totalElements: number;
}

export default function Medicamentos() {
  const [medicamentos, setMedicamentos] = useState<Medicamento[]>([]);
  const [pets, setPets] = useState<Pet[]>([]);

  const [nome, setNome] = useState("");
  const [dosagem, setDosagem] = useState("");
  const [frequencia, setFrequencia] = useState("");
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const [petId, setPetId] = useState("");

  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");
  const [carregando, setCarregando] = useState(false);

  // =====================================================
  // CARREGAR MEDICAMENTOS E PETS
  // =====================================================

  async function carregarDados() {
    setErro("");

    // -----------------------------------------
    // CARREGAR MEDICAMENTOS
    // -----------------------------------------

    try {
      const medicamentosResponse =
        await api.get<PaginaMedicamentos>(
          "/medicamentos?page=0&size=50"
        );

      setMedicamentos(medicamentosResponse.data.content);

    } catch (error) {
      console.error(
        "Erro ao carregar medicamentos:",
        error
      );
    }

    // -----------------------------------------
    // CARREGAR PETS
    // -----------------------------------------

    try {
      const petsResponse =
        await api.get<PaginaPets>(
          "/pets?page=0&size=50"
        );

      console.log(
        "PETS RECEBIDOS:",
        petsResponse.data
      );

      setPets(petsResponse.data.content);

    } catch (error) {
      console.error(
        "Erro ao carregar pets:",
        error
      );

      setErro(
        "Não foi possível carregar os pets."
      );
    }
  }

  useEffect(() => {
    carregarDados();
  }, []);

  // =====================================================
  // CADASTRAR MEDICAMENTO
  // =====================================================

  async function cadastrarMedicamento(
    event: React.FormEvent
  ) {
    event.preventDefault();

    setErro("");
    setSucesso("");
    setCarregando(true);

    // Verifica se um pet foi selecionado
    if (!petId) {
      setErro("Selecione um pet.");
      setCarregando(false);
      return;
    }

    try {
      await api.post("/medicamentos", {
        nome,
        dosagem,
        frequencia,
        dataInicio,
        dataFim,
        petId: Number(petId),
      });

      setSucesso(
        "Medicamento cadastrado com sucesso!"
      );

      // Limpar formulário
      setNome("");
      setDosagem("");
      setFrequencia("");
      setDataInicio("");
      setDataFim("");
      setPetId("");

      // Atualizar lista
      await carregarDados();

    } catch (error) {
      console.error(
        "Erro ao cadastrar medicamento:",
        error
      );

      setErro(
        "Não foi possível cadastrar o medicamento."
      );

    } finally {
      setCarregando(false);
    }
  }

  // =====================================================
  // EXCLUIR MEDICAMENTO
  // =====================================================

  async function excluirMedicamento(id: number) {
    const confirmar = window.confirm(
      "Tem certeza que deseja excluir este medicamento?"
    );

    if (!confirmar) {
      return;
    }

    try {
      await api.delete(`/medicamentos/${id}`);

      setSucesso(
        "Medicamento excluído com sucesso!"
      );

      setErro("");

      await carregarDados();

    } catch (error) {
      console.error(
        "Erro ao excluir medicamento:",
        error
      );

      setErro(
        "Não foi possível excluir o medicamento."
      );

      setSucesso("");
    }
  }

  // =====================================================
  // FORMATAR DATA
  // =====================================================

  function formatarData(data: string) {
    if (!data) {
      return "-";
    }

    const partes = data.split("-");

    if (partes.length !== 3) {
      return data;
    }

    return `${partes[2]}/${partes[1]}/${partes[0]}`;
  }

  // =====================================================
  // VERIFICAR SE MEDICAMENTO ESTÁ ATIVO
  // =====================================================

  function medicamentoAtivo(dataFim: string) {
    const hoje = new Date();
    const fim = new Date(
      `${dataFim}T23:59:59`
    );

    return fim >= hoje;
  }

  // =====================================================
  // TELA
  // =====================================================

  return (
    <div className="page-container">

      {/* CABEÇALHO */}

      <div className="page-header">
        <div>
          <h1>💊 Medicamentos</h1>

          <p>
            Gerencie os tratamentos dos pets.
          </p>
        </div>
      </div>

      {/* MENSAGENS */}

      {erro && (
        <div className="alert error">
          {erro}
        </div>
      )}

      {sucesso && (
        <div className="alert success">
          {sucesso}
        </div>
      )}

      <div className="content-grid">

        {/* ================================================= */}
        {/* FORMULÁRIO */}
        {/* ================================================= */}

        <section className="form-card">

          <h2>Novo medicamento</h2>

          <form onSubmit={cadastrarMedicamento}>

            {/* MEDICAMENTO */}

            <div className="form-group">

              <label>
                Medicamento
              </label>

              <input
                type="text"
                placeholder="Ex: Amoxicilina"
                value={nome}
                onChange={(event) =>
                  setNome(event.target.value)
                }
                required
              />

            </div>

            {/* DOSAGEM */}

            <div className="form-group">

              <label>
                Dosagem
              </label>

              <input
                type="text"
                placeholder="Ex: 500mg"
                value={dosagem}
                onChange={(event) =>
                  setDosagem(event.target.value)
                }
                required
              />

            </div>

            {/* FREQUÊNCIA */}

            <div className="form-group">

              <label>
                Frequência
              </label>

              <input
                type="text"
                placeholder="Ex: 8 em 8 horas"
                value={frequencia}
                onChange={(event) =>
                  setFrequencia(event.target.value)
                }
                required
              />

            </div>

            {/* PET */}

            <div className="form-group">

              <label>
                Pet
              </label>

              <select
                value={petId}
                onChange={(event) =>
                  setPetId(event.target.value)
                }
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

            {/* DATAS */}

            <div className="form-row">

              <div className="form-group">

                <label>
                  Data de início
                </label>

                <input
                  type="date"
                  value={dataInicio}
                  onChange={(event) =>
                    setDataInicio(
                      event.target.value
                    )
                  }
                  required
                />

              </div>

              <div className="form-group">

                <label>
                  Data de término
                </label>

                <input
                  type="date"
                  value={dataFim}
                  onChange={(event) =>
                    setDataFim(
                      event.target.value
                    )
                  }
                  required
                />

              </div>

            </div>

            {/* BOTÃO */}

            <button
              type="submit"
              disabled={carregando}
            >
              {carregando
                ? "Cadastrando..."
                : "Cadastrar medicamento"}
            </button>

          </form>

        </section>

        {/* ================================================= */}
        {/* LISTA */}
        {/* ================================================= */}

        <section className="list-card">

          <div className="section-title">

            <div>

              <h2>
                Medicamentos cadastrados
              </h2>

              <p>
                {medicamentos.length} medicamento(s)
              </p>

            </div>

          </div>

          {/* NENHUM MEDICAMENTO */}

          {medicamentos.length === 0 ? (

            <div className="empty-state">

              <span>💊</span>

              <h3>
                Nenhum medicamento cadastrado
              </h3>

              <p>
                Cadastre um medicamento para
                começar a acompanhar os
                tratamentos.
              </p>

            </div>

          ) : (

            <div className="medicamentos-list">

              {medicamentos.map(
                (medicamento) => {

                  const ativo =
                    medicamentoAtivo(
                      medicamento.dataFim
                    );

                  return (

                    <div
                      className="medicamento-card"
                      key={medicamento.id}
                    >

                      {/* TOPO */}

                      <div className="medicamento-top">

                        <div>

                          <h3>
                            {medicamento.nome}
                          </h3>

                          <span
                            className={`status ${
                              ativo
                                ? "status-active"
                                : "status-finished"
                            }`}
                          >
                            {ativo
                              ? "ATIVO"
                              : "FINALIZADO"}
                          </span>

                        </div>

                        <button
                          type="button"
                          className="delete-button"
                          onClick={() =>
                            excluirMedicamento(
                              medicamento.id
                            )
                          }
                        >
                          🗑️
                        </button>

                      </div>

                      {/* INFORMAÇÕES */}

                      <div className="medicamento-info">

                        <div>

                          <strong>
                            🐶 Pet
                          </strong>

                          <span>
                            {medicamento.pet?.nome ||
                              "Não informado"}
                          </span>

                        </div>

                        <div>

                          <strong>
                            💊 Dosagem
                          </strong>

                          <span>
                            {medicamento.dosagem}
                          </span>

                        </div>

                        <div>

                          <strong>
                            ⏱️ Frequência
                          </strong>

                          <span>
                            {medicamento.frequencia}
                          </span>

                        </div>

                        <div>

                          <strong>
                            📅 Início
                          </strong>

                          <span>
                            {formatarData(
                              medicamento.dataInicio
                            )}
                          </span>

                        </div>

                        <div>

                          <strong>
                            🏁 Término
                          </strong>

                          <span>
                            {formatarData(
                              medicamento.dataFim
                            )}
                          </span>

                        </div>

                      </div>

                    </div>

                  );
                }
              )}

            </div>

          )}

        </section>

      </div>

    </div>
  );
}

