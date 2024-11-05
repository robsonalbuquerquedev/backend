package br.edu.ifpe.manager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Mapeia todas as rotas que começam com /api/
                .allowedOrigins("http://localhost:5173") // Permite solicitações dessas origens
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite os métodos
                .allowedHeaders("*") // Permite todos os cabeçalhos
                .allowCredentials(true); // Permite credenciais
    }
}
