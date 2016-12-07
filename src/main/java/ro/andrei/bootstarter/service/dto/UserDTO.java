package ro.andrei.bootstarter.service.dto;

import static ro.andrei.bootstarter.support.Constants.USERNAME_REGEX;

import java.util.Set;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import ro.andrei.bootstarter.domain.User;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO {

	@Pattern(regexp = USERNAME_REGEX)
	@Size(min = 1, max = 50)
	private String username;

	@Size(max = 50)
	private String firstName;

	@Size(max = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 100)
	private String email;

	private boolean activated = false;

	private Set<String> authorities;

	public UserDTO() {
	}

	public UserDTO(User user, Set<String> authorities) {

		this.username = user.getUsername();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.activated = user.getActive();
		this.authorities = authorities;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public boolean isActivated() {
		return activated;
	}

	public Set<String> getAuthorities() {
		return authorities;
	}

	@Override
	public String toString() {
		return "UserDTO{" + "username='" + username + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' + 
				", email='" + email + '\'' +
				", activated=" + activated + '\'' +
				", authorities=" + authorities +
				"}";
	}
}
