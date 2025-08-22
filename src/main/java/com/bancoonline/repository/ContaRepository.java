package com.bancoonline.repository;

import com.bancoonline.model.Conta;
import com.bancoonline.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    
 
    Optional<Conta> findByNumeroConta(String numeroConta);
    
   
    List<Conta> findByCliente(Cliente cliente);
    
   
    List<Conta> findByClienteAndAtivaTrue(Cliente cliente);
    
    
    Optional<Conta> findByNumeroContaAndAgencia(String numeroConta, String agencia);
    

    boolean existsByNumeroConta(String numeroConta);
    
 
    @Query("SELECT c FROM Conta c WHERE c.cliente.cpf = :cpf AND c.ativa = true")
    List<Conta> findContasAtivasByCpf(@Param("cpf") String cpf);
}