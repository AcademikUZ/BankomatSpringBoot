package fan.company.bankomatspringboot.security.config;

import fan.company.bankomatspringboot.security.PasswordValidator;
import fan.company.bankomatspringboot.security.filter.JwtFilter;
import fan.company.bankomatspringboot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecuryConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthService authService;
    @Autowired
    JwtFilter jwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/login").permitAll()

                //for test
                .antMatchers("/api/bank/**").permitAll()


                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); //filterni o'rnatish uchun kerak
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  //har safar tokenni tekshirish

    }

    /**
     * Buni WebSecurityConfigurerAdapter ni o'rniga ishlatadi
     */

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                        .csrf().disable()
//                        .httpBasic().disable()
//                        .authorizeRequests()
//                        .antMatchers("/api/auth/register").permitAll()
//                        .anyRequest().authenticated();
//
//        return http.build();
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        /**
         * Usernameni username orqali topish
         */

        auth
                .userDetailsService(authService).passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    PasswordValidator passwordValidator(){
        return new PasswordValidator();
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
