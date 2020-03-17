package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;
import junit.framework.Assert;

public class LocacaoServiceTest {
	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void testeLocacao() throws Exception {
		// cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario usuario = new Usuario("Leandro");
		Filme filme = new Filme("Titanic", 10, 5.0);

		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filme);

		// verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)),
				is(true));

	}

	@Test(expected = Exception.class)
	public void testeLocacao_filmeSemEstoque() throws Exception {
		// cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario usuario = new Usuario("Leandro");
		Filme filme = new Filme("Titanic", 0, 5.0);

		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filme);

	}

	@Test
	public void testeLocacao_filmeSemEstoque2() {
		// cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario usuario = new Usuario("Leandro");
		Filme filme = new Filme("Titanic", 0, 5.0);

		// acao
		try {
			locacaoService.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lancado um excecao");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme sem estoque"));
		}

	}

	@Test
	public void testeLocacao_filmeSemEstoque3() throws Exception {
		// cenario
		LocacaoService locacaoService = new LocacaoService();
		Usuario usuario = new Usuario("Leandro");
		Filme filme = new Filme("Titanic", 0, 5.0);


		expectedException.expect(Exception.class);
		expectedException.expectMessage("Filme sem estoque");
		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filme);
		

	}
}
