package com.soundintotext.srt_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@SpringBootApplication
public class SrtGeneratorApplication {

	public static void main(String[] args) throws IOException {
		extractTruststoreIfPresent();
		SpringApplication.run(SrtGeneratorApplication.class, args);
	}

	/**
	 * El truststore de Kafka va empaquetado dentro del jar (src/main/resources),
	 * pero Kafka necesita abrirlo como archivo real del sistema de ficheros.
	 * Lo extraemos a un archivo temporal antes de que arranque el contexto de Spring.
	 */
	private static void extractTruststoreIfPresent() throws IOException {
		try (InputStream in = SrtGeneratorApplication.class
				.getResourceAsStream("/aiven-ca-truststore.jks")) {

			if (in == null) {
				return; // no hay truststore empaquetado (p.ej. en local sin SSL), no hacemos nada
			}

			Path target = Paths.get(System.getProperty("java.io.tmpdir"), "aiven-ca-truststore.jks");
			Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);

			System.setProperty("KAFKA_TRUSTSTORE_LOCATION", "file:" + target.toAbsolutePath());
		}
	}
}
