package com.mars.workflow.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("logDelegate")
public class LogDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String nome = (String) execution.getVariable("nome");
        System.out.println("Executando LogDelegate — Nome recebido: " + nome);
    }
}