package com.mtb.twitter.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mtb.twitter.dtos.CredentialsRequestDto;
import com.mtb.twitter.dtos.TweetResponseDto;
import com.mtb.twitter.dtos.UserRequestDto;
import com.mtb.twitter.dtos.UserResponseDto;
import com.mtb.twitter.entities.Tweet;
import com.mtb.twitter.entities.User;
import com.mtb.twitter.exceptions.InvalidRequestException;
import com.mtb.twitter.mappers.TweetMapper;
import com.mtb.twitter.mappers.UserMapper;
import com.mtb.twitter.repositories.TweetRepository;
import com.mtb.twitter.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private UserRepository userRepository;
	private UserMapper userMapper;
	private TweetRepository tweetRepository;
	private TweetMapper tweetMapper;
	private ValidationService validationService;

	public ResponseEntity<List<UserResponseDto>> getUsers() {
		List<User> users = userRepository.findAllByIsDeletedFalse();
		return new ResponseEntity<List<UserResponseDto>>(userMapper.entitiesToResponseDtos(users), HttpStatus.OK);
	}

	public ResponseEntity<UserResponseDto> addUser(UserRequestDto user) {
		validationService.validateUserRequestDto(user);
		if (validationService.validateUsernameAvailable(user.getCredentials().getUsername())) {
			if (validationService.validateEmailAvailable(user.getProfile().getEmail())) {
				User newUser = userMapper.requestDtoToEntity(user);
				newUser.setIsDeleted(false);
				User returnedUser = userRepository.saveAndFlush(newUser);
				return new ResponseEntity<UserResponseDto>(userMapper.entityToResponseDto(returnedUser), HttpStatus.OK);
			} else {
				throw new InvalidRequestException("That email is already taken.");
			}

		} else {
			Optional<User> optionalExistingUser = userRepository
					.findByUserCredentialsUsername(user.getCredentials().getUsername());
			User existingUser = optionalExistingUser.get();
			if (existingUser.getIsDeleted() == true
					&& existingUser.getUserCredentials().getPassword().equals(user.getCredentials().getPassword())) {
				if (validationService.validateEmailAvailable(user.getProfile().getEmail())
						|| existingUser.getUserProfile().getEmail().equals(user.getProfile().getEmail())) {
					User userFromRequest = userMapper.requestDtoToEntity(user);
					existingUser.setUserCredentials(userFromRequest.getUserCredentials());
					existingUser.setUserProfile(userFromRequest.getUserProfile());
					existingUser.setIsDeleted(false);
					User returnedUser = userRepository.saveAndFlush(existingUser);
					return new ResponseEntity<UserResponseDto>(userMapper.entityToResponseDto(returnedUser),
							HttpStatus.OK);
				} else {
					throw new InvalidRequestException("That email is already taken.");
				}

			} else {
				throw new InvalidRequestException("That username is already taken.");
			}
		}

	}

	public ResponseEntity<UserResponseDto> getUser(String username) {
		Optional<User> optionalUser = userRepository.findByUserCredentialsUsername(username);
		if (optionalUser.isEmpty()) {
			throw new InvalidRequestException(String.format("No user with username %s exists.", username));
		} else {
			User user = optionalUser.get();
			if (user.getIsDeleted() == true) {
				throw new InvalidRequestException(String.format("No user with username %s exists.", username));
			} else {
				return new ResponseEntity<UserResponseDto>(userMapper.entityToResponseDto(user), HttpStatus.OK);
			}
		}
	}

	public ResponseEntity<UserResponseDto> updateUser(String username, UserRequestDto userRequestDto) {
		if (validationService.validateUsernameExists(username) == false) {
			throw new InvalidRequestException(String.format("No user with username %s exists.", username));
		}
		Optional<User> optionalUser = userRepository.findByUserCredentialsUsername(username);
		User user = optionalUser.get();
		if (user.getIsDeleted() == true) {
			throw new InvalidRequestException(String.format("No user with username %s exists.", username));
		}
		validationService.validateUserRequestDto(userRequestDto);
		User userFromCredentials = userMapper.requestDtoToEntity(userRequestDto);
		if (validationService.validateCredentials(username, userRequestDto.getCredentials())) {
			if (validationService.validateEmailAvailable(userRequestDto.getProfile().getEmail())
					|| user.getUserProfile().getEmail().equals(userRequestDto.getProfile().getEmail())) {
				user.setUserCredentials(userFromCredentials.getUserCredentials());
				user.setUserProfile(userFromCredentials.getUserProfile());
				User returnedUser = userRepository.saveAndFlush(user);
				return new ResponseEntity<UserResponseDto>(userMapper.entityToResponseDto(returnedUser), HttpStatus.OK);
			} else {
				throw new InvalidRequestException("That email is already taken.");
			}
		} else {
			throw new InvalidRequestException(String.format("Invalid credentials for username %s", username));
		}
	}

	public ResponseEntity<UserResponseDto> deleteUser(String username, CredentialsRequestDto credentialsRequestDto) {
		Optional<User> optionalUser = userRepository.findByUserCredentialsUsername(username);
		if (optionalUser.isEmpty() || optionalUser.get().getIsDeleted() == true) {
			throw new InvalidRequestException(String.format("No user with username %s exists.", username));
		}
		User user = optionalUser.get();
		validationService.validateCredentialsRequestDto(credentialsRequestDto);
		if (validationService.validateCredentials(username, credentialsRequestDto)) {
			user.setIsDeleted(true);
			userRepository.saveAndFlush(user);
			return new ResponseEntity<UserResponseDto>(userMapper.entityToResponseDto(user), HttpStatus.OK);
		} else {
			throw new InvalidRequestException(String.format("Invalid credentials for username %s", username));
		}
	}

	public ResponseEntity<Void> followUser(String username, CredentialsRequestDto credentialsRequestDto) {
		if (!validationService.validateUsernameExists(username)) {
			throw new InvalidRequestException(String.format("No user with username %s exists.", username));
		}
		if (validationService.validateCredentials(credentialsRequestDto)) {
			Optional<User> optionalFollowedUser = userRepository.findByUserCredentialsUsername(username);
			Optional<User> optionalFollowerUser = userRepository.findByUserCredentialsUsername(credentialsRequestDto.getUsername());
			User followed = optionalFollowedUser.get();
			User follower = optionalFollowerUser.get();
			if (followed.getIsDeleted() == true) {
				throw new InvalidRequestException(String.format("No user with username %s exists.", username));
			}
			if (follower.getIsDeleted() == true) {
				throw new InvalidRequestException(String.format("No user with username %s exists.", follower.getUserCredentials().getUsername()));
			}
			if (followed.getFollowers() == null) {
				followed.setFollowers(new ArrayList<User>());
			}
			if (follower.getFollowing() == null) {
				followed.setFollowing(new ArrayList<User>());
			}
			if (followed.getFollowers().contains(follower)) {
				throw new InvalidRequestException(String.format("User %s is already following user %s.", follower.getUserCredentials().getUsername(), followed.getUserCredentials().getUsername()));
			}
			
			follower.getFollowing().add(followed);
			followed.getFollowers().add(follower);
			userRepository.saveAndFlush(followed);
			userRepository.saveAndFlush(follower);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			throw new InvalidRequestException("Invalid credentials for username " + credentialsRequestDto.getUsername() + ".");
		}
	}

	public ResponseEntity<Void> unfollowUser(String username, CredentialsRequestDto credentialsRequestDto) {
		if (!validationService.validateUsernameExists(username)) {
			throw new InvalidRequestException(String.format("No user with username %s exists.", username));
		}
		if (validationService.validateCredentials(credentialsRequestDto)) {
			Optional<User> optionalFollowedUser = userRepository.findByUserCredentialsUsername(username);
			Optional<User> optionalFollowerUser = userRepository.findByUserCredentialsUsername(credentialsRequestDto.getUsername());
			User followed = optionalFollowedUser.get();
			User follower = optionalFollowerUser.get();
			if (followed.getIsDeleted() == true) {
				throw new InvalidRequestException(String.format("No user with username %s exists.", username));
			}
			if (follower.getIsDeleted() == true) {
				throw new InvalidRequestException(String.format("No user with username %s exists.", follower.getUserCredentials().getUsername()));
			}
			if (followed.getFollowers() == null) {
				followed.setFollowers(new ArrayList<User>());
			}
			if (follower.getFollowing() == null) {
				followed.setFollowing(new ArrayList<User>());
			}
			if (!followed.getFollowers().contains(follower)) {
				throw new InvalidRequestException(String.format("User %s is not following user %s.", follower.getUserCredentials().getUsername(), followed.getUserCredentials().getUsername()));
			}
			
			follower.getFollowing().remove(followed);
			followed.getFollowers().remove(follower);
			userRepository.saveAndFlush(followed);
			userRepository.saveAndFlush(follower);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			throw new InvalidRequestException("Invalid credentials for username " + credentialsRequestDto.getUsername() + ".");
		}
	}

	public ResponseEntity<List<TweetResponseDto>> getUserFeed(String username) {
		User user = validationService.validateUserExistsAndNotDeleted(username);
		List<Tweet> tweets = new ArrayList<Tweet>();
		List<Tweet> userTweets = tweetRepository.findAllByIsDeletedFalseAndAuthorUserCredentialsUsername(username);
		tweets.addAll(userTweets);
		if (user.getFollowing() != null) {
			for (User followed : user.getFollowing()) {
				if (followed.getIsDeleted() == false) {
					tweets.addAll(tweetRepository.findAllByIsDeletedFalseAndAuthorUserCredentialsUsername(followed.getUserCredentials().getUsername()));
				}
			}
		}
		Collections.sort(tweets, (a, b) -> b.getPostedAt().compareTo(a.getPostedAt()));
		return new ResponseEntity<List<TweetResponseDto>>(tweetMapper.entitiesToResponseDtos(tweets), HttpStatus.OK);
		
	}

	public ResponseEntity<List<TweetResponseDto>> getUserTweets(String username) {
		validationService.validateUserExistsAndNotDeleted(username);
		List<Tweet> tweets = tweetRepository.findAllByIsDeletedFalseAndAuthorUserCredentialsUsernameOrderByPostedAtDesc(username);
		return new ResponseEntity<List<TweetResponseDto>>(tweetMapper.entitiesToResponseDtos(tweets), HttpStatus.OK);
	}

	public ResponseEntity<List<TweetResponseDto>> getUserMentions(String username) {
		User user = validationService.validateUserExistsAndNotDeleted(username);
		List<Tweet> mentions = user.getMentions().stream().filter(tweet -> tweet.getIsDeleted() == false && tweet.getAuthor().getIsDeleted() == false).collect(Collectors.toList());
		Collections.sort(mentions, (a, b) -> b.getPostedAt().compareTo(a.getPostedAt()));
		return new ResponseEntity<List<TweetResponseDto>>(tweetMapper.entitiesToResponseDtos(mentions), HttpStatus.OK);
	}

	public ResponseEntity<List<UserResponseDto>> getUserFollowers(String username) {
		User user = validationService.validateUserExistsAndNotDeleted(username);
		List<User> followers = user.getFollowers().stream().filter(follower -> follower.getIsDeleted() == false).collect(Collectors.toList());
		return new ResponseEntity<List<UserResponseDto>>(userMapper.entitiesToResponseDtos(followers), HttpStatus.OK);
	}

	public ResponseEntity<List<UserResponseDto>> getUserFollowing(String username) {
		User user = validationService.validateUserExistsAndNotDeleted(username);
		List<User> following = user.getFollowing().stream().filter(followingUser -> followingUser.getIsDeleted() == false).collect(Collectors.toList());
		return new ResponseEntity<List<UserResponseDto>>(userMapper.entitiesToResponseDtos(following), HttpStatus.OK);
	}

}
