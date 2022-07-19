package com.eleks.academy.whoami.db.dto;

import com.eleks.academy.whoami.security.AuthTokenFilter;
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
	private String token;

	private String type = AuthTokenFilter.BEARER;

	private String refreshToken;

	private Long id;

	private String username;

	private String email;
}
