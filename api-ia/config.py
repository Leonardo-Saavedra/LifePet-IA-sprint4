import os
from dotenv import load_dotenv

load_dotenv()

GEMINI_API_KEY = os.getenv("GEMINI_API_KEY")
MODEL = "gemini-3.5-flash-lite"
EMBEDDING_MODEL = "gemini-embedding-001"

if not GEMINI_API_KEY:
    raise RuntimeError("GEMINI_API_KEY não encontrada. Verifique o arquivo .env.")