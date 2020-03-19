package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.dao.LocacaoDAO;
import br.ce.wcaquino.dao.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueExceptions;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	@InjectMocks
	private LocacaoService locacaoService;
	@Mock
	private SPCService spc;
	@Mock
	private LocacaoDAO dao;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		/*
		locacaoService = new LocacaoService();
		dao = Mockito.mock(LocacaoDAO.class);
		locacaoService.setLocacaoDAO(dao);
		spc = Mockito.mock(SPCService.class);
		locacaoService.setSPCService(spc);
		*/ 
	}

	@Parameter
	public List<Filme> filmes;
	@Parameter(value = 1)
	public Double valorLocacao;
	@Parameter(value = 2)
	public String cenario;

	private static Filme Titanic = umFilme().agora();
	private static Filme Parasita = umFilme().agora();
	private static Filme Moonlight = umFilme().agora();
	private static Filme Roma = umFilme().agora();
	private static Filme PulpFiction = umFilme().agora();
	private static Filme KillBill = umFilme().agora();
	private static Filme LaranjaMecanica = umFilme().agora();
	

	@Parameters(name="{2}")
	public static Collection<Object[]> getParametros() {
		return Arrays.asList(new Object[][] { 
				{ Arrays.asList(Titanic, Parasita), 8.0, "2 Filmes: Sem desconto" },
				{ Arrays.asList(Titanic, Parasita, Moonlight), 11.0, "3 Filmes: 25%" },
				{ Arrays.asList(Titanic, Parasita, Moonlight, Roma), 13.0, "4 Filmes: 50%" },
				{ Arrays.asList(Titanic, Parasita, Moonlight, Roma, PulpFiction), 14.0, "5 Filmes: 75%" },
				{ Arrays.asList(Titanic, Parasita, Moonlight, Roma, PulpFiction, KillBill), 14.0, "6 Filmes: 100%"},
				{ Arrays.asList(Titanic, Parasita, Moonlight, Roma, PulpFiction, KillBill, LaranjaMecanica), 18.0, "7 Filmes: Sem desconto" } });
	}

	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueExceptions, LocadoraException {
		// cenario
		Usuario usuario = umUsuario().agora();

		// acao
		Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

		// verificacao
		assertThat(resultado.getValor(), is(valorLocacao));
		
	}

}
