package com.devonfw.module.security.common;

import java.security.Principal;
import java.util.Set;

import javax.inject.Inject;
import javax.security.auth.Subject;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class DummyUserDetailsService implements UserDetailsService {
	@Inject
	private KerberosConfigProperties kerbprop;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			ServiceSubjectFactory fact= new ServiceSubjectFactory(kerbprop);
			Subject subject=fact.login();
			Set<Principal> principals=subject.getPrincipals();
			for(Principal principal:principals) {
				if(principal.getName().equalsIgnoreCase(username)) {
					return new User(username, "abc@123", true, true, true, true,
							AuthorityUtils.createAuthorityList("ROLE_USER"));
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
