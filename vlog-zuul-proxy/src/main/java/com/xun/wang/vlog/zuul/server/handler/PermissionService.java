/**
 * 
 */
package com.xun.wang.vlog.zuul.server.handler;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

/**
 * @author jojo
 *
 */
public interface PermissionService {
	
	boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
