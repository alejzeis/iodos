package xyz.ajann.iodos.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Configures SpringSecurity using
 * SpringBoot
 *
 * @author jython234
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers()
                .and().authorizeRequests().anyRequest().permitAll()
                .and().csrf().disable()
                .formLogin().disable()
                .sessionManagement().disable()
                .httpBasic().disable();
    }
}
