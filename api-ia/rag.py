import numpy as np
from google import genai

import config
from documentos import DOCUMENTOS

client = genai.Client(api_key=config.GEMINI_API_KEY)

_embeddings = {}


def gerar_embedding(texto: str) -> list:
    resposta = client.models.embed_content(
        model=config.EMBEDDING_MODEL,
        contents=texto
    )
    return resposta.embeddings[0].values


def similaridade(vetor_a: list, vetor_b: list) -> float:
    a = np.array(vetor_a)
    b = np.array(vetor_b)
    return float(np.dot(a, b) / (np.linalg.norm(a) * np.linalg.norm(b)))


def indexar_documentos() -> None:
    for doc in DOCUMENTOS:
        if doc["fonte"] not in _embeddings:
            _embeddings[doc["fonte"]] = gerar_embedding(doc["conteudo"])


def buscar_documentos(pergunta: str, top_k: int = 3) -> list:
    indexar_documentos()
    embedding_pergunta = gerar_embedding(pergunta)

    pontuados = []
    for doc in DOCUMENTOS:
        score = similaridade(embedding_pergunta, _embeddings[doc["fonte"]])
        pontuados.append((score, doc))

    pontuados.sort(key=lambda x: x[0], reverse=True)
    return [doc for _, doc in pontuados[:top_k]]


def formatar_contexto(documentos: list) -> str:
    contexto = ""
    for doc in documentos:
        contexto += f"## {doc['titulo']}\n{doc['conteudo']}\n\n"
    return contexto.strip()