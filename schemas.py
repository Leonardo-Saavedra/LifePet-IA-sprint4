from typing import Optional
from pydantic import BaseModel


class VacinaIn(BaseModel):
    nome: str
    dataAplicacao: str
    proximaDose: str


class ConsultaIn(BaseModel):
    data: str
    veterinario: str
    observacoes: str
    status: str


class MedicamentoIn(BaseModel):
    nome: str
    dosagem: str
    frequencia: str
    dataInicio: str
    dataFim: str


class HistoricoIn(BaseModel):
    descricao: str
    dataRegistro: str
    observacoes: Optional[str] = None


class PetIn(BaseModel):
    nome: str
    idade: int
    raca: str
    especie: str
    peso: float
    vacinas: list[VacinaIn] = []
    consultas: list[ConsultaIn] = []
    medicamentos: list[MedicamentoIn] = []
    historicos: list[HistoricoIn] = []


class ChatRequest(BaseModel):
    pergunta: str
    pet: PetIn
    conversaId: Optional[str] = None


class ChatResponse(BaseModel):
    resposta: str
    conversaId: str