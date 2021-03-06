package ro.andrei.bootstarter.service;

import java.util.List;

import ro.andrei.bootstarter.domain.support.BaseEntity;

/**
 * Provides basic CRUD operations for all services extending it
 */
public interface EntityService<T extends BaseEntity>{
	
	T find(Long id);
	
	T create(T entity);
	
	T update(T entity);
	
	T delete(Long id);
	
	List<T> findAll();
}
