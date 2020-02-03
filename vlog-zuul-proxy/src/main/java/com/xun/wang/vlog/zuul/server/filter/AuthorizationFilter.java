
package com.xun.wang.vlog.zuul.server.filter;

import javax.servlet.http.HttpServletRequest;

import com.xun.wang.vlog.zuul.server.domain.TokenInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class AuthorizationFilter extends ZuulFilter {

    @Value("${auth.server.mapping-url}")
    private String authMappingUrl;

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

        log.info("authorization start");

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        if (isNeedAuth(request)) {
            TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");
            if (tokenInfo != null && tokenInfo.isActive()) {
                if (!hasPermission(tokenInfo, request)) {
                    log.info("audit log update fail 403");
                    handleError(403, requestContext);
                }
                requestContext.addZuulRequestHeader("username", tokenInfo.getUser_name());
            } else {
                if (!StringUtils.contains(request.getRequestURI(), authMappingUrl)) {
                    log.info("audit log update fail 401");
                    handleError(401, requestContext);
                }
            }
        }

        return null;
    }

    private void handleError(int status, RequestContext requestContext) {
        requestContext.getResponse().setContentType("application/json");
        requestContext.setResponseStatusCode(status);
        requestContext.setResponseBody("{\"message\":\"auth fail\"}");
        requestContext.setSendZuulResponse(false);
    }

    private boolean hasPermission(TokenInfo tokenInfo, HttpServletRequest request) {
        return true;
    }

    private boolean isNeedAuth(HttpServletRequest request) {
        return true;
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
        return 3;
    }

}
