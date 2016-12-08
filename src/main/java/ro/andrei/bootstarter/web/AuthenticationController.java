package ro.andrei.bootstarter.web;

import static ro.andrei.bootstarter.web.support.AuthenticationUtils.checkPasswordLength;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ro.andrei.bootstarter.domain.PersistentToken;
import ro.andrei.bootstarter.domain.User;
import ro.andrei.bootstarter.service.PersistentTokenService;
import ro.andrei.bootstarter.service.UserService;
import ro.andrei.bootstarter.web.support.HeaderUtil;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

	private final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PersistentTokenService persistentTokenService;

	@RequestMapping(path = "/register", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<?> registerAccount(@Valid @RequestBody User user) {
		HttpHeaders textPlainHeaders = new HttpHeaders();
		textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);
		
		User existing = userService.findUserWithAuthoritiesByUsername(user.getUsername().toLowerCase());
		if(existing !=null) {
			return new ResponseEntity<>("Username already in use", textPlainHeaders, HttpStatus.BAD_REQUEST);
		}
		
		existing = userService.findUserByEmail(user.getEmail());
		
		if(existing !=null) {
			return new ResponseEntity<>("Email already in use", textPlainHeaders, HttpStatus.BAD_REQUEST);
		}
		
		user.setActive(true);
		user = userService.create(user);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the username if the user is authenticated
     */
	@RequestMapping(path = "/authenticate", method = RequestMethod.GET)
	public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
	}
	
	@RequestMapping(path = "/account", method = RequestMethod.GET)
	public ResponseEntity<User> getAccount() {
		User user = userService.findUserWithAuthorities();
		if(user!=null) {
			return new ResponseEntity<>(user, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(path = "/account", method = RequestMethod.POST)
	public ResponseEntity<String> saveAccount(@Valid @RequestBody User user) {
		User existingUser = userService.findUserByEmail(user.getEmail());
		if(existingUser != null && !existingUser.getUsername().equalsIgnoreCase(user.getUsername())){
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use")).body(null);
		}
		
		existingUser = userService.findUserWithAuthorities();
		if(existingUser != null) {
			existingUser.update(user);
			userService.updateUserWithAuthorities(existingUser);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping(path="/change_password", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<?> changepassword(@RequestBody String password) {
		if (!checkPasswordLength(password)) {
			return new ResponseEntity<>("Incorect Password", HttpStatus.BAD_REQUEST);
		}
		
		userService.changePassword(password);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(path="/account/sessions", method = RequestMethod.GET)
	public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
		User user = userService.findUserWithAuthorities();
		if(user!=null) {
			return new ResponseEntity<>(persistentTokenService.findByUser(user), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
    /**
     * DELETE  /account/sessions?series={series} : invalidate an existing session.
     *
     * - You can only delete your own sessions, not any other user's session
     * - If you delete one of your existing sessions, and that you are currently logged in on that session, you will
     *   still be able to use that session, until you quit your browser: it does not work in real time (there is
     *   no API for that), it only removes the "remember me" cookie
     * - This is also true if you invalidate your current session: you will still be able to use it until you close
     *   your browser or that the session times out. But automatic login (the "remember me" cookie) will not work
     *   anymore.
     *   There is an API to invalidate the current session, but there is no API to check which session uses which
     *   cookie.
     *
     * @param series the series of an existing session
     * @throws UnsupportedEncodingException if the series couldnt be URL decoded
     */
	@RequestMapping(path="/account/sessions/{series}")
	public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
		String decodedSeries = URLDecoder.decode(series,  "UTF-8");
		User user = userService.findUserWithAuthorities();
		if(user!=null) {
			for (PersistentToken persistentToken : persistentTokenService.findByUser(user)) {
				if(persistentToken.getSeries().equals(decodedSeries)){
					persistentTokenService.delete(decodedSeries);
				}
			}
		}
	}
}
