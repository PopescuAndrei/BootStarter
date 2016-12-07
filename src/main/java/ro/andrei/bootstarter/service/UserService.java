package ro.andrei.bootstarter.service;

import java.util.Optional;

import ro.andrei.bootstarter.domain.User;

/**
 * Service class for managing users.
 */
public interface UserService extends EntityService<User>{
 
	User updateUserWithAuthorities(User user);
	
	Optional<User> getUserWithAuthoritiesByUsername(String username);
	
	User getUserWithAuthorities(Long id);
	
	User getUserWithAuthorities();

	void changePassword(String password);	
}