from fastapi import FastAPI, HTTPException

from schemas import ChatRequest, ChatResponse
from vetbot import conversar

app = FastAPI(
    title="LifePet VetBot API",
    description="API de inteligência artificial para assistência veterinária.",
    version="1.0.0"
)


@app.get("/")
def home():
    return {"mensagem": "LifePet VetBot API está funcionando!"}


@app.post("/chat", response_model=ChatResponse)
def chat(requisicao: ChatRequest):
    try:
        resposta, conversa_id = conversar(
            requisicao.pergunta,
            requisicao.pet.model_dump(),
            requisicao.conversaId,
        )
    except Exception as erro:
        print(f"ERRO REAL DO VETBOT: {repr(erro)}")
        raise HTTPException(
            status_code=502,
            detail=f"Falha ao consultar o modelo de IA: {erro}"
        )
    return ChatResponse(resposta=resposta, conversaId=conversa_id)