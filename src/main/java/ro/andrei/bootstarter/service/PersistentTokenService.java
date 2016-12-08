package ro.andrei.bootstarter.service;

import java.time.LocalDate;
import java.util.List;

import ro.andrei.bootstarter.domain.PersistentToken;
import ro.andrei.bootstarter.domain.User;

public interface PersistentTokenService {

    List<PersistentToken> findByUser(User user);

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);
    
    void delete(String series);
}
