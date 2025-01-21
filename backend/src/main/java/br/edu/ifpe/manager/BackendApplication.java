package br.edu.ifpe.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/*import br.edu.ifpe.manager.config.ConfigTest;*/

/*import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;*/

@SpringBootApplication
@EnableScheduling
public class BackendApplication /*implements CommandLineRunner*/ {

    /*@Autowired
    private ConfigTest configTest;*/

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    /*@Override
    public void run(String... args) throws Exception {
        // Chama o método que imprime o valor da chave secreta
        configTest.printSecret();
    }*/
}
