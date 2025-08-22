package com.bancoonline.controller;

import com.bancoonline.model.Conta;
import com.bancoonline.model.Cliente;
import com.bancoonline.repository.ContaRepository;
import com.bancoonline.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping
    public ResponseEntity<?> criarConta(@RequestBody CriarContaRequest request) {
        try {
            Optional<Cliente> clienteOpt = clienteRepository.findById(request.getClienteId());
            if (clienteOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Cliente não encontrado");
            }

            Cliente cliente = clienteOpt.get();
            String numeroConta = gerarNumeroConta();
            
            Conta novaConta = new Conta(numeroConta, "0001", request.getTipo(), cliente);
            Conta contaSalva = contaRepository.save(novaConta);
            
            return ResponseEntity.ok(contaSalva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao criar conta: " + e.getMessage());
        }
    }

    
    @GetMapping("/{numeroConta}/saldo")
    public ResponseEntity<?> consultarSaldo(@PathVariable String numeroConta) {
        Optional<Conta> contaOpt = contaRepository.findByNumeroConta(numeroConta);
        if (contaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Conta não encontrada");
        }
        
        Conta conta = contaOpt.get();
        return ResponseEntity.ok(new SaldoResponse(conta.getNumeroConta(), conta.getSaldo()));
    }

  
    @PostMapping("/{numeroConta}/depositar")
    public ResponseEntity<?> depositar(@PathVariable String numeroConta, 
                                     @RequestBody OperacaoRequest request) {
        try {
            Optional<Conta> contaOpt = contaRepository.findByNumeroConta(numeroConta);
            if (contaOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Conta não encontrada");
            }

            Conta conta = contaOpt.get();
            conta.depositar(request.getValor());
            contaRepository.save(conta);

            return ResponseEntity.ok(new OperacaoResponse("Depósito realizado com sucesso", conta.getSaldo()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

   
    @PostMapping("/{numeroConta}/sacar")
    public ResponseEntity<?> sacar(@PathVariable String numeroConta, 
                                 @RequestBody OperacaoRequest request) {
        try {
            Optional<Conta> contaOpt = contaRepository.findByNumeroConta(numeroConta);
            if (contaOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Conta não encontrada");
            }

            Conta conta = contaOpt.get();
            conta.sacar(request.getValor());
            contaRepository.save(conta);

            return ResponseEntity.ok(new OperacaoResponse("Saque realizado com sucesso", conta.getSaldo()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro: " + e.getMessage());
        }
    }

   
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Conta>> listarContasCliente(@PathVariable Long clienteId) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Conta> contas = contaRepository.findByClienteAndAtivaTrue(clienteOpt.get());
        return ResponseEntity.ok(contas);
    }

    
    private String gerarNumeroConta() {
        String numeroConta;
        do {
            numeroConta = String.format("%08d", new Random().nextInt(100000000));
        } while (contaRepository.existsByNumeroConta(numeroConta));
        
        return numeroConta;
    }

   
    public static class CriarContaRequest {
        private Long clienteId;
        private Conta.TipoConta tipo;

        public Long getClienteId() { return clienteId; }
        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
        public Conta.TipoConta getTipo() { return tipo; }
        public void setTipo(Conta.TipoConta tipo) { this.tipo = tipo; }
    }

    public static class OperacaoRequest {
        private BigDecimal valor;

        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }
    }

    public static class SaldoResponse {
        private String numeroConta;
        private BigDecimal saldo;

        public SaldoResponse(String numeroConta, BigDecimal saldo) {
            this.numeroConta = numeroConta;
            this.saldo = saldo;
        }

        public String getNumeroConta() { return numeroConta; }
        public BigDecimal getSaldo() { return saldo; }
    }

    public static class OperacaoResponse {
        private String mensagem;
        private BigDecimal saldoAtual;

        public OperacaoResponse(String mensagem, BigDecimal saldoAtual) {
            this.mensagem = mensagem;
            this.saldoAtual = saldoAtual;
        }

        public String getMensagem() { return mensagem; }
        public BigDecimal getSaldoAtual() { return saldoAtual; }
    }
}