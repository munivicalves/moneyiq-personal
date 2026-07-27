import { useState } from "react";
import Modal from "../Modal";
import AlertModal from "../AlertModal";
import { criarDespesaCartao } from "../../services/cartaoService";
import { getApiErrorMessage } from "../../services/apiError";

export default function FormCartao({ onClose, cartaoId, competencia }) {
  const [parcelado, setParcelado] = useState(false);
  const [alert, setAlert] = useState(null);

  const [form, setForm] = useState({
    descricao: "",
    valor: "",
    categoriaDespesa: "",
    parcelas: 1,
    dataCompra: "",
  });

  function handleChange(e) {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  }

  function formatCurrency(value) {
    value = value.replace(/\D/g, "");

    return (Number(value) / 100).toLocaleString("pt-BR", {
      style: "currency",
      currency: "BRL",
    });
  }

  async function handleSubmit(e) {
    e.preventDefault();

    try {
      const valorNumerico = Number(
        form.valor
          .replace("R$", "")
          .replace(/\./g, "")
          .replace(",", ".")
          .trim(),
      );

      const payload = {
        dataCompra: form.dataCompra,
        descricao: form.descricao,
        valor: valorNumerico,
        categoriaDespesa: form.categoriaDespesa,
        parcelaAtual: 1,
        totalParcelas: parcelado ? Number(form.parcelas) : 1,
        competencia,
      };

      await criarDespesaCartao(cartaoId, payload);

      setAlert({
        type: "success",
        title: "Sucesso!",
        message: "Despesa cadastrada com sucesso!",
        onClose: () => {
          setAlert(null);
          onClose?.();
        },
      });
    } catch (error) {
      console.error("Erro ao cadastrar despesa no cartão", error);
      console.error("Resposta da API:", JSON.stringify(error?.response?.data));
      setAlert({
        type: "error",
        title: "Erro",
        message: getApiErrorMessage(error),
        onClose: () => setAlert(null),
      });
    }
  }

  return (
    <>
      <Modal title="Despesa no Cartão" onClose={onClose}>
        <form onSubmit={handleSubmit} className="flex flex-col gap-3">
          <input
            type="date"
            name="dataCompra"
            className="input"
            value={form.dataCompra}
            onChange={handleChange}
            required
          />

          <input
            name="descricao"
            placeholder="Descrição"
            className="input"
            value={form.descricao}
            onChange={handleChange}
            required
          />

          <input
            placeholder="Valor total"
            className="input"
            value={form.valor}
            onChange={(e) =>
              setForm({
                ...form,
                valor: formatCurrency(e.target.value),
              })
            }
            required
          />

          <select
            name="categoriaDespesa"
            className="input"
            value={form.categoriaDespesa}
            onChange={handleChange}
            required
          >
            <option value="">Categoria</option>

            <option value="ALIMENTACAO">Alimentação</option>
            <option value="TRANSPORTE">Transporte</option>
            <option value="SAUDE">Saúde</option>
            <option value="EDUCACAO">Educação</option>
            <option value="LAZER">Lazer</option>
            <option value="ASSINATURAS">Assinaturas</option>
            <option value="AGUA">Água</option>
            <option value="ENERGIA">Energia</option>
            <option value="FARMACIA">Farmácia</option>
            <option value="IMPOSTOS">Impostos</option>
            <option value="ALUGUEL">Aluguel</option>
            <option value="OUTROS">Outros</option>
          </select>

          <div className="flex items-center gap-2 text-slate-700 dark:text-slate-200">
            <input
              type="checkbox"
              className="accent-violet-600"
              checked={parcelado}
              onChange={() => setParcelado(!parcelado)}
            />

            <label>Compra parcelada</label>
          </div>

          {parcelado && (
            <input
              type="number"
              min="2"
              name="parcelas"
              placeholder="Quantidade de parcelas"
              className="input"
              value={form.parcelas}
              onChange={handleChange}
              required
            />
          )}

          <button type="submit" className="btn">
            Salvar Despesa
          </button>
        </form>
      </Modal>

      {alert && <AlertModal {...alert} />}
    </>
  );
}
