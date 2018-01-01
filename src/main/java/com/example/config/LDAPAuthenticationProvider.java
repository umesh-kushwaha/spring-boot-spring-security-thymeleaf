package com.example.config;

import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;

public class LDAPAuthenticationProvider extends LdapAuthenticationProvider {

	public LDAPAuthenticationProvider(LdapAuthenticator authenticator) {
		super(authenticator);
	}

}
