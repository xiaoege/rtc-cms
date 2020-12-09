package com.rtc.cms.service.serviceImpl;

import com.rtc.cms.dao.rtc.RtcUserDTO;
import com.rtc.cms.dao.rtc.RtcUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * spring security专用
 *
 * @author ChenHang
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    RtcUserMapper rtcUserMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        RtcUserDTO rtcUser = rtcUserMapper.selectByPhoneOrAccount(account);
        if (rtcUser == null) {
            throw new InternalAuthenticationServiceException("该账号不存在");
        }
        String uuid = rtcUser.getUuid();

        // 角色
        if ("root".equals(rtcUser.getRoleName())) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_root");
            grantedAuthorities.add(grantedAuthority);
            return new User(account, rtcUser.getPassword(), grantedAuthorities);
        } else if ("rtc".equals(rtcUser.getRoleName())) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_rtc");
            grantedAuthorities.add(grantedAuthority);
            return new User(account, rtcUser.getPassword(), grantedAuthorities);
        }

        return null;
    }


}
