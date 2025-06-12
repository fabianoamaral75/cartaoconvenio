package br.com.uaitagcartaoconvenio.cartaoconvenio.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.uaitagcartaoconvenio.cartaoconvenio.ExceptionCustomizada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusCartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusTaxaConv;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.ConveniadosMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.FuncionarioMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.PessoaFisicaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.PessoaJuridicaMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.mapper.UsuarioMapper;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Acesso;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Cartao;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.ContratoConveniado;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Conveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Funcionario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.PeriodoCobrancaTaxa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Pessoa;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaConveniados;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.TaxaExtraConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Usuario;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.UsuarioAcesso;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.VigenciaContratoConveniada;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.CartaoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.EntidadeLogadoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.FuncionarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UauarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UsuarioAcessoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UsuarioDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto.UsuarioLogadoDTO;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.ConveniadosRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.FuncionarioRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.PessoaRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.repository.UsuarioRepository;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.FuncoesUteis;



@Service
@Primary
@Transactional
public class UsuarioService implements UserDetailsService{


	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private AcessoService acessoService;
/*	
	@Autowired
	private ConveniadosService conveniadosService;
*/	
	@Autowired
    private TxCalcLimitCredFuncService txCalcLimitCredFuncService;
	
	@Autowired
	private CartaoService cartaoService;
	
	@Autowired
	private ConveniadosRepository conveniadosRepository;
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
//	@Autowired
//	private ConveniadosService conveniadosService;
	
	private static final Logger logger = LogManager.getLogger(UsuarioService.class);
	
    /* ******************************************************************************************************************************** */
	/*                                                                                                                                  */
	/*        Procedimento de validação para salvar um Usuario de Pessoa Fisica para a diministração e manutenção do sistema            */
	/*                                                                                                                                  */
    /* ******************************************************************************************************************************** */
	public UsuarioDTO salvarUsuarioPF( Usuario userPF ) throws ExceptionCustomizada {
		
		
		if (userPF.getIdUsuario() != null && usuarioRepository.findByLogin(userPF.getLogin()) != null) {
			throw new ExceptionCustomizada("Já existe o Login cadastrado: " + userPF.getLogin() );
		}

		String cpf = FuncoesUteis.removerCaracteresNaoNumericos(userPF.getPessoa().getPessoaFisica().getCpf());
		userPF.getPessoa().getPessoaFisica().setCpf(cpf);

		if (userPF.getIdUsuario() == null && usuarioRepository.pesquisaPorCpfPF(userPF.getPessoa().getPessoaFisica().getCpf()) != null) {
			throw new ExceptionCustomizada("Já existe CPF cadastrado com o número: " + userPF.getPessoa().getPessoaFisica().getCpf());
		}
		
		// Trata as informações de Role de Acesso do Usuário
		if( userPF.getUsuarioAcesso().size() == 0 ) throw new ExceptionCustomizada("ERRO não foi informado o 'O(s) Role de Acesso do Usuário'!");	
		for( int i = 0; i < userPF.getUsuarioAcesso().size(); i++ ) {
			userPF.getUsuarioAcesso().get(i).setUsuario(userPF);
		}
				
		if( userPF.getPessoa() == null ) throw new ExceptionCustomizada("ERRO não foi informado os dados Referente a Pessoa Pessoa Fisíca'!");

		userPF.getPessoa().setUsuario(userPF);
		userPF.getPessoa().setPessoaFisica(userPF.getPessoa().getPessoaFisica());
		userPF.getPessoa().getPessoaFisica().setPessoa(userPF.getPessoa());
		
		userPF.getPessoa().setFuncionario(null);
		userPF.getPessoa().setPessoaJuridica(null);
		
		String encryptedPass = new BCryptPasswordEncoder().encode(userPF.getPassword());
		userPF.setSenha(encryptedPass);		
		
		userPF = usuarioRepository.saveAndFlush( userPF );
		
		UsuarioDTO dto = UsuarioMapper.INSTANCE.toDto(userPF);
		
//		UauarioDTO uauarioPFDTO = getUauarioPFDTO( userPF );
		
		return dto;
				
	}

    /* ******************************************************************************************************************************** */
	/*                                                                                                                                  */
	/*                  Procedimento de validação para salvar um Usuario de Pessoa Juridica para uma Conveniada                         */
	/*                                                                                                                                  */
    /* ******************************************************************************************************************************** */
	@Transactional
	public UsuarioDTO salvarUsuarioPJConveniada(Usuario userPJConveniado) throws ExceptionCustomizada {
	    try {
	        // 1. Validações iniciais
	        validarDadosUsuario(userPJConveniado);
	        
	        // 2. Configurar e salvar Usuario primeiro (sem Pessoa ainda)
	        configurarUsuarioBasico(userPJConveniado);
	        Usuario usuarioSalvo = usuarioRepository.save(userPJConveniado);
	        
	        // 3. Configurar Pessoa e PessoaJuridica
	        Pessoa pessoa = userPJConveniado.getPessoa();
//	        configurarPessoaEJuridica(pessoa, usuarioSalvo);
	        
	        // 4. Configurar Conveniados
	        Conveniados conveniados = pessoa.getConveniados();
	        if (conveniados != null) {
	            configurarConveniado(conveniados, pessoa);
	            conveniados = conveniadosRepository.save(conveniados);
	            // pessoa.setConveniados(conveniados);
	        }
	        
	        // 5. Salvar Pessoa (que agora tem Usuario persistido e Conveniados persistido)
	        Pessoa pessoaSalva = pessoaRepository.save(pessoa);
	        
	        // 6. Atualizar Usuario com Pessoa persistida
	        usuarioSalvo.setPessoa(pessoaSalva);
	        usuarioRepository.save(usuarioSalvo);
	        
	        return UsuarioMapper.INSTANCE.toDto(usuarioSalvo);
	    } catch (Exception e) {
	        logger.error("Erro ao salvar usuário PJ conveniada", e);
	        throw new ExceptionCustomizada("Erro ao salvar usuário: " + e.getMessage());
	    }
	}

	private void validarDadosUsuario(Usuario usuario) throws ExceptionCustomizada {
	    if (usuario == null || usuario.getPessoa() == null || usuario.getPessoa().getPessoaJuridica() == null) {
	        throw new ExceptionCustomizada("Dados obrigatórios não informados");
	    }
	    
	    // Validações de CNPJ, login, etc.
	    String cnpj = FuncoesUteis.removerCaracteresNaoNumericos(usuario.getPessoa().getPessoaJuridica().getCnpj());
	    usuario.getPessoa().getPessoaJuridica().setCnpj(cnpj);
	    
	    if (usuarioRepository.findByLogin(usuario.getLogin()).isPresent() ) {
	        throw new ExceptionCustomizada("Já existe um usuário com este login");
	    }
	    
	    if (usuarioRepository.pesquisaPorCpfPF(cnpj) != null) {
	        throw new ExceptionCustomizada("CNPJ já cadastrado");
	    }
	    
	    if (usuario.getUsuarioAcesso().isEmpty()) {
	        throw new ExceptionCustomizada("Pelo menos um role de acesso deve ser informado");
	    }
	}

	private void configurarUsuarioBasico(Usuario usuario) {
	    // Configurar senha criptografada
	    usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
	    
	    // Configurar UsuarioAcesso
	    usuario.getUsuarioAcesso().forEach(ua -> ua.setUsuario(usuario));
	    
	    usuario.getPessoa().setUsuario(usuario);
	    usuario.getPessoa().setPessoaJuridica(usuario.getPessoa().getPessoaJuridica());
	    usuario.getPessoa().getPessoaJuridica().setPessoa(usuario.getPessoa());

	    
        // Limpa relacionamentos não utilizados
	    usuario.getPessoa().setPessoaFisica(null);
	    usuario.getPessoa().setFuncionario(null);


	}
/*
	private void configurarPessoaEJuridica(Pessoa pessoa, Usuario usuario) {
	    // Configurar Usuario na Pessoa
//	    pessoa.setUsuario(usuario);
	    
	    // Configurar PessoaJuridica
	    PessoaJuridica pj = pessoa.getPessoaJuridica();
	    pj.setPessoa(pessoa);
	    
	    // Limpar relacionamentos não utilizados
	    pessoa.setPessoaFisica(null);
	    pessoa.setFuncionario(null);
	    
	    // Limpar relacionamento com Conveniados temporariamente
	//    Conveniados conveniados = pessoa.getConveniados();
	//    pessoa.setConveniados(null);
	    

	}
*/
	private void configurarConveniado(Conveniados conveniados, Pessoa pessoa) {
	    // Configurar pessoa no conveniado
//	    conveniados.setPessoa(pessoa);
	    
	    // Configurar status padrão
	    conveniados.setDescStatusConveniada(StatusConveniada.AGUARDANDO_CONFIRMACAO);
	    
	    // Configurar taxas conveniados
	    if (conveniados.getTaxaConveniados() == null || conveniados.getTaxaConveniados().isEmpty()) {
	        TaxaConveniados taxa = new TaxaConveniados();
	        taxa.setDescStatusTaxaCon(StatusTaxaConv.ATUAL);
	        taxa.setConveniados(conveniados);
	        conveniados.setTaxaConveniados(List.of(taxa));
	    } else {
	    	conveniados.getTaxaConveniados().forEach(t -> {
	    		t.setDescStatusTaxaCon(StatusTaxaConv.ATUAL);
	    	    t.setConveniados(conveniados);    	    
	    	});
	    }
	    
	    // Configurar taxas extras
	    if (conveniados.getTaxaExtraConveniada() != null) {
	        for (TaxaExtraConveniada taxaExtra : conveniados.getTaxaExtraConveniada()) {
	            taxaExtra.setConveniados(conveniados);
	            
	            // Configurar período de cobrança
	            PeriodoCobrancaTaxa periodo = taxaExtra.getPeriodoCobrancaTaxa();
	            if (periodo != null) {
	                periodo.setTaxaExtraConveniada(taxaExtra);
	                
	                // Validar datas do período
	                if (periodo.getDataInicio().isAfter(periodo.getDataFim())) {
	                    throw new IllegalArgumentException("Data de início não pode ser posterior à data de fim");
	                }
	            }
	        }
	    }
	    
	    // Configurar contratos
	    if (conveniados.getContratoConveniado() != null) {
	        for (ContratoConveniado contrato : conveniados.getContratoConveniado()) {
	            contrato.setConveniados(conveniados);
	            
	            // Configurar vigencias
	            if (contrato.getVigencias() != null) {
	                for (VigenciaContratoConveniada vigencia : contrato.getVigencias()) {
	                    vigencia.setContratoConveniado(contrato);
	                    
	                    // Validações de datas
	                    if (vigencia.getDataInicio() == null || vigencia.getDataFinal() == null) {
	                        throw new IllegalArgumentException("Datas de vigência são obrigatórias");
	                    }
	                    if (vigencia.getDataInicio().isAfter(vigencia.getDataFinal())) {
	                        throw new IllegalArgumentException("Data de início não pode ser posterior à data final");
	                    }
	                }
	            }
	        }
	    }
	}
	
	/*	
	public UsuarioDTO salvarUsuarioPJConveniada( Usuario userPJConveniado ) throws ExceptionCustomizada {
		
		if (userPJConveniado.getIdUsuario() != null && usuarioRepository.findByLogin(userPJConveniado.getLogin()) != null) {
			throw new ExceptionCustomizada("Já existe o Login cadastrado: " + userPJConveniado.getLogin() );
		}
		
		String cnpj = FuncoesUteis.removerCaracteresNaoNumericos(userPJConveniado.getPessoa().getPessoaJuridica().getCnpj());
		userPJConveniado.getPessoa().getPessoaJuridica().setCnpj(cnpj);
	
		if (userPJConveniado.getIdUsuario() == null && usuarioRepository.pesquisaPorCpfPF(userPJConveniado.getPessoa().getPessoaJuridica().getCnpj()) != null) {
			throw new ExceptionCustomizada("Já existe CNPJ cadastrado com o número: " + userPJConveniado.getPessoa().getPessoaJuridica().getCnpj());
		}
		
		String tel = FuncoesUteis.removerCaracteresNaoNumericos( userPJConveniado.getPessoa().getTelefone() );
		userPJConveniado.getPessoa().setTelefone(tel);
		
		
		// Trata as informações de Role de Acesso do Usuário
		if( userPJConveniado.getUsuarioAcesso().size() == 0 ) throw new ExceptionCustomizada("ERRO não foi informado o 'O(s) Role de Acesso do Usuário'!");	
		for( int i = 0; i < userPJConveniado.getUsuarioAcesso().size(); i++ ) {
			userPJConveniado.getUsuarioAcesso().get(i).setUsuario(userPJConveniado);
		}
				
		if( userPJConveniado.getPessoa() == null ) throw new ExceptionCustomizada("ERRO não foi informado os dados 'Referente a Pessoa Pessoa Jurídijo'!");

		userPJConveniado.getPessoa().setUsuario(userPJConveniado);
		userPJConveniado.getPessoa().setPessoaJuridica(userPJConveniado.getPessoa().getPessoaJuridica());
		userPJConveniado.getPessoa().getPessoaJuridica().setPessoa(userPJConveniado.getPessoa());

		Conveniados conveniados = conveniadosService.salvarConveniadosService(userPJConveniado.getPessoa().getConveniados());

		userPJConveniado.getPessoa().setConveniados( conveniados );
		userPJConveniado.getPessoa().setPessoaFisica(null);
		userPJConveniado.getPessoa().setFuncionario(null);
		
		String encryptedPass = new BCryptPasswordEncoder().encode(userPJConveniado.getPassword());
		userPJConveniado.setSenha(encryptedPass);

		userPJConveniado = usuarioRepository.saveAndFlush( userPJConveniado );
		
		conveniados.setPessoa(userPJConveniado.getPessoa());
		
		UsuarioDTO dto = UsuarioMapper.INSTANCE.toDto(userPJConveniado);
		
		return dto;
				
	}
*/
	
    /* ******************************************************************************************************************************** */
	/*                                                                                                                                  */
	/*                  Procedimento de validação para salvar um Usuario do tipo Funcionário para uma Entidade.                         */
	/*                                                                                                                                  */
    /* ******************************************************************************************************************************** */
	public UsuarioDTO salvarUsuarioFuncionario( Usuario userFuncionario ) throws ExceptionCustomizada {

		if (userFuncionario.getIdUsuario() != null && usuarioRepository.findByLogin(userFuncionario.getLogin()) != null) {
			throw new ExceptionCustomizada("Já existe o Login cadastrado: " + userFuncionario.getLogin() );
		}
		String cpf = FuncoesUteis.removerCaracteresNaoNumericos( userFuncionario.getPessoa().getPessoaFisica().getCpf() );
		
		userFuncionario.getPessoa().getPessoaFisica().setCpf(cpf);
	
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
		cartao.setDtValidade( FuncoesUteis.somarAnosADataAtual(5) ); // indica 5 anos para a data de validação do cartão
		userFuncionario.getPessoa().getFuncionario().getCartao().add(cartao);
		
		/* Valista se o Funcionario esta associado a uma Secreária ou é um Funcionario lotado diretamente na Prefeitura */
		// Se o ID da secretária for igual a -1, é porque não esta associado a nenhua Secretária 		
		if( userFuncionario.getPessoa().getFuncionario().getSecretaria().getIdSecretaria() == -1 ) 
			userFuncionario.getPessoa().getFuncionario().setSecretaria(null);
		else userFuncionario.getPessoa().getFuncionario().getSecretaria().setFuncionario(userFuncionario.getPessoa().getFuncionario());
		
		// Associa o Salario passado no Jeson ao Funcionario em cadastro
//			userFuncionario.getPessoa().getFuncionario().getEntidade().setFuncionario(userFuncionario.getPessoa().getFuncionario());
		
		userFuncionario.getPessoa().setPessoaJuridica(null);
		
		String encryptedPass = new BCryptPasswordEncoder().encode(userFuncionario.getPassword());
		userFuncionario.setSenha(encryptedPass);		
		
		userFuncionario = usuarioRepository.saveAndFlush( userFuncionario );
		
//		UauarioDTO uauarioPFDTO = getUauarioPFDTO( userFuncionario );
		UsuarioDTO dto = UsuarioMapper.INSTANCE.toDto(userFuncionario);		
		return dto;

	}

    /* ******************************************************************************************************************************** */
	/*                                                                                                                                  */
	/*                  Procedimento de validação para salvar um Usuario do tipo Pessoa Física para uma Conveniada.                     */
	/*                                                                                                                                  */
    /* ******************************************************************************************************************************** */
	public UsuarioDTO salvarUsuarioPFConveniada( Usuario userPFConveniada ) throws ExceptionCustomizada {

		if (userPFConveniada.getIdUsuario() != null && usuarioRepository.findByLogin(userPFConveniada.getLogin()) != null) {
			throw new ExceptionCustomizada("Já existe o Login cadastrado para: " + userPFConveniada.getLogin() );
		}
		String cpf = FuncoesUteis.removerCaracteresNaoNumericos( userPFConveniada.getPessoa().getPessoaFisica().getCpf() );
		
		userPFConveniada.getPessoa().getPessoaFisica().setCpf(cpf);
	
		if (userPFConveniada.getIdUsuario() == null && usuarioRepository.pesquisaPorCpfPF(userPFConveniada.getPessoa().getPessoaFisica().getCpf()) != null) {
			throw new ExceptionCustomizada("Já existe CPF cadastrado com o número: " + userPFConveniada.getPessoa().getPessoaFisica().getCpf());
		}
		
		// Trata as informações de Role de Acesso do Usuário
		if( userPFConveniada.getUsuarioAcesso().size() == 0 ) throw new ExceptionCustomizada("ERRO não foi informado o 'O(s) Role de Acesso do Usuário'!");	
		for( int i = 0; i < userPFConveniada.getUsuarioAcesso().size(); i++ ) {
			userPFConveniada.getUsuarioAcesso().get(i).setUsuario(userPFConveniada);
		}
				
		if( userPFConveniada.getPessoa() == null ) throw new ExceptionCustomizada("ERRO não foi informado os dados 'Referente a Pessoa Pessoa Fisíca'!");

		userPFConveniada.getPessoa().setUsuario(userPFConveniada);
		
		// Valida as informaçoes referente aos dados da Pesoa Fisica
		userPFConveniada.getPessoa().setPessoaFisica(userPFConveniada.getPessoa().getPessoaFisica());
		userPFConveniada.getPessoa().getPessoaFisica().setPessoa(userPFConveniada.getPessoa());

		if( userPFConveniada.getPessoa().getConveniados().getIdConveniados() == null ) 
			throw new ExceptionCustomizada("ERRO não foi informado os dados 'Referente a Pessoa Pessoa Fisíca'!");
			
		Conveniados conveniados = conveniadosRepository.findById( userPFConveniada.getPessoa().getConveniados().getIdConveniados() )
	            .orElseThrow(() -> new UsernameNotFoundException("ID da Conveniada não encontrado"));

		userPFConveniada.getPessoa().setConveniados( conveniados );
		userPFConveniada.getPessoa().setFuncionario   (null);
		userPFConveniada.getPessoa().setPessoaJuridica(null);
		
		String encryptedPass = new BCryptPasswordEncoder().encode(userPFConveniada.getPassword());
		userPFConveniada.setSenha(encryptedPass);		
		
		userPFConveniada = usuarioRepository.saveAndFlush( userPFConveniada );
		
//		UauarioDTO uauarioPFDTO = getUauarioPFDTO( userPFConveniada );
		UsuarioDTO uauarioPFDTO = UsuarioMapper.INSTANCE.toDto(userPFConveniada);
		
		return uauarioPFDTO;

	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/
	public UauarioDTO pesquisaUsuarioPFByLongin(String login ) {
		
	//	Usuario userPF = usuarioRepository.findByLogin(login);
		Usuario userPF = usuarioRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));
		
		UauarioDTO uauarioPFDTO = getUauarioPFDTO( userPF );
		return uauarioPFDTO;
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/
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

		uauarioPFDTO.getPessoa().setPessoaFisica  ( PessoaFisicaMapper.INSTANCE.toDto( user.getPessoa().getPessoaFisica()     ) );
		uauarioPFDTO.getPessoa().setPessoaJuridica( PessoaJuridicaMapper.INSTANCE.toDto( user.getPessoa().getPessoaJuridica() ) );
		uauarioPFDTO.getPessoa().setConveniados   ( ConveniadosMapper.INSTANCE.toResumoDto(user.getPessoa().getConveniados()  ) );
		uauarioPFDTO.getPessoa().setFuncionario   ( FuncionarioMapper.INSTANCE.toResumoDto( user.getPessoa().getFuncionario() ) );

		
		return uauarioPFDTO;		
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public Usuario getUsuarioByLogin( String login )  {
		
//		Usuario usuario = usuarioRepository.BuscaByLogin( login );
		Usuario usuario = usuarioRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));
		
		return usuario;		
	}

	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	    Usuario usuario = usuarioRepository.findByLogin(username)
	            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
	    
	    // Carrega explicitamente as relações necessárias
	    Hibernate.initialize(usuario.getUsuarioAcesso());
	    if (usuario.getUsuarioAcesso() != null) {
	        for (UsuarioAcesso ua : usuario.getUsuarioAcesso()) {
	            Hibernate.initialize(ua.getAcesso());
	        }
	    }
	    
	    return usuario;
	}
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
	public String atualizaPass( String login, String pass ) {
		
		String retorno = "Senha atualizada com sucesso e enviada por e-mail!";
		
		if(!usuarioRepository.isLogin(login)) retorno = "Usuário não encontrado!";
		
		String encryptedPass = new BCryptPasswordEncoder().encode( pass.trim() );
		
		
		usuarioRepository.updatePass(login, encryptedPass);

		return retorno;
		
	}
	
	
	 public UsuarioLogadoDTO validaUserLogadoByIdOrLogin( Long idUserLogado, String login ) {

		 Usuario userlogado = new Usuario();
		 if( idUserLogado > 0 && login == null) {
			 userlogado = usuarioRepository.findById( idUserLogado )
			            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
		 }else {
				
			 userlogado = usuarioRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
		 }
		 UsuarioLogadoDTO usuarioLogado =  validaUserLogado( userlogado );
		 
		 
		 
		 return usuarioLogado;		 
	 }
	
	
	/******************************************************************/
	/*                                                                */
	/*                                                                */
	/******************************************************************/	
    public UsuarioLogadoDTO validaUserLogado( Usuario userlogado ) {
//    	Usuario userlogado = usuarioRepository.findById( idUserLogado )
//	            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    	
    	UsuarioLogadoDTO usuarioLogado = new UsuarioLogadoDTO();
    	
    	usuarioLogado.setIdUsuario( userlogado.getIdUsuario() );
    	usuarioLogado.setLogin    ( userlogado.getLogin()     );
    	usuarioLogado.setSenha    ( userlogado.getSenha()     );  	

    	// Carrega os tipos de acessos do Usuário.
		for( int i = 0; i < userlogado.getUsuarioAcesso().size(); i++ ) {
			UsuarioAcessoDTO usuarioAcesso = new UsuarioAcessoDTO();
			usuarioAcesso.setIdUsuarioAcesso( userlogado.getUsuarioAcesso().get(i).getIdUsuarioAcesso() );
						
			usuarioAcesso.getAcesso().setIdAcesso  ( userlogado.getUsuarioAcesso().get(i).getAcesso().getIdAcesso()   );
			usuarioAcesso.getAcesso().setDescAcesso( userlogado.getUsuarioAcesso().get(i).getAcesso().getDescAcesso() );
			
			usuarioAcesso.getUsuario().setIdUsuario( userlogado.getUsuarioAcesso().get(i).getUsuario().getIdUsuario()  );
			usuarioAcesso.getUsuario().setLogin    ( userlogado.getUsuarioAcesso().get(i).getUsuario().getLogin() );
			
			usuarioLogado.getUsuarioAcesso().add(usuarioAcesso);
		}
		
		// Info. pertinentes aos Usuários.
		usuarioLogado.getPessoa().setIdPessoa  ( userlogado.getPessoa().getIdPessoa()   );
		usuarioLogado.getPessoa().setNomePessoa( userlogado.getPessoa().getNomePessoa() );
   	
    	// verifica o tipo de unsuario logado (User de Sistema / User Conveniado / User Entidade)
		
		if( userlogado.getPessoa().getConveniados() != null ) { // User do tipo Conveniado
			usuarioLogado.setIsConveniada ( true  );
			usuarioLogado.setIsUserSistema( false );
			usuarioLogado.setIsEntidade   ( false );
			
			
			Pessoa pessoaConveniadoPJ = pessoaRepository.getPessoaConveniadaPJ( userlogado.getPessoa().getConveniados().getIdConveniados() )
		            .orElseThrow(() -> new UsernameNotFoundException("Pessoa da Conveiada não encontrado"));

//			Conveniados conveniado = conveniadosRepository.findById( userlogado.getPessoa().getConveniados().getIdConveniados() )
//		            .orElseThrow(() -> new UsernameNotFoundException("Pessoa da Conveiada não encontrado"));
			
			usuarioLogado.getPessoa().getConveniados().setIdConveniados ( userlogado.getPessoa().getConveniados().getIdConveniados() );
			usuarioLogado.getPessoa().getConveniados().setNomeConveniado( pessoaConveniadoPJ.getNomePessoa()                     );
			
		}else {
			
			Optional<Funcionario> funcionario = funcionarioRepository.findByIdPessoa( userlogado.getPessoa().getIdPessoa() );
			funcionario.ifPresentOrElse(
				    f -> { // código se existir  
							usuarioLogado.setIsConveniada ( false  );
							usuarioLogado.setIsUserSistema( false );
							usuarioLogado.setIsEntidade   ( true );
							
					        // Garante que o FuncionarioDTO está inicializado
					        if (usuarioLogado.getPessoa().getFuncionario() == null) {
					            usuarioLogado.getPessoa().setFuncionario(new FuncionarioDTO());
					        }

							usuarioLogado.getPessoa().getFuncionario().setIdFuncionario( f.getIdFuncionario() );
							usuarioLogado.getPessoa().getFuncionario().setDtCriacao    ( f.getDtCriacao()     );
							usuarioLogado.getPessoa().getFuncionario().setDtAlteracao  ( f.getDtAlteracao()   );
							// Info. sobre limite de credito usuario logado
							usuarioLogado.getPessoa().getFuncionario().getLimiteCredito().setIdLimiteCredito( f.getLimiteCredito().getIdLimiteCredito() );
							usuarioLogado.getPessoa().getFuncionario().getLimiteCredito().setLimite         ( f.getLimiteCredito().getLimite()          );
							usuarioLogado.getPessoa().getFuncionario().getLimiteCredito().setValorUtilizado ( f.getLimiteCredito().getValorUtilizado()  );
							usuarioLogado.getPessoa().getFuncionario().getLimiteCredito().setDtCriacao      ( f.getLimiteCredito().getDtCriacao()       );
							usuarioLogado.getPessoa().getFuncionario().getLimiteCredito().setDtAlteracao    ( f.getLimiteCredito().getDtAlteracao()     );
							// Info. sobre salario usuario logado
							usuarioLogado.getPessoa().getFuncionario().getSalario().setIdSalario   ( f.getSalario().getIdSalario()    );
							usuarioLogado.getPessoa().getFuncionario().getSalario().setValorBruto  ( f.getSalario().getValorBruto()   );
							usuarioLogado.getPessoa().getFuncionario().getSalario().setValorLiquido( f.getSalario().getValorLiquido() );
							usuarioLogado.getPessoa().getFuncionario().getSalario().setDtCriacao   ( f.getSalario().getDtCriacao()    );
							usuarioLogado.getPessoa().getFuncionario().getSalario().setDtAlteracao ( f.getSalario().getDtAlteracao()  );
							// Info. sobre a pessoa, neste caso não será necessario. informado acima no objeto
							usuarioLogado.getPessoa().getFuncionario().setPessoa(null);
							// Info. sobre a Secretária caso o funcionário perteça a uma.
							usuarioLogado.getPessoa().getFuncionario().getSecretaria().setIdSecretaria  ( f.getSecretaria().getIdSecretaria()   );
							usuarioLogado.getPessoa().getFuncionario().getSecretaria().setNomeSecretaria( f.getSecretaria().getNomeSecretaria() );
							// Info. dos cartões do usuario logado. Cartões Desativados e Ativo.
							for( Cartao carta : f.getCartao() ) {
								CartaoDTO cartaoDTO = new CartaoDTO();
								cartaoDTO.setIdCartao    ( carta.getIdCartao()     );
								cartaoDTO.setNumeracao   ( carta.getNumeracao()    );
								cartaoDTO.setDtCriacao   ( carta.getDtCriacao()    );
								cartaoDTO.setDtAlteracao ( carta.getDtAlteracao()  );
								cartaoDTO.setDtValidade  ( carta.getDtValidade()   );
								cartaoDTO.setStatusCartao( carta.getStatusCartao() );
								usuarioLogado.getPessoa().getFuncionario().getCartao().add(cartaoDTO);
							}
							
							usuarioLogado.getPessoa().getFuncionario().getEntidade().setIdEntidade  ( f.getEntidade().getIdEntidade()   );
							usuarioLogado.getPessoa().getFuncionario().getEntidade().setNomeEntidade( f.getEntidade().getNomeEntidade() );
							
					        // Garante que o FuncionarioDTO está inicializado
					        if (usuarioLogado.getPessoa().getEntidade() == null) {
					            usuarioLogado.getPessoa().setEntidade(new EntidadeLogadoDTO());
					        }

							
							usuarioLogado.getPessoa().getEntidade().setIdEntidade  ( f.getEntidade().getIdEntidade()   );
							usuarioLogado.getPessoa().getEntidade().setNomeEntidade( f.getEntidade().getNomeEntidade() );
				    	
				    },
				    () -> { // código se não existir 
						usuarioLogado.setIsConveniada ( false );
						usuarioLogado.setIsUserSistema( true  );
						usuarioLogado.setIsEntidade   ( false );
				    }
				);			
		}
				
		if( userlogado.getPessoa().getPessoaFisica() != null ) {
			
			usuarioLogado.getPessoa().getPessoaFisica().setIdPessoaFisica( userlogado.getPessoa().getPessoaFisica().getIdPessoaFisica() );
			usuarioLogado.getPessoa().getPessoaFisica().setDtNascimento  ( userlogado.getPessoa().getPessoaFisica().getDtNascimento()   );
			usuarioLogado.getPessoa().getPessoaFisica().setCpf           ( userlogado.getPessoa().getPessoaFisica().getCpf()            );
			usuarioLogado.getPessoa().getPessoaFisica().setPessoa(null);
			
		}else  if( userlogado.getPessoa().getPessoaJuridica() != null ) {
			
			usuarioLogado.getPessoa().getPessoaJuridica().setIdPessoa     ( userlogado.getPessoa().getPessoaJuridica().getIdPessoaJuridica() );
			usuarioLogado.getPessoa().getPessoaJuridica().setCnpj         ( userlogado.getPessoa().getPessoaJuridica().getCnpj()             );
			usuarioLogado.getPessoa().getPessoaJuridica().setInscEstadual ( userlogado.getPessoa().getPessoaJuridica().getInscEstadual()     );
			usuarioLogado.getPessoa().getPessoaJuridica().setInscMunicipal( userlogado.getPessoa().getPessoaJuridica().getInscMunicipal()    );
			usuarioLogado.getPessoa().getPessoaJuridica().setNomeFantasia ( userlogado.getPessoa().getPessoaJuridica().getNomeFantasia()     );			
			usuarioLogado.getPessoa().getPessoaJuridica().setRazaoSocial  ( userlogado.getPessoa().getPessoaJuridica().getRazaoSocial()      );
			usuarioLogado.getPessoa().getPessoaJuridica().setIdPessoa     ( userlogado.getPessoa().getIdPessoa());
		}
		
		return usuarioLogado;
	}
	
	
}

