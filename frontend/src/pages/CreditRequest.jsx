import React, { useState } from "react";
import { Controller, useForm } from "react-hook-form";

const steps = [
  { id: 1, label: "Dados pessoais" },
  { id: 2, label: "Dados financeiros" },
  { id: 3, label: "Resumo e envio" },
];

export default function CreditRequest() {
  const [submitting, setSubmitting] = useState(false);
  const [feedback, setFeedback] = useState(null);

  const {
    register,
    handleSubmit,
    reset,
    control,
    formState: { errors },
  } = useForm({
    defaultValues: {
      nome: "",
      cpf: "",
      rendaMensal: null,
      valorSolicitado: null,
      observacoes: "",
    },
  });

  const onSubmit = async (data) => {
    const payload = {
      cpf: data.cpf,
      nome: data.nome,
      rendaMensal: data.rendaMensal,
      valorSolicitado: data.valorSolicitado,
      observacoes: data.observacoes || null,
    };

    try {
      setSubmitting(true);
      setFeedback(null);

      const response = await fetch("/api/credit-requests", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorBody = await response.json().catch(() => ({}));
        const message =
          errorBody.message ||
          errorBody.error ||
          "Não foi possível iniciar o processo.";
        throw new Error(message);
      }

      const result = await response.json();
      setFeedback({
        type: "success",
        message: `Processo iniciado com sucesso. ID: ${result.processInstanceId}`,
      });
      reset();
    } catch (error) {
      setFeedback({
        type: "error",
        message: error.message || "Erro inesperado ao enviar solicitação.",
      });
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="relative min-h-screen overflow-hidden bg-[#0B0218] text-[#F7F5FF]">
      <div className="pointer-events-none absolute inset-0 overflow-hidden">
        <div className="absolute -left-32 top-8 h-80 w-80 rounded-full bg-[#8A05BE]/25 blur-3xl" />
        <div className="absolute right-0 top-40 h-96 w-96 rounded-full bg-[#4C0EDA]/30 blur-3xl" />
        <div className="absolute bottom-[-120px] left-1/2 h-96 w-96 -translate-x-1/2 rounded-full bg-[#0a011a] blur-3xl" />
      </div>

      <div className="relative z-10 flex min-h-screen flex-col lg:grid lg:grid-cols-2">
        <section className="flex flex-col justify-between px-6 py-12 lg:px-16">
          <div className="space-y-8">
            <span className="inline-flex items-center gap-2 rounded-full bg-white/10 px-4 py-1 text-xs font-semibold uppercase tracking-[0.3em] text-[#D9C2FF]">
              Mars Bank
            </span>
            <div className="space-y-4">
              <h1 className="text-4xl font-black leading-tight text-white lg:text-5xl">
                A sua próxima linha de crédito, com a experiência Cosmic Mars que você gosta.
              </h1>
              <p className="max-w-md text-sm text-[#D0C6FF]/90">
                Solicite em minutos, acompanhe tudo pelo dashboard e receba a análise em até 12 horas.
                Transparência total e comunicação simples do início ao fim.
              </p>
            </div>

            <div className="rounded-[32px] border border-white/10 bg-gradient-to-br from-[#8A05BE] via-[#5E1EBB] to-[#1C0538] p-8 text-white shadow-[0_35px_80px_-40px_rgba(90,20,140,0.8)]">
              <div className="flex items-center justify-between text-xs uppercase tracking-[0.4em] text-white/80">
                <span>Conta crédito</span>
                <span>Cosmic • Mars</span>
              </div>
              <div className="mt-6 space-y-6">
                <p className="text-sm text-white/70">Saldo disponível</p>
                <p className="text-4xl font-black tracking-tight">R$ 42.500,00</p>
                <div className="flex items-center justify-between text-xs text-white/70">
                  <div>
                    <p>Limite total</p>
                    <p className="font-semibold text-white">R$ 60.000,00</p>
                  </div>
                  <div>
                    <p>Próxima avaliação</p>
                    <p className="font-semibold text-white">30 dias</p>
                  </div>
                  <div>
                    <p>Análise expressa</p>
                    <p className="font-semibold text-white">12h úteis</p>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div className="mt-12 hidden lg:block">
            <p className="text-xs font-semibold uppercase tracking-[0.3em] text-[#BFA8FF]">
              Etapas do pedido
            </p>
            <ul className="mt-4 space-y-3">
              {steps.map((item) => (
                <li
                  key={item.id}
                  className="flex items-center gap-3 text-sm text-[#D9CFFE]/90"
                >
                  <span className="flex h-7 w-7 items-center justify-center rounded-full bg-white/10 text-xs font-semibold text-white">
                    {item.id}
                  </span>
                  {item.label}
                </li>
              ))}
            </ul>
          </div>
        </section>

        <main className="flex items-center justify-center px-6 py-12 lg:px-16">
          <div className="w-full max-w-xl space-y-10">
            <header className="space-y-3">
              <span className="inline-flex items-center gap-2 rounded-full bg-[#241040] px-4 py-1 text-xs font-semibold uppercase tracking-[0.3em] text-[#C9B4FF]">
                Nova solicitação
              </span>
              <div className="space-y-2">
                <h2 className="text-3xl font-semibold tracking-tight text-white">
                  Dados de quem solicita o crédito
                </h2>
                <p className="text-sm text-[#D0C6FF]/90">
                  Preencha as informações solicitadas para dar entrada no pedido de crédito.
                  Esses dados ajudam a nossa equipe a avaliar a proposta e seguir com a análise
                  automatizada.
                </p>
              </div>
            </header>

            <form
              onSubmit={handleSubmit(onSubmit)}
              className="space-y-10 rounded-3xl border border-white/10 bg-[#15062F]/80 p-10 shadow-[0_25px_80px_-40px_rgba(24,8,58,0.9)] backdrop-blur"
            >
              <section className="space-y-6">
                <h3 className="text-xs font-semibold uppercase tracking-[0.35em] text-[#BFA8FF]/80">
                  Dados pessoais
                </h3>
                <div className="grid gap-6 md:grid-cols-2">
                  <Field
                    label="Nome completo"
                    placeholder="Ex: Ana Souza"
                    error={errors.nome?.message}
                    {...register("nome", {
                      required: "Informe o nome completo",
                    })}
                  />
                  <Controller
                    name="cpf"
                    control={control}
                    rules={{
                      required: "Informe o CPF",
                      validate: (value) =>
                        validateCPF(value) || "CPF inválido",
                    }}
                    render={({ field }) => (
                      <Field
                        label="CPF"
                        placeholder="000.000.000-00"
                        inputMode="numeric"
                        maxLength={14}
                        value={formatCPF(field.value)}
                        onChange={(event) =>
                          field.onChange(unmaskCPF(event.target.value))
                        }
                        onBlur={field.onBlur}
                        error={errors.cpf?.message}
                      />
                    )}
                  />
                </div>
              </section>

              <section className="space-y-6">
                <h3 className="text-xs font-semibold uppercase tracking-[0.35em] text-[#BFA8FF]/80">
                  Dados financeiros
                </h3>
                <div className="grid gap-6 md:grid-cols-2">
                  <Controller
                    name="rendaMensal"
                    control={control}
                    rules={{ required: "Informe a renda mensal" }}
                    render={({ field }) => (
                      <Field
                        label="Renda mensal (R$)"
                        placeholder="R$ 5.000,00"
                        inputMode="numeric"
                        value={formatCurrency(field.value)}
                        onChange={(event) =>
                          handleCurrencyInput(event, field.onChange)
                        }
                        onBlur={field.onBlur}
                        error={errors.rendaMensal?.message}
                      />
                    )}
                  />
                  <Controller
                    name="valorSolicitado"
                    control={control}
                    rules={{ required: "Informe o valor solicitado" }}
                    render={({ field }) => (
                      <Field
                        label="Valor solicitado (R$)"
                        placeholder="R$ 20.000,00"
                        inputMode="numeric"
                        value={formatCurrency(field.value)}
                        onChange={(event) =>
                          handleCurrencyInput(event, field.onChange)
                        }
                        onBlur={field.onBlur}
                        error={errors.valorSolicitado?.message}
                      />
                    )}
                  />
                </div>
                <Field
                  as="textarea"
                  rows={3}
                  label="Observações"
                  placeholder="Contexto adicional para a avaliação de crédito"
                  error={errors.observacoes?.message}
                  className="resize-none"
                  {...register("observacoes")}
                />
              </section>

              <section className="space-y-4 rounded-2xl border border-white/10 bg-white/5 p-6">
                <h3 className="text-xs font-semibold uppercase tracking-[0.35em] text-[#BFA8FF]/80">
                  Confirmar envio
                </h3>
                <p className="text-sm text-[#E9E2FF]/80">
                  Ao prosseguir, os dados do cliente são persistidos e o fluxo do Camunda
                  inicia a jornada de aprovação.
                </p>
                <div className="flex flex-wrap items-center gap-3">
                  <span className="inline-flex items-center gap-2 rounded-full bg-[#3D1D6A]/70 px-3 py-1 text-xs font-medium text-[#F5E8FF]">
                    <span className="h-2 w-2 rounded-full bg-[#A36BFF]" />
                    SLA 12h úteis
                  </span>
                  <span className="inline-flex items-center gap-2 rounded-full bg-[#2A1454]/80 px-3 py-1 text-xs font-medium text-[#DFCFFF]">
                    Segurança de ponta
                  </span>
                </div>
              </section>

              {feedback && (
                <div
                  className={`rounded-2xl border px-4 py-3 text-sm font-medium ${
                    feedback.type === "success"
                      ? "border-[#53FFDC]/30 bg-[#0F2F2A]/70 text-[#96FFE9]"
                      : "border-[#FF7BC2]/30 bg-[#311027]/70 text-[#FFC7E6]"
                  }`}
                >
                  {feedback.message}
                </div>
              )}

              <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
                <button
                  type="submit"
                  disabled={submitting}
                  className="inline-flex w-full items-center justify-center rounded-2xl bg-gradient-to-r from-[#8A05BE] via-[#6B1AD6] to-[#3B1EFF] px-6 py-3 text-sm font-semibold text-white shadow-[0_20px_45px_-20px_rgba(132,34,223,0.9)] transition-all hover:from-[#9E24D8] hover:via-[#6F29E8] hover:to-[#4B2FFF] disabled:cursor-not-allowed disabled:opacity-60 md:w-auto"
                >
                  {submitting ? "Enviando..." : "Enviar solicitação para análise"}
                </button>
                <button
                  type="button"
                  onClick={() => {
                    reset();
                    setFeedback(null);
                  }}
                  className="inline-flex items-center justify-center rounded-2xl border border-white/15 px-5 py-3 text-sm font-semibold text-white/80 transition hover:bg-white/10 md:px-6"
                >
                  Limpar campos
                </button>
              </div>
            </form>
          </div>
        </main>
      </div>
    </div>
  );
}

const Field = React.forwardRef(function Field(
  { label, as = "input", error, className = "", ...props },
  ref
) {
  const Component = as;
  return (
    <label className="space-y-2">
      <span className="block text-xs font-medium uppercase tracking-[0.35em] text-[#BFA8FF]/80">
        {label}
      </span>
      <Component
        ref={ref}
        className={`w-full rounded-2xl border ${
          error ? "border-[#FF8AC2]/60" : "border-white/10"
        } bg-[#1B0D3E]/80 px-4 py-3 text-sm text-white outline-none transition focus:border-[#A66CFF] focus:ring-2 focus:ring-[#A66CFF]/30 ${className}`}
        {...props}
      />
      {error && (
        <span className="block text-xs font-medium text-[#FF9CCE]">{error}</span>
      )}
    </label>
  );
});

const currencyFormatter = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
});

function formatCurrency(value) {
  if (value === null || value === undefined || value === "") return "";
  return currencyFormatter.format(Number(value));
}

function handleCurrencyInput(event, onChange) {
  const digits = event.target.value.replace(/\D/g, "");
  const numericValue = digits ? Number(digits) / 100 : null;
  onChange(numericValue);
}

function formatCPF(value) {
  const digits = (value || "").replace(/\D/g, "").slice(0, 11);
  let formatted = digits.replace(/^(\d{3})(\d)/, "$1.$2");
  formatted = formatted.replace(/^(\d{3})\.(\d{3})(\d)/, "$1.$2.$3");
  formatted = formatted.replace(/\.(\d{3})(\d{1,2})$/, ".$1-$2");
  return formatted;
}

function unmaskCPF(value) {
  return (value || "").replace(/\D/g, "").slice(0, 11);
}

function validateCPF(value) {
  const cpf = unmaskCPF(value);
  if (!cpf || cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;

  let sum = 0;
  for (let i = 0; i < 9; i += 1) {
    sum += Number(cpf[i]) * (10 - i);
  }
  let firstDigit = (sum * 10) % 11;
  if (firstDigit === 10) firstDigit = 0;
  if (firstDigit !== Number(cpf[9])) return false;

  sum = 0;
  for (let i = 0; i < 10; i += 1) {
    sum += Number(cpf[i]) * (11 - i);
  }
  let secondDigit = (sum * 10) % 11;
  if (secondDigit === 10) secondDigit = 0;
  return secondDigit === Number(cpf[10]);
}
