package main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import annotation.Article;
import annotation.ReadArticleService;
import config.AppCtx;

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
			System.out.println("[main] a1 == a2 : " + (a1 == a2)); // false
			service.getArticleAndReadCnt(0);
		} catch (Exception e) {
			System.out.println("[main]" + e.getMessage());
		}
	}

}
