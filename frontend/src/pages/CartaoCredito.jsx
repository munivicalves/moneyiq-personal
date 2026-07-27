import { useCallback, useEffect, useState } from "react";
import FormCartao from "../components/forms/FormCartao";
import FormCartaoCredito from "../components/forms/FormCartaoCredito";
import AlertModal from "../components/AlertModal";
import {
  listarCartoes,
  buscarFatura,
  excluirCartao,
  excluirDespesaCartao,
} from "../services/cartaoService";

export default function CartaoCredito() {
  const [mes, setMes] = useState(() => getCompetenciaAtual());

  const [openCartao, setOpenCartao] = useState(false);

  const [cartaoEditando, setCartaoEditando] = useState(null);

  const [cartaoId, setCartaoId] = useState("");

  const [cartoes, setCartoes] = useState([]);

  const [aba, setAba] = useState("faturas");

  const [lancamentos, setLancamentos] = useState([]);

  const [total, setTotal] = useState(0);

  const [open, setOpen] = useState(false);

  const [paginaAtual, setPaginaAtual] = useState(1);

  const [alert, setAlert] = useState(null);

  const itensPorPagina = 15;

  const carregarCartoes = useCallback(async () => {
    try {
      const data = await listarCartoes();

      setCartoes(data);

      setCartaoId((idAtual) => idAtual || data[0]?.id || "");
    } catch (error) {
      console.error("Erro ao carregar cartões", error);
    }
  }, []);

  const carregarFatura = useCallback(async () => {
    try {
      const data = await buscarFatura(cartaoId, mes);

      setLancamentos(data.despesas || []);
      setTotal(Number(data.totalFatura || 0));

      setPaginaAtual(1);
    } catch (error) {
      console.error("Erro ao carregar fatura", error);
    }
  }, [cartaoId, mes]);

  useEffect(() => {
    // Fetches server state once when opening the credit card page.
    // eslint-disable-next-line react-hooks/set-state-in-effect
    carregarCartoes();
  }, [carregarCartoes]);

  useEffect(() => {
    if (cartaoId && aba === "faturas") {
      // Fetches the invoice whenever its selected filters change.
      // eslint-disable-next-line react-hooks/set-state-in-effect
      carregarFatura();
    }
  }, [cartaoId, mes, aba, carregarFatura]);

  async function handleExcluirCartao(cartao) {
    const confirmar = window.confirm(
      `Deseja realmente excluir o cartão "${cartao.nome}"?`,
    );

    if (!confirmar) return;

    try {
      await excluirCartao(cartao.id);

      setAlert({
        type: "success",
        title: "Sucesso!",
        message: "Cartão excluído com sucesso",
        onClose: async () => {
          setAlert(null);
          await carregarCartoes();
        },
      });
    } catch (error) {
      setAlert({
        type: "error",
        title: "Erro",
        message: error?.response?.data || "Não foi possível excluir o cartão",
        onClose: () => setAlert(null),
      });
    }
  }

  async function handleExcluirDespesa(despesa) {
    const confirmar = window.confirm(
      `Deseja excluir a despesa "${despesa.descricao}"?`,
    );

    if (!confirmar) return;

    try {
      await excluirDespesaCartao(cartaoId, despesa.id);

      await carregarFatura();

      setAlert({
        type: "success",
        title: "Sucesso!",
        message: "Despesa excluída com sucesso",
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
      <div className="flex flex-col gap-4 lg:flex-row lg:justify-between lg:items-center">
        <div>
          <h1 className="text-2xl font-semibold text-slate-900 dark:text-white">
            Cartão de Crédito
          </h1>

          <p className="text-violet-600 font-medium mt-1">
            Competência: {formatCompetencia(mes)}
          </p>

          <p className="text-slate-500 dark:text-slate-400 text-sm">
            Gerencie seus cartões e acompanhe suas faturas
          </p>
        </div>

        <div
          className="
            flex flex-wrap items-center gap-1
            bg-slate-100 dark:bg-slate-800
            p-1
            rounded-xl
          "
        >
          <button
            onClick={() => setAba("faturas")}
            className={`
              flex-1 sm:flex-none px-4 py-2 rounded-lg text-sm font-medium transition-all
              ${
                aba === "faturas"
                  ? "bg-violet-600 text-white shadow-md"
                  : "text-slate-600 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700"
              }
            `}
          >
            Faturas
          </button>

          <button
            onClick={() => setAba("cartoes")}
            className={`
              flex-1 sm:flex-none px-4 py-2 rounded-lg text-sm font-medium transition-all
              ${
                aba === "cartoes"
                  ? "bg-violet-600 text-white shadow-md"
                  : "text-slate-600 dark:text-slate-300 hover:bg-slate-200 dark:hover:bg-slate-700"
              }
            `}
          >
            Cartões
          </button>

          {aba === "faturas" && (
            <button
              onClick={() => setOpen(true)}
              className="btn w-full sm:w-auto"
              disabled={!cartaoId}
            >
              Nova Despesa
            </button>
          )}
        </div>
      </div>

      {/* ABA FATURAS */}
      {aba === "faturas" && (
        <>
          {/* FILTROS */}
          <div
            className="
              card
              grid md:grid-cols-3 gap-4
              hover:shadow-lg
              transition-all duration-300
            "
          >
            <div>
              <label className="label">Competência</label>

              <input
                type="month"
                value={mes}
                onChange={(e) => setMes(e.target.value)}
                className="input"
              />

              <p className="text-sm text-violet-600 mt-2 font-medium">
                {formatCompetencia(mes)}
              </p>
            </div>

            <div>
              <label className="label">Cartão</label>

              <select
                value={cartaoId}
                onChange={(e) => setCartaoId(e.target.value)}
                className="input"
              >
                {cartoes.map((cartao) => (
                  <option key={cartao.id} value={cartao.id}>
                    {cartao.nome}
                  </option>
                ))}
              </select>
            </div>

            <div className="flex items-end justify-start md:justify-end">
              <div>
                <p className="text-sm text-slate-500 dark:text-slate-400">
                  Total da fatura
                </p>

                <h2 className="text-2xl font-bold text-red-600">
                  {formatCurrency(total)}
                </h2>
              </div>
            </div>
          </div>

          {/* TABELA */}
          <div className="card">
            <div className="overflow-x-auto">
              <table className="w-full min-w-[780px] text-sm">
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
                    <th>Categoria</th>
                    <th>Parcela</th>
                    <th className="text-right">Valor</th>
                    <th className="text-center">Ações</th>
                  </tr>
                </thead>

                <tbody>
                  {lancamentos.length > 0 ? (
                    lancamentosPaginados.map((l) => (
                      <Row
                        key={l.id}
                        {...l}
                        onDelete={() => handleExcluirDespesa(l)}
                      />
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

            <p className="text-sm text-slate-500 dark:text-slate-400 mt-4 text-right">
              Exibindo {lancamentosPaginados.length} de {lancamentos.length}{" "}
              lançamento(s)
            </p>

            {totalPaginas > 1 && (
              <div className="flex flex-wrap justify-center items-center gap-2 mt-6">
                <button
                  onClick={() => setPaginaAtual((p) => Math.max(1, p - 1))}
                  disabled={paginaAtual === 1}
                  className="
                    h-9 w-9 rounded-lg border
                    border-slate-200 dark:border-slate-700
                    text-slate-700 dark:text-slate-200
                    disabled:opacity-40
                    hover:bg-slate-50 dark:hover:bg-slate-800
                  "
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
                        className={`
                        h-9 min-w-9 px-3 rounded-lg text-sm font-medium transition
                        ${
                          paginaAtual === pagina
                            ? "bg-violet-600 text-white shadow"
                            : "bg-white dark:bg-slate-900 border border-slate-200 dark:border-slate-700 text-slate-700 dark:text-slate-200 hover:bg-slate-50 dark:hover:bg-slate-800"
                        }
                      `}
                      >
                        {pagina}
                      </button>
                    </div>
                  ))}

                <button
                  onClick={() =>
                    setPaginaAtual((p) => Math.min(totalPaginas, p + 1))
                  }
                  disabled={paginaAtual === totalPaginas}
                  className="
                    h-9 w-9 rounded-lg border
                    border-slate-200 dark:border-slate-700
                    text-slate-700 dark:text-slate-200
                    disabled:opacity-40
                    hover:bg-slate-50 dark:hover:bg-slate-800
                  "
                >
                  →
                </button>
              </div>
            )}
          </div>
        </>
      )}

      {/* ABA CARTÕES */}
      {aba === "cartoes" && (
        <div className="card">
          <div className="flex flex-col gap-3 sm:flex-row sm:justify-between sm:items-center mb-6">
            <h2 className="text-lg font-semibold text-slate-900 dark:text-white">
              Meus Cartões
            </h2>

            <button className="btn" onClick={() => setOpenCartao(true)}>
              Novo Cartão
            </button>
          </div>

          {cartoes.length > 0 ? (
            <div className="grid md:grid-cols-2 gap-4">
              {cartoes.map((cartao) => (
                <div
                  key={cartao.id}
                  className="
                    border rounded-2xl p-5
                    bg-gradient-to-r
                    from-violet-50 to-white
                    dark:from-slate-900 dark:to-slate-800
                    border-slate-200 dark:border-slate-700
                    shadow-sm hover:shadow-md
                    transition
                    "
                >
                  <div className="flex flex-col gap-4 sm:flex-row sm:justify-between sm:items-start">
                    <div>
                      <h3 className="font-semibold text-lg text-slate-900 dark:text-white">
                        {cartao.nome}
                      </h3>

                      <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">
                        {cartao.bandeira || "Sem bandeira"}
                      </p>

                      <p className="text-sm text-slate-500 dark:text-slate-400">
                        Vencimento: dia {cartao.diaVencimento}
                      </p>
                    </div>

                    <div className="flex flex-wrap gap-2">
                      <button
                        className="px-3 py-1 text-sm rounded-lg bg-slate-100 dark:bg-slate-700
                        hover:bg-slate-200 dark:hover:bg-slate-600
                        text-slate-700 dark:text-slate-200"
                        onClick={() => {
                          setCartaoEditando(cartao);
                          setOpenCartao(true);
                        }}
                      >
                        Editar
                      </button>

                      <button
                        className="px-3 py-1 text-sm rounded-lg bg-red-100 text-red-700
                        dark:bg-red-900/40 dark:text-red-300 hover:bg-red-200 dark:hover:bg-red-900/60"
                        onClick={() => handleExcluirCartao(cartao)}
                      >
                        Excluir
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-10 text-slate-500 dark:text-slate-400">
              Nenhum cartão cadastrado.
            </div>
          )}
        </div>
      )}
      {open && (
        <FormCartao
          cartaoId={cartaoId}
          competencia={mes}
          onClose={() => {
            setOpen(false);
            carregarFatura();
          }}
        />
      )}
      {openCartao && (
        <FormCartaoCredito
          cartao={cartaoEditando}
          onClose={() => {
            setOpenCartao(false);
            setCartaoEditando(null);
          }}
          onSuccess={() => {
            carregarCartoes();
            setOpenCartao(false);
            setCartaoEditando(null);
          }}
        />
      )}

      {alert && <AlertModal {...alert} />}
    </div>
  );
}

function getCompetenciaAtual() {
  const hoje = new Date();
  const mes = String(hoje.getMonth() + 1).padStart(2, "0");

  return `${hoje.getFullYear()}-${mes}`;
}

function Row({
  descricao,
  categoriaDespesa,
  parcelaAtual,
  totalParcelas,
  valor,
  dataCompra,
  dataCriacao,
  onDelete,
}) {
  return (
    <tr
      className="
        h-12
        border-b border-slate-200 dark:border-slate-700
        hover:bg-violet-50 dark:hover:bg-violet-900/20
        text-slate-700 dark:text-slate-300
        transition
      "
    >
      <td className="text-slate-500 dark:text-slate-400">
        {formatDate(dataCompra || dataCriacao)}
      </td>

      <td className="font-medium text-slate-900 dark:text-slate-100">
        {descricao}
      </td>

      <td>
        <span
          className="bg-violet-100 text-violet-700
          dark:bg-violet-900/40 dark:text-violet-300 px-3 py-1 rounded-full text-xs font-medium"
        >
          {formatCategoria(categoriaDespesa)}
        </span>
      </td>

      <td>
        {parcelaAtual}/{totalParcelas}
      </td>

      <td className="text-right text-red-600 font-semibold">
        {formatCurrency(valor)}
      </td>

      <td className="text-center">
        <button
          onClick={onDelete}
          className="
            px-3 py-1
            text-xs
            rounded-lg
            bg-red-100 text-red-700
            dark:bg-red-900/40 dark:text-red-300
            hover:bg-red-200 dark:hover:bg-red-900/60
            transition
          "
        >
          Excluir
        </button>
      </td>
    </tr>
  );
}

function formatCategoria(categoria) {
  const categorias = {
    ALIMENTACAO: "Alimentação",
    TRANSPORTE: "Transporte",
    SAUDE: "Saúde",
    EDUCACAO: "Educação",
    LAZER: "Lazer",
    ASSINATURAS: "Assinaturas",
    AGUA: "Água",
    ENERGIA: "Energia",
    FARMACIA: "Farmácia",
    IMPOSTOS: "Impostos",
    ALUGUEL: "Aluguel",
    OUTROS: "Outros",
  };

  return categorias[categoria] || categoria;
}

function formatCurrency(value) {
  return Number(value).toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL",
  });
}

function formatDate(date) {
  if (!date) return "-";

  const [ano, mes, dia] = date.split("T")[0].split("-");

  return `${dia}/${mes}/${ano}`;
}

function formatCompetencia(competencia) {
  if (!competencia) return "";

  const [ano, mes] = competencia.split("-");

  const meses = [
    "Janeiro",
    "Fevereiro",
    "Março",
    "Abril",
    "Maio",
    "Junho",
    "Julho",
    "Agosto",
    "Setembro",
    "Outubro",
    "Novembro",
    "Dezembro",
  ];

  return `${meses[Number(mes) - 1]} de ${ano}`;
}
