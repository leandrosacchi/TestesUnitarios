package br.ce.wcaquino.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueExceptions;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import junit.framework.Assert;

public class LocacaoServiceTest {
	private LocacaoService locacaoService;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setup() {
		locacaoService = new LocacaoService();
	}
	
	@Test
	public void testeLocacao() throws Exception {
		// cenario
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

	@Test(expected = FilmeSemEstoqueExceptions.class)
	public void testeLocacao_filmeSemEstoque() throws Exception {
		// cenario
		Usuario usuario = new Usuario("Leandro");
		Filme filme = new Filme("Titanic", 0, 5.0);

		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filme);

	}

	@Test
	public void testeLocacao_usuarioVazio() throws FilmeSemEstoqueExceptions {
		// cenario
		Filme filme = new Filme("Titanic", 1, 5.0);

		// acao
		try {
			locacaoService.alugarFilme(null, filme);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}

	}

	@Test
	public void testeLocacao_filmeVazio() throws FilmeSemEstoqueExceptions, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Leandro");

		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Filme vazio");

		// acao
		locacaoService.alugarFilme(usuario, null);

	}
}