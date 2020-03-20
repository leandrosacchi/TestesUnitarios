package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHoje;
import static br.ce.wcaquino.matchers.MatchersProprios.ehHojeComDiferencaDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterData;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueExceptions;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.runners.ParallelRunner;
import br.ce.wcaquino.utils.DataUtils;
import junit.framework.Assert;

@RunWith(ParallelRunner.class)
public class LocacaoServiceTest {
	
	@InjectMocks
	@Spy
	private LocacaoService locacaoService;

	@Mock
	private SPCService spc;
	@Mock
	private LocacaoDAO dao;
	@Mock
	private EmailService email;

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		System.out.println("iniciando 2");
		/*
		 * locacaoService = new LocacaoService(); dao = Mockito.mock(LocacaoDAO.class);
		 * locacaoService.setLocacaoDAO(dao); spc = Mockito.mock(SPCService.class);
		 * locacaoService.setSPCService(spc); email = Mockito.mock(EmailService.class);
		 * locacaoService.setEmailService(email);
		 */

	}
	
	@After
	public void tearDown () {
		System.out.println("finalizando 2..");
	}

	@Test
	public void deveAlugarFilme() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

	//	PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(20, 03, 2020));
		Mockito.doReturn(DataUtils.obterData(20, 03, 2020)).when(locacaoService).obterData();
		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

		// verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		// error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
	//	error.checkThat(locacao.getDataLocacao(), ehHoje());
		// error.checkThat(isMesmaData(locacao.getDataRetorno(),
		// DataUtils.obterDataComDiferencaDias(1)), is(true));
	//	error.checkThat(locacao.getDataRetorno(), ehHojeComDiferencaDias(1));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), obterData(20, 03, 2020)), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterData(21, 03, 2020)), is(true));

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
	public void nãoDeveDevolverFilmeNoDomingo() throws Exception {

		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		Mockito.doReturn(DataUtils.obterData(21, 03, 2020)).when(locacaoService).obterData();

		// acao
		Locacao retorno = locacaoService.alugarFilme(usuario, filmes);

		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
		
	

	}

	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("User 2").agora();

		List<Filme> filmes = Arrays.asList(umFilme().agora());

		when(spc.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);

		// acao
		try {
			locacaoService.alugarFilme(usuario, filmes);

			// verificacao
			fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario negativado"));
		}

		verify(spc).possuiNegativacao(usuario);
	}

	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		// cenario
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("User em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();

		List<Locacao> locacoes = Arrays.asList(umLocacao().comUsuario(usuario).atrasada().agora(),
				umLocacao().comUsuario(usuario2).agora(), umLocacao().atrasada().comUsuario(usuario3).agora(),
				umLocacao().atrasada().comUsuario(usuario3).agora());

		when(dao.obterLocacoesPendentes()).thenReturn(locacoes);

		// acao
		locacaoService.notificarAtrasos();

		// verificacao
		verify(email, Mockito.times(3)).notificarAtraso(Mockito.any(Usuario.class));
		verify(email).notificarAtraso(usuario);
		verify(email, Mockito.atLeastOnce()).notificarAtraso(usuario3);
		verify(email, Mockito.never()).notificarAtraso(usuario2);
		verifyNoMoreInteractions(email);
		verifyZeroInteractions(spc);

	}

	@Test
	public void deveTratarErroNoSPC() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Falha catastrofica"));
		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Problema com SPC, tente novamente");

		// acao
		locacaoService.alugarFilme(usuario, filmes);

		// verificacao
	}

	@Test
	public void deveProrrogarUmaLocacao() {
		// cenario
		Locacao locacao = umLocacao().agora();

		// acao
		locacaoService.prorrogarLocacao(locacao, 3);

		// verificacao
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		Mockito.verify(dao).salvar(argCapt.capture());
		Locacao locacaoRetornada = argCapt.getValue();

		error.checkThat(locacaoRetornada.getValor(), is(12.0));
		error.checkThat(locacaoRetornada.getDataLocacao(), ehHoje());
		error.checkThat(locacaoRetornada.getDataRetorno(), ehHojeComDiferencaDias(3));
	}
	
	@Test
	public void deveCalcularValorLocacao() throws Exception {
		//cenario
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		
		//acao
		Class<LocacaoService> clazz = LocacaoService.class;
		Method metodo = clazz.getDeclaredMethod("calcularValorLocacao", List.class);
		metodo.setAccessible(true);
		Double valor = (Double)metodo.invoke(locacaoService, filmes);
		
		//verificacao
		assertThat(valor, is(4.0));
		
	}

}