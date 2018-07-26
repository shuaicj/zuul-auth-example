package shuaicj.example.security.gateway;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import shuaicj.example.security.common.JwtAuthenticationConfig;

/**
 * Add request header 'X-Username'.
 *
 * @author shuaicj 2018/07/26
 */
@Component
public class UsernameZuulFilter extends ZuulFilter {

    @Autowired
    @Lazy
    JwtAuthenticationConfig jwtConfig;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 100;
    }

    @Override
    public boolean shouldFilter() {
        return !RequestContext.getCurrentContext().getRequest().getRequestURI().equals(jwtConfig.getUrl());
    }

    @Override
    public Object run() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RequestContext.getCurrentContext().addZuulRequestHeader("X-Username", auth.getName());
        return null;
    }
}
