package com.mtb.twitter.dtos;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HashtagResponseDto {
	private String label;
	private Timestamp firstUsed;
	private Timestamp lastUsed;
}
