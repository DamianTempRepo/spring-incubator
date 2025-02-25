package entelect.training.incubator.spring.booking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("{noop}pass").roles("USER");
        auth.inMemoryAuthentication().withUser("loyalty_user").password("{noop}pass").roles("LOYALTY_USER");
        auth.inMemoryAuthentication().withUser("admin").password("{noop}pass").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // !!! Disclaimer: NEVER DISABLE CSRF IN PRODUCTION !!!
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/bookings/**").hasAnyRole("USER", "LOYALTY_USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/bookings/**").hasAnyRole("USER", "LOYALTY_USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/bookings/api-docs").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/swagger-ui/**").hasAnyRole("ADMIN")
                .anyRequest().denyAll()
                .and()
                .httpBasic();
    }
}
