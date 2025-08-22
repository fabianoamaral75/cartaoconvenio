
package br.com.uaitagcartaoconvenio.cartaoconvenio.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.uaitagcartaoconvenio.cartaoconvenio.excecoes.ExceptionHandlerFilter;
import br.com.uaitagcartaoconvenio.cartaoconvenio.service.UsuarioService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebConfigSecurity {
    
    @Autowired
    private SecurityFilter securityFilter;
    
    @Autowired
    private ExceptionHandlerFilter exceptionHandlerFilter;
    
    @Configuration
    public class WebConfig implements WebMvcConfigurer {
      @Override
      public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
          .addResourceHandler("/images/**")
          .addResourceLocations("classpath:/static/images/");
        registry
          .addResourceHandler("/**")
          .addResourceLocations("classpath:/static/");
      }
      
      @Override
      public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
          PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
          resolver.setOneIndexedParameters(true); // Para começar a paginação em 1 ao invés de 0
          resolver.setFallbackPageable(PageRequest.of(0, 10));
          resolvers.add(resolver);
      }

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita CSRF para APIs REST (habilitar se for aplicação web com sessões)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configuração de autorização
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers(
                		"/auth/**"                          , 
                		"/v3/api-docs/**"                   , 
                		"/swagger-ui/**"                    ,
                        "/api/antecipacoes/*/aprovar"       ,
                        "/api/antecipacoes/*/reprovar"      ,
                        "/antecipacao/popup"                ,
                        "/antecipacao/resultado-antecipacao",     
                        "/images/**", "/css/**", "/js/**", "/webjars/**"
                  ).permitAll()
                
                // Endpoints de antecipação exigem autenticação
                .requestMatchers("/api/antecipacoes/**", "/api/relatorios-faturamento/**").authenticated()
                
                
                // Demais endpoints
                .anyRequest().permitAll()
            )
            
            // Configura autenticação básica (para APIs)
            .httpBasic(httpBasic -> httpBasic
                .realmName("CartaoConvenio API")
            )
            
            // Configura cabeçalhos de segurança
            .headers(headers -> headers
                .frameOptions().sameOrigin()
                .httpStrictTransportSecurity().disable()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(exceptionHandlerFilter, SecurityFilter.class)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            );
        
        return http.build();
    }
    
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UsuarioService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
}
