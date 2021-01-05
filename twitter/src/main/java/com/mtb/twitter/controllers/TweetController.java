package com.mtb.twitter.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtb.twitter.dtos.ContextResponseDto;
import com.mtb.twitter.dtos.CredentialsRequestDto;
import com.mtb.twitter.dtos.HashtagResponseDto;
import com.mtb.twitter.dtos.TweetRequestDto;
import com.mtb.twitter.dtos.TweetResponseDto;
import com.mtb.twitter.dtos.UserResponseDto;
import com.mtb.twitter.services.TweetService;

@RestController
@RequestMapping(value = "/tweets")
public class TweetController {

	private TweetService tweetService;

	public TweetController(TweetService tweetService) {
		this.tweetService = tweetService;
	}

	@GetMapping("")
	public ResponseEntity<List<TweetResponseDto>> getTweets() {
		return new ResponseEntity<List<TweetResponseDto>>(tweetService.getTweetResponseDTOList(tweetService.getTweets()), HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<TweetResponseDto> addTweet(@RequestBody TweetRequestDto tweetRequestDto) {
		return new ResponseEntity<TweetResponseDto>(tweetService.getTweetResponseDTO(tweetService.addTweet(tweetRequestDto)), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TweetResponseDto> getTweetById(@PathVariable String id) {
		return new ResponseEntity<TweetResponseDto>(tweetService.getTweetResponseDTO(tweetService.getTweetById(id)), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<TweetResponseDto> deleteTweet(@PathVariable String id,
			@RequestBody CredentialsRequestDto credentialsRequestDto) {
		return new ResponseEntity<TweetResponseDto>(tweetService.getTweetResponseDTO(tweetService.deleteTweet(id, credentialsRequestDto)), HttpStatus.OK);
	}

	@PostMapping("/{id}/like")
	public ResponseEntity<Void> likeTweet(@PathVariable String id,
			@RequestBody CredentialsRequestDto credentialsRequestDto) {
		return tweetService.likeTweet(id, credentialsRequestDto);
	}

	@PostMapping("/{id}/reply")
	public ResponseEntity<TweetResponseDto> replyToTweet(@PathVariable String id,
			@RequestBody TweetRequestDto tweetRequestDto) {
		return tweetService.replyToTweet(id, tweetRequestDto);
	}

	@PostMapping("/{id}/repost")
	public ResponseEntity<TweetResponseDto> repostTweet(@PathVariable String id,
			@RequestBody CredentialsRequestDto credentialsRequestDto) {
		return tweetService.repostTweet(id, credentialsRequestDto);
	}

	@GetMapping("/{id}/tags")
	public ResponseEntity<List<HashtagResponseDto>> getTweetTags(@PathVariable String id) {
		return tweetService.getTweetTags(id);
	}

	@GetMapping("/{id}/likes")
	public ResponseEntity<List<UserResponseDto>> getTweetLikes(@PathVariable String id) {
		return tweetService.getTweetLikes(id);
	}

	@GetMapping("/{id}/context")
	public ResponseEntity<List<ContextResponseDto>> getTweetContext(@PathVariable String id) {
		return tweetService.getTweetContext(id);
	}

	@GetMapping("/{id}/replies")
	public ResponseEntity<List<TweetResponseDto>> getTweetReplies(@PathVariable String id) {
		return tweetService.getTweetReplies(id);
	}

	@GetMapping("/{id}/reposts")
	public ResponseEntity<List<TweetResponseDto>> getTweetReposts(@PathVariable String id) {
		return tweetService.getTweetReposts(id);
	}

	@GetMapping("/{id}/mentions")
	public ResponseEntity<List<UserResponseDto>> getTweetMentions(@PathVariable String id) {
		return tweetService.getTweetMentions(id);
	}
}
