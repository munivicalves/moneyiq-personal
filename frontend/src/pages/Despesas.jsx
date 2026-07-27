import { useCallback, useEffect, useState } from "react";
import { Trash2 } from "lucide-react";

import FormDespesa from "../components/forms/FormDespesas";
import AlertModal from "../components/AlertModal";
import FiltroCompetencia from "../components/FiltroCompetencia";

import { listarDespesas, excluirDespesa } from "../services/despesaService";

export default function DespesasFixas() {
  const [open, setOpen] = useState(false);
  const [despesas, setDespesas] = useState([]);
  const [alert, setAlert] = useState(null);

  const [paginaAtual, setPaginaAtual] = useState(1);

  const itensPorPagina = 20;

  const [competencia, setCompetencia] = useState({
    mes: new Date().getMonth() + 1,
    ano: new Date().getFullYear(),
  });

  const carregarDespesas = useCallback(async () => {
    try {
      const data = await listarDespesas(competencia.mes, competencia.ano);

      setDespesas(data || []);
      setPaginaAtual(1);
    } catch (error) {
      console.error("Erro ao carregar despesas", error);
    }
  }, [competencia.ano, competencia.mes]);

  useEffect(() => {
    // Fetches server state whenever the selected accounting period changes.
    carregarDespesas();
  }, [carregarDespesas]);

  async function handleDelete(id) {
    const confirmar = window.confirm("Deseja realmente excluir esta despesa?");

    if (!confirmar) return;

    try {
      await excluirDespesa(id);

      await carregarDespesas();

      setAlert({
        type: "success",
        title: "Sucesso!",
        message: "Despesa excluída com sucesso!",
        onClose: () => setAlert(null),
      });
    } catch (error) {
      console.error(error);

      setAlert({
        type: "error",
        title: "Erro",
        message: "Erro ao excluir despesa",
        onClose: () => setAlert(null),
      });
    }
  }

  const totalDespesas = despesas.reduce(
    (acc, d) => acc + Number(d.valor || 0),
    0,
  );

  const indiceInicial = (paginaAtual - 1) * itensPorPagina;

  const indiceFinal = indiceInicial + itensPorPagina;

  const despesasPaginadas = despesas.slice(indiceInicial, indiceFinal);

  const totalPaginas = Math.max(1, Math.ceil(despesas.length / itensPorPagina));

  return (
    <div>
      {/* HEADER */}
      <div className="flex flex-col gap-4 sm:flex-row sm:justify-between sm:items-center mb-6">
        <h1 className="text-2xl font-semibold text-slate-900 dark:text-white">
          Despesas
        </h1>

        <div className="flex flex-col gap-3 sm:flex-row">
          <FiltroCompetencia onChange={(c) => setCompetencia(c)} />

          <button onClick={() => setOpen(true)} className="btn">
            + Nova Despesa
          </button>
        </div>
      </div>

      {/* CARDS */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
        <Card
          title="Total Despesas"
          value={formatCurrency(totalDespesas)}
          color="text-red-600"
        />

        <Card
          title="Quantidade"
          value={despesas.length}
          color="text-slate-700 dark:text-slate-200"
        />

        <Card
          title="Média"
          value={
            despesas.length > 0
              ? formatCurrency(totalDespesas / despesas.length)
              : formatCurrency(0)
          }
          color="text-orange-600"
        />
      </div>

      {/* TABELA */}
      <div className="card">
        <div className="overflow-x-auto">
          <table className="w-full min-w-[780px]">
            <thead
              className="
              text-left
              text-sm
              text-slate-500
              dark:text-slate-400
              border-b
              border-slate-200
              dark:border-slate-700
            "
            >
              <tr className="h-10">
                <th>Descrição</th>
                <th>Categoria</th>
                <th>Recorrência</th>
                <th>Data</th>
                <th className="text-right">Valor</th>
                <th className="text-center">Ações</th>
              </tr>
            </thead>

            <tbody>
              {despesas.length > 0 ? (
                despesasPaginadas.map((d) => (
                  <Row key={d.id} {...d} onDelete={handleDelete} />
                ))
              ) : (
                <tr>
                  <td
                    colSpan="6"
                    className="text-center py-8 text-slate-500 dark:text-slate-400"
                  >
                    Nenhuma despesa encontrada
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
              <div key={pagina}>
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

        <p className="text-sm text-slate-500 dark:text-slate-400 mt-4 text-right">
          {despesas.length} despesa(s) encontrada(s)
        </p>
      </div>

      {/* MODAL */}
      {open && (
        <FormDespesa
          onClose={async () => {
            setOpen(false);
            await carregarDespesas();
          }}
        />
      )}

      {alert && <AlertModal {...alert} />}
    </div>
  );
}

function Card({ title, value, color }) {
  return (
    <div className="card">
      <p className="text-sm text-slate-500 dark:text-slate-400">{title}</p>

      <h3 className={`text-xl font-semibold ${color}`}>{value}</h3>
    </div>
  );
}

function Row({
  id,
  descricao,
  categoriaDespesa,
  despesaFixa,
  dataFimRecorrencia,
  dataTransacao,
  valor,
  onDelete,
}) {
  return (
    <tr
      className="
    h-14
    border-b
    border-slate-200
    dark:border-slate-700
    hover:bg-slate-50
    dark:hover:bg-slate-800
    transition
  "
    >
      <td>
        <div className="font-medium text-slate-800 dark:text-slate-100">
          {descricao}
        </div>
      </td>

      <td>
        <span
          className="
            px-3 py-1 rounded-full
            bg-slate-100 dark:bg-slate-800
            text-slate-700 dark:text-slate-300
            text-xs font-medium
          "
        >
          {categoriaDespesa || "SEM CATEGORIA"}
        </span>
      </td>

      <td>
        {despesaFixa ? (
          <div className="flex flex-col">
            <span className="text-xs font-medium text-green-700">Fixa</span>

            <span className="text-xs text-slate-400">
              {dataFimRecorrencia
                ? `até ${formatDate(dataFimRecorrencia)}`
                : "indeterminada"}
            </span>
          </div>
        ) : (
          <span className="text-xs text-slate-500 dark:text-slate-400">
            Única
          </span>
        )}
      </td>

      <td className="text-slate-500 dark:text-slate-400">
        {formatDate(dataTransacao)}
      </td>

      <td className="text-right font-semibold text-red-600">
        {formatCurrency(valor)}
      </td>

      <td className="text-center">
        <button
          onClick={() => onDelete(id)}
          className="
              p-2
              rounded-lg
              border
              border-red-200
              dark:border-red-900
              text-red-600
              hover:bg-red-50
              dark:hover:bg-red-950
              transition
            "
          title="Excluir"
        >
          <Trash2 size={16} />
        </button>
      </td>
    </tr>
  );
}

function formatCurrency(value) {
  return Number(value).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });
}

function formatDate(date) {
  const [ano, mes, dia] = date.split("-");

  return `${dia}/${mes}/${ano}`;
}
