package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ LocacaoService.class})
public class LocacaoServiceTest_PowerMock {
	@InjectMocks
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
		locacaoService = PowerMockito.spy(locacaoService);
		System.out.println("Iniciando 4");
		CalculadoraTest.ordem.append(4);
		/*
		 * locacaoService = new LocacaoService(); dao = Mockito.mock(LocacaoDAO.class);
		 * locacaoService.setLocacaoDAO(dao); spc = Mockito.mock(SPCService.class);
		 * locacaoService.setSPCService(spc); email = Mockito.mock(EmailService.class);
		 * locacaoService.setEmailService(email);
		 */

	}
	
	@After
	public void tearDown () {
		System.out.println("finalizando 4.");
	}
	
	@AfterClass
	public static void tearDownClass () {
		System.out.println(CalculadoraTest.ordem.toString());
	}
	

	@Test
	public void deveAlugarFilme() throws Exception {
		// cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(5.0).agora());

		PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(20, 03, 2020));

		// acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

		// verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(20, 03, 2020)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(21, 03, 2020)), is(true));

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

		 PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(21,
		 03, 2020));
		
	

		// acao
		Locacao retorno = locacaoService.alugarFilme(usuario, filmes);

		// verificacao
		// boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(),
		// Calendar.MONDAY);
		// Assert.assertTrue(ehSegunda);
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());
		// PowerMockito.verifyNew(Date.class, times(2)).withNoArguments();
	}

		
	@Test
	public void deveAlugarFilme_SemCalcularValor() throws Exception {
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		PowerMockito.doReturn(1.0).when(locacaoService, "calcularValorLocacao", filmes);
		
		//acao
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		//verificacao
		assertThat(locacao.getValor(), is(1.0));
		PowerMockito.verifyPrivate(locacaoService).invoke("calcularValorLocacao", filmes);
	}
	
	@Test
	public void deveCalcularValorLocacao() throws Exception {
		//cenario
		List<Filme> filmes = Arrays.asList(umFilme().agora());

		
		//acao
		Double valor = (Double) Whitebox.invokeMethod(locacaoService, "calcularValorLocacao", filmes);
		
		//verificacao
		assertThat(valor, is(4.0));
		
	}

}