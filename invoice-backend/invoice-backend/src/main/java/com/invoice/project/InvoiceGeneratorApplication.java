package com.invoice.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InvoiceGeneratorApplication {

	public static void main(String[] args) {
        SpringApplication.run(InvoiceGeneratorApplication.class, args);

        System.out.println("Hellow world");
	}
}
