package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveiniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;

@Service
public class ConveniadosService {
	@Autowired
	private ConveniadosRepository conveniadosRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private TaxaConveiniadosService taxaConveiniadosService;
	
	public Conveniados salvarEntidadeService( Conveniados conveniados ) throws ExceptionCustomizada {
		
		conveniados.getNicho().setConveniados(conveniados);
		conveniados.getRamoAtividade().setConveniados(conveniados);
		conveniados.setCicloPagamentoVenda(null);
		
		conveniados.getTaxaConveiniados().get(0).setDescStatusTaxaCon(StatusTaxaConv.ATUAL);
		conveniados.getTaxaConveiniados().get(0).setConveniados(conveniados);
		conveniados.setDescStatusConveniada(StatusConveniada.AGUARDANDO_CONFIRMACAO);
		conveniados.setPessoa(null);
				
		conveniados = conveniadosRepository.saveAndFlush( conveniados );
		
		return conveniados;
		
	}
	
	public Pessoa salvarEntidadeService( Pessoa pessoa ) throws ExceptionCustomizada {
		
		
		pessoa.getConveniados().getNicho().setConveniados(pessoa.getConveniados());
		pessoa.getConveniados().getRamoAtividade().setConveniados(pessoa.getConveniados());
		pessoa.getConveniados().setCicloPagamentoVenda(null);
		
		if( pessoa.getConveniados().getIdConveniados() != null ) {
			TaxaConveiniados taxaConveiniados = conveniadosRepository.findTxConvByIdconv(pessoa.getConveniados().getIdConveniados());
			if( taxaConveiniados != null ) conveniadosRepository.updateStatusTaxaConveiniados(taxaConveiniados.getIdTaxaConveiniados());
			
			Conveniados con = conveniadosRepository.findUserByIdconv( pessoa.getConveniados().getIdConveniados() );
			
			pessoa.setUsuario( con.getPessoa().getUsuario() );
		}
		
		TaxaConveiniados taxaConveiniados = new TaxaConveiniados();
		
		taxaConveiniados.setDescStatusTaxaCon(StatusTaxaConv.ATUAL);
		taxaConveiniados.setTaxa(pessoa.getConveniados().getTaxaConveiniados().get(0).getTaxa());
		taxaConveiniados.setConveniados(pessoa.getConveniados());
		
		taxaConveiniados = taxaConveiniadosService.salvarTaxaConveiniados(taxaConveiniados);
		pessoa.getConveniados().getTaxaConveiniados().add(taxaConveiniados);

//		pessoa.getConveniados().getTaxaConveiniados().get(0).setDescStatusTaxaCon(StatusTaxaConv.ATUAL);
//		pessoa.getConveniados().getTaxaConveiniados().get(0).setConveniados(pessoa.getConveniados());
		pessoa.getConveniados().setDescStatusConveniada(StatusConveniada.AGUARDANDO_CONFIRMACAO);
		pessoa.getConveniados().setPessoa(pessoa.getConveniados().getPessoa());
		
		pessoa.setPessoaFisica(null);
		pessoa.setFuncionario(null);
		
				
		pessoa = pessoaService.savarPassoa(pessoa);
		
		return pessoa;
		
	}


}
