package ro.andrei.bootstarter.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ro.andrei.bootstarter.domain.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long>{

    Optional<User> findOneByEmail(String email);
    
    Optional<User> findOneByUsername(String username);
    
    @Query(value = "select distinct user from User user left join fetch user.authorities",
            countQuery = "select count(user) from User user")
    Page<User> findAllWithAuthorities(Pageable pageable);
}
