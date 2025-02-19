package br.com.uaitagcartaoconvenio.cartaoconvenio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.uaitagcartaoconvenio.cartaoconvenio.enums.RoleAcesso;
import br.com.uaitagcartaoconvenio.cartaoconvenio.model.Acesso;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.AcessoService;

@SpringBootTest(classes = CartaoconvenioApplication.class)
public class CartaoconvenioApplicationTests {

	@Autowired
	private AcessoService acessoService;
	

	
	
	@Test
	void contextLoads() {
	}
	
	
	@Test
	public void cadatastraAcesso() {
		
		Acesso acesso = new Acesso();
		acesso.setDescAcesso(RoleAcesso.CONVENIADA_ADMI);
		acessoService.saveAcesso(acesso);
	}


}
