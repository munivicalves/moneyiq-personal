import { Link } from "react-router-dom";

export default function Sidebar() {
  return (
    <div
      style={{
        width: 220,
        height: "100vh",
        background: "#0f172a",
        color: "white",
        padding: 20,
      }}
    >
      <h2>Finance</h2>

      <nav
        style={{
          marginTop: 20,
          display: "flex",
          flexDirection: "column",
          gap: 10,
        }}
      >
        <Link to="/">Dashboard</Link>
        <Link to="/receitas">Receitas</Link>
        <Link to="/despesas">Despesas Fixas</Link>
        <Link to="/cartao">Cartão</Link>
        <Link to="/conta">Conta Corrente</Link>
      </nav>
    </div>
  );
}
