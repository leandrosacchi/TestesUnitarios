package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	
	private Calculadora calc;
	
	@Before
	public void setup () {
		calc = new Calculadora();
	}

	@Test
	public void deveSomarDoisValores() {
		//cenario
		int a = 5;
		int b = 3;

		//acao
		int resultado = calc.somar(a,b);
		
		//verificacao
		Assert.assertEquals(8, resultado);
		
		}
	
	@Test
	public void deveSubtrairDoisValores() {
		//cenario
		int a = 8;
		int b = 5;
		
		//acao
		int resultado = calc.subtrair(a,b);
		
		//verificacao
		Assert.assertEquals(3, resultado);
		
		}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		//cenario
		int a = 6;
		int b = 3;
		
		//acao
		int resultado = calc.dividir(a,b);
		
		//verificacao
		Assert.assertEquals(2, resultado);
		
		}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		//cenario
		int a = 2;
		int b = 0;
		
		calc.dividir(a,b);
		
	}
	
	@Test
	public void deveMultiplicarDoisValores() {
		//cenario
		int a = 2;
		int b = 3;
		
		//acao
		int resultado = calc.multiplicar(a,b);
		
		//verificacao
		Assert.assertEquals(6, resultado);
		
		}
}
