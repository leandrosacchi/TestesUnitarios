package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiEm;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueExceptions;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.matchers.MatchersProprios;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;
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
	public void deveAlugarFilme() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

		// verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		// error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(locacao.getDataLocacao(), ehHoje());
		// error.checkThat(isMesmaData(locacao.getDataRetorno(),
		// DataUtils.obterDataComDiferencaDias(1)), is(true));
		error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));

	}

	@Test(expected = FilmeSemEstoqueExceptions.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());

		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

	}

	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueExceptions {
		// cenario
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// acao
		try {
			locacaoService.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}

	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueExceptions, LocadoraException {
		// cenario
		Usuario usuario = new Usuario("Leandro");

		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Filme vazio");

		// acao
		locacaoService.alugarFilme(usuario, null);

	}
	/*
	 * Testes simplificados em CalculoValorLocacaoTest
	 * 
	 * @Test public void devePagar75PctNoFilme3() throws FilmeSemEstoqueExceptions,
	 * LocadoraException { //cenario Usuario usuario = new Usuario ("Leandro");
	 * List<Filme> filmes = Arrays.asList( new Filme("Titanic", 10, 4.0), new
	 * Filme("Parasita", 3, 4.0), new Filme("Moonlight", 1, 4.0));
	 * 
	 * //acao Locacao resultado = locacaoService.alugarFilme(usuario, filmes);
	 * 
	 * //verificacao assertThat(resultado.getValor(), is(11.0));
	 * 
	 * }
	 * 
	 * @Test public void devePagar50PctNoFilme4() throws FilmeSemEstoqueExceptions,
	 * LocadoraException { //cenario Usuario usuario = new Usuario ("Leandro");
	 * List<Filme> filmes = Arrays.asList( new Filme("Titanic", 10, 4.0), new
	 * Filme("Parasita", 3, 4.0), new Filme("Moonlight", 1, 4.0), new Filme("Roma",
	 * 1, 4.0));
	 * 
	 * //acao Locacao resultado = locacaoService.alugarFilme(usuario, filmes);
	 * 
	 * //verificacao assertThat(resultado.getValor(), is(13.0));
	 * 
	 * }
	 * 
	 * @Test public void devePagar25PctNoFilme5() throws FilmeSemEstoqueExceptions,
	 * LocadoraException { //cenario Usuario usuario = new Usuario ("Leandro");
	 * List<Filme> filmes = Arrays.asList( new Filme("Titanic", 10, 4.0), new
	 * Filme("Parasita", 3, 4.0), new Filme("Moonlight", 1, 4.0), new Filme("Roma",
	 * 1, 4.0) , new Filme("Pulp Fiction", 1, 4.0));
	 * 
	 * //acao Locacao resultado = locacaoService.alugarFilme(usuario, filmes);
	 * 
	 * //verificacao assertThat(resultado.getValor(), is(14.0));
	 * 
	 * }
	 * 
	 * @Test public void devePagar00PctNoFilme6() throws FilmeSemEstoqueExceptions,
	 * LocadoraException { //cenario Usuario usuario = new Usuario ("Leandro");
	 * List<Filme> filmes = Arrays.asList( new Filme("Titanic", 10, 4.0), new
	 * Filme("Parasita", 3, 4.0), new Filme("Moonlight", 1, 4.0), new Filme("Roma",
	 * 1, 4.0) , new Filme("Pulp Fiction", 1, 4.0), new Filme("Kill Bill", 1, 4.0));
	 * 
	 * //acao Locacao resultado = locacaoService.alugarFilme(usuario, filmes);
	 * 
	 * //verificacao assertThat(resultado.getValor(), is(14.0));
	 * 
	 * }
	 */

	@Test
	public void nãoDeveDevolverFilmeNoDomingo() throws FilmeSemEstoqueExceptions, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		// acao
		Locacao retorno = locacaoService.alugarFilme(usuario, filmes);

		// verificacao
		// boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(),
		// Calendar.MONDAY);
		// Assert.assertTrue(ehSegunda);
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

	}

}