package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Acesso;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Cartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Usuario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UauarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.UsuarioRepository;


@Service
public class UsuarioService {


	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private AcessoService acessoService;
	
	@Autowired
	private ConveniadosService conveniadosService;
	
	@Autowired
    private TxCalcLimitCredFuncService txCalcLimitCredFuncService;
	
	@Autowired
	private CartaoService cartaoService;
	
    /* ******************************************************************************************************************************** */
	/*                                                                                                                                  */
	/*        Procedimento de validação para salvar um Usuario de Pessoa Fisica para a diministração e manutenção do sistema            */
	/*                                                                                                                                  */
    /* ******************************************************************************************************************************** */
	public UauarioDTO salvarUsuarioPF( Usuario userPF ) throws ExceptionCustomizada {
		
		
		if (userPF.getIdUsuario() == null && usuarioRepository.findByLogin(userPF.getLogin()) != null) {
			throw new ExceptionCustomizada("Já existe o Login cadastrado: " + userPF.getLogin() );
		}
	
		if (userPF.getIdUsuario() == null && usuarioRepository.pesquisaPorCpfPF(userPF.getPessoa().getPessoaFisica().getCpf()) != null) {
			throw new ExceptionCustomizada("Já existe CPF cadastrado com o número: " + userPF.getPessoa().getPessoaFisica().getCpf());
		}
		
		// Trata as informações de Role de Acesso do Usuário
		if( userPF.getUsuarioAcesso().size() == 0 ) throw new ExceptionCustomizada("ERRO não foi informado o 'O(s) Role de Acesso do Usuário'!");	
		for( int i = 0; i < userPF.getUsuarioAcesso().size(); i++ ) {
			userPF.getUsuarioAcesso().get(i).setUsuario(userPF);
		}
				
		if( userPF.getPessoa() == null ) throw new ExceptionCustomizada("ERRO não foi informado os dados 'Referente a Pessoa Pessoa Fisíca'!");

		userPF.getPessoa().setUsuario(userPF);
		userPF.getPessoa().setPessoaFisica(userPF.getPessoa().getPessoaFisica());
		userPF.getPessoa().getPessoaFisica().setPessoa(userPF.getPessoa());
		
		userPF.getPessoa().setFuncionario(null);
		userPF.getPessoa().setPessoaJuridica(null);
		
		userPF = usuarioRepository.saveAndFlush( userPF );
		
		UauarioDTO uauarioPFDTO = getUauarioPFDTO( userPF );
		
		return uauarioPFDTO;
				
	}

    /* ******************************************************************************************************************************** */
	/*                                                                                                                                  */
	/*                  Procedimento de validação para salvar um Usuario de Pessoa Juridica para uma Conveniada                         */
	/*                                                                                                                                  */
    /* ******************************************************************************************************************************** */
	public UauarioDTO salvarUsuarioPJConveniada( Usuario userPJConveniado ) throws ExceptionCustomizada {
		
		
		if (userPJConveniado.getIdUsuario() == null && usuarioRepository.findByLogin(userPJConveniado.getLogin()) != null) {
			throw new ExceptionCustomizada("Já existe o Login cadastrado: " + userPJConveniado.getLogin() );
		}
	
		if (userPJConveniado.getIdUsuario() == null && usuarioRepository.pesquisaPorCpfPF(userPJConveniado.getPessoa().getPessoaJuridica().getCnpj()) != null) {
			throw new ExceptionCustomizada("Já existe CPF cadastrado com o número: " + userPJConveniado.getPessoa().getPessoaJuridica().getCnpj());
		}
		
		// Trata as informações de Role de Acesso do Usuário
		if( userPJConveniado.getUsuarioAcesso().size() == 0 ) throw new ExceptionCustomizada("ERRO não foi informado o 'O(s) Role de Acesso do Usuário'!");	
		for( int i = 0; i < userPJConveniado.getUsuarioAcesso().size(); i++ ) {
			userPJConveniado.getUsuarioAcesso().get(i).setUsuario(userPJConveniado);
		}
				
		if( userPJConveniado.getPessoa() == null ) throw new ExceptionCustomizada("ERRO não foi informado os dados 'Referente a Pessoa Pessoa Jurídijo'!");

		userPJConveniado.getPessoa().setUsuario(userPJConveniado);
		userPJConveniado.getPessoa().setPessoaJuridica(userPJConveniado.getPessoa().getPessoaJuridica());
		userPJConveniado.getPessoa().getPessoaJuridica().setPessoa(userPJConveniado.getPessoa());

		Conveniados conveniados = conveniadosService.salvarEntidadeService(userPJConveniado.getPessoa().getConveniados());

		userPJConveniado.getPessoa().setConveniados( conveniados );
		userPJConveniado.getPessoa().setPessoaFisica(null);
		userPJConveniado.getPessoa().setFuncionario(null);
		userPJConveniado = usuarioRepository.saveAndFlush( userPJConveniado );
		
		UauarioDTO uauarioPFDTO = getUauarioPFDTO( userPJConveniado );
		
		return uauarioPFDTO;
				
	}

	
    /* ******************************************************************************************************************************** */
	/*                                                                                                                                  */
	/*                  Procedimento de validação para salvar um Usuario de Pessoa Juridica para uma Conveniada                         */
	/*                                                                                                                                  */
    /* ******************************************************************************************************************************** */
	public UauarioDTO salvarUsuarioFuncionario( Usuario userFuncionario ) throws ExceptionCustomizada {

		if (userFuncionario.getIdUsuario() == null && usuarioRepository.findByLogin(userFuncionario.getLogin()) != null) {
			throw new ExceptionCustomizada("Já existe o Login cadastrado: " + userFuncionario.getLogin() );
		}
	
		if (userFuncionario.getIdUsuario() == null && usuarioRepository.pesquisaPorCpfPF(userFuncionario.getPessoa().getPessoaFisica().getCpf()) != null) {
			throw new ExceptionCustomizada("Já existe CPF cadastrado com o número: " + userFuncionario.getPessoa().getPessoaFisica().getCpf());
		}
		
		// Trata as informações de Role de Acesso do Usuário
		if( userFuncionario.getUsuarioAcesso().size() == 0 ) throw new ExceptionCustomizada("ERRO não foi informado o 'O(s) Role de Acesso do Usuário'!");	
		for( int i = 0; i < userFuncionario.getUsuarioAcesso().size(); i++ ) {
			userFuncionario.getUsuarioAcesso().get(i).setUsuario(userFuncionario);
		}
				
		if( userFuncionario.getPessoa() == null ) throw new ExceptionCustomizada("ERRO não foi informado os dados 'Referente a Pessoa Pessoa Fisíca'!");

		userFuncionario.getPessoa().setUsuario(userFuncionario);
		
		// Valida as informaçoes referente aos dados da Pesoa Fisica
		userFuncionario.getPessoa().setPessoaFisica(userFuncionario.getPessoa().getPessoaFisica());
		userFuncionario.getPessoa().getPessoaFisica().setPessoa(userFuncionario.getPessoa());
		
        // Valida as informaçoes de Limite Creditodo Funcionario
		userFuncionario.getPessoa().getFuncionario().setPessoa(userFuncionario.getPessoa());
		userFuncionario.getPessoa().getFuncionario().getLimiteCredito().setFuncionario(userFuncionario.getPessoa().getFuncionario());
		userFuncionario.getPessoa().getFuncionario().getLimiteCredito().setLimite(txCalcLimitCredFuncService.getCalculoLimiteCredito( 
				                                                     userFuncionario.getPessoa().getFuncionario().getEntidade().getIdEntidade(), userFuncionario.getPessoa().getFuncionario().getSalario().getValorBruto() )  ); // O valor bruto deverá ser validado com a área usuária.
		
		// Associa o Salario passado no Jeson ao Funcionario em cadastro
		userFuncionario.getPessoa().getFuncionario().getSalario().setFuncionario(userFuncionario.getPessoa().getFuncionario());
		
		/* Criar um Cartão automaticamente para o Funcionário */		
		Cartao cartao = new Cartao();
		cartao.setFuncionario(userFuncionario.getPessoa().getFuncionario());
		// Gera uma numeração para o Novo Cartão.
		cartao.setNumeracao(cartaoService.getNovoCartao());
		cartao.setStatusCartao(StatusCartao.ATIVO);
		userFuncionario.getPessoa().getFuncionario().getCartao().add(cartao);
		
		/* Valista se o Funcionario esta associado a uma Secreária ou é um Funcionario lotado diretamente na Prefeitura */
		// Se o ID da secretária for igual a -1, é porque não esta associado a nenhua Secretária 		
		if( userFuncionario.getPessoa().getFuncionario().getSecretaria().getIdSecretaria() == -1 ) 
			userFuncionario.getPessoa().getFuncionario().setSecretaria(null);
		else userFuncionario.getPessoa().getFuncionario().getSecretaria().setFuncionario(userFuncionario.getPessoa().getFuncionario());
		
		// Associa o Salario passado no Jeson ao Funcionario em cadastro
		userFuncionario.getPessoa().getFuncionario().getEntidade().setFuncionario(userFuncionario.getPessoa().getFuncionario());
		
		userFuncionario.getPessoa().setPessoaJuridica(null);
		
		userFuncionario = usuarioRepository.saveAndFlush( userFuncionario );
		
		UauarioDTO uauarioPFDTO = getUauarioPFDTO( userFuncionario );
		
		return uauarioPFDTO;

	}

	public UauarioDTO pesquisaUsuarioPFByLongin(String login ) {
		
		Usuario userPF = usuarioRepository.findByLogin(login);
		
		UauarioDTO uauarioPFDTO = getUauarioPFDTO( userPF );
		return uauarioPFDTO;
	}

	private UauarioDTO getUauarioPFDTO( Usuario user ) {
	
        Date dtCriacao    = user.getDtCriacao();
        Date dtAtualSenha = user.getDataAtualSenha();
        // Define o formato desejado
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        // Formata a data como uma String
        String dtCriacaoFormatada    = formato.format( dtCriacao    );
        String dtAtualSenhaFormatada = formato.format( dtAtualSenha );
        // Exibe a data formatada
		
		UauarioDTO uauarioPFDTO = new UauarioDTO();
		
		uauarioPFDTO.setIdUsuario     ( user.getIdUsuario() );
		uauarioPFDTO.setLogin         ( user.getLogin()     );
		uauarioPFDTO.setDtCriacao     ( dtCriacaoFormatada    );		
		uauarioPFDTO.setDataAtualSenha( dtAtualSenhaFormatada );
		
		List<Acesso> acesso = acessoService.getListaAcesso();
		
		List<Acesso> acessosDTO = new ArrayList<Acesso>();
		
		for( int i = 0; i < user.getUsuarioAcesso().size(); i++ ) {
			for( int j = 0; j < acesso.size(); j++ ) {
			  if( user.getUsuarioAcesso().get(i).getAcesso().getIdAcesso() == acesso.get(j).getIdAcesso() ) {
				  acessosDTO.add(acesso.get(j));	
				  break;
			  }
			}
		}
		
		uauarioPFDTO.setAcesso(acessosDTO);
		
		uauarioPFDTO.getPessoa().setIdPessoa    ( user.getPessoa().getIdPessoa()   );
		uauarioPFDTO.getPessoa().setNomePessoa  ( user.getPessoa().getNomePessoa() );
		uauarioPFDTO.getPessoa().setLogradoro   ( user.getPessoa().getLogradoro()  );
		uauarioPFDTO.getPessoa().setUf          ( user.getPessoa().getUf()         );
		uauarioPFDTO.getPessoa().setCidade      ( user.getPessoa().getCidade()     );
		uauarioPFDTO.getPessoa().setCep         ( user.getPessoa().getCep()        );
		uauarioPFDTO.getPessoa().setNumero      ( user.getPessoa().getNumero()     );
		uauarioPFDTO.getPessoa().setNumero      ( user.getPessoa().getNumero()     );
		uauarioPFDTO.getPessoa().setComplemento ( user.getPessoa().getComplemento());
		uauarioPFDTO.getPessoa().setBairro      ( user.getPessoa().getBairro()     );
		uauarioPFDTO.getPessoa().setEmail       ( user.getPessoa().getEmail()      );
		uauarioPFDTO.getPessoa().setTelefone    ( user.getPessoa().getTelefone()   );

		uauarioPFDTO.getPessoa().setPessoaFisica(user.getPessoa().getPessoaFisica());
		uauarioPFDTO.getPessoa().setPessoaJuridica(user.getPessoa().getPessoaJuridica());
		uauarioPFDTO.getPessoa().setConveniados(user.getPessoa().getConveniados());
		uauarioPFDTO.getPessoa().setFuncionario(user.getPessoa().getFuncionario());
		
		return uauarioPFDTO;		
	}
	
	
	
}

