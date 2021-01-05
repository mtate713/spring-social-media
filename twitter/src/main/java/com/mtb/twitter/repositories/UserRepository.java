package com.mtb.twitter.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mtb.twitter.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserCredentialsUsername(String username);
	
	Optional<User> findByUserProfileEmail(String email);
	
	List<User> findAllByIsDeletedFalse();

//	// In JPQL, you are operating on Java objects, not tables. JPQL takes care of the table stuff behind the scenes.
//	@Query("SELECT u FROM User u WHERE u.credentials.username = ?1")
//	User getByUsername(String username);
//	
//	Optional<User> findByProfileEmail(String email);
}