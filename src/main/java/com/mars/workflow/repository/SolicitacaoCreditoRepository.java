package com.mars.workflow.repository;

import com.mars.workflow.entity.SolicitacaoCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoCreditoRepository extends JpaRepository<SolicitacaoCredito, Long> {
    List<SolicitacaoCredito> findByClienteId(Long clienteId);
}
