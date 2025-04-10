package br.com.uaitagcartaoconvenio.cartaoconvenio.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

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
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Venda;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.ValidaVendaCataoPassaword;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.VendaDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.VendaService;

@RestController
public class VendaController {

	
	@Autowired
	private VendaService vendaService;

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/salvarVenda")
	public ResponseEntity<VendaDTO> salvarVenda( @RequestBody Venda venda ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( venda == null ) throw new ExceptionCustomizada("ERRO ao tentar cadastrar uma Venda. Valores vazios!");		
		
		venda = vendaService.salvarVendaService(venda);
		
		VendaDTO dto = VendaMapper.INSTANCE.toDto(venda);
		
		return new ResponseEntity<VendaDTO>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@PostMapping(value = "/validaVenda")
	public ResponseEntity<String> validaVenda( @RequestBody ValidaVendaCataoPassaword validaVendaCataoPassaword ) throws ExceptionCustomizada, UnsupportedEncodingException{

		if( validaVendaCataoPassaword == null ) throw new ExceptionCustomizada("ERRO ao tentar validar uma Venda. Valores vazios!");
		
		String retorno = vendaService.validaVenda(validaVendaCataoPassaword);
		
		return new ResponseEntity<String>(retorno, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getVendaByIdVendas/{idVenda}")
	public ResponseEntity<VendaDTO> getVendaByIdVendas( @PathVariable("idVenda") Long idVenda ) throws ExceptionCustomizada{

		Venda venda = vendaService.getVendaByIdVendas(idVenda);
		
		if(venda == null) {
			throw new ExceptionCustomizada("Não existe Entidades relacionada ao CNPJ: " + idVenda );
		}
		
		VendaDTO dto = VendaMapper.INSTANCE.toDto(venda);
		
		return new ResponseEntity<VendaDTO>(dto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByAnoMes/{anoMes}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByAnoMes( @PathVariable("anoMes") String anoMes ) throws ExceptionCustomizada{

		List<Venda> listVenda = vendaService.getListaVendaByAnoMes( anoMes );
		
		if(listVenda == null) {
			throw new ExceptionCustomizada("Não foi encontrado vendas para este período: " + anoMes);
		}
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda); 
		
		return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByDtVenda/{dtCriacaoIni}/{dtCriacaoFim}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByDtVenda( @PathVariable("dtCriacaoIni") String dtCriacaoIni ,
			                                                   @PathVariable("dtCriacaoFim") String dtCriacaoFim) throws ExceptionCustomizada{

		List<Venda> listVenda = vendaService.getListaVendaByDtVenda( dtCriacaoIni, dtCriacaoFim );
		
		if(listVenda == null) {
			throw new ExceptionCustomizada("Não existe Venda para o período entre: " + dtCriacaoIni + " e " + dtCriacaoFim);
		}
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda); 
		
		return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByLoginUser/{loginUser}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByLoginUser( @PathVariable("loginUser") String loginUser ) throws ExceptionCustomizada{

		List<Venda> listVenda = vendaService.getListaVendaByLoginUser( loginUser );
		
		if(listVenda == null) {
			throw new ExceptionCustomizada("Não foi encontrado vendas para este Login: " + loginUser);
		}
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda); 
		
		return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByDescStatusVendaReceb/{status}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByDescStatusVendaReceb(@PathVariable("status") String status ){
		
		StatusVendaReceb statusVendaReceb = StatusVendaReceb.valueOf(status);
		List<Venda> listVenda = vendaService.getListaVendaByDescStatusVendaReceb( statusVendaReceb );
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda); 
		
	    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByDescStatusVendaPg/{status}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByDescStatusVendaPg(@PathVariable("status") String status ){
		
		StatusVendaPg statusVendaPg = StatusVendaPg.valueOf(status);
		List<Venda> listVenda = vendaService.getListaVendaByDescStatusVendaPg( statusVendaPg );
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda); 
		
	    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByDescStatusVendas/{status}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByDescStatusVendas(@PathVariable("status") String status ){
		
		StatusVendas statusVenda = StatusVendas.valueOf(status);
		List<Venda> listVenda = vendaService.getListaVendaByDescStatusVendas( statusVenda );
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda); 
		
	    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByIdConveniados/{idConveniados}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByIdConveniados(  @PathVariable("idConveniados") Long   idConveniados ) throws ExceptionCustomizada{

		List<Venda> listVenda  = vendaService.getListaVendaByIdConveniados( idConveniados);
		
		if(listVenda == null) {
			throw new ExceptionCustomizada("Não foi encontrado vendas para o ID da Conveniada: " + idConveniados);
		}
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda); 
		
		return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByIdConveniadosAnoMes/{idConveniados}/{anoMes}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByIdConveniadosAnoMes(  @PathVariable("idConveniados") Long   idConveniados,
			                                                                @PathVariable("anoMes"       ) String anoMes 
			                                                               ) throws ExceptionCustomizada{

		List<Venda> listVenda  = vendaService.getListaVendaByIdConveniadosAnoMes(anoMes, idConveniados);
		
		if(listVenda == null) {
			throw new ExceptionCustomizada("Não foi encontrado vendas para o período '"+ anoMes + "' e ID da Conveniada: " + idConveniados);
		}
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda); 
		
		return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByIdConveniadosDtVenda/{dtCriacaoIni}/{dtCriacaoFim}/{idConveniados}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByIdConveniadosDtVenda( @PathVariable("dtCriacaoIni" ) String dtCriacaoIni,
			                                                                @PathVariable("dtCriacaoFim" ) String dtCriacaoFim,
			                                                                @PathVariable("idConveniados") Long   idConveniados) throws ExceptionCustomizada{

		List<Venda> listVenda = vendaService.getListaVendaByIdConveniadosDtVenda( dtCriacaoIni, dtCriacaoFim, idConveniados );
		
		if(listVenda == null) {
			throw new ExceptionCustomizada("Não existe Venda para o período entre: " + dtCriacaoIni + " e " + dtCriacaoFim + " do ID Conveniados: " + idConveniados );
		}
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda);
		
		return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByIdConveniadosStatusVendas/{status}/{idConveniados}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByIdConveniadosStatusVendas(@PathVariable("status") String status, @PathVariable("idConveniados") Long idConveniados )throws ExceptionCustomizada{
		
		StatusVendas statusVenda = StatusVendas.valueOf(status);
		List<Venda> listVenda = vendaService.getListaVendaByIdConveniadosDescStatusVendas( statusVenda, idConveniados );

		if(listVenda == null)  throw new ExceptionCustomizada("Não existe Venda para o status: "+ StatusVendas.valueOf(status).toString() + " do ID Conveniados: " + idConveniados );
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda);
		
	    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByIdConveniadosStatusVendaPg/{status}/{idConveniados}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByIdConveniadosStatusVendaPg(@PathVariable("status") String status, @PathVariable("idConveniados") Long idConveniados )throws ExceptionCustomizada{
		
		StatusVendaPg statusVendaPg = StatusVendaPg.valueOf(status);
		List<Venda> listVenda = vendaService.getListaVendaByIdConveniadosDescStatusVendaPg( statusVendaPg, idConveniados );
		
		if(listVenda == null)  throw new ExceptionCustomizada("Não existe Venda para o status: "+ StatusVendaPg.valueOf(status).toString() + " do ID Conveniados: " + idConveniados );

		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda);
		
	    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByIdConveniadosStatusVendaReceb/{status}/{idConveniados}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByIdConveniadosStatusVendaReceb(@PathVariable("status") String status, @PathVariable("idConveniados") Long idConveniados )throws ExceptionCustomizada{
		
		StatusVendaReceb statusVendaReceb = StatusVendaReceb.valueOf(status);
		List<Venda> listVenda = vendaService.getListaVendaByIdConveniadosDescStatusVendaReceb( statusVendaReceb, idConveniados );
		
		if(listVenda == null)  throw new ExceptionCustomizada("Não existe Venda para o status: "+ StatusVendaReceb.valueOf(status).toString() + " do ID Conveniados: " + idConveniados );
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda);
		
	    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByNomeConveniado/{nomeConveniado}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByNomeConveniado(@PathVariable("nomeConveniado") String nomeConveniado ) throws ExceptionCustomizada{
		
		List<Venda> listVenda = vendaService.getListaVendaByNomeConveniado( nomeConveniado );
		
		if(listVenda == null)  throw new ExceptionCustomizada("Não existe Venda para a Conveniado: "+ nomeConveniado );
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda);
		
	    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByCartao/{numCartao}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByCartao(@PathVariable("numCartao") String numCartao ) throws ExceptionCustomizada{
		
		List<Venda> listVenda = vendaService.getListaVendaByCartao( numCartao );
		if(listVenda == null)  throw new ExceptionCustomizada("Não existe Venda para o Número do Cartão: " + numCartao );
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda);
		
	    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@ResponseBody
	@GetMapping(value = "/getListaVendaByCartao/{status}/{numCartao}")
	public ResponseEntity<List<VendaDTO>> getListaVendaByCartao(@PathVariable("status") String status, @PathVariable("numCartao") String numCartao ) throws ExceptionCustomizada {
		
		StatusVendas statusVenda = StatusVendas.valueOf(status);
		List<Venda> listVenda = vendaService.getListaVendaByCartaoDescStatusVendas(statusVenda, numCartao );
		if(listVenda == null)  throw new ExceptionCustomizada("Não existe Venda para o Status da Venda '" + StatusVendas.valueOf(status).getDescStatusVendas() +"' e Número do Cartão: " + numCartao );
		
		List<VendaDTO> dto = VendaMapper.INSTANCE.toListDto(listVenda);
		
	    return new ResponseEntity<List<VendaDTO>>(dto, HttpStatus.OK);
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
