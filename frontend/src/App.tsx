import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import Pets from "./pages/Pets";
import Consultas from "./pages/Consultas";
import Medicamentos from "./pages/Medicamentos";
import Vacinas from "./pages/Vacinas";
import Historico from "./pages/Historico";
import Chat from "./pages/Chat";

function App() {
  const token = localStorage.getItem("token");

  return (
    <BrowserRouter>
      <Routes>

        {/* LOGIN */}
        <Route
          path="/"
          element={
            token ? (
              <Navigate to="/dashboard" replace />
            ) : (
              <Login />
            )
          }
        />

        {/* DASHBOARD */}
        <Route
          path="/dashboard"
          element={
            token ? (
              <Dashboard />
            ) : (
              <Navigate to="/" replace />
            )
          }
        />

        {/* PETS */}
        <Route
          path="/pets"
          element={
            token ? (
              <Pets />
            ) : (
              <Navigate to="/" replace />
            )
          }
        />

        {/* CONSULTAS */}
        <Route
          path="/consultas"
          element={
            token ? (
              <Consultas />
            ) : (
              <Navigate to="/" replace />
            )
          }
        />

        {/* MEDICAMENTOS */}
        <Route
          path="/medicamentos"
          element={
            token ? (
              <Medicamentos />
            ) : (
              <Navigate to="/" replace />
            )
          }
        />

        {/* VACINAS */}
        <Route
          path="/vacinas"
          element={
            token ? (
              <Vacinas />
            ) : (
              <Navigate to="/" replace />
            )
          }
        />

        {/* HISTÓRICO */}
        <Route
          path="/historico"
          element={
            token ? (
              <Historico />
            ) : (
              <Navigate to="/" replace />
            )
          }
        />
        {/* CHAT VETBOT */}
        <Route
          path="/chat"
          element={
            token ? (
              <Chat />
            ) : (
              <Navigate to="/" replace />
            )
          }
        />
        {/* ROTA NÃO ENCONTRADA */}
        <Route
          path="*"
          element={<Navigate to="/" replace />}
        />

      </Routes>
    </BrowserRouter>
  );
}

export default App;