

package myProject.Traineeship_security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http
	        .authorizeHttpRequests(authz -> authz
	        	.requestMatchers("/role-select", "/register/redirect", "/register-student","/register-professor", "/register-professor", "/register-company", "/register-committee").permitAll() // επιτρέπει την πρόσβαση σε αυτές τις διαδρομές χωρίς authentication
	        	.requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
	        	.requestMatchers("/student_dashboard").permitAll()
	            .requestMatchers("/committee_dashboard").permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form
	        	    .loginPage("/login")
	        	    .loginProcessingUrl("/login")
	        	    .successHandler(customLoginSuccessHandler()) 
	        	    .permitAll()
	        )

	        .logout(logout -> logout
	            .logoutUrl("/logout")
	            .logoutSuccessUrl("/login?logout")
	            .permitAll()
	        );

	    return http.build();
	}

	@Bean
	public CustomLoginSuccessHandler customLoginSuccessHandler() {
	    return new CustomLoginSuccessHandler();
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();   
    }
}
