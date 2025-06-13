package kr.gdu.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.zaxxer.hikari.HikariDataSource;

import kr.gdu.interceptor.BoardInterceptor;

@Configuration 
//@EnableAspectJAutoProxy //AOP 사용을 위한 설정
//@EnableWebMvc  //기본 웹처리를 위한 설정. 
//   http://localhost:8080/user/login
public class MvcConfig implements WebMvcConfigurer{
	//예외처리 객체
	@Bean
	public SimpleMappingExceptionResolver exceptionHandler() {
		SimpleMappingExceptionResolver ser = new SimpleMappingExceptionResolver();
		Properties pr = new Properties();
		pr.put("exception.ShopException", "exception");
		//exception.ShopException 예외 발생시 exception.jsp 페이지 이동
		ser.setExceptionMappings(pr);
		return ser;
	}
	//기본 웹파일 처리를 위한 설정
//	@Override
//	public void configureDefaultServletHandling
//	(DefaultServletHandlerConfigurer configurer) {
//		configurer.enable();
//	}	
	@Bean
    @Primary   //우선 적용
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {  //Connection 객체
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type
        		(HikariDataSource.class).build(); //Connection POOL객체
    }
    
    //인터셉터관련 설정
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new BoardInterceptor())
    	.addPathPatterns("/board/write")
    	.addPathPatterns("/board/update")
    	.addPathPatterns("/board/delete");
    	// url 요청시 C보다 먼저 실행
    }
}
