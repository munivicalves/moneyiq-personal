import { useState } from "react";
import Modal from "../Modal";
import AlertModal from "../AlertModal";
import { criarDespesa } from "../../services/despesaService";
import { getApiErrorMessage } from "../../services/apiError";

export default function FormDespesa({ onClose }) {
  const [form, setForm] = useState({
    descricao: "",
    valor: "",
    dataTransacao: "",
    categoriaDespesa: "",
    despesaFixa: false,
    dataFimRecorrencia: "",
  });

  const [alert, setAlert] = useState(null);

  function handleChange(e) {
    const { name, value, type, checked } = e.target;

    setForm({
      ...form,
      [name]: type === "checkbox" ? checked : value,
    });
  }

  function formatCurrency(value) {
    value = value.replace(/\D/g, "");

    value = (Number(value) / 100).toLocaleString("pt-BR", {
      style: "currency",
      currency: "BRL",
    });

    return value;
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
        tipo: "DESPESA",
        descricao: form.descricao,
        valor: valorNumerico,
        dataTransacao: form.dataTransacao,
        categoriaDespesa: form.categoriaDespesa,
        despesaFixa: form.despesaFixa,
        dataFimRecorrencia: form.dataFimRecorrencia || null,
      };

      await criarDespesa(payload);

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
      console.error("Erro ao cadastrar despesa", error);
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
      <Modal title="Nova Despesa" onClose={onClose}>
        <form onSubmit={handleSubmit} className="flex flex-col gap-3">
          <input
            name="descricao"
            placeholder="Descrição"
            className="input"
            value={form.descricao}
            onChange={handleChange}
            required
          />

          <input
            name="valor"
            placeholder="Valor"
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
            <option value="">Selecione uma categoria</option>

            <option value="ALUGUEL">Aluguel</option>
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
            <option value="OUTROS">Outros</option>
          </select>

          <input
            type="date"
            name="dataTransacao"
            className="input"
            value={form.dataTransacao}
            onChange={handleChange}
            required
          />

          <label className="flex items-center gap-2 text-sm text-slate-700 dark:text-slate-200">
            <input
              type="checkbox"
              name="despesaFixa"
              className="accent-violet-600"
              checked={form.despesaFixa}
              onChange={handleChange}
            />
            Despesa fixa
          </label>

          {form.despesaFixa && (
            <>
              <label className="text-sm text-slate-500 dark:text-slate-400">
                Data final da recorrência
              </label>

              <input
                type="date"
                name="dataFimRecorrencia"
                className="input"
                value={form.dataFimRecorrencia}
                onChange={handleChange}
              />
            </>
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
