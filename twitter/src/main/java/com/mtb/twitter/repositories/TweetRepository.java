package com.mtb.twitter.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mtb.twitter.entities.Hashtag;
import com.mtb.twitter.entities.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

	// In JPQL, you are operating on Java objects, not tables. JPQL takes care of
	// the table stuff behind the scenes.
	@Query("SELECT t FROM Tweet t WHERE t.author.isDeleted = false AND t.isDeleted = false")
	List<Tweet> getAllActiveTweets();
	
	List<Tweet> findAllByIsDeletedFalseAndAuthorIsDeletedFalseOrderByPostedAtDesc();
	
	List<Tweet> findAllByIsDeletedFalseAndAuthorUserCredentialsUsername(String username);
	
	List<Tweet> findAllByIsDeletedFalseAndAuthorUserCredentialsUsernameOrderByPostedAtDesc(String username);
	
	List<Tweet> findAllByIsDeletedFalseAndHashtagsContainsOrderByPostedAtDesc(Hashtag hashtag);
}