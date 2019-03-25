package com.czb.mysecurity.conf.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法
 * attemptAuthentication ：接收并解析用户凭证。
 * successfulAuthentication ：用户成功登录后，这个方法会被调用，我们在这个方法里生成token。
 * @author zhaoxinguo on 2017/9/12.
 */
@Component
public class LoginFilter extends UsernamePasswordAuthenticationFilter {


    // 接收并解析用户凭证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse res) throws AuthenticationException {
        try {
        	System.out.println("attemptAuthentication:========");
        	BufferedReader br = request.getReader();
			String str, wholeStr = "";
			while((str = br.readLine()) != null){   
				wholeStr += str;
			}
			System.out.println(wholeStr);
			if(wholeStr==null || wholeStr.length()==0){
				return null;
			}
			JSONObject user = JSON.parseObject(wholeStr);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.get("username"),
                            user.get("password"),
                            new ArrayList<>())
            );
        } catch (IOException e) {
        	System.out.println("============");
            throw new RuntimeException(e);
        }
    }

    // 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
    @Override
    protected void successfulAuthentication(HttpServletRequest request, 
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
    	System.out.println("登录成功=====");
        // builder the token
        String token = null;
        try {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            // 定义存放角色集合的对象
            List roleList = new ArrayList<>();
            for (GrantedAuthority grantedAuthority : authorities) {
                roleList.add(grantedAuthority.getAuthority());
            }
//            Calendar calendar = Calendar.getInstance();
//            Date now = calendar.getTime();
//            // 设置签发时间
//            calendar.setTime(new Date());
//            // 设置过期时间
//            calendar.add(Calendar.MINUTE, 10);// 10分钟
//            Date time = calendar.getTime();
//            token = Jwts.builder()
//                    .setSubject(auth.getName() + "-" + roleList)
//                    .setIssuedAt(now)//签发时间
//                    .setExpiration(time)//过期时间
//                    .signWith(SignatureAlgorithm.HS512, ConstantKey.SIGNING_KEY) //采用什么算法是可以自己选择的，不一定非要采用HS512
//                    .compact();
//            // 登录成功后，返回token到header里面
//            response.addHeader("Authorization", "Bearer " + token);
            Map<String,Object> map = new HashMap<>();
    		map.put("msg", "登录成功token:xxxxxx");
    		System.out.println(JSON.toJSONString(map));
    		response.setCharacterEncoding("UTF-8");  
    	    response.setContentType("application/json; charset=utf-8");  
    		response.getWriter().write(JSON.toJSONString(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 注意这段要写，不然会报错
     */
    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
    
    

}