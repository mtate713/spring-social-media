package com.mtb.twitter.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mtb.twitter.dtos.CredentialsRequestDto;
import com.mtb.twitter.dtos.ProfileDto;
import com.mtb.twitter.dtos.UserRequestDto;
import com.mtb.twitter.entities.Hashtag;
import com.mtb.twitter.entities.User;
import com.mtb.twitter.exceptions.InvalidRequestException;
import com.mtb.twitter.repositories.HashtagRepository;
import com.mtb.twitter.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ValidationService {

	private UserRepository userRepository;
	private HashtagRepository hashtagRepository;

	public Boolean validateTagExists(String label) {
		Optional<Hashtag> optionalTag = hashtagRepository.findByLabel(label);
		return optionalTag.isPresent();
	}

	public Boolean validateUsernameExists(String username) {
		Optional<User> optionalUser = userRepository.findByUserCredentialsUsername(username);
		return optionalUser.isPresent();
	}

	public Boolean validateUsernameAvailable(String username) {
		Optional<User> optionalUser = userRepository.findByUserCredentialsUsername(username);
		return optionalUser.isEmpty();
	}

	public Boolean validateEmailAvailable(String email) {
		Optional<User> optionalUser = userRepository.findByUserProfileEmail(email);
		return optionalUser.isEmpty();
	}

	public void validateCredentialsRequestDto(CredentialsRequestDto credentials) {
		if (credentials == null) {
			throw new InvalidRequestException("Credentials are required for this request.");
		}
		String username = credentials.getUsername();
		String password = credentials.getPassword();
		if (username == null || password == null || username.isBlank() || password.isBlank()) {
			throw new InvalidRequestException("Credentials require a username and password.");
		}
	}

	public Boolean validateCredentials(CredentialsRequestDto credentials) {
		Optional<User> optionalUser = userRepository.findByUserCredentialsUsername(credentials.getUsername());
		return optionalUser.isPresent()
				&& optionalUser.get().getUserCredentials().getPassword().equals(credentials.getPassword());
	}

	public Boolean validateCredentials(String username, CredentialsRequestDto credentials) {
		if (!username.equals(credentials.getUsername())) {
			return false;
		}
		Optional<User> optionalUser = userRepository.findByUserCredentialsUsername(username);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			return user.getUserCredentials().getPassword().equals(credentials.getPassword());
		} else {
			return false;
		}
	}

	public void validateProfileDto(ProfileDto profile) {
		if (profile == null) {
			throw new InvalidRequestException("A profile is required for this request.");
		}
		String email = profile.getEmail();
		if (email.isBlank() || email == null) {
			throw new InvalidRequestException("Missing required email field in profile.");
		}
	}

	public void validateUserRequestDto(UserRequestDto userRequestDto) {
		CredentialsRequestDto credentials = userRequestDto.getCredentials();
		validateCredentialsRequestDto(credentials);
		ProfileDto profile = userRequestDto.getProfile();
		validateProfileDto(profile);
	}
	
	public User validateUserExistsAndNotDeleted(String username) {
		Optional<User> optionalUser = userRepository.findByUserCredentialsUsername(username);
		if (optionalUser.isPresent() && optionalUser.get().getIsDeleted() == false) {
			return optionalUser.get();
		} else {
			throw new InvalidRequestException(String.format("No user with username %s exists.", username));
		}
	}

}
