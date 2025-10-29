package com.mars.workflow.controller;

import com.mars.workflow.dto.ClienteDTO;
import com.mars.workflow.entity.Cliente;
import com.mars.workflow.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listar() {
        List<ClienteDTO> clientes = clienteService.listarTodos()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(clientes);
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscar(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(cliente -> ResponseEntity.ok(toDTO(cliente)))
                .orElse(ResponseEntity.notFound().build());
    }

    // CRIAR
    @PostMapping
    public ResponseEntity<ClienteDTO> criar(@RequestBody ClienteDTO dto) {
        Cliente novo = toEntity(dto);
        Cliente salvo = clienteService.salvar(novo);
        return ResponseEntity.ok(toDTO(salvo));
    }

    // ATUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> atualizar(@PathVariable Long id, @RequestBody ClienteDTO dto) {
        Cliente atualizado = clienteService.atualizar(id, toEntity(dto));
        return ResponseEntity.ok(toDTO(atualizado));
    }

    // DELETAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // --- MÉTODOS DE CONVERSÃO ENTRE DTO E ENTITY ---

    private ClienteDTO toDTO(Cliente c) {
        return new ClienteDTO(
                c.getId(),
                c.getNome(),
                c.getCpf(),
                c.getRendaMensal(),
                c.getDataCadastro()
        );
    }

    private Cliente toEntity(ClienteDTO dto) {
        Cliente c = new Cliente();
        c.setId(dto.getId());
        c.setNome(dto.getNome());
        c.setCpf(dto.getCpf());
        c.setRendaMensal(dto.getRendaMensal());
        c.setDataCadastro(dto.getDataCadastro());
        return c;
    }
}