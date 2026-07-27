import { createElement } from "react";
import { useNavigate } from "react-router-dom";
import {
  BarChart3,
  CheckCircle2,
  CreditCard,
  Landmark,
  LockKeyhole,
  Receipt,
  TrendingUp,
  Wallet,
} from "lucide-react";

const features = [
  {
    title: "Dashboard financeiro",
    description: "Acompanhe receitas, despesas, saldo e taxa de economia.",
    icon: BarChart3,
    color: "text-blue-600 bg-blue-50 border-blue-100",
  },
  {
    title: "Receitas e despesas",
    description: "Registre movimentações fixas, variáveis e recorrentes.",
    icon: Wallet,
    color: "text-green-600 bg-green-50 border-green-100",
  },
  {
    title: "Cartões de crédito",
    description: "Controle faturas, parcelas, vencimentos e compras.",
    icon: CreditCard,
    color: "text-violet-600 bg-violet-50 border-violet-100",
  },
  {
    title: "Conta corrente",
    description: "Veja o extrato consolidado de toda a vida financeira.",
    icon: Landmark,
    color: "text-cyan-600 bg-cyan-50 border-cyan-100",
  },
];

const steps = [
  "Cadastre suas movimentações",
  "Acompanhe faturas e recorrências",
  "Analise sua saúde financeira",
];

export default function Splash() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <header className="sticky top-0 z-30 border-b border-slate-200 bg-white/95 backdrop-blur">
        <div className="mx-auto flex max-w-7xl items-center justify-between gap-4 px-4 py-4 sm:px-6 lg:px-8">
          <button
            type="button"
            onClick={() => navigate("/splash")}
            className="flex items-center gap-3 text-left"
          >
            <img
              src="/logo.png"
              alt="MoneyIQ Personal"
              className="h-10 w-10 object-contain"
            />

            <div>
              <p className="text-sm font-bold leading-tight sm:text-base">
                MoneyIQ Personal
              </p>

              <p className="hidden text-xs text-slate-500 sm:block">
                Controle financeiro inteligente
              </p>
            </div>
          </button>

          <div className="flex items-center gap-2">
            <button
              type="button"
              onClick={() => navigate("/login")}
              className="rounded-lg px-3 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-100"
            >
              Login
            </button>

            <button
              type="button"
              onClick={() => navigate("/register")}
              className="rounded-lg bg-slate-900 px-3 py-2 text-sm font-medium text-white transition hover:bg-slate-700 sm:px-4"
            >
              Criar conta
            </button>
          </div>
        </div>
      </header>

      <main>
        <section className="relative overflow-hidden bg-slate-950 text-white">
          <div className="absolute inset-0 opacity-20">
            <div className="h-full w-full bg-[linear-gradient(to_right,#ffffff14_1px,transparent_1px),linear-gradient(to_bottom,#ffffff14_1px,transparent_1px)] bg-[size:48px_48px]" />
          </div>

          <div className="relative mx-auto grid min-h-[82vh] max-w-7xl items-center gap-10 px-4 py-16 sm:px-6 lg:px-8 lg:py-20">
            <div className="max-w-3xl">
              <div className="mb-6 inline-flex items-center gap-2 rounded-full border border-white/15 bg-white/10 px-3 py-1 text-sm text-slate-200">
                <TrendingUp size={16} />
                Finanças pessoais com visão clara e prática
              </div>

              <h1 className="text-4xl font-bold leading-tight sm:text-5xl lg:text-6xl">
                MoneyIQ Personal
              </h1>

              <p className="mt-5 max-w-2xl text-base leading-7 text-slate-300 sm:text-lg">
                Organize receitas, despesas, cartões e extrato em uma
                experiência única para tomar decisões financeiras com mais
                confiança.
              </p>

              <div className="mt-8 flex flex-col gap-3 sm:flex-row">
                <button
                  type="button"
                  onClick={() => navigate("/register")}
                  className="rounded-lg bg-white px-5 py-3 text-sm font-semibold text-slate-950 transition hover:bg-slate-200"
                >
                  Criar conta grátis
                </button>

                <button
                  type="button"
                  onClick={() => navigate("/login")}
                  className="rounded-lg border border-white/20 px-5 py-3 text-sm font-semibold text-white transition hover:bg-white/10"
                >
                  Entrar na minha conta
                </button>
              </div>
            </div>

            <div className="grid gap-4 rounded-lg border border-white/10 bg-white/10 p-4 shadow-2xl backdrop-blur sm:grid-cols-3">
              <Metric label="Saldo previsto" value="R$ 4.820" tone="blue" />
              <Metric label="Economia" value="22,4%" tone="green" />
              <Metric label="Fatura atual" value="R$ 1.230" tone="red" />
            </div>
          </div>
        </section>

        <section className="mx-auto max-w-7xl px-4 py-14 sm:px-6 lg:px-8">
          <div className="max-w-2xl">
            <p className="text-sm font-semibold uppercase text-violet-600">
              Funcionalidades
            </p>

            <h2 className="mt-2 text-3xl font-bold text-slate-950">
              Tudo que você precisa para entender seu dinheiro
            </h2>
          </div>

          <div className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
            {features.map((feature) => (
              <FeatureCard key={feature.title} {...feature} />
            ))}
          </div>
        </section>

        <section className="border-y border-slate-200 bg-white">
          <div className="mx-auto grid max-w-7xl gap-10 px-4 py-14 sm:px-6 lg:grid-cols-[1fr_1.2fr] lg:px-8">
            <div>
              <p className="text-sm font-semibold uppercase text-violet-600">
                Método simples
              </p>

              <h2 className="mt-2 text-3xl font-bold text-slate-950">
                Da movimentação ao insight em poucos passos
              </h2>

              <p className="mt-4 text-slate-600">
                O MoneyIQ Personal combina registros do dia a dia, faturas e
                relatórios para mostrar onde seu dinheiro entra, sai e pode
                render melhor.
              </p>
            </div>

            <div className="grid gap-3">
              {steps.map((step, index) => (
                <div
                  key={step}
                  className="flex items-center gap-4 rounded-lg border border-slate-200 bg-slate-50 p-4"
                >
                  <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-lg bg-slate-900 text-sm font-bold text-white">
                    {index + 1}
                  </span>

                  <p className="font-medium text-slate-800">{step}</p>
                </div>
              ))}
            </div>
          </div>
        </section>

        <section className="mx-auto max-w-7xl px-4 py-14 sm:px-6 lg:px-8">
          <div className="grid gap-4 lg:grid-cols-3">
            <TrustItem
              icon={LockKeyhole}
              title="Acesso protegido"
              description="Sua área financeira fica separada por login."
            />
            <TrustItem
              icon={Receipt}
              title="Histórico organizado"
              description="Filtros, paginação e extratos para consulta rápida."
            />
            <TrustItem
              icon={CheckCircle2}
              title="Pronto para rotina"
              description="Interface responsiva para desktop, tablet e celular."
            />
          </div>
        </section>

        <section className="bg-slate-950 px-4 py-14 text-white sm:px-6 lg:px-8">
          <div className="mx-auto flex max-w-5xl flex-col items-start justify-between gap-6 sm:flex-row sm:items-center">
            <div>
              <h2 className="text-3xl font-bold">Comece a organizar hoje</h2>
              <p className="mt-2 text-slate-300">
                Crie sua conta e veja sua vida financeira com mais clareza.
              </p>
            </div>

            <button
              type="button"
              onClick={() => navigate("/register")}
              className="w-full rounded-lg bg-white px-5 py-3 text-sm font-semibold text-slate-950 transition hover:bg-slate-200 sm:w-auto"
            >
              Criar conta
            </button>
          </div>
        </section>
      </main>
    </div>
  );
}

function Metric({ label, value, tone }) {
  const colors = {
    blue: "text-blue-300",
    green: "text-green-300",
    red: "text-red-300",
  };

  return (
    <div className="rounded-lg border border-white/10 bg-slate-950/70 p-4">
      <p className="text-sm text-slate-400">{label}</p>
      <p className={`mt-2 text-2xl font-bold ${colors[tone]}`}>{value}</p>
    </div>
  );
}

function FeatureCard({ title, description, icon, color }) {
  return (
    <div className="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
      <div
        className={`mb-4 flex h-11 w-11 items-center justify-center rounded-lg border ${color}`}
      >
        {createElement(icon, { size: 22 })}
      </div>

      <h3 className="font-semibold text-slate-950">{title}</h3>
      <p className="mt-2 text-sm leading-6 text-slate-600">{description}</p>
    </div>
  );
}

function TrustItem({ icon, title, description }) {
  return (
    <div className="rounded-lg border border-slate-200 bg-white p-5">
      {createElement(icon, { className: "text-violet-600", size: 24 })}
      <h3 className="mt-4 font-semibold text-slate-950">{title}</h3>
      <p className="mt-2 text-sm leading-6 text-slate-600">{description}</p>
    </div>
  );
}
