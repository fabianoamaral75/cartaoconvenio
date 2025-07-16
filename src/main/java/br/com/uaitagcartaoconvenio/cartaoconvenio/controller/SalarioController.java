package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.SalarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.SalarioUpdateDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.SalarioService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class SalarioController {

    @Autowired
    private SalarioService salarioService;

    @ResponseBody
    @GetMapping(value = "/getAllSalarios")
    public ResponseEntity<?> findAll(HttpServletRequest request) {
        try {
            List<SalarioDTO> salarios = salarioService.findAll();
            return ResponseEntity.ok(salarios);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @GetMapping(value = "/getSalariosById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id, HttpServletRequest request) {
        try {
            SalarioDTO salario = salarioService.findById(id);
            if (salario == null) {
                throw new ExceptionCustomizada("Salário não encontrado com ID: " + id);
            }
            return ResponseEntity.ok(salario);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @PostMapping(value = "/salvaSalarios")
    public ResponseEntity<?> create(@RequestBody SalarioDTO salarioDTO, HttpServletRequest request) {
        try {
            if (salarioDTO == null) {
                throw new ExceptionCustomizada("Dados do salário não podem ser nulos");
            }
            SalarioDTO novoSalario = salarioService.create(salarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoSalario);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @PutMapping(value = "/atualizarSalarios/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SalarioDTO salarioDTO, HttpServletRequest request) {
        try {
            if (salarioDTO == null) {
                throw new ExceptionCustomizada("Dados do salário não podem ser nulos");
            }
            SalarioDTO salarioAtualizado = salarioService.update(id, salarioDTO);
            if (salarioAtualizado == null) {
                throw new ExceptionCustomizada("Salário não encontrado com ID: " + id);
            }
            return ResponseEntity.ok(salarioAtualizado);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @DeleteMapping(value = "/deleteSalarios/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest request) {
        try {
            boolean deletado = salarioService.delete(id);
            if (!deletado) {
                throw new ExceptionCustomizada("Salário não encontrado com ID: " + id);
            }
            return ResponseEntity.noContent().build();
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }
    
    @ResponseBody
    @GetMapping("/findAllGroupByEntidadeAndSecretaria")
    public ResponseEntity<?> findAllGroupByEntidadeAndSecretaria(HttpServletRequest request) {
        try {
            List<SalarioDTO> salarios = salarioService.findAllGroupByEntidadeAndSecretaria();
            return ResponseEntity.ok(salarios);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @GetMapping("/findByEntidadeIdGroupByEntidadeAndSecretaria/{idEntidade}")
    public ResponseEntity<?> findByEntidadeIdGroupByEntidadeAndSecretaria(
            @PathVariable Long idEntidade, HttpServletRequest request) {
        try {
            List<SalarioDTO> salarios = salarioService.findByEntidadeIdGroupByEntidadeAndSecretaria(idEntidade);
            if (salarios == null || salarios.isEmpty()) {
                throw new ExceptionCustomizada("Nenhum salário encontrado para a entidade com ID: " + idEntidade);
            }
            return ResponseEntity.ok(salarios);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @GetMapping("/findByFuncionarioId/{idFuncionario}")
    public ResponseEntity<?> findByFuncionarioId(
            @PathVariable Long idFuncionario, HttpServletRequest request) {
        try {
            SalarioDTO salario = salarioService.findByFuncionarioId(idFuncionario);
            if (salario == null) {
                throw new ExceptionCustomizada("Salário não encontrado para o funcionário com ID: " + idFuncionario);
            }
            return ResponseEntity.ok(salario);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @GetMapping("/findAllByEntidadeId/{idEntidade}")
    public ResponseEntity<?> findAllByEntidadeId(
            @PathVariable Long idEntidade, HttpServletRequest request) {
        try {
            List<SalarioDTO> salarios = salarioService.findAllByEntidadeId(idEntidade);
            if (salarios == null || salarios.isEmpty()) {
                throw new ExceptionCustomizada("Nenhum salário encontrado para a entidade com ID: " + idEntidade);
            }
            return ResponseEntity.ok(salarios);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @PutMapping("/updateSalarioFuncionario/{idFuncionario}")
    public ResponseEntity<?> updateSalarioFuncionario(
            @PathVariable Long idFuncionario,
            @RequestBody SalarioUpdateDTO salarioUpdateDTO,
            HttpServletRequest request) {
        try {
            // Validação dos valores
            if (salarioUpdateDTO == null) {
                throw new ExceptionCustomizada("Dados de atualização não podem ser nulos");
            }
            
            if (salarioUpdateDTO.getValorBruto() != null && salarioUpdateDTO.getValorBruto().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ExceptionCustomizada("Valor bruto deve ser maior que zero");
            }
            
            if (salarioUpdateDTO.getValorLiquido() != null && salarioUpdateDTO.getValorLiquido().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ExceptionCustomizada("Valor líquido deve ser maior que zero");
            }
            
            SalarioDTO salarioAtualizado = salarioService.updateSalarioFuncionario(
                idFuncionario, 
                salarioUpdateDTO.getValorLiquido(), 
                salarioUpdateDTO.getValorBruto());
            
            if (salarioAtualizado == null) {
                throw new ExceptionCustomizada("Funcionário não encontrado com ID: " + idFuncionario);
            }
            
            return ResponseEntity.ok(salarioAtualizado);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    private ResponseEntity<ErrorResponse> handleException(ExceptionCustomizada ex, HttpServletRequest request) {
        long timestamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        String dataFormatada = sdf.format(new Date(timestamp));
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            request.getRequestURI(),
            dataFormatada
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}