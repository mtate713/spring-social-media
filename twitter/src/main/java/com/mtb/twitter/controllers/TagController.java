package com.mtb.twitter.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mtb.twitter.dtos.HashtagResponseDto;
import com.mtb.twitter.dtos.TweetResponseDto;
import com.mtb.twitter.services.TagService;

@RestController
@RequestMapping(value = "/tags")
public class TagController {

	private TagService tagService;

	public TagController(TagService tagService) {
		this.tagService = tagService;
	}

	@GetMapping("")
	public ResponseEntity<List<HashtagResponseDto>> getTags() {
		return tagService.getTags();
	}

	@GetMapping("/{label}")
	public ResponseEntity<List<TweetResponseDto>> getTweetsByLabel(@PathVariable String label) {
		return tagService.getTweetsByLabel(label);
	}
}
