package com.quick.beauty.quickbeautyapi.configurations;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class JwtBlacklist {
	
	private Set<String> blacklist = new HashSet<>();
	
	public void invalidateToken(String token) {
        blacklist.add(token);
    }
	
	public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

}
