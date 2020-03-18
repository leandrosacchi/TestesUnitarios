package br.ce.wcaquino.servicos;

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

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueExceptions;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	private LocacaoService locacaoService;

	@Before
	public void setup() {
		locacaoService = new LocacaoService();
	}

	@Parameter
	public List<Filme> filmes;
	@Parameter(value = 1)
	public Double valorLocacao;
	@Parameter(value = 2)
	public String cenario;

	private static Filme Titanic = new Filme("Titanic", 10, 4.0);
	private static Filme Parasita = new Filme("Parasita", 10, 4.0);
	private static Filme Moonlight = new Filme("Moonlight", 10, 4.0);
	private static Filme Roma = new Filme("Roma", 10, 4.0);
	private static Filme PulpFiction = new Filme("PulpFiction", 10, 4.0);
	private static Filme KillBill = new Filme("KillBill", 10, 4.0);
	private static Filme LaranjaMecanica = new Filme("LaranjaMecanica", 10, 4.0);
	

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
		Usuario usuario = new Usuario("Leandro");

		// acao
		Locacao resultado = locacaoService.alugarFilme(usuario, filmes);

		// verificacao
		assertThat(resultado.getValor(), is(valorLocacao));
		
	}

}
