import { useCallback, useEffect, useState } from "react";
import {
  buscarDashboard,
  buscarCategorias,
} from "../services/dashboardService";

import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
  Legend,
} from "recharts";

export default function Dashboard() {
  const [competencia, setCompetencia] = useState(() => getCompetenciaAtual());

  const [dashboard, setDashboard] = useState(null);

  const [categorias, setCategorias] = useState([]);

  const taxaEconomia = dashboard
    ? (
        (Number(dashboard.saldo) /
          Math.max(Number(dashboard.totalReceitas), 1)) *
        100
      ).toFixed(1)
    : 0;

  const carregarDashboard = useCallback(async () => {
    try {
      const [data, categoriasData] = await Promise.all([
        buscarDashboard(competencia),
        buscarCategorias(competencia),
      ]);

      setDashboard(data);
      setCategorias(categoriasData);
    } catch (error) {
      console.error("Erro ao carregar dashboard", error);
    }
  }, [competencia]);

  useEffect(() => {
    // Fetches server state when the selected accounting period changes.
    // eslint-disable-next-line react-hooks/set-state-in-effect
    carregarDashboard();
  }, [carregarDashboard]);

  if (!dashboard) {
    return (
      <div className="flex items-center justify-center h-full">
        Carregando...
      </div>
    );
  }
  const chartData = dashboard.chartData || [];

  const saldoChart = chartData.map((item) => ({
    ...item,
    saldo: Number(item.receitas) - Number(item.despesas),
  }));

  const fixasVariaveis = [
    {
      name: "Fixas",
      value: Number(dashboard.fixasVsVariaveis?.fixas || 0),
    },
    {
      name: "Variáveis",
      value: Number(dashboard.fixasVsVariaveis?.variaveis || 0),
    },
  ];

  const colors = ["#06b6d4", "#f59e0b"];

  return (
    <div className="flex flex-col gap-6">
      {/* HEADER */}
      <div className="flex flex-col gap-4 sm:flex-row sm:justify-between sm:items-center">
        <div>
          <h1 className="text-2xl font-semibold text-slate-900 dark:text-white">
            Dashboard Financeiro
          </h1>

          <p className="text-sm text-slate-500 dark:text-slate-400">
            Visão geral da sua vida financeira
          </p>
        </div>

        <div className="flex w-full sm:w-auto items-center justify-between gap-2 bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-800 px-3 py-2 rounded-xl shadow-sm">
          <span className="text-sm text-slate-500 dark:text-slate-400">
            Competência
          </span>

          <input
            type="month"
            value={competencia}
            onChange={(e) => setCompetencia(e.target.value)}
            className="
              min-w-0
              bg-transparent
              text-slate-900
              dark:text-slate-100
              outline-none
            "
          />
        </div>
      </div>

      {/* CARDS */}
      <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-5 gap-4">
        <Card
          title="Receitas"
          value={dashboard.totalReceitas}
          color="text-green-600"
        />

        <Card
          title="Despesas"
          value={dashboard.totalDespesas}
          color="text-red-600"
        />

        <Card
          title="Despesas Fixas"
          value={dashboard.totalFixas}
          color="text-slate-700 dark:text-slate-200"
        />

        <Card
          title="Saldo"
          value={dashboard.saldo}
          color={
            Number(dashboard.saldo) >= 0 ? "text-blue-600" : "text-red-600"
          }
        />

        <CardPercentual title="Taxa de Economia" value={taxaEconomia} />
      </div>

      <div className="card hover:shadow-lg transition-all duration-300">
        <h3 className="font-semibold mb-2 text-slate-900 dark:text-white">
          Saúde Financeira
        </h3>

        <p className="text-sm text-slate-500 dark:text-slate-400">
          {taxaEconomia >= 20
            ? "Excelente. Você está poupando mais de 20% da renda."
            : taxaEconomia >= 10
              ? "Boa. Há espaço para aumentar sua reserva."
              : "Atenção. Suas despesas estão consumindo quase toda sua renda."}
        </p>
      </div>

      {/* GRÁFICOS */}
      <div className="grid grid-cols-1 xl:grid-cols-2 gap-4">
        {/* RECEITAS X DESPESAS */}
        <div className="card hover:shadow-lg transition-all duration-300">
          <h3 className="font-semibold mb-4 text-slate-900 dark:text-white">
            Receitas x Despesas
          </h3>

          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={saldoChart}>
              <XAxis dataKey="mes" tick={{ fill: "#94a3b8" }} />

              <YAxis tick={{ fill: "#94a3b8" }} />

              <Tooltip
                formatter={(value) =>
                  Number(value).toLocaleString("pt-BR", {
                    style: "currency",
                    currency: "BRL",
                  })
                }
                contentStyle={{
                  backgroundColor: "#0f172a",
                  border: "1px solid #334155",
                  color: "#fff",
                }}
              />

              <Legend wrapperStyle={{ color: "#cbd5e1" }} />

              <Bar
                dataKey="receitas"
                name="Receitas"
                radius={8}
                fill="#16a34a"
              />

              <Bar
                dataKey="despesas"
                name="Despesas"
                radius={8}
                fill="#dc2626"
              />
            </BarChart>
          </ResponsiveContainer>
        </div>

        <div className="card">
          <h3 className="font-semibold mb-4 text-slate-900 dark:text-white">
            Evolução do Saldo
          </h3>

          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={saldoChart}>
              <XAxis dataKey="mes" tick={{ fill: "#94a3b8" }} />

              <YAxis tick={{ fill: "#94a3b8" }} />

              <Tooltip
                contentStyle={{
                  backgroundColor: "#0f172a",
                  border: "1px solid #334155",
                  color: "#fff",
                }}
              />

              <Bar dataKey="saldo" name="Saldo" fill="#2563eb" radius={8} />
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* FIXAS X VARIÁVEIS */}
        <div className="card hover:shadow-lg transition-all duration-300">
          <h3 className="font-semibold mb-4 text-slate-900 dark:text-white">
            Fixas x Variáveis
          </h3>

          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={fixasVariaveis}
                dataKey="value"
                nameKey="name"
                outerRadius={90}
                label
              >
                {fixasVariaveis.map((_, index) => (
                  <Cell key={index} fill={colors[index]} />
                ))}
              </Pie>

              <Tooltip
                formatter={(value) =>
                  Number(value).toLocaleString("pt-BR", {
                    style: "currency",
                    currency: "BRL",
                  })
                }
                contentStyle={{
                  backgroundColor: "#0f172a",
                  border: "1px solid #334155",
                  color: "#fff",
                }}
              />

              <Legend wrapperStyle={{ color: "#cbd5e1" }} />
            </PieChart>
          </ResponsiveContainer>
        </div>

        <div className="card">
          <h3 className="font-semibold mb-4 text-slate-900 dark:text-white">
            Gastos por Categoria
          </h3>

          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={categorias}
                dataKey="valor"
                nameKey="categoria"
                outerRadius={90}
                label
              >
                {categorias.map((_, index) => (
                  <Cell
                    key={index}
                    fill={
                      ["#2563eb", "#16a34a", "#dc2626", "#ca8a04", "#9333ea"][
                        index % 5
                      ]
                    }
                  />
                ))}
              </Pie>

              <Tooltip
                contentStyle={{
                  backgroundColor: "#0f172a",
                  border: "1px solid #334155",
                  color: "#fff",
                }}
              />

              <Legend wrapperStyle={{ color: "#cbd5e1" }} />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
}

function getCompetenciaAtual() {
  const hoje = new Date();
  const mes = String(hoje.getMonth() + 1).padStart(2, "0");

  return `${hoje.getFullYear()}-${mes}`;
}

function Card({ title, value, color }) {
  return (
    <div className="card hover:shadow-lg transition-all duration-300">
      <p className="text-sm text-slate-500 dark:text-slate-400">{title}</p>

      <h2 className={`text-2xl font-semibold ${color}`}>
        {Number(value || 0).toLocaleString("pt-BR", {
          style: "currency",
          currency: "BRL",
        })}
      </h2>
    </div>
  );
}

function CardPercentual({ title, value }) {
  return (
    <div className="card hover:shadow-lg transition-all duration-300">
      <p className="text-sm text-slate-500 dark:text-slate-400">{title}</p>

      <h2
        className={`text-2xl font-semibold ${
          value >= 20
            ? "text-green-600"
            : value >= 10
              ? "text-yellow-600"
              : "text-red-600"
        }`}
      >
        {value}%
      </h2>
    </div>
  );
}
