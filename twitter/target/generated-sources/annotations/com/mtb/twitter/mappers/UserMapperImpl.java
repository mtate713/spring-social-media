package com.mtb.twitter.mappers;

import com.mtb.twitter.dtos.CredentialsRequestDto;
import com.mtb.twitter.dtos.ProfileDto;
import com.mtb.twitter.dtos.UserRequestDto;
import com.mtb.twitter.dtos.UserResponseDto;
import com.mtb.twitter.entities.User;
import com.mtb.twitter.entities.UserCredentials;
import com.mtb.twitter.entities.UserProfile;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-03T14:24:46-0600",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 14 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User requestDtoToEntity(UserRequestDto userRequestDto) {
        if ( userRequestDto == null ) {
            return null;
        }

        User user = new User();

        user.setUserProfile( profileDtoToUserProfile( userRequestDto.getProfile() ) );
        user.setUserCredentials( credentialsRequestDtoToUserCredentials( userRequestDto.getCredentials() ) );

        return user;
    }

    @Override
    public UserResponseDto entityToResponseDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setUsername( userUserCredentialsUsername( user ) );
        userResponseDto.setProfile( userProfileToProfileDto( user.getUserProfile() ) );
        userResponseDto.setJoinedAt( user.getJoinedAt() );

        return userResponseDto;
    }

    @Override
    public List<UserResponseDto> entitiesToResponseDtos(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponseDto> list = new ArrayList<UserResponseDto>( users.size() );
        for ( User user : users ) {
            list.add( entityToResponseDto( user ) );
        }

        return list;
    }

    protected UserProfile profileDtoToUserProfile(ProfileDto profileDto) {
        if ( profileDto == null ) {
            return null;
        }

        UserProfile userProfile = new UserProfile();

        userProfile.setFirstName( profileDto.getFirstName() );
        userProfile.setLastName( profileDto.getLastName() );
        userProfile.setEmail( profileDto.getEmail() );
        userProfile.setPhoneNumber( profileDto.getPhoneNumber() );

        return userProfile;
    }

    protected UserCredentials credentialsRequestDtoToUserCredentials(CredentialsRequestDto credentialsRequestDto) {
        if ( credentialsRequestDto == null ) {
            return null;
        }

        UserCredentials userCredentials = new UserCredentials();

        userCredentials.setUsername( credentialsRequestDto.getUsername() );
        userCredentials.setPassword( credentialsRequestDto.getPassword() );

        return userCredentials;
    }

    private String userUserCredentialsUsername(User user) {
        if ( user == null ) {
            return null;
        }
        UserCredentials userCredentials = user.getUserCredentials();
        if ( userCredentials == null ) {
            return null;
        }
        String username = userCredentials.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }

    protected ProfileDto userProfileToProfileDto(UserProfile userProfile) {
        if ( userProfile == null ) {
            return null;
        }

        ProfileDto profileDto = new ProfileDto();

        profileDto.setFirstName( userProfile.getFirstName() );
        profileDto.setLastName( userProfile.getLastName() );
        profileDto.setEmail( userProfile.getEmail() );
        profileDto.setPhoneNumber( userProfile.getPhoneNumber() );

        return profileDto;
    }
}
