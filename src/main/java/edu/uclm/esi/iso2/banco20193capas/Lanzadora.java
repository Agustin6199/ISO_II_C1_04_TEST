package edu.uclm.esi.iso2.banco20193capas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import edu.uclm.esi.iso2.banco20193capas.model.Cliente;
import edu.uclm.esi.iso2.banco20193capas.model.Cuenta;

@SpringBootApplication
public class Lanzadora {
	final static int number = 1000;
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Lanzadora.class, args);
		try {
			Cliente pepe = new Cliente("12345X", "Pepe", "Pérez");
			pepe.insert();
			Cuenta cuenta = new Cuenta();
			cuenta.addTitular(pepe);
			cuenta.insert();
			cuenta.ingresar(number);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
