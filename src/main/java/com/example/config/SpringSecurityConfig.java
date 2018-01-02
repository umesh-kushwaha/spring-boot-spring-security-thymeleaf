package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.example.service.UserService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${authentication.type}")
	private String authenticationType;

	@Value("${ldap.server}")
	private String server;

	@Value("${ldap.root_dn}")
	private String rootDn;

	@Value("${ldap.user_search_base}")
	private String userSearchBase;

	@Value("${ldap.user_search_filter}")
	private String userSearchFilter;

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

	@Autowired
	private LdapAuthenticationProvider ldapAuthenticationProvider;

	@Autowired
	private UserService userService;

	// roles admin allow to access /admin/**
	// roles user allow to access /user/**
	// custom 403 access denied handler
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().headers().frameOptions().disable();
		http.authorizeRequests().antMatchers("/", "/home", "/about").permitAll().antMatchers("/admin/**")
				.hasAnyRole("ADMIN").antMatchers("/user/**").hasAnyRole("USER").anyRequest().authenticated().and()
				.formLogin().loginPage("/login").permitAll().and().logout().permitAll().and().exceptionHandling()
				.accessDeniedHandler(accessDeniedHandler);

	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		if (AuthenticationType.LDAP.equals(AuthenticationType.valueOf(authenticationType))) {
			auth.authenticationProvider(ldapAuthenticationProvider);
		} else {
			auth.userDetailsService(userService);
			// auth.inMemoryAuthentication().withUser("hiren").password("password").roles("USER").and().withUser("admin")
			// .password("password").roles("ADMIN");
		}
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

	@Bean
	public LdapAuthenticator ldapAuthenticator() {
		DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(
				server + "/" + rootDn);
		// contextSource.setUserDn(ldapConfigParameters.getManagerDn());
		// contextSource.setPassword(ldapConfigParameters.getManagerPassword());
		contextSource.setReferral("follow");
		contextSource.setCacheEnvironmentProperties(true);
		contextSource.setAnonymousReadOnly(false);
		contextSource.setPooled(true);
		contextSource.afterPropertiesSet();
		BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
		bindAuthenticator.setUserDnPatterns(new String[] { userSearchFilter + "," + userSearchBase });
		return bindAuthenticator;
	}

	@Bean
	public LdapAuthenticationProvider ldapAuthenticationProvider(LdapAuthenticator ldapAuthenticator) {
		return new LdapAuthenticationProvider(ldapAuthenticator);
	}

}
