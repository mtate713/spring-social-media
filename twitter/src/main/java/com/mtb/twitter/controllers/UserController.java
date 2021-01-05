package com.mtb.twitter.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtb.twitter.dtos.CredentialsRequestDto;
import com.mtb.twitter.dtos.TweetResponseDto;
import com.mtb.twitter.dtos.UserRequestDto;
import com.mtb.twitter.dtos.UserResponseDto;
import com.mtb.twitter.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("")
	public ResponseEntity<List<UserResponseDto>> getUsers() {
		return userService.getUsers();
	}

	@PostMapping("")
	public ResponseEntity<UserResponseDto> addUser(@RequestBody UserRequestDto user) {
		return userService.addUser(user);
	}

	@GetMapping("/@{username}")
	public ResponseEntity<UserResponseDto> getUser(@PathVariable String username) {
		return userService.getUser(username);
	}

	@PatchMapping("/@{username}")
	public ResponseEntity<UserResponseDto> updateUser(@PathVariable String username,
			@RequestBody UserRequestDto userRequestDto) {
		return userService.updateUser(username, userRequestDto);
	}

	@DeleteMapping("/@{username}")
	public ResponseEntity<UserResponseDto> deleteUser(@PathVariable String username, @RequestBody CredentialsRequestDto credentialsRequestDto) {
		return userService.deleteUser(username, credentialsRequestDto);
	}

	@PostMapping("/@{username}/follow")
	public ResponseEntity<Void> followUser(@PathVariable String username,
			@RequestBody CredentialsRequestDto credentialsRequestDto) {
		return userService.followUser(username, credentialsRequestDto);
	}

	@PostMapping("/@{username}/unfollow")
	public ResponseEntity<Void> unfollowUser(@PathVariable String username,
			@RequestBody CredentialsRequestDto credentialsRequestDto) {
		return userService.unfollowUser(username, credentialsRequestDto);
	}

	@GetMapping("/@{username}/feed")
	public ResponseEntity<List<TweetResponseDto>> getUserFeed(@PathVariable String username) {
		return userService.getUserFeed(username);
	}

	@GetMapping("/@{username}/tweets")
	public ResponseEntity<List<TweetResponseDto>> getUserTweets(@PathVariable String username) {
		return userService.getUserTweets(username);
	}

	@GetMapping("/@{username}/mentions")
	public ResponseEntity<List<TweetResponseDto>> getUserMentions(@PathVariable String username) {
		return userService.getUserMentions(username);
	}

	@GetMapping("/@{username}/followers")
	public ResponseEntity<List<UserResponseDto>> getUserFollowers(@PathVariable String username) {
		return userService.getUserFollowers(username);
	}

	@GetMapping("/@{username}/following")
	public ResponseEntity<List<UserResponseDto>> getUserFollowing(@PathVariable String username) {
		return userService.getUserFollowing(username);
	}

}
