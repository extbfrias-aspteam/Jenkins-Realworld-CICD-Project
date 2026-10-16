package com.asp.digitalizacion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class JwtProperties {
	
	@Value("${eiyu.jwt.secret}")
	private String secretEiyu;
	
	@Value("${eiyu.jwt.issuer}")
	private String issuertEiyu;
	
	@Value("${eiyu.jwt.audience}")
	private String audiencetEiyu;

}
