package com.dodo.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.dodo.system.service.HomeService;

/**
 * Author Sangwon Hyun on 2019-07-07
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private HomeService homeService;

    /* DaoAuthenticationProvider는 내부적으로 UserDetailsService를 이용해 사용자 정보를 읽는다.*/
    @Bean
    public DaoAuthenticationProvider authenticationProvider(HomeService homeService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(homeService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider(homeService));
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
        	.authorizeRequests()
	            .antMatchers("/","/login","/error","/login-fail","/valid-recaptcha").permitAll()
	            .antMatchers("/home").hasAnyAuthority("USER","ADMIN","MANAGER")
	            .antMatchers("/admin/**").hasAnyAuthority("ADMIN")
	            .antMatchers("/home/**").hasAnyAuthority("USER","MANAGER")
	            .anyRequest().authenticated()
	        .and()
	            .csrf().disable()
	            .formLogin()
	            .loginPage("/login")
	            .usernameParameter("id")
	            .passwordParameter("password")
	            .defaultSuccessUrl("/home")
	            .failureUrl("/login-fail")
	        .and()
	            .logout()
	            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	            .deleteCookies("JSESSIONID")
	        .and()                        
	            .exceptionHandling()
	            .accessDeniedPage("/error")
	        .and()
	        	.sessionManagement()
	        	.maximumSessions(1) //같은 아이디로 1명만 로그인
	        	.maxSessionsPreventsLogin(true) //false :신규 로그인은 허용, 기존 사용자는 세션 아웃  true: 이미 로그인한 세션이있으면 로그인 불가 
	        	.expiredUrl("/login"); //세션 아웃되면 이동할 url
        
    }//end http-config
    
}
