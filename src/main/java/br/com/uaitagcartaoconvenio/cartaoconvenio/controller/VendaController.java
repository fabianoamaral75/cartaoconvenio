package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;
import java.nio.file.AccessDeniedException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaPg;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendaReceb;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusVendas;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.VendaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ErrorResponse;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Venda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ValidaVendaCataoPassaword;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.VendaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.VendaService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class VendaController {

	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private VendaMapper vendaMapper; // Injete o mapper

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/salvarVenda")
	public ResponseEntity<?> salvarVenda( @RequestBody Venda venda, HttpServletRequest request ) throws ExceptionCustomizada, UnsupportedEncodingException{
		try {
			
			if( venda == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar uma Venda. Valores vazios!");		
			
			venda = vendaService.salvarVendaService(venda);
			
			VendaDTO dto = vendaMapper.toDto(venda);
			
			return new ResponseEntity<VendaDTO>(dto, HttpStatus.OK);
			
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
	/**
	 * @throws AccessDeniedException ****************************************************************/	
	@ResponseBody
	@PostMapping(value = "/validaVenda")
	public ResponseEntity<?> validaVenda( @RequestBody ValidaVendaCataoPassaword validaVendaCataoPassaword, HttpServletRequest request ) throws ExceptionCustomizada, UnsupportedEncodingException{
		try {
			if( validaVendaCataoPassaword == null ) throw new ExceptionCustomizada("ERRO ao tentar validar uma Venda. Valores vazios!");
			
			String retorno = vendaService.validaVenda(validaVendaCataoPassaword);
			
			return new ResponseEntity<String>(retorno, HttpStatus.OK);
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
	@GetMapping(value = "/getVendaByIdVendas/{idVenda}")
	public ResponseEntity<?> getVendaByIdVendas( @PathVariable("idVenda") Long idVenda, HttpServletRequest request ) throws ExceptionCustomizada{
		try {
			
			Venda venda = vendaService.getVendaByIdVendas(idVenda);
			
			if(venda == null) {
				throw new ExceptionCustomizada("Não existe Entidades relacionada ao CNPJ: " + idVenda );
			}
			
			VendaDTO dto = vendaMapper.toDto(venda);
			
			return new ResponseEntity<VendaDTO>(dto, HttpStatus.OK);
		
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
	@GetMapping(value = "/getListaVendaByAnoMes/{anoMes}")
	public ResponseEntity<?> getListaVendaByAnoMes( @PathVariable("anoMes") String anoMes, HttpServletRequest request ) throws ExceptionCustomizada{
		
		try {
			
			List<Venda> listVenda = vendaService.getListaVendaByAnoMes( anoMes );
			
			if(listVenda == null) {
				throw new ExceptionCustomizada("Não foi encontrado vendas para este período: " + anoMes);
			}
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda); 
			
			return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
			
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
	@GetMapping(value = "/getListaVendaByDtVenda/{dtCriacaoIni}/{dtCriacaoFim}")
	public ResponseEntity<?> getListaVendaByDtVenda( @PathVariable("dtCriacaoIni") String dtCriacaoIni ,
			                                                      @PathVariable("dtCriacaoFim") String dtCriacaoFim , 
			                                                      HttpServletRequest request) throws ExceptionCustomizada{
		try {
			
			List<Venda> listVenda = vendaService.getListaVendaByDtVenda( dtCriacaoIni, dtCriacaoFim );
			
			if(listVenda == null) {
				throw new ExceptionCustomizada("Não existe Venda para o período entre: " + dtCriacaoIni + " e " + dtCriacaoFim);
			}
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda); 
			
			return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
			
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
	@GetMapping(value = "/getListaVendaByLoginUser/{loginUser}")
	public ResponseEntity<?> getListaVendaByLoginUser( @PathVariable("loginUser") String loginUser, HttpServletRequest request ) throws ExceptionCustomizada{
		
		try {
			
			List<Venda> listVenda = vendaService.getListaVendaByLoginUser( loginUser );
			
			if(listVenda == null) {
				throw new ExceptionCustomizada("Não foi encontrado vendas para este Login: " + loginUser);
			}
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda); 
			
			return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
			
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
	@GetMapping(value = "/getListaVendaByDescStatusVendaReceb/{status}")
	public ResponseEntity<?> getListaVendaByDescStatusVendaReceb(@PathVariable("status") String status, HttpServletRequest request ){
		
		try {
			
			StatusVendaReceb statusVendaReceb = StatusVendaReceb.valueOf(status);
			List<Venda> listVenda = vendaService.getListaVendaByDescStatusVendaReceb( statusVendaReceb );
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda); 
			
			if(dto == null) {
				throw new ExceptionCustomizada("Não existe Venda com este Status: " + statusVendaReceb );
			}
			
		    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);		    
		    
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
	@GetMapping(value = "/getListaVendaByDescStatusVendaPg/{status}")
	public ResponseEntity<?> getListaVendaByDescStatusVendaPg(@PathVariable("status") String status, HttpServletRequest request ){
		
		try {
			StatusVendaPg statusVendaPg = StatusVendaPg.valueOf(status);
			List<Venda> listVenda = vendaService.getListaVendaByDescStatusVendaPg( statusVendaPg );
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda); 
			
			if(dto == null) {
				throw new ExceptionCustomizada("Não existe Venda com este Status: " + statusVendaPg );
			}

			
		    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
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
	@GetMapping(value = "/getListaVendaByDescStatusVendas/{status}")
	public ResponseEntity<?> getListaVendaByDescStatusVendas(@PathVariable("status") String status, HttpServletRequest request ){
		
		try {
			StatusVendas statusVenda = StatusVendas.valueOf(status);
			List<Venda> listVenda = vendaService.getListaVendaByDescStatusVendas( statusVenda );
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda); 
			
			if(dto == null) {
				throw new ExceptionCustomizada("Não existe Venda com este Status: " + statusVenda );
			}
			
		    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	    
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
	@GetMapping(value = "/getListaVendaByIdConveniados/{idConveniados}")
	public ResponseEntity<?> getListaVendaByIdConveniados(  @PathVariable("idConveniados") Long idConveniados, HttpServletRequest request ) throws ExceptionCustomizada{

		try {
			
			List<Venda> listVenda  = vendaService.getListaVendaByIdConveniados( idConveniados);
			
			if(listVenda == null) {
				throw new ExceptionCustomizada("Não foi encontrado vendas para o ID da Conveniada: " + idConveniados);
			}
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda); 
			
			return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
		
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
	@GetMapping(value = "/getListaVendaByIdConveniadosAnoMes/{idConveniados}/{anoMes}")
	public ResponseEntity<?> getListaVendaByIdConveniadosAnoMes(  @PathVariable("idConveniados") Long   idConveniados,
			                                                      @PathVariable("anoMes"       ) String anoMes       , 
			                                                      HttpServletRequest request 
			                                                               ) throws ExceptionCustomizada{
		
		try {
			
		List<Venda> listVenda  = vendaService.getListaVendaByIdConveniadosAnoMes(anoMes, idConveniados);
		
		if(listVenda == null) {
			throw new ExceptionCustomizada("Não foi encontrado vendas para o período '"+ anoMes + "' e ID da Conveniada: " + idConveniados);
		}
		
		List<VendaDTO> dto = vendaMapper.toListDto(listVenda); 
		
		return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);	
		
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
	@GetMapping(value = "/getListaVendaByIdConveniadosDtVenda/{dtCriacaoIni}/{dtCriacaoFim}/{idConveniados}")
	public ResponseEntity<?> getListaVendaByIdConveniadosDtVenda( @PathVariable("dtCriacaoIni" ) String dtCriacaoIni,
			                                                      @PathVariable("dtCriacaoFim" ) String dtCriacaoFim   ,
			                                                      @PathVariable("idConveniados") Long   idConveniados  , 
			                                                      HttpServletRequest request) throws ExceptionCustomizada{
		try {
			
			List<Venda> listVenda = vendaService.getListaVendaByIdConveniadosDtVenda( dtCriacaoIni, dtCriacaoFim, idConveniados );
			
			if(listVenda == null) {
				throw new ExceptionCustomizada("Não existe Venda para o período entre: " + dtCriacaoIni + " e " + dtCriacaoFim + " do ID Conveniados: " + idConveniados );
			}
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda);
			
			return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
			
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
	@GetMapping(value = "/getListaVendaByIdConveniadosStatusVendas/{status}/{idConveniados}")
	public ResponseEntity<?> getListaVendaByIdConveniadosStatusVendas(@PathVariable("status") String status, @PathVariable("idConveniados") Long idConveniados, HttpServletRequest request )throws ExceptionCustomizada{
		
		try {
			
			StatusVendas statusVenda = StatusVendas.valueOf(status);
			List<Venda> listVenda = vendaService.getListaVendaByIdConveniadosDescStatusVendas( statusVenda, idConveniados );
	
			if(listVenda == null)  throw new ExceptionCustomizada("Não existe Venda para o status: "+ StatusVendas.valueOf(status).toString() + " do ID Conveniados: " + idConveniados );
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda);
			
		    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	    
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
	@GetMapping(value = "/getListaVendaByIdConveniadosStatusVendaPg/{status}/{idConveniados}")
	public ResponseEntity<?> getListaVendaByIdConveniadosStatusVendaPg(@PathVariable("status") String status, @PathVariable("idConveniados") Long idConveniados, HttpServletRequest request )throws ExceptionCustomizada{
		
		try {
			StatusVendaPg statusVendaPg = StatusVendaPg.valueOf(status);
			List<Venda> listVenda = vendaService.getListaVendaByIdConveniadosDescStatusVendaPg( statusVendaPg, idConveniados );
			
			if(listVenda == null)  throw new ExceptionCustomizada("Não existe Venda para o status: "+ StatusVendaPg.valueOf(status).toString() + " do ID Conveniados: " + idConveniados );
	
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda);
			
		    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
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
	@GetMapping(value = "/getListaVendaByIdConveniadosStatusVendaReceb/{status}/{idConveniados}")
	public ResponseEntity<?> getListaVendaByIdConveniadosStatusVendaReceb(@PathVariable("status") String status, @PathVariable("idConveniados") Long idConveniados, HttpServletRequest request )throws ExceptionCustomizada{
		
		try {
			
			StatusVendaReceb statusVendaReceb = StatusVendaReceb.valueOf(status);
			List<Venda> listVenda = vendaService.getListaVendaByIdConveniadosDescStatusVendaReceb( statusVendaReceb, idConveniados );
			
			if(listVenda == null)  throw new ExceptionCustomizada("Não existe Venda para o status: "+ StatusVendaReceb.valueOf(status).toString() + " do ID Conveniados: " + idConveniados );
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda);
			
		    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	    
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
	@GetMapping(value = "/getListaVendaByNomeConveniado/{nomeConveniado}")
	public ResponseEntity<?> getListaVendaByNomeConveniado(@PathVariable("nomeConveniado") String nomeConveniado, HttpServletRequest request ) throws ExceptionCustomizada{
		
		try {
			
			List<Venda> listVenda = vendaService.getListaVendaByNomeConveniado( nomeConveniado );
			
			if(listVenda == null)  throw new ExceptionCustomizada("Não existe Venda para a Conveniado: "+ nomeConveniado );
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda);
			
		    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
		    
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
	@GetMapping(value = "/getListaVendaByCartao/{numCartao}")
	public ResponseEntity<?> getListaVendaByCartao(@PathVariable("numCartao") String numCartao, HttpServletRequest request ) throws ExceptionCustomizada{
		
		try {
			
			List<Venda> listVenda = vendaService.getListaVendaByCartao( numCartao );
			if(listVenda == null)  throw new ExceptionCustomizada("Não existe Venda para o Número do Cartão: " + numCartao );
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda);
			
		    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	    
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
	@GetMapping(value = "/getListaVendaByCartao/{status}/{numCartao}")
	public ResponseEntity<?> getListaVendaByCartao(@PathVariable("status") String status, @PathVariable("numCartao") String numCartao, HttpServletRequest request ) throws ExceptionCustomizada {
		
		try {
			
			StatusVendas statusVenda = StatusVendas.valueOf(status);
			List<Venda> listVenda = vendaService.getListaVendaByCartaoDescStatusVendas(statusVenda, numCartao );
			if(listVenda == null)  throw new ExceptionCustomizada("Não existe Venda para o Status da Venda '" + StatusVendas.valueOf(status).getDescStatusVendas() +"' e Número do Cartão: " + numCartao );
			
			List<VendaDTO> dto = vendaMapper.toListDto(listVenda);
			
		    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	    
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
	@PostMapping(value = "/updateStatusVendas/{status}/{idConveniados}")
	public ResponseEntity<String> updateStatusVendas(@PathVariable("status")        String status, 
			                                         @PathVariable("idConveniados") Long   idConveniados){
		
		StatusVendas statusVendas = StatusVendas.valueOf(status);
		vendaService.updateStatusVendas(idConveniados, statusVendas);	
		
	    return new ResponseEntity<String>("sucesso", HttpStatus.OK);
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/updateStatusVendaPg/{status}/{idConveniados}")
	public ResponseEntity<String> updateStatusVendaPg(@PathVariable("status")        String status, 
			                                          @PathVariable("idConveniados") Long   idConveniados){
		
		StatusVendaPg statusVendaPg = StatusVendaPg.valueOf(status);
		vendaService.updateStatusVendaPg(idConveniados, statusVendaPg);	
		
	    return new ResponseEntity<String>("sucesso", HttpStatus.OK);
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/updateStatusVendaReceb/{status}/{idConveniados}")
	public ResponseEntity<String> updateStatusVendaReceb(@PathVariable("status")        String status, 
			                                             @PathVariable("idConveniados") Long   idConveniados){
		
		StatusVendaReceb statusVendaReceb = StatusVendaReceb.valueOf(status);
		vendaService.updateStatusVendaReceb(idConveniados, statusVendaReceb);	
		
	    return new ResponseEntity<String>("sucesso", HttpStatus.OK);
	}
	
}
