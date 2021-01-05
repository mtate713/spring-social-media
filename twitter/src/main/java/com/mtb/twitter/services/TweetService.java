package com.mtb.twitter.services;

import java.util.ArrayList;
import java.util.List;

import com.mtb.twitter.entities.Hashtag;
import com.mtb.twitter.entities.Tweet;
import com.mtb.twitter.entities.User;
import com.mtb.twitter.exceptions.InvalidRequestException;
import com.mtb.twitter.mappers.HashtagMapper;
import com.mtb.twitter.mappers.TweetMapper;
import com.mtb.twitter.mappers.UserMapper;
import com.mtb.twitter.repositories.HashtagRepository;
import com.mtb.twitter.repositories.TweetRepository;
import com.mtb.twitter.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mtb.twitter.dtos.ContextResponseDto;
import com.mtb.twitter.dtos.CredentialsRequestDto;
import com.mtb.twitter.dtos.HashtagResponseDto;
import com.mtb.twitter.dtos.TweetRequestDto;
import com.mtb.twitter.dtos.TweetResponseDto;
import com.mtb.twitter.dtos.UserResponseDto;

@Service
@AllArgsConstructor
public class TweetService {

	private TweetRepository tweetRepository;
	private UserRepository userRepository;
	private HashtagRepository hashtagRepository;
	private TweetMapper tweetMapper;
	private UserMapper userMapper;
	private HashtagMapper hashtagMapper;
	private ValidationService validationService;




	public List<Tweet> getTweets() {
		return tweetRepository.findAllByIsDeletedFalseAndAuthorIsDeletedFalseOrderByPostedAtDesc();
	}

	public Tweet addTweet(TweetRequestDto tweetRequestDto) {
		if (validationService.validateCredentials(tweetRequestDto.getCredentials())) {
			User author = userRepository.findByUserCredentialsUsername(tweetRequestDto.getCredentials().getUsername()).get();
			Tweet newTweet = tweetMapper.requestDtoToEntity(tweetRequestDto);
			newTweet.setAuthor(author);
			Tweet createdTweet = tweetRepository.saveAndFlush(newTweet);
			createdTweet.setHashtags(scanForHashtags(newTweet));
			createdTweet.setMentions(scanForMentions(newTweet));
			createdTweet.setLikes(0L);
			createdTweet.setLikedBy(new ArrayList<User>());
			createdTweet.setIsDeleted(false);
			createdTweet.setReplies(new ArrayList<Tweet>());
			createdTweet.setReposts(new ArrayList<Tweet>());
			tweetRepository.saveAndFlush(createdTweet);
			author.getTweets().add(createdTweet);
			userRepository.saveAndFlush(author);
			return createdTweet;
			
		}
		else {
			throw new InvalidRequestException("Incorrect Credentials. Unable to post tweet.");
		}
	}

	public Tweet getTweetById(String id) {
		if (tweetRepository.findById(Long.valueOf(id)).isPresent()) {
			Tweet tweet = tweetRepository.getOne(Long.valueOf(id));
			if (tweet.getIsDeleted())
				throw new InvalidRequestException("This tweet has been deleted.");

			else
				return tweet;
		}

		else
			throw new InvalidRequestException("Unable to find tweet.");
	}

	public Tweet deleteTweet(String id, CredentialsRequestDto credentialsRequestDto) {
		if (validationService.validateCredentials(credentialsRequestDto)) {
			Tweet deleted = getTweetById(id);
			deleted.setIsDeleted(true);
			tweetRepository.saveAndFlush(deleted);
			return deleted;
		}
		else {
			throw new InvalidRequestException("Incorrect Credentials. Unable to delete tweet.");
		}
	}

	public ResponseEntity<Void> likeTweet(String id, CredentialsRequestDto credentialsRequestDto) {
		if (validationService.validateCredentials(credentialsRequestDto)) {
			User user = userRepository.findByUserCredentialsUsername(credentialsRequestDto.getUsername()).get();
			Tweet likedTweet = getTweetById(id);
			likedTweet.setLikes(likedTweet.getLikes() + 1L);
			likedTweet.getLikedBy().add(user);
			user.getLikedTweets().add(likedTweet);
			tweetRepository.saveAndFlush(likedTweet);
			userRepository.saveAndFlush(user);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		else {
			throw new InvalidRequestException("Incorrect Credentials. Unable to like tweet.");
		}
	}

	public ResponseEntity<TweetResponseDto> replyToTweet(String id, TweetRequestDto tweetRequestDto) {
		if (validationService.validateCredentials(tweetRequestDto.getCredentials())) {
			Tweet inReplyTo = getTweetById(id);
			Tweet reply = addTweet(tweetRequestDto);
			reply.setInReplyTo(inReplyTo);
			inReplyTo.getReplies().add(reply);
			tweetRepository.saveAndFlush(reply);
			tweetRepository.saveAndFlush(inReplyTo);
			return new ResponseEntity<TweetResponseDto>(tweetMapper.entityToResponseDto(reply), HttpStatus.OK);
		}
		else {
			throw new InvalidRequestException("Incorrect Credentials. Unable to post reply tweet.");
		}
	}

	public ResponseEntity<TweetResponseDto> repostTweet(String id, CredentialsRequestDto credentialsRequestDto) {
		if (validationService.validateCredentials(credentialsRequestDto)) {
			Tweet repostOf = getTweetById(id);
			TweetRequestDto repostDto = new TweetRequestDto(repostOf.getContent(), credentialsRequestDto);
			Tweet repost = addTweet(repostDto);
			repost.setRepostOf(repostOf);
			repostOf.getReposts().add(repost);
			tweetRepository.saveAndFlush(repost);
			tweetRepository.saveAndFlush(repostOf);
			return new ResponseEntity<TweetResponseDto>(tweetMapper.entityToResponseDto(repost), HttpStatus.OK);
		}
		else {
			throw new InvalidRequestException("Incorrect Credentials. Unable to post reply tweet.");
		}
	}

	public ResponseEntity<List<HashtagResponseDto>> getTweetTags(String id) {
		Tweet tweet = getTweetById(id);
		List<Hashtag> hashtags = tweet.getHashtags();
		if (hashtags.isEmpty())
			throw new InvalidRequestException("This tweet does not have any hashtags associated with it.");
		else
			return new ResponseEntity<List<HashtagResponseDto>>(hashtagMapper.entitiesToResponseDtos(hashtags), HttpStatus.OK);
	}

	public ResponseEntity<List<UserResponseDto>> getTweetLikes(String id) {
		Tweet tweet = getTweetById(id);
		if (tweet.getLikedBy().isEmpty())
			throw new InvalidRequestException("There are no users that have liked this tweet.");
		else
			return new ResponseEntity<List<UserResponseDto>>(userMapper.entitiesToResponseDtos(tweet.getLikedBy()), HttpStatus.OK);
	}

	public ResponseEntity<List<ContextResponseDto>> getTweetContext(String id) {
		if (tweetRepository.findById(Long.valueOf(id)).isPresent()) {
			List<ContextResponseDto> result = new ArrayList<ContextResponseDto>();
			Tweet target = tweetRepository.getOne(Long.valueOf(id));
			Tweet temp = new Tweet();
			temp.setInReplyTo(target.getInReplyTo());
			temp.setReplies(target.getReplies());
			List<Tweet> before = new ArrayList<Tweet>();
			List<Tweet> after = new ArrayList<Tweet>();
			while (temp.getInReplyTo() != null) {
				if (!temp.getIsDeleted())
					before.add(0, temp.getInReplyTo());
				temp = temp.getInReplyTo();
			}
			temp = target;
			ContextResponseDto template = new ContextResponseDto(tweetMapper.entityToResponseDto(target), tweetMapper.entitiesToResponseDtos(before), tweetMapper.entitiesToResponseDtos(after));
			if (!getContextAfterAux(after, temp, template, result))
				result.add(template);
			return new ResponseEntity<List<ContextResponseDto>>(result, HttpStatus.OK);

		}
		else
			throw new InvalidRequestException("Unable to find tweet.");

	}

	public boolean getContextAfterAux (List<Tweet> thread, Tweet target, ContextResponseDto template, List<ContextResponseDto> result) {
		if (target.getReplies().isEmpty()) {
			return false;
		}
		else {
			for (Tweet t : target.getReplies()) {
				if (!t.getIsDeleted())
					thread.add(t);
				if (!getContextAfterAux(thread, t, template, result)) {
					result.add(new ContextResponseDto(template.getTarget(), template.getBefore(), tweetMapper.entitiesToResponseDtos(thread)));

				}
				thread.remove(thread.size() - 1);
			}
			return true;
		}
	}



	public ResponseEntity<List<TweetResponseDto>> getTweetReplies(String id) {
		Tweet tweet = getTweetById(id);
		if (tweet.getReplies().isEmpty())
			throw new InvalidRequestException("This tweet does not have any replies.");
		else
			return new ResponseEntity<List<TweetResponseDto>>(tweetMapper.entitiesToResponseDtos(tweet.getReplies()), HttpStatus.OK);

	}

	public ResponseEntity<List<TweetResponseDto>> getTweetReposts(String id) {
		Tweet tweet = getTweetById(id);
		if (tweet.getReposts().isEmpty())
			throw new InvalidRequestException("This tweet does not have any reposts.");
		else
			return new ResponseEntity<List<TweetResponseDto>>(tweetMapper.entitiesToResponseDtos(tweet.getReposts()), HttpStatus.OK);
	}

	public ResponseEntity<List<UserResponseDto>> getTweetMentions(String id) {
		Tweet tweet = getTweetById(id);
		if (tweet.getMentions().isEmpty())
			throw new InvalidRequestException("This tweet does not have any mentions of users.");
		else
			return new ResponseEntity<List<UserResponseDto>>(userMapper.entitiesToResponseDtos(tweet.getMentions()), HttpStatus.OK);
	}
	
	public List<Hashtag> scanForHashtags(Tweet t) {
		List<Hashtag> result = new ArrayList<Hashtag>();
		List<String> tagLabels = new ArrayList<String>();
		String tag = "";
		String text = t.getContent();
		boolean tag_found = false;
		for (int i = 0; i < text.length(); i++) {
			if (tag_found) {
				if (!Character.isLetterOrDigit(text.charAt(i))) {
					tagLabels.add(tag.toLowerCase());
					tag = "";
					tag_found = false;
				}
				else
					tag += text.charAt(i);



			}
			if ((tag_found) && (i == (text.length() - 1))) {
				tagLabels.add(tag.toLowerCase());
				tag = "";
				tag_found = false;
			}


			if (text.charAt(i) == '#')
				tag_found = true;
			
			
			
		}
		
		Hashtag hashtag;
		for (String label : tagLabels) {
			if (hashtagRepository.findByLabel(label).isPresent()) 
				hashtag = hashtagRepository.findByLabel(label).get();
			else {
				hashtag = new Hashtag();
				hashtag.setLabel(label);
				hashtag.setTweets(new ArrayList<Tweet>());
			}
			
			hashtag.getTweets().add(t);
			hashtagRepository.saveAndFlush(hashtag);
			result.add(hashtag);
		}
		return result;
		
		
	}
	
	public List<User> scanForMentions(Tweet t) {
		List<User> result = new ArrayList<User>();
		List<String> mentionNames = new ArrayList<String>();
		String name = "";
		String text = t.getContent();
		boolean mention_found = false;
		for (int i = 0; i < text.length(); i++) {
			if (mention_found) {
				if (!Character.isLetterOrDigit(text.charAt(i))) {
					mentionNames.add(name);
					name = "";
					mention_found = false;
				}
				else
					name += text.charAt(i);


			}
			if ((mention_found) && (i == (text.length() - 1))) {
				mentionNames.add(name.toString());
				name = "";
				mention_found = false;
			}

			if (i == (text.length() - 1)) {
				mentionNames.add(name.toString());
				name = "";
				mention_found = false;
			}

			if (text.charAt(i) == '@')
				mention_found = true;
			
			
			
		}
		
		User user;
		for (String username : mentionNames) {
			if (userRepository.findByUserCredentialsUsername(username).isPresent()) {
				user = userRepository.findByUserCredentialsUsername(username).get();
				if (user.getMentions() == null)
					user.setMentions(new ArrayList<Tweet>());
				user.getMentions().add(t);
				result.add(user);
				userRepository.saveAndFlush(user);
			}
		}
		
		return result;
		
	}

	public TweetResponseDto getTweetResponseDTO(Tweet t) {
		
		return tweetMapper.entityToResponseDto(t);
	}

	public List<TweetResponseDto> getTweetResponseDTOList(List<Tweet> t) {

		return tweetMapper.entitiesToResponseDtos(t);
	}
	
}
