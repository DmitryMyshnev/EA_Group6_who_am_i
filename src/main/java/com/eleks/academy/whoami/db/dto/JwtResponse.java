package com.eleks.academy.whoami.db.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtResponse {
	private Long userId;

	private String username;

	private String email;

	private String token;

	private String type;

	private String refreshToken;

}
