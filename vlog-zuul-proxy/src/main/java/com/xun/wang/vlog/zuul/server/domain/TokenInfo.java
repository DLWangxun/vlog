/**
 * 
 */
package com.xun.wang.vlog.zuul.server.domain;

import java.util.Date;

import lombok.Data;

/**
 * @author jojo
 *
 */
@Data
public class TokenInfo {

	private boolean active;
	
	private String client_id;
	
	private String[] scope;
	
	private String user_name;

	//resourceIds
	private String[] aud;

	//expire time
	private Date exp;

	private String[] authorities;
 	
}
