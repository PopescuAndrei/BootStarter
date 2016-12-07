package ro.andrei.bootstarter.service;

import static ro.andrei.bootstarter.security.AuthoritiesConstants.USER;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ro.andrei.bootstarter.domain.Authority;
import ro.andrei.bootstarter.domain.User;
import ro.andrei.bootstarter.repository.AuthorityRepository;
import ro.andrei.bootstarter.repository.BaseRepository;
import ro.andrei.bootstarter.repository.PersistentTokenRepository;
import ro.andrei.bootstarter.repository.UserRepository;
import ro.andrei.bootstarter.security.SecurityUtils;

@Service
public class UserServiceImpl extends EntityServiceImpl<User> implements UserService{
	
    public UserServiceImpl(BaseRepository<User, Long> repository) {
		super(repository);
	}

	private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private AuthorityRepository authorityRepository;
    
    @Override
    @Transactional
    public User create(User user) {
    	User newUser = user;
    	
        Authority authority = authorityRepository.findOne(USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setActive(true);
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
    
    @Override
    @Transactional
    public User updateUserWithAuthorities(User updated) {
    	User user = userRepository.findOne(updated.getId());
        
    	user.setUsername(updated.getUsername());
    	user.setFirstName(updated.getFirstName());
        user.setLastName(updated.getLastName());
        user.setEmail(updated.getEmail());
        user.setActive(updated.getActive());
        Set<Authority> managedAuthorities = user.getAuthorities();
        managedAuthorities.clear();
        updated.getAuthorities().forEach(
        		authority -> managedAuthorities.add(authorityRepository.findOne(authority.getName()))
        );
        
        log.debug("Changed Information for User: {}", user);
        return user;
    }
    
    @Override
    @Transactional
    public void changePassword(String password) {
        userRepository.findOneByUsername(SecurityUtils.getCurrentUserUsername()).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
            log.debug("Changed password for User: {}", user);
        });
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByUsername(String username) {
        return userRepository.findOneByUsername(username).map(user -> {
            user.getAuthorities().size();
            return user;
        });
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        User user = userRepository.findOne(id);
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        Optional<User> optionalUser = userRepository.findOneByUsername(SecurityUtils.getCurrentUserUsername());
        User user = null;
        if (optionalUser.isPresent()) {
          user = optionalUser.get();
            user.getAuthorities().size(); // eagerly load the association
         }
         return user;
    }
    
    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = LocalDate.now();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token -> {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        });
    }
}