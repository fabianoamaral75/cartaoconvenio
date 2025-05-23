package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.StatusEmtidade;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.CnpjDeserializer;
import br.com.uaitagcartaoconvenio.cartaoconvenio.util.CnpjSerializer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "idEntidade")
@JsonIdentityInfo(
	    generator = ObjectIdGenerators.PropertyGenerator.class,
	    property = "idEntidade",
	    scope = EntidadeDTO.class
	)
public class EntidadeDTO {
    private Long idEntidade;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtCriacao;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date dtAlteracao;
	private Long diaRecebimento;	
	private Long mesRecebimentoPosFechamento;	
    private String site;
    private String obs;
    private String nomeEntidade;
    @JsonSerialize(using = CnpjSerializer.class)
    @JsonDeserialize(using = CnpjDeserializer.class)
    private String cnpj;
    private String inscEstadual;
    private String inscMunicipal;
    private String logradoro;
    private String uf;
    private String cidade;
    private String cep;
    private String numero;
    private String complemento;
    private String bairro;
    private StatusEmtidade descStatusEmtidade;
    private List<SecretariaResumoDTO> secretaria;
    private List<TaxaEntidadeResumoDTO> taxaEntidade;
    private List<FuncionarioResumoDTO> listaFuncionario;
    private List<TaxaCalcLimiteCreditoFuncResumoDTO> taxaCalcLimiteCreditoFunc;
    private List<ContratoEntidadeDTO> contratoEntidade = new ArrayList<ContratoEntidadeDTO>();
}
