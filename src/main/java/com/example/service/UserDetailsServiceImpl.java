package com.example.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entity.User;
import com.example.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	private Collection<GrantedAuthority> grantedAuthorities;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsernameIgnoreCaseAndIsActiveTrueAndIsDeletedFalse(username);

		if (user == null) {
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		} else if (!user.getIsActive()) {
			try {
				throw new Exception("User " + user + " is not activated");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		grantedAuthorities = new ArrayList<>();
		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getRole());
		grantedAuthorities.add(grantedAuthority);


		// password expiry logic

		/*Long passwordExpiration = user.getPasswordExpirationDate();
		if (passwordExpiration != null) {
			Long currTimeStamp = System.currentTimeMillis();
			if (currTimeStamp < passwordExpiration) {
				System.out.println("-----Password not Expired----");
			} else {
				System.out.println("-------Password expired----");
				// throw new CustomInvalidRequestException("Your Credential has
				// been expired");
			}
		}*/

		return new org.springframework.security.core.userdetails.User(user.getUsername(),
				user.getPassword() == null ? "" : user.getPassword(), true, true, true, !user.getIsLocked(),
				grantedAuthorities);
	}

}
