import { useCallback, useEffect, useState } from "react";
import { Trash2 } from "lucide-react";
import FormReceita from "../components/forms/FormReceita";
import AlertModal from "../components/AlertModal";
import FiltroCompetencia from "../components/FiltroCompetencia";

import { listarReceitas, excluirReceita } from "../services/receitaService";

export default function Receitas() {
  const [open, setOpen] = useState(false);

  const [receitas, setReceitas] = useState([]);
  const [alert, setAlert] = useState(null);

  const [paginaAtual, setPaginaAtual] = useState(1);

  const itensPorPagina = 20;

  const [competencia, setCompetencia] = useState({
    mes: new Date().getMonth() + 1,
    ano: new Date().getFullYear(),
  });

  const carregarReceitas = useCallback(async () => {
    try {
      const data = await listarReceitas(competencia.mes, competencia.ano);

      setReceitas(data);
      setPaginaAtual(1);
    } catch (error) {
      console.error("Erro ao carregar receitas", error);
    }
  }, [competencia.ano, competencia.mes]);

  useEffect(() => {
    // Fetches server state whenever the selected accounting period changes.
    carregarReceitas();
  }, [carregarReceitas]);

  async function handleDelete(id) {
    const confirmar = window.confirm("Deseja realmente excluir esta receita?");

    if (!confirmar) return;

    try {
      await excluirReceita(id);

      carregarReceitas();

      setAlert({
        type: "success",
        title: "Sucesso!",
        message: "Receita excluída com sucesso!",
        onClose: () => setAlert(null),
      });
    } catch (error) {
      console.error(error);

      setAlert({
        type: "error",
        title: "Erro",
        message: "Erro ao excluir receita",
        onClose: () => setAlert(null),
      });
    }
  }

  // RESUMOS
  const totalReceitas = receitas.reduce((acc, r) => acc + Number(r.valor), 0);

  const salario = receitas
    .filter((r) => r.tipoReceita === "SALARIO")
    .reduce((acc, r) => acc + Number(r.valor), 0);

  const extras = receitas
    .filter((r) => r.tipoReceita !== "SALARIO")
    .reduce((acc, r) => acc + Number(r.valor), 0);

  const indiceInicial = (paginaAtual - 1) * itensPorPagina;

  const indiceFinal = indiceInicial + itensPorPagina;

  const receitasPaginadas = receitas.slice(indiceInicial, indiceFinal);

  const totalPaginas = Math.max(1, Math.ceil(receitas.length / itensPorPagina));

  return (
    <div>
      {/* HEADER */}
      <div className="flex flex-col gap-4 sm:flex-row sm:justify-between sm:items-center mb-6">
        <h1 className="text-2xl font-semibold text-slate-900 dark:text-white">
          Receitas
        </h1>

        <div className="flex flex-col gap-3 sm:flex-row">
          <FiltroCompetencia onChange={(c) => setCompetencia(c)} />

          <button onClick={() => setOpen(true)} className="btn">
            + Nova Receita
          </button>
        </div>
      </div>

      {/* CARDS */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 mb-6">
        <Card
          title="Total Receitas"
          value={formatCurrency(totalReceitas)}
          color="text-green-600"
        />

        <Card
          title="Salário"
          value={formatCurrency(salario)}
          color="text-blue-600"
        />

        <Card
          title="Extras"
          value={formatCurrency(extras)}
          color="text-orange-600"
        />
      </div>

      {/* TABELA */}
      <div className="card">
        <div className="overflow-x-auto">
          <table className="w-full min-w-[720px]">
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
                <th>Tipo</th>
                <th>Tipo Receita</th>
                <th>Data</th>
                <th className="text-right">Valor</th>
                <th className="text-center">Ações</th>
              </tr>
            </thead>

            <tbody>
              {receitasPaginadas.map((r) => (
                <Row key={r.id} {...r} onDelete={handleDelete} />
              ))}
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
          {receitas.length} receita(s) encontrada(s)
        </p>
      </div>

      {/* MODAL */}
      {open && (
        <FormReceita
          onClose={async () => {
            setOpen(false);
            await carregarReceitas();
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

      <h3 className={`text-2xl font-bold ${color}`}>{value}</h3>
    </div>
  );
}

function Row({ id, descricao, tipoReceita, dataTransacao, valor, onDelete }) {
  return (
    <tr
      className="
    h-12
    border-b
    border-slate-200
    dark:border-slate-700
    hover:bg-slate-50
    dark:hover:bg-slate-800
    transition-colors"
    >
      <td className="font-medium text-slate-900 dark:text-slate-100">
        {descricao}
      </td>

      <td>
        <span className="px-2 py-1 rounded-lg bg-green-100 text-green-700 text-xs font-medium">
          RECEITA
        </span>
      </td>

      <td>
        <span className="px-2 py-1 rounded-lg bg-blue-100 text-blue-700 text-xs font-medium">
          {tipoReceita}
        </span>
      </td>

      <td className="text-slate-500 dark:text-slate-400">
        {formatDate(dataTransacao)}
      </td>

      <td className="text-right font-semibold text-green-600">
        {formatCurrency(valor)}
      </td>
      <td className="text-center">
        <button
          onClick={() => onDelete(id)}
          className="
            p-2
            rounded-md
            border
            border-red-200
            dark:border-red-900
            text-red-600
            hover:bg-red-50
            dark:hover:bg-red-950
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
