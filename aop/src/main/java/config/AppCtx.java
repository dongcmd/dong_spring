package config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration // spring 환경을 설정하는 클래스
@ComponentScan(basePackages ={"annotation"})
// basePackages의 패키지에 속한 클래스 중
//	@Component을 가진 클래스의 객체를 생성하여 저장
@EnableAspectJAutoProxy // AOP 설정. AOP 관련 어노테이션 사용 가능.
public class AppCtx {
	
}
