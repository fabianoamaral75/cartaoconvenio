package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaCalcLimiteCredFuncionaro;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.TaxaCalcLimiteCreditoFuncMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaCalcLimiteCreditoFunc;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaCalcLimiteCreditoFuncDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.TaxaUpdateDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.TxCalcLimitCredFuncService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TxCalcLimitCredFuncController {

	@Autowired
	private TxCalcLimitCredFuncService txCalcLimitCredFuncService;
	
    @Autowired
    private TaxaCalcLimiteCreditoFuncMapper taxaCalcLimiteCreditoFuncMapper;
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaTaxaCalcLimiteCreditoFuncByDescStatusPagamento/{status}")
	public ResponseEntity<?> getListaTaxaCalcLimiteCreditoFuncByDescStatusPagamento( @PathVariable("status") String status, HttpServletRequest request) throws ExceptionCustomizada{

		try {
			
			StatusTaxaCalcLimiteCredFuncionaro statusTaxaCalcLimite = StatusTaxaCalcLimiteCredFuncionaro.valueOf(status);
			
			List<TaxaCalcLimiteCreditoFunc> listTaxaCalcLimiteCreditoFunc = txCalcLimitCredFuncService.getListaTaxaCalcLimiteCreditoFuncByDescStatusPagamento( statusTaxaCalcLimite );
			
			if(listTaxaCalcLimiteCreditoFunc == null) {
				throw new ExceptionCustomizada("Não existe Taxa para o Status: " + StatusTaxaCalcLimiteCredFuncionaro.valueOf(status).getDescTaxaCalcLimiteCredFuncionaro() );
			}
			
			List<TaxaCalcLimiteCreditoFuncDTO> dto = TaxaCalcLimiteCreditoFuncMapper.INSTANCE.toListDto(listTaxaCalcLimiteCreditoFunc);
			
			return new ResponseEntity<List<TaxaCalcLimiteCreditoFuncDTO>>(dto, HttpStatus.OK);
			
	    } catch (ExceptionCustomizada ex) {
	    	
	    	long timestamp = System.currentTimeMillis();

	    	// Criar formato desejado
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Fuso horário opcional

	    	// Converter
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
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getCicloPagamentoVendaByNomeConveniado/{nomeFuncionario}")
	public ResponseEntity<?> getCicloPagamentoVendaByNomeConveniado( @PathVariable("nomeFuncionario") String nomeFuncionario, HttpServletRequest request) throws ExceptionCustomizada{

		try {
			
			List<TaxaCalcLimiteCreditoFunc> listTaxaCalcLimiteCreditoFunc = txCalcLimitCredFuncService.getlistaTaxaCalcLimiteCreditoFuncByNomeFuncionario( nomeFuncionario );
			
			if(listTaxaCalcLimiteCreditoFunc == null) {
				throw new ExceptionCustomizada("Não existe Taxa para o Funcionarário: " + nomeFuncionario );
			}
			
			List<TaxaCalcLimiteCreditoFuncDTO> dto = TaxaCalcLimiteCreditoFuncMapper.INSTANCE.toListDto(listTaxaCalcLimiteCreditoFunc);
			
			return new ResponseEntity<List<TaxaCalcLimiteCreditoFuncDTO>>(dto, HttpStatus.OK);	
		
	    } catch (ExceptionCustomizada ex) {
	    	
	    	long timestamp = System.currentTimeMillis();
	
	    	// Criar formato desejado
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    	sdf.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo")); // Fuso horário opcional
	
	    	// Converter
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

    @ResponseBody
    @GetMapping("/findAllTxCalcLimCredFuncByEntidadeId/{idEntidade}")
    public ResponseEntity<?> findAllByEntidadeId(
            @PathVariable Long idEntidade,
            HttpServletRequest request) {
        try {
            List<TaxaCalcLimiteCreditoFunc> taxas = txCalcLimitCredFuncService.findAllByEntidadeId(idEntidade);
            List<TaxaCalcLimiteCreditoFuncDTO> dtos = taxaCalcLimiteCreditoFuncMapper.toListDto(taxas);
            return ResponseEntity.ok(dtos);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @GetMapping("/findAtualTxCalcLimCredFuncByEntidadeId/{idEntidade}/atual")
    public ResponseEntity<?> findAtualByEntidadeId(
            @PathVariable Long idEntidade,
            HttpServletRequest request) {
        try {
            TaxaCalcLimiteCreditoFunc taxa = txCalcLimitCredFuncService.findAtualByEntidadeId(idEntidade);
            TaxaCalcLimiteCreditoFuncDTO dto = taxaCalcLimiteCreditoFuncMapper.toDto(taxa);
            return ResponseEntity.ok(dto);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @GetMapping("/findTxCalcLimCredFuncByEntidadeIdAndStatus/{idEntidade}/status/{status}")
    public ResponseEntity<?> findByEntidadeIdAndStatus(
            @PathVariable Long idEntidade,
            @PathVariable String status,
            HttpServletRequest request) {
        try {
            StatusTaxaCalcLimiteCredFuncionaro statusTaxa = StatusTaxaCalcLimiteCredFuncionaro.valueOf(status);
            List<TaxaCalcLimiteCreditoFunc> taxas = txCalcLimitCredFuncService.findByEntidadeIdAndStatus(idEntidade, statusTaxa);
            List<TaxaCalcLimiteCreditoFuncDTO> dtos = taxaCalcLimiteCreditoFuncMapper.toListDto(taxas);
            return ResponseEntity.ok(dtos);
        } catch (IllegalArgumentException e) {
            return handleException(new ExceptionCustomizada("Status inválido: " + status), request);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @PutMapping("/atualizarTxCalcLimCredFuncTaxaBase/{idEntidade}")
    public ResponseEntity<?> atualizarTaxaBase(
            @PathVariable Long idEntidade,
            @RequestBody TaxaUpdateDTO taxaUpdateDTO,
            HttpServletRequest request) {
        try {
            if (taxaUpdateDTO.getTaxa() == null || taxaUpdateDTO.getTaxa().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ExceptionCustomizada("Valor da taxa deve ser maior que zero");
            }

            TaxaCalcLimiteCreditoFunc taxaAtualizada = txCalcLimitCredFuncService.atualizarTaxaBase(
                idEntidade, 
                taxaUpdateDTO.getTaxa()
            );

            TaxaCalcLimiteCreditoFuncDTO dto = taxaCalcLimiteCreditoFuncMapper.toDto(taxaAtualizada);
            return ResponseEntity.ok(dto);
        } catch (ExceptionCustomizada ex) {
            return handleException(ex, request);
        }
    }

    @ResponseBody
    @PutMapping("/atualizarTxCalcLimCredFuncStatusTaxa/{idEntidade}/{novoStatus}")
    public ResponseEntity<?> atualizarStatusTaxa(
            @PathVariable Long idEntidade,
            @PathVariable String novoStatus,
            HttpServletRequest request) {
        try {
            StatusTaxaCalcLimiteCredFuncionaro status = StatusTaxaCalcLimiteCredFuncionaro.valueOf(novoStatus);
            TaxaCalcLimiteCreditoFunc taxaAtualizada = txCalcLimitCredFuncService.atualizarStatusTaxa(idEntidade, status);
            
            TaxaCalcLimiteCreditoFuncDTO dto = taxaCalcLimiteCreditoFuncMapper.toDto(taxaAtualizada);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return handleException(new ExceptionCustomizada("Status inválido: " + novoStatus), request);
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
