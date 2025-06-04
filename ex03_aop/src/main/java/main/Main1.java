package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import annotation.Article;
import annotation.Member;
import annotation.MemberService;
import annotation.ReadArticleService;
import annotation.UpdateInfo;
import config.AppCtx;
/*
	1. 환경 설정을 위해 클래스에서 사용되는 Annotation
	  @Configuration : 환경 설정 자바 클래스. xml 대체되는 클래스
	  @ComponentScan : 객체 생성을 위한 패키지 설정.
	  @EnableAspectJAutoProxy : AOP를 사용하게 설정
	  @Bean : 객체를 생성.
	  
	2. 클래스에서 사용되는 Annotation
	  @Component : ComponentScan에 의해 객체화 되는 클래스.
	  @Autowired : 자료형 기준으로 객체를 주입.
	  	주입대상의 객체가 없으면 오류 ex) 인터페이스일 때
	  	주입 필수
	  @Autowired(required=false) : 대상의 객체가 없으면 null 주입.
	  	주입 선택
	  @Scope : 일회용 객체로 생성. 사용될 때마다 새로운 객체 생성.
	  
	3. AOP 관련 Annotation
	  @Aspect : AOP로 사용될 클래스로 지정
	  @Order(실행순위) : 순서는 before 기준. after는 역순으로 실행.
	  @Before : "핵심 기능 수행 전" 실행
	  @AfterReturning : "핵심 기능 정상 수행 후" 실행
	  @AfterThrowing : "핵심 기능 오류 수행 후" 실행
	  @After : "핵심 기능 수행 후" 실행
	  @Around : "핵심 기능 수행 전, 후" 모두 실행
 */

public class Main1 {
	public static void main(String[] args) {
		// ApplicationContext : 컨테이너. 객체들을 저장하고 있음.
		ApplicationContext ctx =
				new AnnotationConfigApplicationContext(AppCtx.class);
		// service : ReadArticleServiceImole 객체를 참조
		ReadArticleService service =
				ctx.getBean("readArticleService", ReadArticleService.class);
		try {
			Article a1 = service.getArticleAndReadCnt(1);
			Article a2 = service.getArticleAndReadCnt(1);
			System.out.println("[main] a1 == a2 : " + (a1 == a2));
			service.getArticleAndReadCnt(0);
		} catch (Exception e) {
			System.out.println("[main]" + e.getMessage());
		}
		System.out.println("\nUpdateTraceAspect 연습");
		MemberService ms = ctx.getBean("memberService", MemberService.class);
		ms.regist(new Member());
		ms.update("hong", new UpdateInfo());
		ms.delete("hong2", "test", new UpdateInfo());
		
	}

}
