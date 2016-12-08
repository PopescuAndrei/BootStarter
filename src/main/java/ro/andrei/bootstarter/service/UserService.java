package ro.andrei.bootstarter.service;

import ro.andrei.bootstarter.domain.User;

/**
 * Service class for managing users.
 */
public interface UserService extends EntityService<User>{
	
	User findUserWithAuthoritiesByUsername(String username);
	
	User findUserByEmail(String email);
	
	User findUserWithAuthorities(Long id);
	
	User findUserWithAuthorities();

	void changePassword(String password);	
	
	User updateUserWithAuthorities(User user);
}