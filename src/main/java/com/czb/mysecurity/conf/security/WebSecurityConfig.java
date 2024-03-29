package com.czb.mysecurity.conf.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity的配置
 * 通过SpringSecurity的配置，将JWTLoginFilter，JWTAuthenticationFilter组合在一起
 * @author zhaoxinguo on 2017/9/13.
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 需要放行的URL
     */
    public static final String[] AUTH_WHITELIST = {
            // -- register url
            "/users/signup",
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/login",
            "/favicon.ico",
            "/error"
            // other public endpoints of your API may be appended to this array
    };

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private MyAuthenticationProvider authenticationProvider;
    
    @Autowired
    private LoginFilter loginFilter;
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;
    @Autowired
    private MyTokenFilter myTokenFilter;
    @Autowired
    private FilterSecurityInterceptor myFilterSecurityInterceptor;
    


    // 设置 HTTP 验证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
//                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().permitAll()
//                .anyRequest().authenticated()  // 所有请求需要身份认证
                .and()
                .exceptionHandling().authenticationEntryPoint(myAuthenticationEntryPoint) // 未登录
                .and()
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler) // 没有权限
                .and()
                //.addFilter(loginFilter)  // 不用注入 
                .addFilterBefore(myTokenFilter,UsernamePasswordAuthenticationFilter.class); // 通过token参数从redis加载权限
                //.addFilter(myTokenFilter)
                
                //.logout() // 默认注销行为为logout，可以通过下面的方式来修改
                //.logoutUrl("/logout")
                //.logoutSuccessUrl("/login")// 设置注销成功后跳转页面，默认是跳转到登录页面;
//                .logoutSuccessHandler(customLogoutSuccessHandler)
                
                //.permitAll();
        
        //http.addFilterBefore(myTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);  // 不用注入
                
    }

    // 该方法是登录的时候会进入
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义身份验证组件
        //auth.authenticationProvider(new CustomAuthenticationProvider(userDetailsService, bCryptPasswordEncoder));
        //auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(authenticationProvider); 
    }
    
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    
}