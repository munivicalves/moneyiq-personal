import { useCallback, useEffect, useState } from "react";
import { buscarContaCorrente } from "../services/contaCorrenteService";

export default function ContaCorrente() {
  const [inicio, setInicio] = useState(() => getPrimeiroDiaMesAtual());
  const [fim, setFim] = useState(() => getUltimoDiaMesAtual());
  const [busca, setBusca] = useState("");

  const [lancamentos, setLancamentos] = useState([]);
  const [saldoPeriodo, setSaldoPeriodo] = useState(0);

  const [paginaAtual, setPaginaAtual] = useState(1);

  const itensPorPagina = 20;

  const carregarContaCorrente = useCallback(
    async (inicioConsulta, fimConsulta, buscaConsulta) => {
      try {
        const data = await buscarContaCorrente(
          inicioConsulta,
          fimConsulta,
          buscaConsulta,
        );

        setLancamentos(data.transacoes || []);
        setSaldoPeriodo(data.saldoPeriodo || 0);
        setPaginaAtual(1);
      } catch (error) {
        console.error("Erro ao carregar conta corrente", error);
      }
    },
    [],
  );

  useEffect(() => {
    // Fetches initial statement data once when opening the page.
    // eslint-disable-next-line react-hooks/set-state-in-effect
    carregarContaCorrente(
      getPrimeiroDiaMesAtual(),
      getUltimoDiaMesAtual(),
      "",
    );
  }, [carregarContaCorrente]);

  async function aplicarFiltro() {
    await carregarContaCorrente(inicio, fim, busca);
  }

  async function limparFiltro() {
    const primeiroDia = getPrimeiroDiaMesAtual();
    const ultimoDia = getUltimoDiaMesAtual();

    setInicio(primeiroDia);
    setFim(ultimoDia);
    setBusca("");

    await carregarContaCorrente(primeiroDia, ultimoDia, "");
  }

  function exportarCSV() {
    const header = "Data,Descricao,Origem,Entrada,Saida,Saldo\n";

    const rows = lancamentos
      .map(
        (l) =>
          `${l.data},${l.descricao},${l.origem},${l.entrada},${l.saida},${l.saldo}`,
      )
      .join("\n");

    const blob = new Blob([header + rows], {
      type: "text/csv",
    });

    const url = window.URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = url;
    a.download = "conta-corrente.csv";
    a.click();
  }

  const indiceInicial = (paginaAtual - 1) * itensPorPagina;

  const indiceFinal = indiceInicial + itensPorPagina;

  const lancamentosPaginados = lancamentos.slice(indiceInicial, indiceFinal);

  const totalPaginas = Math.max(
    1,
    Math.ceil(lancamentos.length / itensPorPagina),
  );

  return (
    <div className="flex flex-col gap-6">
      {/* HEADER */}
      <div className="flex flex-col gap-4 sm:flex-row sm:justify-between sm:items-center">
        <div>
          <h1 className="text-2xl font-semibold text-slate-900 dark:text-white">
            Conta Corrente
          </h1>

          <p className="text-slate-500 dark:text-slate-400 text-sm">
            Extrato consolidado de receitas, despesas e faturas
          </p>
        </div>

        <button onClick={exportarCSV} className="btn">
          Exportar
        </button>
      </div>

      {/* FILTROS */}
      <div className="card">
        <div className="grid grid-cols-1 gap-4 items-end md:grid-cols-2 xl:grid-cols-4">
          <div>
            <label className="label">Data inicial</label>

            <input
              type="date"
              value={inicio}
              onChange={(e) => setInicio(e.target.value)}
              className="input"
            />
          </div>

          <div>
            <label className="label">Data final</label>

            <input
              type="date"
              value={fim}
              onChange={(e) => setFim(e.target.value)}
              className="input"
            />
          </div>

          <div>
            <label className="label">Buscar</label>

            <input
              placeholder="Descrição..."
              value={busca}
              onChange={(e) => setBusca(e.target.value)}
              className="input"
            />
          </div>

          <div className="flex flex-col gap-2 sm:flex-row">
            <button onClick={aplicarFiltro} className="btn">
              Buscar
            </button>

            <button onClick={limparFiltro} className="btn-secondary">
              Limpar
            </button>
          </div>
        </div>
      </div>

      {/* TABELA */}
      <div className="card">
        <div className="overflow-x-auto">
          <table className="w-full min-w-[820px]">
            <thead
              className="
              text-left text-sm
              text-slate-500 dark:text-slate-400
              border-b border-slate-200 dark:border-slate-700
            "
            >
              <tr className="h-10">
                <th>Data</th>
                <th>Descrição</th>
                <th>Origem</th>
                <th className="text-right">Entrada</th>
                <th className="text-right">Saída</th>
                <th className="text-right">Saldo</th>
              </tr>
            </thead>

            <tbody>
              {lancamentos.length > 0 ? (
                lancamentosPaginados.map((l, index) => (
                  <Row key={index} {...l} />
                ))
              ) : (
                <tr>
                  <td
                    colSpan="6"
                    className="text-center py-8 text-slate-500 dark:text-slate-400"
                  >
                    Nenhum lançamento encontrado
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
        <div className="flex flex-wrap justify-center items-center gap-2 mt-6">
          <button
            onClick={() => setPaginaAtual((p) => Math.max(1, p - 1))}
            disabled={paginaAtual === 1}
            className="btn-secondary"
          >
            ←
          </button>

          {Array.from({ length: totalPaginas }, (_, index) => index + 1)
            .filter(
              (pagina) =>
                pagina === 1 ||
                pagina === totalPaginas ||
                Math.abs(paginaAtual - pagina) <= 1,
            )
            .map((pagina, index, array) => (
              <div key={pagina} className="flex items-center">
                {index > 0 && pagina - array[index - 1] > 1 && (
                  <span className="px-2 text-slate-400">...</span>
                )}

                <button
                  onClick={() => setPaginaAtual(pagina)}
                  className={paginaAtual === pagina ? "btn" : "btn-secondary"}
                >
                  {pagina}
                </button>
              </div>
            ))}

          <button
            onClick={() => setPaginaAtual((p) => Math.min(totalPaginas, p + 1))}
            disabled={paginaAtual === totalPaginas}
            className="btn-secondary"
          >
            →
          </button>
        </div>
      </div>

      {/* Saldo */}
      <div className="flex justify-end mt-6 border-t border-slate-200 dark:border-slate-700 pt-4">
        <div className="text-right">
          <p className="text-sm text-slate-500 dark:text-slate-400">
            Saldo do período
          </p>

          <h2
            className={`text-2xl font-bold ${
              saldoPeriodo >= 0 ? "text-green-600" : "text-red-600"
            }`}
          >
            {formatCurrency(saldoPeriodo)}
          </h2>
        </div>
      </div>
    </div>
  );
}

function formatDateInput(date) {
  const ano = date.getFullYear();
  const mes = String(date.getMonth() + 1).padStart(2, "0");
  const dia = String(date.getDate()).padStart(2, "0");

  return `${ano}-${mes}-${dia}`;
}

function getPrimeiroDiaMesAtual() {
  const hoje = new Date();

  return formatDateInput(new Date(hoje.getFullYear(), hoje.getMonth(), 1));
}

function getUltimoDiaMesAtual() {
  const hoje = new Date();

  return formatDateInput(new Date(hoje.getFullYear(), hoje.getMonth() + 1, 0));
}

function Row({ data, descricao, origem, entrada, saida, saldo }) {
  return (
    <tr
      className="
        h-12
        border-b border-slate-200 dark:border-slate-700
        hover:bg-slate-50 dark:hover:bg-slate-800
        text-slate-700 dark:text-slate-300
        transition
      "
    >
      <td className="text-slate-500 dark:text-slate-400">
        {formatDate(data)}
      </td>

      <td className="font-medium text-slate-900 dark:text-slate-100">
        {descricao}
      </td>

      <td>
        <span
          className={`px-2 py-1 rounded-lg text-xs font-medium ${
            origem === "RECEITA"
              ? "bg-green-100 text-green-700 dark:bg-green-900/40 dark:text-green-300"
              : origem === "DESPESA"
                ? "bg-red-100 text-red-700 dark:bg-red-900/40 dark:text-red-300"
                : "bg-violet-100 text-violet-700 dark:bg-violet-900/40 dark:text-violet-300"
          }`}
        >
          {formatOrigem(origem)}
        </span>
      </td>

      <td className="text-right text-green-600 font-medium">
        {entrada > 0 ? formatCurrency(entrada) : "-"}
      </td>

      <td className="text-right text-red-600 font-medium">
        {saida > 0 ? formatCurrency(saida) : "-"}
      </td>

      <td className="text-right font-semibold text-slate-900 dark:text-slate-100">
        {formatCurrency(saldo)}
      </td>
    </tr>
  );
}

function formatOrigem(origem) {
  const map = {
    RECEITA: "Receita",
    DESPESA: "Despesa",
    CARTAO: "Cartão",
  };

  return map[origem] || origem;
}

function formatCurrency(value) {
  return Number(value || 0).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });
}

function formatDate(date) {
  const [ano, mes, dia] = date.split("-");

  return `${dia}/${mes}/${ano}`;
}
