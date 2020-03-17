package br.ce.wcaquino.servicos;


import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {
	@Test
	public void teste() {
		//cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario usuario = new Usuario("Leandro");
		Filme filme = new Filme("Titanic", 10, 5.0);
			
		//acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filme);
		
		//verificacao
		Assert.assertEquals(5.0, locacao.getValor(), 0.01);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
		
		
	}
}
