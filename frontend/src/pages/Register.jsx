import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Moon, Sun } from "lucide-react";
import { registrar } from "../services/authService";

export default function Register() {
  const navigate = useNavigate();

  const [nome, setNome] = useState("");
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [confirmarSenha, setConfirmarSenha] = useState("");
  const [erro, setErro] = useState("");
  const [carregando, setCarregando] = useState(false);
  const [darkMode, setDarkMode] = useState(
    localStorage.getItem("theme") === "dark",
  );

  useEffect(() => {
    if (darkMode) {
      document.documentElement.classList.add("dark");
      localStorage.setItem("theme", "dark");
    } else {
      document.documentElement.classList.remove("dark");
      localStorage.setItem("theme", "light");
    }
  }, [darkMode]);

  async function handleRegister(event) {
    event.preventDefault();
    setErro("");

    if (senha !== confirmarSenha) {
      setErro("As senhas não conferem");
      return;
    }

    setCarregando(true);

    try {
      await registrar({ nome, email, senha });
      navigate("/");
    } catch (error) {
      console.error("Erro ao criar conta:", error);
      setErro("Não foi possível criar a conta");
    } finally {
      setCarregando(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-100 dark:bg-slate-950 px-4 py-8">
      <button
        type="button"
        onClick={() => setDarkMode(!darkMode)}
        className="
          fixed right-4 top-4
          flex items-center gap-2 rounded-xl
          border border-slate-200 dark:border-slate-800
          bg-white dark:bg-slate-900
          px-3 py-2 text-sm
          text-slate-700 dark:text-slate-200
          hover:bg-slate-50 dark:hover:bg-slate-800
          transition
        "
      >
        {darkMode ? <Sun size={18} /> : <Moon size={18} />}
        {darkMode ? "Claro" : "Escuro"}
      </button>

      <div className="w-full max-w-sm bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 p-6 sm:p-8 rounded-2xl shadow-sm">
        <div className="text-center mb-6">
          <h1 className="text-2xl font-semibold text-slate-900 dark:text-white">
            Criar conta
          </h1>
        </div>

        <form className="flex flex-col gap-3" onSubmit={handleRegister}>
          <input
            placeholder="Nome"
            className="input"
            value={nome}
            onChange={(e) => setNome(e.target.value)}
            required
          />

          <input
            placeholder="Email"
            className="input"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            type="password"
            placeholder="Senha"
            className="input"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            minLength={6}
            required
          />

          <input
            type="password"
            placeholder="Confirmar senha"
            className="input"
            value={confirmarSenha}
            onChange={(e) => setConfirmarSenha(e.target.value)}
            minLength={6}
            required
          />

          {erro && <p className="text-sm text-red-600 dark:text-red-400">{erro}</p>}

          <button className="btn" disabled={carregando}>
            {carregando ? "Criando..." : "Criar conta"}
          </button>

          <p className="text-sm text-center text-slate-500 dark:text-slate-400">
            Já possui conta?{" "}
            <button
              type="button"
              className="text-slate-900 dark:text-violet-300 cursor-pointer font-medium"
              onClick={() => navigate("/login")}
            >
              Login
            </button>
          </p>
        </form>
      </div>
    </div>
  );
}
