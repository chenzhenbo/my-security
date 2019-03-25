package com.czb.mysecurity.conf.security;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {
	@Autowired
	private UserDetailsService userDetailService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println("authenticate ======");
		String userName = authentication.getName();// 这个获取表单输入中的用户名
        String password = (String) authentication.getCredentials();// 这个是表单中输入的密码
 
        /** 判断用户是否存在 */
        UserDetails userDetails = userDetailService.loadUserByUsername(userName); // 这里调用我们的自己写的获取用户的方法；
        if (userDetails == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
 
        /** 判断密码是否正确 */
//        try {
//            String encodePwd = MD5.encode(password.concat(userDetails.getSalt()));
//            if (!userInfo.getPassword().equals(encodePwd)) {
//                throw new BadCredentialsException("密码不正确");
//            }
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
 
        /** 判断账号是否停用/删除 */
//        if (SystemUserConstants.STOP.equals(userInfo.getStatus()) || SystemUserConstants.DELETED.equals(userInfo.getStatus())) {
//            throw new DisabledException("账户不可用");
//        }
 
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities(); 
        return new UsernamePasswordAuthenticationToken(userDetails, password, authorities);// 构建返回的用户登录成功的token
    }



	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}

}
