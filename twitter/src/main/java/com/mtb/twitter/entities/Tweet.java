package com.mtb.twitter.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tweet {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private User author;

	@CreationTimestamp
	private Timestamp postedAt;

	private String content;

	@ManyToOne
	@JoinColumn(name = "reply_id")
	private Tweet inReplyTo;

	@ManyToOne
	@JoinColumn(name = "repost_id")
	private Tweet repostOf;

	@ManyToMany
	@JoinTable(name = "hashtag_tweet", joinColumns = @JoinColumn(name = "tweet_id"), inverseJoinColumns = @JoinColumn(name = "hashtag_id"))
	private List<Hashtag> hashtags;

	@ManyToMany(mappedBy = "mentions")
	private List<User> mentions;

	@OneToMany(mappedBy = "repostOf")
	private List<Tweet> reposts;

	@OneToMany(mappedBy = "inReplyTo")
	private List<Tweet> replies;

	@ManyToMany(mappedBy = "likedTweets")
	private List<User> likedBy;

	private Long likes;

	private Boolean isDeleted;

}
