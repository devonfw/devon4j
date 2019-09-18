package com.devonfw.module.security.jwt.util;

import java.util.Map;

import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.Signer;

public class TokenCreator {

	private CharSequence content;
	private Signer signer;
	private Map<String, String> headers;

	public TokenCreator(CharSequence content, Signer signer, Map<String, String> headers) {
		super();
		this.content = content;
		this.signer = signer;
		this.headers = headers;
	}

	public Jwt generateToken(CharSequence content, Signer signer, Map<String, String> headers) {

		Jwt token=JwtHelper.encode(content, signer, headers);
		return token;
	}

}
