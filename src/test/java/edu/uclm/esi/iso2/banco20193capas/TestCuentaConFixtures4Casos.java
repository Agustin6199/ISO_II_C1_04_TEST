package edu.uclm.esi.iso2.banco20193capas;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import edu.uclm.esi.iso2.banco20193capas.exceptions.ClienteNoAutorizadoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.ClienteNoEncontradoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.CuentaInvalidaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.ImporteInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.PinInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.SaldoInsuficienteException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.TarjetaBloqueadaException;
import edu.uclm.esi.iso2.banco20193capas.exceptions.TokenInvalidoException;
import edu.uclm.esi.iso2.banco20193capas.model.Cliente;
import edu.uclm.esi.iso2.banco20193capas.model.Cuenta;
import edu.uclm.esi.iso2.banco20193capas.model.Manager;
import edu.uclm.esi.iso2.banco20193capas.model.TarjetaCredito;
import edu.uclm.esi.iso2.banco20193capas.model.TarjetaDebito;
import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCuentaConFixtures4Casos extends TestCase {
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
		
		this.pepe = new Cliente("12345X", "Pepe", "Pérez"); this.pepe.insert();
		this.ana = new Cliente("98765F", "Ana", "López"); this.ana.insert();
		this.cuentaPepe = new Cuenta(1); this.cuentaAna = new Cuenta(2);
		
		try {
			this.cuentaPepe.addTitular(pepe);
			this.cuentaPepe.insert();
			this.cuentaPepe.ingresar(1000);
			this.cuentaAna.addTitular(ana);
			this.cuentaAna.insert();
			this.cuentaAna.ingresar(5000);
			this.tcPepe = this.cuentaPepe.emitirTarjetaCredito(pepe.getNif(), 2000);
			this.tcAna = this.cuentaAna.emitirTarjetaCredito(ana.getNif(), 10000);
			this.tdPepe = this.cuentaPepe.emitirTarjetaDebito(pepe.getNif());
			this.tdAna = this.cuentaAna.emitirTarjetaDebito(ana.getNif());

			this.tcPepe.cambiarPin(tcPepe.getPin(), 1234);
			this.tcAna.cambiarPin(tcAna.getPin(), 1234);
			this.tdPepe.cambiarPin(tdPepe.getPin(), 1234);
			this.tdAna.cambiarPin(tdAna.getPin(), 1234);

		} catch (Exception e) {
			fail("Excepción inesperada en setUp(): " + e);
		}
	}

	@Test
	public void testRetiradaSinSaldo() {
		try {
			this.cuentaPepe.retirar(2000);
			fail("Esperaba SaldoInsuficienteException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (SaldoInsuficienteException e) {
		}
	}

  @Test
	public void testTransferencia() {
		try {
			this.cuentaPepe.transferir(this.cuentaAna.getId(), 500, "Alquiler");
			assertTrue(this.cuentaPepe.getSaldo() == 495);
			assertTrue(this.cuentaAna.getSaldo() == 5500);
		} catch (Exception e) {
			fail("Excepción inesperada: " + e.getMessage());
		}
	}
	
	@Test 
	public void testCompraConTC() {
		try {
			cuentaPepe.retirar(200);
			assertTrue(cuentaPepe.getSaldo() == 800);

			TarjetaCredito tc = cuentaPepe.emitirTarjetaCredito("12345X", 1000);
			tc.comprar(tc.getPin(), 300);
			assertTrue(tc.getCreditoDisponible() == 700);
			tc.liquidar();
			assertTrue(tc.getCreditoDisponible() == 1000);
			assertTrue(cuentaPepe.getSaldo() == 500);
		} catch (Exception e) {
			fail("Excepción inesperada: " + e.getMessage());
		}
	}
	//////////////////////////////////////////////////
	@Test
	public void testCompraPorInternetConTC() {
		try {
			
			int token = this.tcPepe.comprarPorInternet(tcPepe.getPin(), 1000);
			assertTrue(this.tcPepe.getCreditoDisponible()==2000);
			this.tcPepe.confirmarCompraPorInternet(token);
			assertTrue(this.tcPepe.getCreditoDisponible()==1000);
			this.tcPepe.liquidar();
			assertTrue(this.tcPepe.getCreditoDisponible()==2000);
		} catch (Exception e) {
			fail("Excepción inesperada: " + e.getMessage());
		}
	}

	@Test
	public void testComprarPorInternetConTCSaldoInsuficiente() {
		try {
			assertTrue(this.cuentaPepe.getSaldo()==1000);
			assertTrue(this.tcPepe.getCreditoDisponible()==2000);
			int token = this.tcPepe.comprarPorInternet(tcPepe.getPin(), 2500);
			fail("Se esperaba SaldoInsuficienteException");
		}catch(SaldoInsuficienteException e) {
		}catch(Exception e) {
			fail("Esperaba SaldoInsuficienteException");
		}
	}
	@Test
	public void testComprarPorInternetConTCImporteInvalido() {
		try {
			assertTrue(this.cuentaPepe.getSaldo()==1000);
			assertTrue(this.tcPepe.getCreditoDisponible()==2000);
			int token = this.tcPepe.comprarPorInternet(tcPepe.getPin(), -1);
			fail("Se esperaba ImporteInvalidoException");
		}catch(ImporteInvalidoException e) {
		}catch(Exception e) {
			fail("Esperaba ImporteInvalidoException");
		}
	}
	
	@Test
	public void testCompraPorInternetConTCPinInvalido() {
			try {
				this.tcPepe.comprarPorInternet(-57, 1000);
			} catch (PinInvalidoException e) {
			} catch (Exception e) {
				fail("Esperaba PinInvalidoException");
			} 

	}
	
	@Test////
	public void testRetiradaImporteInvalido() {
		try {
			this.cuentaPepe.retirar(-150);
		} catch (ImporteInvalidoException e) {
		} catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException");
		}
	}
	
	@Test////
	public void testRetiradaSaldoInsuficiente() {
		try {
			this.cuentaPepe.retirar(3000);
			fail("Esperaba SaldoInsuficienteException");
		} catch (SaldoInsuficienteException e) {
			
		} catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException");
		}
	}
	@Test///
	public void testRetirada() {
		try {
			this.cuentaPepe.retirar(500);
			assertTrue(this.cuentaPepe.getSaldo() == 500);
		} catch (Exception e) {
			fail("Excepción inesperada: " + e.getMessage());
		}
	}

	@Test
	public void testBloqueoTarjeta() {

		try {
			this.tcPepe.comprarPorInternet(0000, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			this.tcPepe.comprarPorInternet(1111, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}
		try {
			this.tcPepe.comprarPorInternet(9999, 100);
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Esperaba PinInvalidoException");
		}

		try {
			this.tcPepe.comprarPorInternet(1234, 30);
			fail("Esperaba que saltase TarjetaBloqueadaException");
		} catch (TarjetaBloqueadaException e) {
		} catch (Exception e) {
			fail("Esperaba TarjetaBloqueadaException");
		}

	}

	@Test
	public void testSacarDineroTC_1() {
		try {
			this.tcPepe.sacarDinero(1234, 10000);
			fail("Esperaba SaldoInsuficienteException");
		} catch (SaldoInsuficienteException e) {

		} catch (ImporteInvalidoException e) {
			fail("Se ha producido  SaldoInsuficienteException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido SaldoInsuficienteException");
		} catch (PinInvalidoException e) {
			fail("Esperaba SaldoInsuficienteException");
		}
	}

	@Test
	public void testSacarDineroTC_2() {
		try {
			this.tcPepe.sacarDinero(1234, -1000);
			fail("Esperaba mporteInvalidoException");
		} catch (ImporteInvalidoException e) {

		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (PinInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		}

	}

	@Test
	public void testSacarDineroTC_3() {
		try {
			this.tcPepe.sacarDinero(0, 100);
			fail("Esperaba mporteInvalidoException");
		} catch (PinInvalidoException e) {

		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		}

	}

	@Test
	public void testSacarDineroTC_4() {
		try {
			this.tcPepe.sacarDinero(3000, 100);
			fail("Esperaba mporteInvalidoException");
		} catch (PinInvalidoException e) {

		} catch (SaldoInsuficienteException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (TarjetaBloqueadaException e) {
			fail("Se ha producido ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
			fail("Se ha producido ImporteInvalidoException");
		}

	}

	@Test
	public void confirmarCompraPorInternet() {
		try {
			int token = this.tcPepe.comprarPorInternet(tcPepe.getPin(), 300);
			this.tcPepe.confirmarCompraPorInternet(-2);
			fail("Se  esperaba TokenInvalido");
		} catch (TokenInvalidoException e) {

		} catch (Exception e) {
			fail("Se  esperaba TokenInvalido");
		}

	}

	public void confirmarCompraPorInternet_2() {
		try {
			int token = this.tcPepe.comprarPorInternet(tcPepe.getPin(), 300);
			this.tcPepe.confirmarCompraPorInternet(2);
			fail("Se esperaba TokenInvalido");
		} catch (TokenInvalidoException e) {

		} catch (Exception e) {
			fail("Se esperaba TokenInvalido");
		}

	}

	public void confirmarCompraPorInternet_3() {
		try {
			int token = this.tcPepe.comprarPorInternet(tcPepe.getPin(), 300);
			int t = token;
			this.tcPepe.confirmarCompraPorInternet(t);
			assertTrue(token == t);
		} catch (Exception e) {
			fail("Excepcion inseperada " + e.getMessage());
		}

	}

	public void confirmarCompraPorInternet_4() {
		try {
			int token = this.tcPepe.comprarPorInternet(tcPepe.getPin(), 300);
			this.tcPepe.confirmarCompraPorInternet(1000);
			fail("Se  esperaba TokenInvalido");
		} catch (TokenInvalidoException e) {

		} catch (Exception e) {
			fail("Se  esperaba TokenInvalido");
		}
  }
	
	@Test
  public void testSacarDineroTD_1() {
		try {
			this.tdAna.sacarDinero(-1500, 500);
			fail("Esperaba PinInvalidoException");
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Se esperaba PinInvalidoException");
		}
	}

	@Test
	public void testSacarDineroTD_2() {
		try {
			this.tdAna.sacarDinero(1111, 500);
			fail("Esperaba PinInvalidoException");
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Se esperaba PinInvalidoException");
		}
	}

	@Test
	public void testSacarDineroTD_3() {
		try {
			this.tdAna.sacarDinero(1234, -1000);
			fail("Esperaba ImporteInvalidoException");
		} catch (ImporteInvalidoException e) {
		} catch (Exception e) {
			fail("Se esperaba ImporteInvalidoException");
		}
	}

	@Test
	public void testSacarDineroTD_4() {
		try {
			this.tdAna.sacarDinero(5632, 500);
			fail("Esperaba PinInvalidoException");
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Se esperaba PinInvalidoException");
		}

	}

	@Test
	public void testSacarDineroTD_5() {
		try {
			this.tdAna.sacarDinero(1234, 5500);
			fail("Se esperaba SaldoInsuficienteException");
		} catch (SaldoInsuficienteException e) {
		} catch (Exception e) {
			fail("Se esperaba SaldoInsuficienteException");
		}

	}
	
	@Test
	public void testCambiarPinTD_1() {
		try {
			this.tdAna.cambiarPin(-1500,2654);
			fail("Se esperaba PinInvalidoException");
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Se esperaba PinInvalidoException");
    }
  }

  public void testTransferencia_1() {
		try {
			this.cuentaPepe.transferir(-2l, 250.00, "Lo que sea");
			fail("Esperaba CuentaInvalidaException");
		}catch(CuentaInvalidaException e) {
		}catch(Exception e) {
			fail("Esperaba CuentaInvalidaException");
		}
	}
	
	@Test
	public void testTransferencia_2() {
		try {
			this.cuentaPepe.transferir(1l, 250.00, "Lo que sea");
			fail("Esperaba CuentaInvalidaException");
		}catch(CuentaInvalidaException e) {
		}catch(Exception e) {
			fail("Esperaba CuentaInvalidaException");
		}
	}
	
	
	@Test
	public void testTransferencia_3() {
		try {
			this.cuentaPepe.transferir(this.cuentaAna.getId(), -1.00, "Lo que sea"); //this.cuentaAna.getId() == 2l
			fail("Esperaba ImporteInvalidoException");
		}catch(ImporteInvalidoException e) {
		}catch(Exception e) {
			fail("Esperaba ImporteInvalidoException");
		}
	}
	
	@Test
	public void testTransferencia_4() {
		try {
			this.cuentaPepe.transferir(this.cuentaAna.getId(), 250.00, "Lo que sea"); //this.cuentaAna.getId() == 2l
            assertTrue(this.cuentaPepe.getSaldo()==(1000.00 - 250.00 - 250.00*0.01));
            assertTrue(this.cuentaAna.getSaldo()==(5000.00 + 250.00));
		}catch(Exception e) {
			fail("Excepción inesperada: " + e.getMessage());
		}
	}
	
	@Test
	public void testTransferencia_5() {
		try {
			this.cuentaPepe.transferir(this.cuentaAna.getId(), 1100.00, "Lo que sea"); //this.cuentaAna.getId() == 2l
			fail("Se esperaba SaldoInsuficienteException");
		}catch(SaldoInsuficienteException e){
		}catch(Exception e) {
			fail("Se esperaba SaldoInsuficienteException");
		}
	}
	
	@Test
	public void testTransferencia_6() {
		try {
			this.cuentaPepe.transferir(this.cuentaAna.getId(), 0.00, "Lo que sea"); //this.cuentaAna.getId() == 2l
			fail("Se esperaba ImporteInvalidoException");
		}catch(ImporteInvalidoException e){
		}catch(Exception e) {
			fail("Se esperaba ImporteInvalidoException");
		}
	}
	
	@Test
	public void testCambiarPinTD_2() {
		try {
			this.tdAna.cambiarPin(1111, 2654);
			fail("Se esperaba PinInvalidoException");
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Se esperaba PinInvalidoException");
		}
	}
	
	@Test
	public void testCambiarPinTD_3() {
		try {
			this.tdAna.cambiarPin(1234, -1548);
			fail("Se esperaba PinInvalidoException");
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Se esperaba PinInvalidoException");
		}
	}
	
	@Test
	public void testCambiarPinTD_4() {
		try {
			this.tdAna.cambiarPin(5632, 2654);
			fail("Se esperaba PinInvalidoException");
		} catch (PinInvalidoException e) {
		} catch (Exception e) {
			fail("Se esperaba PinInvalidoException");
		}	
	}

	public void testTransferencia_7() {
		try {
			this.cuentaPepe.transferir(3l, 250.00, "Lo que sea"); //this.cuentaAna.getId() == 2l
			fail("Se esperaba CuentaInvalidaException");
		}catch(CuentaInvalidaException e){
		}catch(Exception e) {
			fail("Se esperaba CuentaInvalidaException");
		}
	}
	
	@Test 
	public void testIngresar_1() {
		try {
			this.cuentaPepe.ingresar(-100.00);
			fail("Se esperaba ImporteInvalidoException");
		}catch(ImporteInvalidoException e){
		}catch(Exception e) {
			fail("Se esperaba ImporteInvalidoException");
		}
	}
	
	@Test 
	public void testIngresar_2() {
		try {
			this.cuentaPepe.ingresar(3000.00);
			assertTrue(this.cuentaPepe.getSaldo() == 4000.00);
		}catch(Exception e) {
			fail("Excepción inesperada: " + e.getMessage());
		}
	}
	
	@Test 
	public void testIngresar_3() {
		try {
			this.cuentaPepe.ingresar(0.00);
			fail("Se esperaba ImporteInvalidoException");
		}catch(ImporteInvalidoException e){
		}catch(Exception e) {
			fail("Se esperaba ImporteInvalidoException");
		}
	}

	
	@Test
	public void testComprarPorInternet_1() {
		try {
			this.tdPepe.comprarPorInternet(-1000, 250);
			fail("Se esperaba PinInvalidoException");
		}catch(PinInvalidoException e){
			
		}catch(Exception e) {
			fail("Se esperaba PinInvalidoException");
		}
	}
	
	@Test
	public void testComprarPorInternet_2() {
		try {
			this.tdPepe.comprarPorInternet(293, 250);
			fail("Se esperaba PinInvalidoException");
		}catch(PinInvalidoException e){
			
		}catch(Exception e) {
			fail("Se esperaba PinInvalidoException");
		}
	}
	
	@Test
	public void testComprarPorInternet_3() {
		try {
			this.tdPepe.comprarPorInternet(1234, 250);			
		}catch(Exception e) {
			fail("Excepcion inesperada: " + e.getMessage());
		}
	}
	
	@Test
	public void testComprarPorInternet_4() {
		
		try {
			this.tdPepe.comprarPorInternet(9999, 250);
			fail("Se esperaba PinInvalidoException");
		}catch(PinInvalidoException e){
			
		}catch(Exception e) {
			fail("Se esperaba PinInvalidoException");
		}		
		try {
			this.tdPepe.comprarPorInternet(9999, 250);
			fail("Se esperaba PinInvalidoException");
		}catch(PinInvalidoException e){
			
		}catch(Exception e) {
			fail("Se esperaba PinInvalidoException");
		}
		try {
			this.tdPepe.comprarPorInternet(9999, 250);
			fail("Se esperaba PinInvalidoException");
		}catch(PinInvalidoException e){
			
		}catch(Exception e) {
			fail("Se esperaba PinInvalidoException");
		}
		
		try {
			this.tdPepe.comprarPorInternet(1234, 250);
			fail("Se esperaba TarjetaBloqueadaException");
		}catch(TarjetaBloqueadaException e){
			
		}catch(Exception e) {
			fail("Se esperaba TarjetaBloqueadaException");
		}
	}
	
	@Test
	public void testComprarPorInternet_5() {
		try {
			this.tdPepe.comprarPorInternet(1234, -1);
		}catch(Exception e) {
			fail("Excepcion inesperada: " + e.getMessage());
		}
	}
	
	@Test
	public void testComprarPorInternet_6() {
		try {
			this.tdPepe.comprarPorInternet(1234, 1100);
		}catch(Exception e) {
			fail("Excepcion inesperada: " + e.getMessage());
		}
	}
	
	@Test
	public void emitirTarjetaCredito_1() {
		try {
			TarjetaCredito tc = this.cuentaPepe.emitirTarjetaCredito("12345X",-100);
			assertTrue(tc.getCredito() == -100);
		}catch(Exception e) {
			fail("Excepcion inesperada: " + e.getMessage());
		}
	}
	
	@Test
	public void emitirTarjetaCredito_2() {
		try {
			TarjetaCredito tc = this.cuentaPepe.emitirTarjetaCredito("12345X",250);
			assertTrue(tc.getCredito() == 250);
		}catch(Exception e) {
			fail("Excepcion inesperada: " + e.getMessage());
		}
	}
	
	@Test
	public void emitirTarjetaCredito_3() {
		try {
			TarjetaCredito tc = this.cuentaPepe.emitirTarjetaCredito("98765F",250);
			fail("Se esperaba ClienteNoAutorizadoException");
		}catch(ClienteNoAutorizadoException e) {
		}catch(Exception e) {
			fail("Se esperaba ClienteNoAutorizadoException");
		}
	}
	
	@Test
	public void emitirTarjetaCredito_4() {
		try {
			TarjetaCredito tc = this.cuentaPepe.emitirTarjetaCredito("91919S",250);
			fail("Se esperaba ClienteNoEncontradoException");
		}catch(ClienteNoEncontradoException e) {
		}catch(Exception e) {
			fail("Se esperaba ClienteNoEncontradoException");
		}
	}
	
}
