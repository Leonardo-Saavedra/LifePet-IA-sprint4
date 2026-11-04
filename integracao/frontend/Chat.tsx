import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

type PetOpcao = { id: number; nome: string };
type Mensagem = { autor: "tutor" | "vetbot"; texto: string };

function formatar(texto: string) {
  return texto.split(/(\*\*[^*]+\*\*)/g).map((parte, i) =>
    parte.startsWith("**") && parte.endsWith("**") ? (
      <strong key={i}>{parte.slice(2, -2)}</strong>
    ) : (
      parte
    )
  );
}

function Chat() {
  const navigate = useNavigate();
  const [pets, setPets] = useState<PetOpcao[]>([]);
  const [petId, setPetId] = useState<number | null>(null);
  const [mensagens, setMensagens] = useState<Mensagem[]>([]);
  const [pergunta, setPergunta] = useState("");
  const [conversaId, setConversaId] = useState<string | null>(null);
  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState("");
  const fimRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    api
      .get("/pets")
      .then((resposta) => {
        const dados = resposta.data;
        const lista: PetOpcao[] = Array.isArray(dados) ? dados : dados.content ?? [];
        setPets(lista);
        if (lista.length > 0) setPetId(lista[0].id);
      })
      .catch(() => setErro("Não foi possível carregar os pets."));
  }, []);

  useEffect(() => {
    fimRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [mensagens, carregando]);

  function trocarPet(id: number) {
    setPetId(id);
    setMensagens([]);
    setConversaId(null);
  }

  async function enviar() {
    const texto = pergunta.trim();
    if (!texto || petId === null || carregando) return;

    setMensagens((atual) => [...atual, { autor: "tutor", texto }]);
    setPergunta("");
    setCarregando(true);

    try {
      const resposta = await api.post("/chat", { pergunta: texto, petId, conversaId });
      setMensagens((atual) => [
        ...atual,
        { autor: "vetbot", texto: resposta.data.resposta },
      ]);
      setConversaId(resposta.data.conversaId);
    } catch {
      setMensagens((atual) => [
        ...atual,
        { autor: "vetbot", texto: "Não foi possível obter a resposta do VetBot. Tente novamente." },
      ]);
    } finally {
      setCarregando(false);
    }
  }

  return (
    <div
      style={{
        maxWidth: 720,
        margin: "0 auto",
        padding: 24,
        background: "#ffffff",
        color: "#1a1a1a",
        minHeight: "100vh",
        boxSizing: "border-box",
        display: "flex",
        flexDirection: "column",
        gap: 16,
      }}
    >
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <h1 style={{ margin: 0, fontSize: 24 }}>VetBot</h1>
        <button onClick={() => navigate("/dashboard")}>Voltar</button>
      </div>

      <div style={{ display: "flex", gap: 8, alignItems: "center" }}>
        <label htmlFor="pet">Pet:</label>
        <select
          id="pet"
          value={petId ?? ""}
          onChange={(e) => trocarPet(Number(e.target.value))}
        >
          {pets.map((pet) => (
            <option key={pet.id} value={pet.id}>
              {pet.nome}
            </option>
          ))}
        </select>
      </div>

      {erro && <p style={{ color: "#b00020" }}>{erro}</p>}

      <div
        style={{
          flex: 1,
          minHeight: 320,
          border: "1px solid #d0d0d0",
          borderRadius: 8,
          padding: 12,
          overflowY: "auto",
          display: "flex",
          flexDirection: "column",
          gap: 8,
          background: "#fafafa",
        }}
      >
        {mensagens.length === 0 && (
          <p style={{ color: "#666" }}>
            Pergunte algo sobre a saúde do seu pet, por exemplo: a vacina está em dia?
          </p>
        )}
        {mensagens.map((mensagem, i) => (
          <div
            key={i}
            style={{
              alignSelf: mensagem.autor === "tutor" ? "flex-end" : "flex-start",
              background: mensagem.autor === "tutor" ? "#d9ecff" : "#e8e8e8",
              padding: "8px 12px",
              borderRadius: 8,
              maxWidth: "80%",
              whiteSpace: "pre-wrap",
            }}
          >
            {formatar(mensagem.texto)}
          </div>
        ))}
        {carregando && <p style={{ color: "#666" }}>VetBot está respondendo...</p>}
        <div ref={fimRef} />
      </div>

      <div style={{ display: "flex", gap: 8 }}>
        <input
          style={{ flex: 1, padding: 8 }}
          value={pergunta}
          onChange={(e) => setPergunta(e.target.value)}
          onKeyDown={(e) => {
            if (e.key === "Enter") enviar();
          }}
          placeholder="Digite sua pergunta"
        />
        <button onClick={enviar} disabled={carregando || petId === null}>
          Enviar
        </button>
      </div>
    </div>
  );
}

export default Chat;