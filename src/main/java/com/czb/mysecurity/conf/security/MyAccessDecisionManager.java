package com.czb.mysecurity.conf.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by yangyibo on 17/1/19.
 */
@Service
public class MyAccessDecisionManager implements AccessDecisionManager {

    //decide 方法是判定是否拥有权限的决策方法
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
    	System.out.println("MyAccessDecisionManager:"+object);
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        String url, method;
        AntPathRequestMatcher matcher;
//        matcher = new AntPathRequestMatcher("/securety/testSend");
//        if(matcher.matches(request)){
//        	System.out.println("&&&&&&&&&");
//        }
        if(configAttributes==null){
        	// url在白名单中，直接放行
        	System.out.println(object+"在白名单中直接放行");
        	return;
        }
        for (GrantedAuthority ga : authentication.getAuthorities()) {
//            if (ga instanceof MyGrantedAuthority) {
//                MyGrantedAuthority urlGrantedAuthority = (MyGrantedAuthority) ga;
//                url = urlGrantedAuthority.getPermissionUrl();
//                method = urlGrantedAuthority.getMethod();
//                matcher = new AntPathRequestMatcher(url);
//                if (matcher.matches(request)) {
//                    //当权限表权限的method为ALL时表示拥有此路径的所有请求方式权利。
//                    if (method.equals(request.getMethod()) || "ALL".equals(method)) {
//                        return;
//                    }
//                }
//            } else if (ga.getAuthority().equals("ROLE_ANONYMOUS")) {//未登录只允许访问 login 页面
//                matcher = new AntPathRequestMatcher("/login");
//                if (matcher.matches(request)) {
//                    return;
//                }
//            }
//        	if("/securety/testSend".equals(ga.getAuthority())){
//        		return;
//        	}
          matcher = new AntPathRequestMatcher(ga.getAuthority());
          if(matcher.matches(request)){
          	System.out.println("MyAccessDecisionManager:该用户匹配到权限了，直接放行");
          	return;
          }
        }
        throw new AccessDeniedException("no right");
    }



    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
