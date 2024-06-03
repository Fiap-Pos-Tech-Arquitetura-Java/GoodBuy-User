package br.com.fiap.postech.goodbuy.user.security;

import br.com.fiap.postech.goodbuy.user.entity.User;
import br.com.fiap.postech.goodbuy.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByLogin(userName);
		if (user.isPresent()) {
			return new UserDetailsImpl(user.get());
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
	}
}