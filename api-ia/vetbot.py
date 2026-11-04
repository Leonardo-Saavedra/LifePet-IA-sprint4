import json
from datetime import date

from google import genai

import config
from ferramentas import criar_ferramentas, executar_ferramenta
from rag import buscar_documentos, formatar_contexto

client = genai.Client(api_key=config.GEMINI_API_KEY)

SYSTEM_PROMPT = """
Você é o VetBot, assistente virtual do LifePet — um app de saúde contínua para pets.

Seu papel é ajudar tutores a acompanhar a saúde dos seus animais de forma preventiva e personalizada.

Você tem acesso a ferramentas que consultam o histórico real do pet, incluindo vacinas, consultas e medicamentos.

Regras:
- responda sempre em português;
- seja objetivo, empático e claro;
- use as ferramentas disponíveis para buscar informações antes de responder;
- nunca invente dados sobre vacinas, consultas ou medicamentos;
- se não encontrar o pet, informe educadamente;
- quando identificar algo que exige atenção veterinária, recomende uma consulta;
- responda em até 150 palavras por mensagem;
- não forneça diagnósticos médicos definitivos.

Contexto do sistema:
- o app envia os dados do pet em atendimento junto com a pergunta do tutor;
- use a data de hoje para dizer se vacinas e medicamentos estão em dia ou atrasados;
- seu objetivo principal é promover o cuidado preventivo e contínuo do pet.
"""


def montar_system(pergunta: str, pet: dict) -> str:
    documentos = buscar_documentos(pergunta, top_k=3)
    contexto = formatar_contexto(documentos)

    return (
        SYSTEM_PROMPT
        + f"\nData de hoje: {date.today().isoformat()}"
        + f"\nPet em atendimento: {pet['nome']}"
        + f"\n\nCONTEXTO DA BASE DE CONHECIMENTO:\n{contexto}"
    )


MAX_RODADAS = 5


def conversar(pergunta: str, pet: dict, conversa_id: str | None = None):
    system = montar_system(pergunta, pet)

    parametros = {
        "model": config.MODEL,
        "input": pergunta,
        "tools": criar_ferramentas(),
        "system_instruction": system,
    }

    if conversa_id:
        parametros["previous_interaction_id"] = conversa_id

    interaction = client.interactions.create(
        **parametros,
        timeout=30
    )

    for rodada in range(MAX_RODADAS):
        chamadas = [
            step for step in interaction.steps
            if step.type == "function_call"
        ]

        if not chamadas:
            break

        resultados = []

        for chamada in chamadas:
            resultado = executar_ferramenta(
                pet,
                chamada.name,
                chamada.arguments
            )

            resultados.append({
                "type": "function_result",
                "name": chamada.name,
                "call_id": chamada.id,
                "result": [
                    {
                        "type": "text",
                        "text": json.dumps(resultado, ensure_ascii=False)
                    }
                ],
            })

        interaction = client.interactions.create(
            model=config.MODEL,
            previous_interaction_id=interaction.id,
            input=resultados,
            tools=criar_ferramentas(),
            system_instruction=system,
        )

    texto = interaction.output_text or "Desculpe, não consegui processar sua pergunta."

    return texto, interaction.id
