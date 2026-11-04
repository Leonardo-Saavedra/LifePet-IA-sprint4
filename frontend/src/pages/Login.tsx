
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

interface LoginResponse {
  token: string;
  perfil: "TUTOR" | "VETERINARIO";
}

export default function Login() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [erro, setErro] = useState("");
  const [carregando, setCarregando] = useState(false);

  async function handleLogin(event: React.FormEvent) {
    event.preventDefault();

    setErro("");
    setCarregando(true);

    try {
      const response = await api.post<LoginResponse>("/auth/login", {
        email: email.trim(),
        senha: senha,
      });

      console.log("LOGIN REALIZADO COM SUCESSO");
      console.log("PERFIL:", response.data.perfil);

      localStorage.setItem("token", response.data.token);
      localStorage.setItem("perfil", response.data.perfil);

      window.location.href = "/dashboard";

    } catch (error: any) {

      console.error("========== ERRO NO LOGIN ==========");
      console.error("ERRO COMPLETO:", error);
      console.error("STATUS:", error.response?.status);
      console.error("RESPOSTA DA API:", error.response?.data);
      console.error("===================================");

      if (error.response) {

        if (error.response.status === 401) {
          setErro("E-mail ou senha inválidos.");
        } else if (error.response.status === 403) {
          setErro("Acesso negado pelo servidor (403).");
        } else if (error.response.status === 404) {
          setErro("Endpoint de login não encontrado (404).");
        } else if (error.response.status >= 500) {
          setErro("Erro interno no servidor.");
        } else {
          setErro(
            error.response.data?.mensagem ||
            error.response.data?.message ||
            `Erro HTTP ${error.response.status}`
          );
        }

      } else if (error.request) {

        setErro(
          "Não foi possível conectar ao backend. Verifique se o Spring Boot está rodando."
        );

      } else {

        setErro("Erro ao realizar login.");

      }

    } finally {
      setCarregando(false);
    }
  }

  return (
    <div className="login-container">

      <div className="login-card">

        <div className="login-header">

          <h1>🐾 LifePet</h1>

          <p>
            Cuidados inteligentes para seu pet
          </p>

        </div>

        <form onSubmit={handleLogin}>

          <label htmlFor="email">
            E-mail
          </label>

          <input
            id="email"
            type="email"
            placeholder="Digite seu e-mail"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            required
          />

          <label htmlFor="senha">
            Senha
          </label>

          <input
            id="senha"
            type="password"
            placeholder="Digite sua senha"
            value={senha}
            onChange={(event) => setSenha(event.target.value)}
            required
          />

          {erro && (
            <p className="error-message">
              {erro}
            </p>
          )}

          <button
            type="submit"
            disabled={carregando}
          >
            {carregando ? "Entrando..." : "Entrar"}
          </button>

        </form>

      </div>

    </div>
  );
}

