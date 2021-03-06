# spring-boot-spring-security-thymeleaf

Overview:

This project demonstrates spring security implementation using spring-boot and tymeleaf as viwe resolver. There are two types of users availabel in application : 1). Adming 2). User (Normal user). Based on the user type URL access is allowed to user. User with Admin role can only access user pattern */admin/** and user with USER role can access */user/**. Normal user can not access /admin/** url pattern. If un-authorized user try to access URL it will be redirected to custome access-denied page.

Spring security configuration is provided in `SpringSecurityConfig` file. 


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
        		.headers().frameOptions().disable();
                http.authorizeRequests()
                .antMatchers("/", "/home", "/about").permitAll()
                .antMatchers("/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/user/**").hasAnyRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        
    }


There are two types of authentication mechanism are provided 1). DAOAuthentication and 2). LDAPAuthentication. For these two we have extednded AuthenticationProvider implementation of their respective appropriate classes. Which type of authentication mechanism will be used are described by `authentication.type` property in application.properties (i.e `authentication.type=OAUTH` for DOA and `authentication.type=LDAP` for LDAP).
