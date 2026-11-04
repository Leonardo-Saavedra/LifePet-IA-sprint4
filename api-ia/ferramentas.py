FUNCOES_AUTORIZADAS = {
    "buscar_pet",
    "listar_vacinas",
    "listar_consultas",
    "listar_medicamentos",
}


def _pet_confere(pet: dict, nome: str) -> bool:
    return pet["nome"].lower() == nome.lower()


def buscar_pet(pet: dict, nome: str) -> dict:
    if not _pet_confere(pet, nome):
        return {"sucesso": False, "erro": f"Pet '{nome}' não encontrado."}
    return {"sucesso": True, "pet": pet}


def _listar(pet: dict, nome: str, campo: str) -> dict:
    if not _pet_confere(pet, nome):
        return {"sucesso": False, "erro": f"Pet '{nome}' não encontrado."}
    return {"sucesso": True, "pet": pet["nome"], campo: pet.get(campo, [])}


def listar_vacinas(pet: dict, nome: str) -> dict:
    return _listar(pet, nome, "vacinas")


def listar_consultas(pet: dict, nome: str) -> dict:
    return _listar(pet, nome, "consultas")


def listar_medicamentos(pet: dict, nome: str) -> dict:
    return _listar(pet, nome, "medicamentos")


def executar_ferramenta(pet: dict, nome_funcao: str, argumentos: dict) -> dict:
    if nome_funcao not in FUNCOES_AUTORIZADAS:
        return {"sucesso": False, "erro": f"Ferramenta '{nome_funcao}' não autorizada."}
    return globals()[nome_funcao](pet, **argumentos)


def _declaracao(nome: str, descricao: str) -> dict:
    return {
        "type": "function",
        "name": nome,
        "description": descricao,
        "parameters": {
            "type": "object",
            "properties": {
                "nome": {"type": "string", "description": "Nome do pet."}
            },
            "required": ["nome"],
        },
    }


def criar_ferramentas() -> list:
    return [
        _declaracao("buscar_pet", "Busca informações completas de um pet pelo nome."),
        _declaracao("listar_vacinas", "Lista as vacinas de um pet e as datas de próxima aplicação."),
        _declaracao("listar_consultas", "Lista o histórico de consultas veterinárias de um pet."),
        _declaracao("listar_medicamentos", "Lista os medicamentos em uso de um pet."),
    ]