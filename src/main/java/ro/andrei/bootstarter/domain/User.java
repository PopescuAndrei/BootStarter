package ro.andrei.bootstarter.domain;

import static ro.andrei.bootstarter.domain.support.DbNames.ACTIVE;
import static ro.andrei.bootstarter.domain.support.DbNames.APP_USER;
import static ro.andrei.bootstarter.domain.support.DbNames.AUTHORITY_NAME;
import static ro.andrei.bootstarter.domain.support.DbNames.EMAIL;
import static ro.andrei.bootstarter.domain.support.DbNames.FIRST_NAME;
import static ro.andrei.bootstarter.domain.support.DbNames.ID;
import static ro.andrei.bootstarter.domain.support.DbNames.LAST_LOGIN_DATE;
import static ro.andrei.bootstarter.domain.support.DbNames.LAST_NAME;
import static ro.andrei.bootstarter.domain.support.DbNames.NAME;
import static ro.andrei.bootstarter.domain.support.DbNames.PASSWORD;
import static ro.andrei.bootstarter.domain.support.DbNames.USER_AUTHORITY;
import static ro.andrei.bootstarter.domain.support.DbNames.USER_ID;
import static ro.andrei.bootstarter.domain.support.DbNames.USER_NAME;
import static ro.andrei.bootstarter.support.Constants.USERNAME_REGEX;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.andrei.bootstarter.domain.support.BaseEntity;

@Entity
@Table(name= APP_USER)
public class User extends BaseEntity{

private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

    @NotNull
    @Pattern(regexp = USERNAME_REGEX)
	@Column(name = USER_NAME, nullable = false, unique = true, length = 100)
	private String username;
    
    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
	@Column(name = PASSWORD, nullable = false, length = 60)
	private String password;
    
	@Size(max = 100)
	@Column(name = FIRST_NAME, length = 59)
	private String firstName;

	@Size(max = 100)
	@Column(name = LAST_NAME, length = 50)
	private String lastName;

	@Column(name = EMAIL, unique = true)
	private String email;

    @NotNull
	@Column(name = ACTIVE, length = 1)
	private Boolean active = false;

	@Column(name = LAST_LOGIN_DATE, nullable = true)
	private ZonedDateTime lastLoginDate;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = USER_AUTHORITY, joinColumns = { @JoinColumn(name = USER_ID, referencedColumnName = ID, nullable = false) }, 
		inverseJoinColumns = {@JoinColumn(name = AUTHORITY_NAME, referencedColumnName = NAME, nullable = false) })
	private Set<Authority> authorities = new HashSet<>();
	
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private Set<PersistentToken> persistentTokens = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username.toLowerCase();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public ZonedDateTime getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(ZonedDateTime lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public Set<PersistentToken> getPersistentTokens() {
		return persistentTokens;
	}

	public void setPersistentTokens(Set<PersistentToken> persistentTokens) {
		this.persistentTokens = persistentTokens;
	}

	@Override

	public void update(BaseEntity entity) {
		if (!(entity instanceof User)) {
			throw new IllegalArgumentException("entity should be a User");
		}
		User user = (User) entity;
		setUsername(user.getUsername());
		setPassword(user.getPassword());
		setEmail(user.getEmail());
		setFirstName(user.getFirstName());
		setLastName(user.getLastName());
		setActive(user.getActive());
	}
}
