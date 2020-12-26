package jp.mynavi.azurejava.booklist.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class BooklistSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().ignoringAntMatchers("/api/**")      // /api/以下はCSRFチェックを除外
                .and()
                .cors()
                .configurationSource(this.corsConfigurationSource());   // CORSの設定を有効にする
    }

    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedMethods(Arrays.asList("POST","GET"));       // 許可するHTTPメソッド
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addAllowedOrigin("http://localhost:8081");            // 許可するオリジン（ローカルテスト用）
        corsConfiguration.addAllowedOrigin("https://azurejavastorage.z11.web.core.windows.net");   // 許可するオリジン（Azure Storage用）

        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/api/**", corsConfiguration);     // このフィルタの対象URL

        return corsSource;
    }
}
