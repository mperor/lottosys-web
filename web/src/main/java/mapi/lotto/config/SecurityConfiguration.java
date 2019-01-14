package mapi.lotto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
//@Order(SecurityProperties.IGNORED_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.inMemoryAuthentication()
                .withUser("marfik007").password("adidas007").roles("ADMIN").and();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers("/").permitAll().and()
		.authorizeRequests().antMatchers("/random").permitAll().and()
		.authorizeRequests().antMatchers("/static").permitAll().and()
		.authorizeRequests().antMatchers("/math").permitAll().and()
		.authorizeRequests().antMatchers("/math-stats").permitAll().and()
		.authorizeRequests().antMatchers("/static-stats").permitAll().and()
		.authorizeRequests().antMatchers("/random-stats").permitAll().and()
  //              .authorizeRequests().antMatchers("/add/**").hasRole("ADMIN").and()
                .authorizeRequests().anyRequest().authenticated().and()
                .formLogin().loginPage("/login").permitAll().and()
                .httpBasic();
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }

}
