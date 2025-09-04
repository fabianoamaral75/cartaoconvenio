package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.LimiteCredito;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.RestabelecerLimitCreditoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.FuncionarioRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.LimitecreditoRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Serviço responsável por gerenciar operações relacionadas a limites de crédito
 * Inclui atualização, restabelecimento e controle do limite de crédito dos funcionários
 */
@Service
public class LimitecreditoService {

    // Repositório para operações com limites de crédito
    @Autowired
    private LimitecreditoRepository limitecreditoRepository;
    
    // Repositório para operações com funcionários
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    
    /******************************************************************/
    /*                  MÉTODOS DE ATUALIZAÇÃO                        */
    /******************************************************************/
    
    /**
     * Atualiza o valor utilizado do limite de crédito de uma venda
     * @param idVenda ID da venda
     * @param valorUtilizado Valor a ser atualizado como utilizado
     */
    public void updateLCredValorUtilizado(Long idVenda, BigDecimal valorUtilizado) {
        // Chama o repositório para atualizar o valor utilizado do limite de crédito
        limitecreditoRepository.updateLimiteCreditoValorUtilizado(idVenda, valorUtilizado);
    }
    
    /**
     * Restabelece o limite de crédito de um funcionário
     * @param idFuncionario ID do funcionário
     * @param valor Valor a ser restabelecido
     * @return Número de registros atualizados
     */
    public int updateRestabelecerLimiteCredito(Long idFuncionario, BigDecimal valor) {
        // Verifica se o valor é positivo
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
        
        // Chama o repositório para restabelecer o limite de crédito
        return limitecreditoRepository.updateRestabelecerLimiteCredito(idFuncionario, valor);
    }
    
    /**
     * Obtém a lista de limites de crédito a serem restabelecidos para um determinado mês/ano
     * @param anoMes Ano e mês no formato YYYY-MM
     * @return Lista de DTOs com informações para restabelecer limite de crédito
     */
    public List<RestabelecerLimitCreditoDTO> listaRestabelecerLimiteCredito(String anoMes) {
        // Obtém dados brutos do repositório e converte para DTO
        return limitecreditoRepository.listaRawRestabelecerLimiteCredito(anoMes).stream()
            .map(obj -> new RestabelecerLimitCreditoDTO(
                ((Number) obj[0]).longValue(),    // ID do funcionário
                ((Number) obj[1]).longValue(),    // ID do limite de crédito
                new BigDecimal(obj[2].toString()) // Valor a restabelecer
            ))
            .collect(Collectors.toList());
    }

    /**
     * Realiza rollback (desfaz) o restabelecimento do limite de crédito
     * @param idFuncionario ID do funcionário
     * @param valor Valor a ser revertido
     * @return Número de registros atualizados
     */
    public int updateRollbackRestabelecerLimiteCredito(Long idFuncionario, BigDecimal valor) {
        // Chama o repositório para fazer rollback do restabelecimento
        return limitecreditoRepository.updateRollbackRestabelecerLimiteCredito(idFuncionario, valor);
    }

    /******************************************************************/
    /*                  MÉTODOS DE ATUALIZAÇÃO ESPECÍFICOS            */
    /******************************************************************/
    
    /**
     * Atualiza o valor do limite de crédito
     * @param idLimiteCredito ID do limite de crédito
     * @param novoLimite Novo valor do limite
     */
    public void atualizarLimite(Long idLimiteCredito, BigDecimal novoLimite) {
        // Verifica se o novo limite não é negativo
        if (novoLimite.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O limite não pode ser negativo");
        }
        
        // Atualiza o limite no repositório
        int updated = limitecreditoRepository.updateLimite(idLimiteCredito, novoLimite);
        
        // Verifica se o registro foi encontrado e atualizado
        if (updated == 0) {
            throw new EntityNotFoundException("Limite de crédito não encontrado com ID: " + idLimiteCredito);
        }
    }

    /**
     * Atualiza o valor utilizado do limite de crédito
     * @param idLimiteCredito ID do limite de crédito
     * @param novoValorUtilizado Novo valor utilizado
     */
    public void atualizarValorUtilizado(Long idLimiteCredito, BigDecimal novoValorUtilizado) {
        // Verifica se o novo valor utilizado não é negativo
        if (novoValorUtilizado.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor utilizado não pode ser negativo");
        }
        
        // Atualiza o valor utilizado no repositório
        int updated = limitecreditoRepository.updateValorUtilizado(idLimiteCredito, novoValorUtilizado);
        
        // Verifica se o registro foi encontrado e atualizado
        if (updated == 0) {
            throw new EntityNotFoundException("Limite de crédito não encontrado com ID: " + idLimiteCredito);
        }
    }
    
    /******************************************************************/
    /*            MÉTODOS TRANSACIONAIS DE LIMITE DE CRÉDITO          */
    /******************************************************************/
    
    /**
     * Aumenta o limite de crédito utilizado de um funcionário
     * @param idFuncionario ID do funcionário
     * @param valor Valor a ser adicionado ao limite utilizado
     */
    @Transactional
    public void aumentarLimiteCredito(Long idFuncionario, BigDecimal valor) {
        // Busca o funcionário pelo ID
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new ExceptionCustomizada("Funcionário não encontrado"));
        
        // Verifica se o funcionário possui limite de crédito configurado
        if (funcionario.getLimiteCredito() == null) {
            throw new ExceptionCustomizada("Funcionário não possui limite de crédito configurado");
        }
        
        // Calcula o novo valor utilizado (atual - valor a ser adicionado)
        BigDecimal novoLimite = funcionario.getLimiteCredito().getValorUtilizado().subtract(valor);

        // Verifica se o novo valor não ficaria negativo
        if (novoLimite.compareTo(BigDecimal.ZERO) < 0) {
            throw new ExceptionCustomizada("Limite de crédito não pode ser negativo");
        }
        
        // Atualiza o valor utilizado do limite de crédito
        funcionario.getLimiteCredito().setValorUtilizado(novoLimite);
        
        // Salva as alterações no funcionário
        funcionarioRepository.save(funcionario);
    }
    
    /**
     * Reduz o limite de crédito utilizado de um funcionário
     * @param idFuncionario ID do funcionário
     * @param valor Valor a ser subtraído do limite utilizado
     */
    @Transactional
    public void reduzirLimiteCredito(Long idFuncionario, BigDecimal valor) {
        // Busca o funcionário pelo ID
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new ExceptionCustomizada("Funcionário não encontrado"));
        
        // Verifica se o funcionário possui limite de crédito configurado
        if (funcionario.getLimiteCredito() == null) {
            throw new ExceptionCustomizada("Funcionário não possui limite de crédito configurado");
        }
        
        // Calcula o novo valor utilizado (atual + valor a ser subtraído)
        BigDecimal novoLimite = funcionario.getLimiteCredito().getValorUtilizado().add(valor);
        
        
        // Atualiza o valor utilizado do limite de crédito
        funcionario.getLimiteCredito().setValorUtilizado(novoLimite);
        
        // Salva as alterações no funcionário
        funcionarioRepository.save(funcionario);
    }

    /**
     * Aumenta o limite de crédito utilizado com base em uma lista de valores de prestações
     * @param idFuncionario ID do funcionário
     * @param valoresPrestacoes Lista de valores das prestações a serem somados
     */
    @Transactional
    public void aumentarLimiteCreditoPorPrestacoes(Long idFuncionario, List<BigDecimal> valoresPrestacoes) {
        // Busca o funcionário pelo ID
        Funcionario funcionario = funcionarioRepository.findById(idFuncionario)
                .orElseThrow(() -> new ExceptionCustomizada("Funcionário não encontrado"));
        
        // Verifica se o funcionário possui limite de crédito configurado
        if (funcionario.getLimiteCredito() == null) {
            throw new ExceptionCustomizada("Funcionário não possui limite de crédito configurado");
        }
        
        // Soma todos os valores das prestações
        BigDecimal valorTotal = valoresPrestacoes.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Calcula o novo valor utilizado (atual + valor total das prestações)
        BigDecimal novoLimite = funcionario.getLimiteCredito().getValorUtilizado().subtract(valorTotal);
        
        // Atualiza o valor utilizado do limite de crédito
        funcionario.getLimiteCredito().setValorUtilizado(novoLimite);
        
        // Salva as alterações no funcionário
        funcionarioRepository.save(funcionario);
    }
    
    public LimiteCredito buscarPorFuncionario(Long idFuncionario) {
        // Usando Optional para tratamento seguro
        return limitecreditoRepository.findByFuncionarioId(idFuncionario)
                .orElseThrow(() -> new EntityNotFoundException("Limite de crédito não encontrado para o funcionário: " + idFuncionario));
    }

}
