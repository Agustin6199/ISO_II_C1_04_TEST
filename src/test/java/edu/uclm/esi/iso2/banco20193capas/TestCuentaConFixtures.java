package edu.uclm.esi.iso2.banco20193capas;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaInvalidaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.ImporteInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.PinInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.SaldoInsuficienteException;
import edu.uclm.esi.iso2.banco20193capas.model.Cliente;
import edu.uclm.esi.iso2.banco20193capas.model.Cuenta;
import edu.uclm.esi.iso2.banco20193capas.model.Manager;
import edu.uclm.esi.iso2.banco20193capas.model.TarjetaCredito;
import edu.uclm.esi.iso2.banco20193capas.model.TarjetaDebito;
import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCuentaConFixtures extends TestCase {
	private Cuenta cuentaPepe, cuentaAna;
	private Cliente pepe, ana;
	private TarjetaDebito tdPepe, tdAna;
	private TarjetaCredito tcPepe, tcAna;

	@Before
	public void setUp() {
		Manager.getMovimientoDAO().deleteAll();
		Manager.getMovimientoTarjetaCreditoDAO().deleteAll();
		Manager.getTarjetaCreditoDAO().deleteAll();
		Manager.getTarjetaDebitoDAO().deleteAll();
		Manager.getCuentaDAO().deleteAll();
		Manager.getClienteDAO().deleteAll();

		this.pepe = new Cliente("12345X", "Pepe", "Pérez");
		this.pepe.insert();
		this.ana = new Cliente("98765F", "Ana", "López");
		this.ana.insert();
		this.cuentaPepe = new Cuenta(1);
		this.cuentaAna = new Cuenta(2);
		try {
			this.cuentaPepe.addTitular(pepe);
			this.cuentaPepe.insert();
			this.cuentaPepe.ingresar(1000);
			this.cuentaAna.addTitular(ana);
			this.cuentaAna.insert();
			this.cuentaAna.ingresar(5000);
			this.tcPepe = this.cuentaPepe.emitirTarjetaCredito(pepe.getNif(), 2000);
			this.tcPepe.cambiarPin(this.tcPepe.getPin(), 1234);
			this.tcAna = this.cuentaAna.emitirTarjetaCredito(ana.getNif(), 10000);
			this.tcAna.cambiarPin(this.tcAna.getPin(), 1234);
			this.tdPepe = this.cuentaPepe.emitirTarjetaDebito(pepe.getNif());
			this.tdPepe.cambiarPin(this.tdPepe.getPin(), 1234);
			this.tdAna = this.cuentaAna.emitirTarjetaDebito(ana.getNif());
			this.tdAna.cambiarPin(this.tdAna.getPin(), 1234);
		} catch (Exception e) {
			fail("Excepción inesperada en setUp(): " + e);
		}
	}

	@Test
	public void testImporteInvalido1() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido2() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido3() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido4() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido5() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido6() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido7() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido8() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido9() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException10_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido10_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException11_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido11_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	@Test
	public void testImporteInvalido12() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testNormal13() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			assertTrue(this.cuentaPepe.getSaldo()==saldoPepe);
			assertTrue(this.cuentaAna.getSaldo()==saldoAna);
		}
		catch (Exception e) {
			fail("Excepción inesperada: " + e);
		}
	}
	@Test
	public void testNormal14() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			assertTrue(this.cuentaPepe.getSaldo()==saldoPepe);
			assertTrue(this.cuentaAna.getSaldo()==saldoAna);
		}
		catch (Exception e) {
			fail("Excepción inesperada: " + e);
		}
	}
	@Test
	public void testImporteInvalido15() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException16_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(0);
//			saldoPepe=saldoPepe+(0);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido16_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	@Test
	public void testNormal17() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			assertTrue(this.cuentaPepe.getSaldo()==saldoPepe);
			assertTrue(this.cuentaAna.getSaldo()==saldoAna);
		}
		catch (Exception e) {
			fail("Excepción inesperada: " + e);
		}
	}
	@Test
	public void testNormal18() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			assertTrue(this.cuentaPepe.getSaldo()==saldoPepe);
			assertTrue(this.cuentaAna.getSaldo()==saldoAna);
		}
		catch (Exception e) {
			fail("Excepción inesperada: " + e);
		}
	}
	@Test
	public void testSaldoInsuficienteException19() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba SaldoInsuficienteException");
		}
		catch (SaldoInsuficienteException e) { }
		catch (Exception e) {
			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
		}
	}
	@Test
	public void testSaldoInsuficienteException20() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba SaldoInsuficienteException");
		}
		catch (SaldoInsuficienteException e) { }
		catch (Exception e) {
			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido21() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido22() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido23() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido24() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido25() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido26() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException27_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido27_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException28_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido28_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	@Test
	public void testImporteInvalido29() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido30() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido31() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException32_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(0);
//			saldoPepe=saldoPepe+(0);
//			this.cuentaPepe.retirar(50);
//			saldoPepe=saldoPepe-(50);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido32_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException33_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(0);
//			saldoPepe=saldoPepe+(0);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//			this.cuentaPepe.retirar(50);
//			saldoPepe=saldoPepe-(50);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido33_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException34_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(0);
//			saldoPepe=saldoPepe+(0);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(-1);
//			saldoPepe=saldoPepe-(-1);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido34_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException35_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(0);
//			saldoPepe=saldoPepe+(0);
//			this.cuentaPepe.retirar(-1);
//			saldoPepe=saldoPepe-(-1);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido35_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	@Test
	public void testImporteInvalido36() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido37() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido38() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	// Warning: 2 oracles and 2 test templates are applicable to this test case
	@Test
	public void testSaldoInsuficienteException39_A() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba SaldoInsuficienteException");
		}
		catch (SaldoInsuficienteException e) { }
		catch (Exception e) {
			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
		}
	}/*
	@Test
	public void testImporteInvalido39_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
*/

	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException40_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(100);
//			saldoPepe=saldoPepe+(100);
//			this.cuentaPepe.retirar(0);
//			saldoPepe=saldoPepe-(0);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido40_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	@Test
	public void testImporteInvalido41() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testSaldoInsuficienteException42() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			fail("Se esperaba SaldoInsuficienteException");
		}
		catch (SaldoInsuficienteException e) { }
		catch (Exception e) {
			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
		}
	}
	@Test
	public void testSaldoInsuficienteException43() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba SaldoInsuficienteException");
		}
		catch (SaldoInsuficienteException e) { }
		catch (Exception e) {
			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
		}
	}

	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException44_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido44_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException45_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido45_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	@Test
	public void testImporteInvalido46() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido47() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido48() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido49() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido50() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido51() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException52_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido52_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException53_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido53_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	@Test
	public void testImporteInvalido54() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido55() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException56_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(1000);
//			saldoPepe=saldoPepe+(1000);
//			this.cuentaPepe.retirar(0);
//			saldoPepe=saldoPepe-(0);
//			this.cuentaPepe.retirar(50);
//			saldoPepe=saldoPepe-(50);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido56_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException57_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(0);
//			saldoPepe=saldoPepe+(0);
//			this.cuentaPepe.retirar(50);
//			saldoPepe=saldoPepe-(50);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//			this.cuentaPepe.retirar(50);
//			saldoPepe=saldoPepe-(50);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido57_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException58_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(0);
//			saldoPepe=saldoPepe+(0);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//			this.cuentaPepe.retirar(50);
//			saldoPepe=saldoPepe-(50);
//			this.cuentaPepe.retirar(-1);
//			saldoPepe=saldoPepe-(-1);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido58_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException59_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(0);
//			saldoPepe=saldoPepe+(0);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(-1);
//			saldoPepe=saldoPepe-(-1);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido59_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException60_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(0);
//			saldoPepe=saldoPepe+(0);
//			this.cuentaPepe.retirar(-1);
//			saldoPepe=saldoPepe-(-1);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido60_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(0);
			saldoPepe=saldoPepe+(0);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	@Test
	public void testImporteInvalido61() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
	@Test
	public void testImporteInvalido62() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException63_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(100);
//			saldoPepe=saldoPepe+(100);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//			this.cuentaPepe.retirar(0);
//			saldoPepe=saldoPepe-(0);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido63_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
	@Test
	public void testSaldoInsuficienteException64_A() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba SaldoInsuficienteException");
		}
		catch (SaldoInsuficienteException e) { }
		catch (Exception e) {
			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
		}
	}
	/*
	@Test
	public void testImporteInvalido64_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}
*/

	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException65_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(100);
//			saldoPepe=saldoPepe+(100);
//			this.cuentaPepe.retirar(0);
//			saldoPepe=saldoPepe-(0);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(0);
//			saldoPepe=saldoPepe-(0);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido65_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(100);
			saldoPepe=saldoPepe+(100);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	@Test
	public void testImporteInvalido66() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	// Warning: 2 oracles and 2 test templates are applicable to this test case
	@Test
	public void testSaldoInsuficienteException67_A() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba SaldoInsuficienteException");
		}
		catch (SaldoInsuficienteException e) { }
		catch (Exception e) {
			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
		}
	}
	/*@Test
	public void testImporteInvalido67_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}*/


	// Warning: 2 oracles and 2 test templates are applicable to this test case
	@Test
	public void testSaldoInsuficienteException68_A() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(1000);
			saldoPepe=saldoPepe+(1000);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba SaldoInsuficienteException");
		}
		catch (SaldoInsuficienteException e) { }
		catch (Exception e) {
			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
		}
	}
//	@Test
//	public void testImporteInvalido68_B() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(1000);
//			saldoPepe=saldoPepe+(1000);
//			this.cuentaPepe.retirar(50);
//			saldoPepe=saldoPepe-(50);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(-1);
//			saldoPepe=saldoPepe-(-1);
//
//			fail("Se esperaba ImporteInvalidoException");
//		}
//		catch (ImporteInvalidoException e) { }
//		catch (Exception e) {
//			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
//		}
//	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException69_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//			this.cuentaPepe.retirar(-1);
//			saldoPepe=saldoPepe-(-1);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido69_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException70_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(50);
//			saldoPepe=saldoPepe-(50);
//			this.cuentaPepe.retirar(-1);
//			saldoPepe=saldoPepe-(-1);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido70_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException71_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(0);
//			saldoPepe=saldoPepe-(0);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido71_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException72_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//			this.cuentaPepe.retirar(50);
//			saldoPepe=saldoPepe-(50);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido72_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException73_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(50);
//			saldoPepe=saldoPepe-(50);
//			this.cuentaPepe.retirar(-1);
//			saldoPepe=saldoPepe-(-1);
//			this.cuentaPepe.retirar(100000);
//			saldoPepe=saldoPepe-(100000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido73_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(100000);
			saldoPepe=saldoPepe-(100000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}


	// Warning: 2 oracles and 2 test templates are applicable to this test case
//	@Test
//	public void testSaldoInsuficienteException74_A() {
//		try {
//			double saldoPepe=1000; double saldoAna=5000;
//			this.cuentaPepe.ingresar(-1);
//			saldoPepe=saldoPepe+(-1);
//			this.cuentaPepe.retirar(-1);
//			saldoPepe=saldoPepe-(-1);
//			this.cuentaPepe.retirar(50);
//			saldoPepe=saldoPepe-(50);
//			this.cuentaPepe.retirar(1000);
//			saldoPepe=saldoPepe-(1000);
//
//			fail("Se esperaba SaldoInsuficienteException");
//		}
//		catch (SaldoInsuficienteException e) { }
//		catch (Exception e) {
//			fail("Se esperaba SaldoInsuficienteException, pero se lanzó " + e);
//		}
//	}
	@Test
	public void testImporteInvalido74_B() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(50);
			saldoPepe=saldoPepe-(50);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

	@Test
	public void testImporteInvalido75() {
		try {
			double saldoPepe=1000; double saldoAna=5000;
			this.cuentaPepe.ingresar(-1);
			saldoPepe=saldoPepe+(-1);
			this.cuentaPepe.retirar(-1);
			saldoPepe=saldoPepe-(-1);
			this.cuentaPepe.retirar(1000);
			saldoPepe=saldoPepe-(1000);
			this.cuentaPepe.retirar(0);
			saldoPepe=saldoPepe-(0);

			fail("Se esperaba ImporteInvalidoException");
		}
		catch (ImporteInvalidoException e) { }
		catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException, pero se lanzó " + e);
		}
	}

}
