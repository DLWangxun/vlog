/**
 * 
 */
package com.xun.wang.vlog.zuul.server.filter;

import javax.servlet.http.HttpServletRequest;
import com.xun.wang.vlog.zuul.server.domain.TokenInfo;
import com.xun.wang.vlog.zuul.server.domain.ZuulContants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.net.UnknownHostException;

/**
 * @author jojo
 *
 */
@Slf4j
@Component
public class OAuthFilter extends ZuulFilter {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${auth.server.mapping-url}")
	private String authMappingUrl;

	@Value("${auth.client.id}")
	private String clientId;

	@Value("${auth.client.secret}")
	private String clientSecret;


	@Autowired
	private Environment environment;

	/* (non-Javadoc)
	 * @see com.netflix.zuul.IZuulFilter#shouldFilter()
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.netflix.zuul.IZuulFilter#run()
	 */
	@Override
	public Object run() throws ZuulException {
		log.info("oauth start");
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();
		if(StringUtils.contains(request.getRequestURI(), authMappingUrl)) {
			return null;
		}
		String authHeader = request.getHeader("Authorization");
		if(StringUtils.isBlank(authHeader)) {
			return null;
		}
		if(!StringUtils.startsWithIgnoreCase(authHeader, "bearer ")) {
			return null;
		}
		try {
			TokenInfo info = getTokenInfo(authHeader);
			request.setAttribute("tokenInfo", info);
		} catch (Exception e) {
			log.error("get token info fail", e);
		}

		return null;
	}

	private TokenInfo getTokenInfo(String authHeader) throws UnknownHostException {
		String token = StringUtils.substringAfter(authHeader, "Bearer ");
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme("http");
		builder.host("vlog-auth-server");
		builder.path(ZuulContants.OATHURL);
		URI oauthServiceUrl = builder.build().encode().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(clientId,clientSecret);
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("token", token);
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
		ResponseEntity<TokenInfo> response = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
		log.info("token info :" + response.getBody().toString());
		return response.getBody();
	}

	/* (non-Javadoc)
	 * @see com.netflix.zuul.ZuulFilter#filterType()
	 */
	@Override
	public String filterType() {
		return "pre";
	}

	/* (non-Javadoc)
	 * @see com.netflix.zuul.ZuulFilter#filterOrder()
	 */
	@Override
	public int filterOrder() {
		return 1;
	}

}
