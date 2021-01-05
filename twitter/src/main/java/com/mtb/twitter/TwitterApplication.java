package com.mtb.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.mtb.twitter.entities.Tweet;
import com.mtb.twitter.entities.User;
import com.mtb.twitter.entities.UserCredentials;
import com.mtb.twitter.entities.UserProfile;
import com.mtb.twitter.repositories.HashtagRepository;
import com.mtb.twitter.repositories.TweetRepository;
import com.mtb.twitter.repositories.UserRepository;

@SpringBootApplication
public class TwitterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner demo(UserRepository userRepository, TweetRepository tweetRepository,
//			HashtagRepository hashtagRepository) {
//		return (args) -> {
//			UserCredentials userCredentials = new UserCredentials("bubbugump", "shrimpstew");
//			UserProfile userProfile = new UserProfile("Bubba", "Gump", "bubbagumpshrimp@email.com", "555-5555");
//			User user = new User(null, userCredentials, userProfile, null, null, null, false, null, null, null);
//			User returnedUser = userRepository.saveAndFlush(user);
//
//			Tweet tweet = new Tweet(null, returnedUser, null, "I love ice cream. #icecream", null, null, null, null,
//					null, null, null, 0L, false);
//
//			Tweet savedTweet = tweetRepository.saveAndFlush(tweet);
//			System.out.println(savedTweet.getId());
//
//			UserCredentials userCredentials2 = new UserCredentials("bobbo", "passwrd123");
//			UserProfile userProfile2 = new UserProfile("bobbo", "bombom", "bobbo@email.com", "555-5555");
//			User user2 = new User(null, userCredentials2, userProfile2, null, null, null, false, null, null, null);
//			User returnedUser2 = userRepository.saveAndFlush(user2);
//
//			returnedUser.setFollowers(new ArrayList<User>());
//			returnedUser.setFollowing(new ArrayList<User>());
//			returnedUser2.setFollowers(new ArrayList<User>());
//			returnedUser2.setFollowing(new ArrayList<User>());
//
//			returnedUser.getFollowers().add(returnedUser2);
//			returnedUser2.getFollowing().add(returnedUser);
//			userRepository.saveAndFlush(returnedUser);
//			userRepository.saveAndFlush(returnedUser2);
//
//			List<Tweet> allTweets = tweetRepository.getAllActiveTweets();
//			for (Tweet aTweet : allTweets) {
//				System.out.println(aTweet.getContent());
//			}
//		};
//	}
}
