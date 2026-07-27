import { useState } from "react";
import Modal from "../Modal";
import AlertModal from "../AlertModal";
import { criarCartao, atualizarCartao } from "../../services/cartaoService";

export default function FormCartaoCredito({ onClose, onSuccess, cartao }) {
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);

  const [form, setForm] = useState({
    nome: cartao?.nome || "",
    bandeira: cartao?.bandeira || "",
    diaVencimento: cartao?.diaVencimento || 10,
  });

  function handleChange(e) {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  }

  async function handleSubmit(e) {
    e.preventDefault();

    try {
      setLoading(true);

      if (cartao) {
        await atualizarCartao(cartao.id, form);
      } else {
        await criarCartao(form);
      }

      setAlert({
        type: "success",
        title: "Sucesso!",
        message: cartao
          ? "Cartão atualizado com sucesso!"
          : "Cartão criado com sucesso!",
        onClose: () => {
          setAlert(null);
          onSuccess?.();
          onClose();
        },
      });
    } catch (error) {
      console.error(error);
      setAlert({
        type: "error",
        title: "Erro",
        message: "Erro ao salvar cartão",
        onClose: () => setAlert(null),
      });
    } finally {
      setLoading(false);
    }
  }
  return (
    <>
      <Modal
        title={cartao ? "Editar Cartão" : "Novo Cartão de Crédito"}
        onClose={onClose}
      >
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          <input
            name="nome"
            placeholder="Nome do cartão"
            className="input"
            value={form.nome}
            onChange={handleChange}
            required
          />

          <select
            name="bandeira"
            className="input"
            value={form.bandeira}
            onChange={handleChange}
          >
            <option value="">Selecione a bandeira</option>

            <option value="VISA">Visa</option>
            <option value="MASTERCARD">Mastercard</option>
            <option value="ELO">Elo</option>
            <option value="AMEX">American Express</option>
            <option value="HIPERCARD">Hipercard</option>
            <option value="OUTRA">Outra</option>
          </select>

          <div>
            <label className="label">Dia do vencimento</label>

            <input
              type="number"
              min="1"
              max="31"
              name="diaVencimento"
              value={form.diaVencimento}
              onChange={handleChange}
              className="input"
              required
            />
          </div>

          <button type="submit" className="btn" disabled={loading}>
            {loading ? "Salvando..." : "Salvar Cartão"}
          </button>
        </form>
      </Modal>

      {alert && <AlertModal {...alert} />}
    </>
  );
}
