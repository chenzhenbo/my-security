package com.czb.mysecurity.conf.security;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
@Component
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {
	
	
	private Map<String, Collection<ConfigAttribute>> map = null;
	
	//此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
    //因为我不想每一次来了请求，都先要匹配一下权限表中的信息是不是包含此url，
    // 我准备直接拦截，不管请求的url 是什么都直接拦截，然后在MyAccessDecisionManager的decide 方法中做拦截还是放行的决策。
    //所以此方法的返回值不能返回 null 此处我就随便返回一下。
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
    	System.out.println("SecurityMetadataSourceService======="+object);
    	if(map == null) {
            loadResourceDefine();
        }
    	
    	HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        for(Map.Entry<String, Collection<ConfigAttribute>> entry : map.entrySet()) {
            String resUrl = entry.getKey();
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(resUrl);
            if(matcher.matches(request)) {
            	System.out.println("SecurityMetadataSourceService=======在白名单中直接发行"); 
            	return null;   // return null 在白名单中直接发行
            }else{
            	continue;
            }
        }
        // 不在白名单中就需要进一步鉴权
        System.out.println("SecurityMetadataSourceService=======不在白名单中就需要进一步鉴权");
        ConfigAttribute configAttribute = new SecurityConfig(request.getRequestURI());
        List<ConfigAttribute> configAttributeList = new ArrayList<>();
        configAttributeList.add(configAttribute);
        return configAttributeList;
    }
    
    
    
    /**
     * 加载白名单权限表中所有权限
     */
    private void loadResourceDefine() {
        map = new HashMap<>();
        if(!StringUtils.isEmpty(WebSecurityConfig.AUTH_WHITELIST)){
        	for(String url:WebSecurityConfig.AUTH_WHITELIST){
        		List<ConfigAttribute> configAttributeList = new ArrayList<>();
        		ConfigAttribute configAttribute = new SecurityConfig(url);
                configAttributeList.add(configAttribute);
                map.put(url,configAttributeList);
        	}
        }

    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
