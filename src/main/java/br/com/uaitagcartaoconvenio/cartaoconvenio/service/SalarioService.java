package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.SalarioMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Salario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.SalarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.LimitecreditoRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.SalarioRepository;

@Service
public class SalarioService {

    @Autowired
    private SalarioRepository salarioRepository;

    @Autowired
    private SalarioMapper salarioMapper;

    
    @Autowired
    private LimitecreditoRepository limitecreditoRepository;
    
    @Autowired
    private TxCalcLimitCredFuncService txCalcLimitCredFuncService;

    @Transactional(readOnly = true)
    public List<SalarioDTO> findAll() {
        List<Salario> salarios = salarioRepository.findAll();
        return salarioMapper.toDtoList(salarios);
    }

    @Transactional(readOnly = true)
    public SalarioDTO findById(Long id) {
        Optional<Salario> salario = salarioRepository.findById(id);
        return salario.map(salarioMapper::toDto).orElse(null);
    }

    @Transactional
    public SalarioDTO create(SalarioDTO salarioDTO) {
        Salario salario = salarioMapper.toEntity(salarioDTO);
        salario = salarioRepository.save(salario);
        return salarioMapper.toDto(salario);
    }

    @Transactional
    public SalarioDTO update(Long id, SalarioDTO salarioDTO) {
        if (!salarioRepository.existsById(id)) {
            return null;
        }
        
        Salario salario = salarioMapper.toEntity(salarioDTO);
        salario.setIdSalario(id);
        salario = salarioRepository.save(salario);
        return salarioMapper.toDto(salario);
    }

    @Transactional
    public boolean delete(Long id) {
        if (!salarioRepository.existsById(id)) {
            return false;
        }
        
        salarioRepository.deleteById(id);
        return true;
    }
    
    @Transactional(readOnly = true)
    public List<SalarioDTO> findAllGroupByEntidadeAndSecretaria() {
        List<Salario> salarios = salarioRepository.findAllGroupByEntidadeAndSecretaria();
        return salarioMapper.toDtoList(salarios);
    }

    @Transactional(readOnly = true)
    public List<SalarioDTO> findByEntidadeIdGroupByEntidadeAndSecretaria(Long idEntidade) {
        List<Salario> salarios = salarioRepository.findByEntidadeIdGrouped(idEntidade);
        return salarioMapper.toDtoList(salarios);
    }

    @Transactional(readOnly = true)
    public SalarioDTO findByFuncionarioId(Long idFuncionario) {
        Salario salario = salarioRepository.findByFuncionarioIdFuncionario(idFuncionario);
        return salario != null ? salarioMapper.toDto(salario) : null;
    }

    @Transactional(readOnly = true)
    public List<SalarioDTO> findAllByEntidadeId(Long idEntidade) {
        List<Salario> salarios = salarioRepository.findAllByEntidadeIdEntidade(idEntidade);
        return salarioMapper.toDtoList(salarios);
    }

    @Transactional
    public SalarioDTO updateSalarioFuncionario(Long idFuncionario, BigDecimal valorLiquido, BigDecimal valorBruto) {
        // Busca o salário do funcionário
        Salario salario = salarioRepository.findByFuncionarioIdFuncionario(idFuncionario);
        if (salario == null) {
            return null;
        }
        
        // Flag para verificar se o valor bruto foi alterado
        boolean valorBrutoAlterado = false;
        
        if (valorLiquido != null) {
            salario.setValorLiquido(valorLiquido);
        }
        
        if (valorBruto != null && !valorBruto.equals(salario.getValorBruto())) {
            salario.setValorBruto(valorBruto);
            valorBrutoAlterado = true;
        }
        
        // Salva as alterações no salário
        salario = salarioRepository.save(salario);
        
        // Se o valor bruto foi alterado, recalcula o limite de crédito
        if (valorBrutoAlterado) {
            recalcularLimiteCredito(salario.getFuncionario());
        }
        
        return salarioMapper.toDto(salario);
    }

    private void recalcularLimiteCredito(Funcionario funcionario) {
        if (funcionario == null || funcionario.getLimiteCredito() == null || 
            funcionario.getEntidade() == null || funcionario.getSalario() == null) {
            return;
        }
        
        // Obtém o novo limite calculado baseado no salário bruto
        BigDecimal novoLimite = txCalcLimitCredFuncService.getCalculoLimiteCredito(
            funcionario.getEntidade().getIdEntidade(), 
            funcionario.getSalario().getValorBruto()
        );
        
        // Atualiza o limite de crédito do funcionário
        funcionario.getLimiteCredito().setLimite(novoLimite);
        
        // Salva a alteração no limite de crédito
        limitecreditoRepository.save(funcionario.getLimiteCredito());
    }
}