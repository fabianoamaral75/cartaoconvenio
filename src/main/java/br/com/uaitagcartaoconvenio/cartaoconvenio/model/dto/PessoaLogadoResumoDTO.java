package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idPessoa")
public class PessoaLogadoResumoDTO {
	private Long idPessoa;
	private String nomePessoa;
/*	
    private PessoaFisicaDTO      pessoaFisica   = new PessoaFisicaDTO();
    private PessoaJuridicaDTO    pessoaJuridica = new PessoaJuridicaDTO();
    private ConveniadosLogadoDTO conveniados    = new ConveniadosLogadoDTO();
    private FuncionarioDTO       funcionario    = new FuncionarioDTO();
    private EntidadeLogadoDTO    entidade       = new EntidadeLogadoDTO();
*/

    private PessoaFisicaDTO pessoaFisica;      // Remova inicialização
    private PessoaJuridicaDTO pessoaJuridica;  // Remova inicialização
    private ConveniadosLogadoDTO conveniados;  // Remova inicialização
    private FuncionarioDTO funcionario;        // Remova inicialização
    private EntidadeLogadoDTO entidade;        // Remova inicialização

    // Getters com inicialização lazy
    public PessoaFisicaDTO getPessoaFisica() {
        if (pessoaFisica == null) {
            pessoaFisica = new PessoaFisicaDTO();
        }
        return pessoaFisica;
    }	
	
}

