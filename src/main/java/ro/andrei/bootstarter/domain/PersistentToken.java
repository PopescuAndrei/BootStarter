package ro.andrei.bootstarter.domain;

import static ro.andrei.bootstarter.domain.support.DbNames.IP_ADDRESS;
import static ro.andrei.bootstarter.domain.support.DbNames.TOKEN_DATE;
import static ro.andrei.bootstarter.domain.support.DbNames.TOKEN_VALUE;
import static ro.andrei.bootstarter.domain.support.DbNames.USER_AGENT;
import static ro.andrei.bootstarter.support.Constants.MAX_USER_AGENT_LENGTH;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ro.andrei.bootstarter.domain.support.DbNames;

/**
 * Persistent tokens are used by Spring Security to automatically log in users.
 *
 * @see ro.andrei.starter.security.CustomPersistentRememberMeServices
 */

@Entity
@Table(name = DbNames.PERSISTENT_TOKEN)
public class PersistentToken implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String series;

    @JsonIgnore
    @NotNull
    @Column(name = TOKEN_VALUE, nullable = false)
    private String tokenValue;
    
    @Column(name = TOKEN_DATE)
    private LocalDate tokenDate;

    @Size(min = 0, max = 39)
    @Column(name = IP_ADDRESS, length = 39)
    private String ipAddress;

    @Column(name = USER_AGENT)
    private String userAgent;

    @JsonIgnore
    @ManyToOne
    private User user;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public LocalDate getTokenDate() {
        return tokenDate;
    }

    public void setTokenDate(LocalDate tokenDate) {
        this.tokenDate = tokenDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        if (userAgent.length() >= MAX_USER_AGENT_LENGTH) {
            this.userAgent = userAgent.substring(0, MAX_USER_AGENT_LENGTH - 1);
        } else {
            this.userAgent = userAgent;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersistentToken that = (PersistentToken) o;

        if (!series.equals(that.series)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return series.hashCode();
    }

    @Override
    public String toString() {
        return "PersistentToken{" +
            "series='" + series + '\'' +
            ", tokenValue='" + tokenValue + '\'' +
            ", tokenDate=" + tokenDate +
            ", ipAddress='" + ipAddress + '\'' +
            ", userAgent='" + userAgent + '\'' +
            "}";
    }
}
