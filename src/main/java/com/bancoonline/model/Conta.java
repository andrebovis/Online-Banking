package com.bancoonline.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "contas")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numeroConta;

    @Column(nullable = false)
    private String agencia;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    private TipoConta tipo;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private Boolean ativa;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    
    public enum TipoConta {
        CORRENTE, POUPANCA, SALARIO
    }

    
    public Conta() {
        this.dataCriacao = LocalDateTime.now();
        this.ativa = true;
        this.saldo = BigDecimal.ZERO;
    }

    
    public Conta(String numeroConta, String agencia, TipoConta tipo, Cliente cliente) {
        this();
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.tipo = tipo;
        this.cliente = cliente;
    }

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

 
    public void depositar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) > 0) {
            this.saldo = this.saldo.add(valor);
        } else {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
    }

    public void sacar(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        if (this.saldo.compareTo(valor) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }
        this.saldo = this.saldo.subtract(valor);
    }

    public boolean podeTransferir(BigDecimal valor) {
        return this.ativa && valor.compareTo(BigDecimal.ZERO) > 0 && 
               this.saldo.compareTo(valor) >= 0;
    }
}