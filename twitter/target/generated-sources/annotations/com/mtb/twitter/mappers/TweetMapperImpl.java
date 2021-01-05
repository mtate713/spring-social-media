package com.mtb.twitter.mappers;

import com.mtb.twitter.dtos.TweetRequestDto;
import com.mtb.twitter.dtos.TweetResponseDto;
import com.mtb.twitter.entities.Tweet;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-03T14:24:46-0600",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 14 (Oracle Corporation)"
)
@Component
public class TweetMapperImpl implements TweetMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto) {
        if ( tweetRequestDto == null ) {
            return null;
        }

        Tweet tweet = new Tweet();

        tweet.setContent( tweetRequestDto.getContent() );

        return tweet;
    }

    @Override
    public TweetResponseDto entityToResponseDto(Tweet tweet) {
        if ( tweet == null ) {
            return null;
        }

        TweetResponseDto tweetResponseDto = new TweetResponseDto();

        tweetResponseDto.setId( tweet.getId() );
        tweetResponseDto.setAuthor( userMapper.entityToResponseDto( tweet.getAuthor() ) );
        tweetResponseDto.setContent( tweet.getContent() );
        tweetResponseDto.setInReplyTo( entityToResponseDto( tweet.getInReplyTo() ) );
        tweetResponseDto.setRepostOf( entityToResponseDto( tweet.getRepostOf() ) );

        return tweetResponseDto;
    }

    @Override
    public List<TweetResponseDto> entitiesToResponseDtos(List<Tweet> tweets) {
        if ( tweets == null ) {
            return null;
        }

        List<TweetResponseDto> list = new ArrayList<TweetResponseDto>( tweets.size() );
        for ( Tweet tweet : tweets ) {
            list.add( entityToResponseDto( tweet ) );
        }

        return list;
    }
}
