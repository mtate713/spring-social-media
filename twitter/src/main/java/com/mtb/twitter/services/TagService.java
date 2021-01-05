package com.mtb.twitter.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mtb.twitter.dtos.HashtagResponseDto;
import com.mtb.twitter.dtos.TweetResponseDto;
import com.mtb.twitter.entities.Hashtag;
import com.mtb.twitter.entities.Tweet;
import com.mtb.twitter.exceptions.InvalidRequestException;
import com.mtb.twitter.mappers.HashtagMapper;
import com.mtb.twitter.mappers.TweetMapper;
import com.mtb.twitter.repositories.HashtagRepository;
import com.mtb.twitter.repositories.TweetRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TagService {

	private HashtagRepository hashtagRepository;
	private HashtagMapper hashtagMapper;
	private TweetMapper tweetMapper;
	private TweetRepository tweetRepository;

	public ResponseEntity<List<HashtagResponseDto>> getTags() {
		List<Hashtag> hashtags = hashtagRepository.findAll();
		return new ResponseEntity<List<HashtagResponseDto>>(hashtagMapper.entitiesToResponseDtos(hashtags),
				HttpStatus.OK);
	}

	public ResponseEntity<List<TweetResponseDto>> getTweetsByLabel(String label) {
		label = label.toLowerCase();
		Optional<Hashtag> optionalHashtag = hashtagRepository.findByLabel(label);
		if (optionalHashtag.isPresent()) {
			Hashtag hashtag = optionalHashtag.get();
			List<Tweet> activeTweets = tweetRepository.findAllByIsDeletedFalseAndHashtagsContainsOrderByPostedAtDesc(hashtag);
			return new ResponseEntity<List<TweetResponseDto>>(tweetMapper.entitiesToResponseDtos(activeTweets),
					HttpStatus.OK);
		} else {
			throw new InvalidRequestException(String.format("No hashtag with label %s exists.", label));
		}
	}

}