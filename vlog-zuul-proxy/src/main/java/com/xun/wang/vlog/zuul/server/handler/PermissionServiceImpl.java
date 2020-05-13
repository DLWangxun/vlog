/**
 * 
 */
package com.xun.wang.vlog.zuul.server.handler;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.stereotype.Service;

/**
 * @author jojo
 *
 */
@Service
public class PermissionServiceImpl implements PermissionService {

	/* (non-Javadoc)
	 * @see com.imooc.security.PermissionService#hasPermission(javax.servlet.http.HttpServletRequest, org.springframework.security.core.Authentication)
	 */
	@Override
	public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
		System.out.println("2 authorize");
		System.out.println(request.getRequestURI());
		System.out.println(ReflectionToStringBuilder.toString(authentication));
		
		if(authentication instanceof AnonymousAuthenticationToken) {
			throw new AccessTokenRequiredException(null);
		}
		
		return true;
	}

}
