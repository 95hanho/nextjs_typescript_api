package me._hanho.nextjs_shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 비활성화 (REST API에서는 주로 비활성화)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/bapi/**").permitAll() // REST API 엔드포인트는 인증 없이 접근 가능
                .anyRequest().authenticated()
            )
            .formLogin(login -> login.disable()) // 폼 로그인 비활성화
            .httpBasic(basic -> basic.disable()); // 기본 인증 비활성화

        return http.build();
    }
}
