import { BrowserRouter, Routes, Route } from "react-router-dom";
import MainLayout from "./layouts/MainLayout";

import Dashboard from "./pages/Dashboard";
import Receitas from "./pages/Receitas";
import Despesas from "./pages/Despesas";
import CartaoCredito from "./pages/CartaoCredito";
import ContaCorrente from "./pages/ContaCorrente";

import Register from "./pages/Register";
import Login from "./pages/Login";
import Splash from "./pages/Splash";

import PrivateRoute from "./routes/PrivateRoute";

import "./index.css";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* páginas sem layout */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/splash" element={<Splash />} />

        {/* páginas com layout */}
        <Route
          path="/"
          element={
            <PrivateRoute>
              <MainLayout />
            </PrivateRoute>
          }
        >
          <Route index element={<Dashboard />} />
          <Route path="receitas" element={<Receitas />} />
          <Route path="despesas" element={<Despesas />} />
          <Route path="cartao" element={<CartaoCredito />} />
          <Route path="conta" element={<ContaCorrente />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
