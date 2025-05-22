package br.com.uaitagcartaoconvenio.cartaoconvenio.model.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.TipoCobrancaTaxaExtraEnt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicoContratoDTO {
    private Long idServicoContrato;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Sao_Paulo")
    private LocalDateTime dtCadastro;
    private String desServico;
    private BigDecimal vlrServico;
    private TipoCobrancaTaxaExtraEnt tipoCobrancaTaxaExtraEnt;
    @JsonFormat(pattern = "dd/MM/yyyy", timezone = "America/Sao_Paulo")
  	private Date dtUltimaCobranca;
    private Integer qtyMesesCobranca;    
    private Integer qtyMesesCobrancaRealizadas;    

}
