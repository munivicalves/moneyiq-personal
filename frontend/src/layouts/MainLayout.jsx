import { useState, useEffect } from "react";
import { NavLink, Outlet, useNavigate } from "react-router-dom";
import {
  LayoutDashboard,
  Wallet,
  CreditCard,
  Landmark,
  Receipt,
  LogOut,
  ShieldCheck,
  UserCircle2,
  Sun,
  Moon,
} from "lucide-react";

export default function MainLayout() {
  return (
    <div className="flex min-h-screen flex-col lg:flex-row bg-slate-50 dark:bg-slate-900">
      <Sidebar />
      <main className="flex-1 p-4 sm:p-6 lg:p-8 overflow-auto bg-slate-50 dark:bg-slate-900">
        <Outlet />
      </main>
    </div>
  );
}

function Sidebar() {
  const navigate = useNavigate();
  const nome = localStorage.getItem("nome") || "Usuário";

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

  function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("nome");
    localStorage.removeItem("email");

    navigate("/login");
  }

  const linkClass =
    "flex min-h-11 items-center gap-3 rounded-xl px-3 py-2 text-sm font-medium transition";

  return (
    <aside
      className="
      w-full
      lg:w-72
      shrink-0
      bg-white
      dark:bg-slate-900
      border-b
      lg:border-b-0
      lg:border-r
      border-slate-200
      dark:border-slate-800
      p-4
      lg:p-5
      shadow-sm
      flex
      flex-col
      lg:h-screen
      "
    >
      {/* LOGO */}
      <div className="mb-4 lg:mb-10 flex items-center gap-3">
        <img
          src="/logo.png"
          alt="Sentinel Prime"
          className="w-12 h-12 object-contain"
        />

        <div>
          <h2 className="font-bold text-slate-800 dark:text-white">
            Sentinel Prime
          </h2>

          <p className="text-xs text-slate-400">
            Controle Financeiro Inteligente
          </p>
        </div>
      </div>

      {/* USUÁRIO */}
      <p className="hidden lg:block text-xs font-semibold uppercase text-slate-400 px-3 mb-2">
        Usuário
      </p>

      <div className="flex items-center gap-2 px-3 mb-4 lg:mb-6">
        <UserCircle2 size={26} className="text-violet-600" />

        <span className="text-sm font-medium text-slate-700 dark:text-slate-200 truncate">
          {nome}
        </span>
      </div>

      {/* DASHBOARD */}
      <p className="hidden lg:block text-xs font-semibold uppercase text-slate-400 px-3 mb-2">
        Principal
      </p>

      <nav className="grid grid-cols-1 gap-2">
        <NavLink
          to="/"
          className={({ isActive }) =>
            `${linkClass} ${
              isActive
                ? "bg-violet-100 text-violet-700 dark:bg-violet-900/40 dark:text-violet-300"
                : "text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800"
            }`
          }
        >
          <LayoutDashboard size={18} className="shrink-0" />
          <span className="min-w-0 leading-tight">Dashboard</span>
        </NavLink>
      </nav>

      {/* FINANCEIRO */}
      <p className="hidden lg:block text-xs font-semibold uppercase text-slate-400 px-3 mt-6 mb-2">
        Financeiro
      </p>

      <nav className="mt-2 grid grid-cols-2 gap-2 lg:mt-0 lg:grid-cols-1">
        <NavLink
          to="/receitas"
          className={({ isActive }) =>
            `${linkClass} min-w-0 ${
              isActive
                ? "bg-green-100 text-green-700 dark:bg-green-900/40 dark:text-green-300"
                : "text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800"
            }`
          }
        >
          <Wallet size={18} className="shrink-0" />
          <span className="min-w-0 leading-tight">Receitas</span>
        </NavLink>

        <NavLink
          to="/despesas"
          className={({ isActive }) =>
            `${linkClass} min-w-0 ${
              isActive
                ? "bg-red-100 text-red-700 dark:bg-red-900/40 dark:text-red-300"
                : "text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800"
            }`
          }
        >
          <Receipt size={18} className="shrink-0" />
          <span className="min-w-0 leading-tight">Despesas</span>
        </NavLink>

        <NavLink
          to="/cartao"
          className={({ isActive }) =>
            `${linkClass} min-w-0 ${
              isActive
                ? "bg-orange-100 text-orange-700 dark:bg-orange-900/40 dark:text-orange-300"
                : "text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800"
            }`
          }
        >
          <CreditCard size={18} className="shrink-0" />
          <span className="min-w-0 leading-tight">Cartão de Crédito</span>
        </NavLink>

        <NavLink
          to="/conta"
          className={({ isActive }) =>
            `${linkClass} min-w-0 ${
              isActive
                ? "bg-blue-100 text-blue-700 dark:bg-blue-900/40 dark:text-blue-300"
                : "text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-800"
            }`
          }
        >
          <Landmark size={18} className="shrink-0" />
          <span className="min-w-0 leading-tight">Conta Corrente</span>
        </NavLink>
      </nav>

      {/* SISTEMA 
      <p className="text-xs font-semibold uppercase text-slate-400 px-3 mt-6 mb-2">
        Sistema
      </p>

      <button
        className={`${linkClass} text-slate-400 cursor-not-allowed`}
        disabled
      >
        <ShieldCheck size={18} />
        Auditoria (em breve)
      </button>

      */}

      {/* LOGOUT */}
      {/* CONFIGURAÇÕES */}
      <div className="mt-4 grid grid-cols-2 gap-2 border-t border-slate-200 pt-4 dark:border-slate-800 lg:mt-auto lg:block lg:space-y-2">
        <button
          onClick={() => setDarkMode(!darkMode)}
          className="
          w-full
          flex
          items-center
          justify-center
          lg:justify-start
          gap-3
          px-3
          py-2
          rounded-xl
          text-slate-600
          dark:text-slate-300
          hover:bg-slate-100
          dark:hover:bg-slate-800
          transition
        "
        >
          {darkMode ? <Sun size={18} /> : <Moon size={18} />}

          <span className="text-sm">
            {darkMode ? "Modo Claro" : "Modo Escuro"}
          </span>
        </button>

        <button
          onClick={logout}
          className="flex w-full items-center justify-center gap-3 rounded-xl px-3 py-2 text-slate-600 transition hover:bg-red-50 hover:text-red-600 dark:text-slate-300 dark:hover:bg-red-950 dark:hover:text-red-300 lg:justify-start"
        >
          <LogOut size={18} />
          Sair
        </button>
      </div>
    </aside>
  );
}
