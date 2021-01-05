package com.mtb.twitter.mappers;

import com.mtb.twitter.dtos.HashtagResponseDto;
import com.mtb.twitter.entities.Hashtag;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-03T14:24:46-0600",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 14 (Oracle Corporation)"
)
@Component
public class HashtagMapperImpl implements HashtagMapper {

    @Override
    public HashtagResponseDto entityToResponseDto(Hashtag hashtag) {
        if ( hashtag == null ) {
            return null;
        }

        HashtagResponseDto hashtagResponseDto = new HashtagResponseDto();

        hashtagResponseDto.setLabel( hashtag.getLabel() );
        hashtagResponseDto.setFirstUsed( hashtag.getFirstUsed() );
        hashtagResponseDto.setLastUsed( hashtag.getLastUsed() );

        return hashtagResponseDto;
    }
}
