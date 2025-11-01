import React from "react";
import { Controller, useForm } from "react-hook-form";

const steps = [
  { id: 1, label: "Dados pessoais" },
  { id: 2, label: "Dados financeiros" },
  { id: 3, label: "Resumo e envio" },
];

export default function CreditRequest() {
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

  const onSubmit = (data) => {
    console.log("Dados enviados:", data);
    alert("Solicitação enviada com sucesso!");
    reset();
  };

  return (
    <div className="min-h-screen bg-slate-950 text-slate-50 lg:grid lg:grid-cols-[360px_1fr]">
      <aside className="hidden lg:flex flex-col justify-between border-r border-white/10 bg-slate-950/60 p-10">
        <div>
          <p className="text-sm uppercase tracking-[0.35em] text-slate-400">
            Mars Workflow
          </p>
          <h1 className="mt-6 text-4xl font-black leading-snug text-white">
            Solicitação de
            <br />
            Crédito
          </h1>
          <p className="mt-4 max-w-xs text-sm text-slate-400">
            Seus clientes passam por uma experiência rápida e segura. Use o
            formulário ao lado para iniciar a jornada no fluxo Camunda.
          </p>
        </div>

        <div className="space-y-6">
          <div>
            <p className="text-xs font-semibold uppercase tracking-[0.3em] text-slate-500">
              Etapas
            </p>
            <ul className="mt-3 space-y-2">
              {steps.map((item) => (
                <li
                  key={item.id}
                  className="flex items-center gap-3 text-sm text-slate-300"
                >
                  <span className="flex h-6 w-6 items-center justify-center rounded-full border border-white/20 text-xs font-semibold text-white/80">
                    {item.id}
                  </span>
                  {item.label}
                </li>
              ))}
            </ul>
          </div>

          <div className="rounded-2xl border border-white/10 bg-white/5 p-6 backdrop-blur">
            <p className="text-xs uppercase tracking-[0.3em] text-slate-500">
              SLA médio
            </p>
            <div className="mt-3 text-3xl font-black text-emerald-400">
              12h
            </div>
            <p className="mt-1 text-xs text-slate-400">
              Após o envio, o Camunda inicia automaticamente a análise.
            </p>
          </div>
        </div>
      </aside>

      <main className="flex min-h-screen w-full items-center justify-center px-6 py-16">
        <div className="w-full max-w-2xl space-y-10">
          <header className="space-y-3">
            <span className="inline-flex items-center rounded-full border border-white/10 bg-white/10 px-3 py-1 text-xs font-medium text-slate-200">
              Pré-análise de cliente
            </span>
            <div>
              <h2 className="text-3xl font-semibold tracking-tight text-white">
                Dados de quem solicita o crédito
              </h2>
              <p className="mt-2 text-sm text-slate-400">
                Informe as credenciais básicas para alimentar a entidade{" "}
                <strong>Cliente</strong>. Esses dados serão usados para disparar
                o fluxo Camunda no próximo passo.
              </p>
            </div>
          </header>

          <form
            onSubmit={handleSubmit(onSubmit)}
            className="space-y-10 rounded-3xl border border-white/10 bg-white/[0.06] p-10 shadow-2xl backdrop-blur"
          >
            <section className="space-y-6">
              <h3 className="text-sm font-semibold uppercase tracking-[0.25em] text-slate-400">
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
              <h3 className="text-sm font-semibold uppercase tracking-[0.25em] text-slate-400">
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

            <section className="space-y-4 rounded-2xl border border-white/10 bg-black/20 p-6">
              <h3 className="text-sm font-semibold uppercase tracking-[0.25em] text-slate-400">
                Confirmar envio
              </h3>
              <p className="text-sm text-slate-300">
                Ao prosseguir, os dados do cliente são persistidos e o fluxo do
                Camunda inicia a jornada de aprovação.
              </p>
              <div className="flex flex-wrap items-center gap-3">
                <span className="inline-flex items-center gap-2 rounded-full border border-emerald-600/40 bg-emerald-500/10 px-3 py-1 text-xs font-medium text-emerald-300">
                  <span className="h-2 w-2 rounded-full bg-emerald-400"></span>
                  SLA 12h
                </span>
                <span className="inline-flex items-center gap-2 rounded-full border border-indigo-500/40 bg-indigo-500/10 px-3 py-1 text-xs font-medium text-indigo-200">
                  Dados criptografados
                </span>
              </div>
            </section>

            <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
              <button
                type="submit"
                className="inline-flex w-full items-center justify-center rounded-2xl bg-gradient-to-r from-indigo-500 via-purple-500 to-sky-500 px-6 py-3 text-sm font-semibold text-white shadow-lg shadow-indigo-900/30 transition-all hover:from-indigo-400 hover:via-purple-400 hover:to-sky-400 hover:shadow-xl md:w-auto"
              >
                Enviar solicitação para análise
              </button>
              <button
                type="button"
                onClick={() => reset()}
                className="inline-flex items-center justify-center rounded-2xl border border-white/10 px-5 py-3 text-sm font-semibold text-slate-200 transition hover:bg-white/10"
              >
                Limpar campos
              </button>
            </div>
          </form>
        </div>
      </main>
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
      <span className="block text-xs font-medium uppercase tracking-[0.3em] text-slate-400">
        {label}
      </span>
      <Component
        ref={ref}
        className={`w-full rounded-xl border ${
          error ? "border-red-400" : "border-white/10"
        } bg-black/30 px-4 py-3 text-sm text-white outline-none transition focus:border-indigo-400 focus:ring-2 focus:ring-indigo-500/40 ${className}`}
        {...props}
      />
      {error && (
        <span className="block text-xs font-medium text-red-300">{error}</span>
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
