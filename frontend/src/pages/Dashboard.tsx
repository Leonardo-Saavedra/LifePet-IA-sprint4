import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
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

interface Consulta {
  id: number;
  data: string;
  veterinario: string;
  observacoes: string;
  status: string;
}

interface Medicamento {
  id: number;
  nome: string;
  dosagem: string;
  frequencia: string;
  dataInicio: string;
  dataFim: string;
  petId: number;
}

interface Vacina {
  id: number;
  nome: string;
  dataAplicacao: string;
  proximaDose: string;
  petId: number;
}

export default function Dashboard() {
  const [pets, setPets] = useState<Pet[]>([]);
  const [consultas, setConsultas] = useState<Consulta[]>([]);
  const [medicamentos, setMedicamentos] = useState<Medicamento[]>([]);
  const [vacinas, setVacinas] = useState<Vacina[]>([]);

  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState("");

  const perfil = localStorage.getItem("perfil");

  async function carregarDashboard() {
    setCarregando(true);
    setErro("");

    try {
      const [
        petsResponse,
        consultasResponse,
        medicamentosResponse,
        vacinasResponse,
      ] = await Promise.allSettled([
        api.get("/pets"),
        api.get("/consultas"),
        api.get("/medicamentos"),
        api.get("/vacinas"),
      ]);

      if (petsResponse.status === "fulfilled") {
        const data = petsResponse.value.data;

        setPets(
          Array.isArray(data)
            ? data
            : data.content || []
        );
      }

      if (consultasResponse.status === "fulfilled") {
        const data = consultasResponse.value.data;

        setConsultas(
          Array.isArray(data)
            ? data
            : data.content || []
        );
      }

      if (medicamentosResponse.status === "fulfilled") {
        const data = medicamentosResponse.value.data;

        setMedicamentos(
          Array.isArray(data)
            ? data
            : data.content || []
        );
      }

      if (vacinasResponse.status === "fulfilled") {
        const data = vacinasResponse.value.data;

        setVacinas(
          Array.isArray(data)
            ? data
            : data.content || []
        );
      }
    } catch (error) {
      console.error("Erro ao carregar dashboard:", error);
      setErro("Não foi possível carregar os dados do dashboard.");
    } finally {
      setCarregando(false);
    }
  }

  useEffect(() => {
    carregarDashboard();
  }, []);

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("perfil");

    window.location.href = "/";
  }

  function formatarData(data: string) {
    if (!data) {
      return "-";
    }

    const [ano, mes, dia] = data.split("-");

    return `${dia}/${mes}/${ano}`;
  }

  function getStatusClass(status: string) {
    switch (status) {
      case "AGENDADA":
        return "status status-agendada";

      case "CONFIRMADA":
        return "status status-confirmada";

      case "REALIZADA":
        return "status status-realizada";

      case "CANCELADA":
        return "status status-cancelada";

      default:
        return "status";
    }
  }

  const consultasProximas = consultas
    .filter(
      (consulta) =>
        consulta.status === "AGENDADA" ||
        consulta.status === "CONFIRMADA"
    )
    .sort((a, b) =>
      a.data.localeCompare(b.data)
    )
    .slice(0, 5);

  const medicamentosAtivos = medicamentos.filter(
    (medicamento) =>
      medicamento.dataFim &&
      new Date(medicamento.dataFim) >= new Date()
  );

  const vacinasProximas = vacinas
    .filter(
      (vacina) =>
        vacina.proximaDose &&
        new Date(vacina.proximaDose) >= new Date()
    )
    .sort((a, b) =>
      a.proximaDose.localeCompare(b.proximaDose)
    )
    .slice(0, 5);

  return (
    <div className="dashboard-page">

      {/* HEADER */}
      <header className="dashboard-header">

        <div>
          <h1>🐾 LifePet</h1>

          <p>
            Cuidados inteligentes para seu pet
          </p>
        </div>

        <div className="dashboard-user">

          <span>
            Perfil:{" "}
            <strong>
              {perfil || "USUÁRIO"}
            </strong>
          </span>

          <button
            type="button"
            onClick={logout}
            className="logout-button"
          >
            Sair
          </button>

        </div>

      </header>

      {/* CONTEÚDO */}
      <main className="dashboard-content">

        {/* BOAS-VINDAS */}
        <div className="welcome-section">

          <div>

            <h2>
              Olá! 👋
            </h2>

            <p>
              Bem-vindo ao seu painel do LifePet.
              Acompanhe todas as informações dos
              seus pets em um só lugar.
            </p>

          </div>

        </div>

        {/* ERRO */}
        {erro && (
          <div className="error-message">
            {erro}
          </div>
        )}

        {/* CARDS */}
        <section className="dashboard-cards">

          {/* PETS */}
          <Link
            to="/pets"
            className="dashboard-card"
          >

            <div className="card-icon">
              🐶
            </div>

            <div>

              <h3>
                Pets
              </h3>

              <strong>
                {carregando
                  ? "..."
                  : pets.length}
              </strong>

              <p>
                Pets cadastrados
              </p>

            </div>

          </Link>

          {/* CONSULTAS */}
          <Link
            to="/consultas"
            className="dashboard-card"
          >

            <div className="card-icon">
              🩺
            </div>

            <div>

              <h3>
                Consultas
              </h3>

              <strong>
                {carregando
                  ? "..."
                  : consultas.length}
              </strong>

              <p>
                Consultas registradas
              </p>

            </div>

          </Link>

          {/* MEDICAMENTOS */}
          <Link
            to="/medicamentos"
            className="dashboard-card"
          >

            <div className="card-icon">
              💊
            </div>

            <div>

              <h3>
                Medicamentos
              </h3>

              <strong>
                {carregando
                  ? "..."
                  : medicamentosAtivos.length}
              </strong>

              <p>
                Tratamentos ativos
              </p>

            </div>

          </Link>

          {/* VACINAS */}
          <Link
            to="/vacinas"
            className="dashboard-card"
          >

            <div className="card-icon">
              💉
            </div>

            <div>

              <h3>
                Vacinas
              </h3>

              <strong>
                {carregando
                  ? "..."
                  : vacinas.length}
              </strong>

              <p>
                Vacinas registradas
              </p>

            </div>

          </Link>

        </section>

        {/* ACESSO RÁPIDO */}
        <section className="dashboard-section">

          <div className="section-header">

            <div>

              <h2>
                Acesso rápido
              </h2>

              <p>
                Gerencie as principais informações
                do LifePet.
              </p>

            </div>

          </div>

          <div className="quick-actions">

            <Link
              to="/pets"
              className="quick-action"
            >

              <span>
                🐾
              </span>

              <div>

                <strong>
                  Meus Pets
                </strong>

                <small>
                  Visualizar e gerenciar pets
                </small>

              </div>

            </Link>

            <Link
              to="/consultas"
              className="quick-action"
            >

              <span>
                🩺
              </span>

              <div>

                <strong>
                  Consultas
                </strong>

                <small>
                  Acompanhar consultas
                </small>

              </div>

            </Link>

            <Link
              to="/medicamentos"
              className="quick-action"
            >

              <span>
                💊
              </span>

              <div>

                <strong>
                  Medicamentos
                </strong>

                <small>
                  Controlar tratamentos
                </small>

              </div>

            </Link>

            <Link
              to="/vacinas"
              className="quick-action"
            >

              <span>
                💉
              </span>

              <div>

                <strong>
                  Vacinas
                </strong>

                <small>
                  Controlar vacinação
                </small>

              </div>

            </Link>

            <Link
              to="/historico"
              className="quick-action"
            >

              <span>
                📋
              </span>

              <div>

                <strong>
                  Histórico
                </strong>

                <small>
                  Ver histórico médico
                </small>

              </div>
            
            </Link>

            <Link
              to="/chat"
              className="quick-action"
            >

              <span>
                🤖
              </span>

              <div>

                <strong>
                  VetBot IA
                </strong>

                <small>
                  Converse com seu assistente virtual
                </small>

              </div>
            
            </Link>
            
          </div>

            

        </section>

        {/* CONSULTAS PRÓXIMAS */}
        <section className="dashboard-section">

          <div className="section-header">

            <div>

              <h2>
                📅 Próximas consultas
              </h2>

              <p>
                Confira as consultas agendadas.
              </p>

            </div>

            <Link
              to="/consultas"
              className="see-all"
            >
              Ver todas
            </Link>

          </div>

          {carregando ? (

            <div className="empty-state">
              Carregando consultas...
            </div>

          ) : consultasProximas.length === 0 ? (

            <div className="empty-state">

              <span>
                📅
              </span>

              <p>
                Nenhuma consulta próxima.
              </p>

              {perfil === "VETERINARIO" && (
                <Link to="/consultas">
                  Gerenciar consultas
                </Link>
              )}

            </div>

          ) : (

            <div className="dashboard-list">

              {consultasProximas.map(
                (consulta) => (

                  <div
                    className="dashboard-list-item"
                    key={consulta.id}
                  >

                    <div className="list-main">

                      <strong>
                        Consulta #{consulta.id}
                      </strong>

                      <span>
                        👨‍⚕️ {consulta.veterinario}
                      </span>

                      <span>
                        📅{" "}
                        {formatarData(
                          consulta.data
                        )}
                      </span>

                    </div>

                    <span
                      className={getStatusClass(
                        consulta.status
                      )}
                    >
                      {consulta.status}
                    </span>

                  </div>

                )
              )}

            </div>

          )}

        </section>

        {/* VACINAS */}
        <section className="dashboard-section">

          <div className="section-header">

            <div>

              <h2>
                💉 Próximas vacinas
              </h2>

              <p>
                Fique atento às próximas doses.
              </p>

            </div>

            <Link
              to="/vacinas"
              className="see-all"
            >
              Ver todas
            </Link>

          </div>

          {carregando ? (

            <div className="empty-state">
              Carregando vacinas...
            </div>

          ) : vacinasProximas.length === 0 ? (

            <div className="empty-state">

              <span>
                💉
              </span>

              <p>
                Nenhuma próxima dose cadastrada.
              </p>

            </div>

          ) : (

            <div className="dashboard-list">

              {vacinasProximas.map(
                (vacina) => (

                  <div
                    className="dashboard-list-item"
                    key={vacina.id}
                  >

                    <div className="list-main">

                      <strong>
                        {vacina.nome}
                      </strong>

                      <span>
                        🐾 Pet #{vacina.petId}
                      </span>

                    </div>

                    <span className="date-highlight">
                      {formatarData(
                        vacina.proximaDose
                      )}
                    </span>

                  </div>

                )
              )}

            </div>

          )}

        </section>

        {/* RODAPÉ */}
        <footer className="dashboard-footer">

          <p>
            LifePet © 2026 — Sistema de
            gerenciamento e cuidados para pets.
          </p>

        </footer>

      </main>

    </div>
  );
}