package com.mtb.twitter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mtb.twitter.entities.Hashtag;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

	Optional<Hashtag> findByLabel(String label);

//	// In JPQL, you are operating on Java objects, not tables. JPQL takes care of the table stuff behind the scenes.
//	@Query("SELECT u FROM User u WHERE u.credentials.username = ?1")
//	User getByUsername(String username);
//	
//	Optional<User> findByProfileEmail(String email);
}
