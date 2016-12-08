package ro.andrei.bootstarter.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ro.andrei.bootstarter.domain.PersistentToken;
import ro.andrei.bootstarter.domain.User;
import ro.andrei.bootstarter.repository.PersistentTokenRepository;

@Service
public class PersistentTokenServiceImpl implements PersistentTokenService{

	@Autowired
	private PersistentTokenRepository repository;
	
	@Override
	public List<PersistentToken> findByUser(User user) {
		return repository.findByUser(user);
	}

	@Override
	public List<PersistentToken> findByTokenDateBefore(LocalDate localDate) {
		return repository.findByTokenDateBefore(localDate);
	}

	@Override
	public void delete(String series) {
		repository.delete(series);
	}


}
