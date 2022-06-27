package com.tian.web.config;

import com.tian.web.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();        //关闭csrf防护

        http.rememberMe()
                //设置数据源
                .tokenRepository(persistentTokenRepository())
                //自定义登陆逻辑一定要添加
                .userDetailsService(userDetailService);

        //表单提交
        http.formLogin()
                //自定义登录页
                .loginPage("/logon")
                //必须和登录表单提交的接口一样，去执行自定义登录逻辑
                .loginProcessingUrl("/login")
                .failureUrl("/logon?error=true")
                //自定义登陆成功跳转页面，可跳网页
                .successHandler(myAuthenticationSuccessHandler);
        //自定义登录失败跳转
        //.failureHandler(new MyAuthenticationFailureHandler("error.html"));

        //对http配置时可用.and()回到http位置而不用重新写一个http配置
        //授权
        http.authorizeRequests()
                //放行登陆界面，否则会出现重定向次数过多的问题
                .antMatchers("/logon").permitAll()
                .antMatchers("/signUp").permitAll()
                //权限控制，严格区分大小写
                //.antMatchers("/search").hasAuthority("admin")
                //匹配多权限
                .antMatchers("/user/**").hasAnyAuthority("admin", "normal");


    }

    //配置密码加密器
    @Bean
    public PasswordEncoder getPw() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        //设置数据源
        jdbcTokenRepository.setDataSource(dataSource);
        //自动建表，第一次启动时开启，第二次启动时注释掉，否则会报错。
        //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

}

