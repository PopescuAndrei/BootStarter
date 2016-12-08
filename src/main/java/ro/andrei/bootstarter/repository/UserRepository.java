package ro.andrei.bootstarter.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ro.andrei.bootstarter.domain.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long>{

    User findOneByEmail(String email);
    
    User findOneByUsername(String username);
    
    @Query(value = "select distinct user from User user left join fetch user.authorities",
            countQuery = "select count(user) from User user")
    Page<User> findAllWithAuthorities(Pageable pageable);
}
