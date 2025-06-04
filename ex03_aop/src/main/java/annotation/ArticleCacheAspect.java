package annotation;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(2)
public class ArticleCacheAspect {
	private Map<Integer, Article> cache = new HashMap<>();
	
	// pointcut : ReadArticleService 클래스 중 public 메서드
	@Around("execution(public * *..ReadArticleService.*(..))")
	public Object cache(ProceedingJoinPoint joinPoint) throws Throwable {
		// getArgs() : 핵심기능의 매개변수 값들
		Integer id = (Integer)joinPoint.getArgs()[0];
		// joinPoint.getSignature().getName() : 핵심 메서드의 이름
		System.out.println("[ArticleCache]"+joinPoint.getSignature().getName()
				+ "(" + id + ") 호출 전");
		Article article = cache.get(id);
		if(article != null) {
			System.out.println("[ArticleCache] cache에서 Article[" + id + "] 가져옴");
			return article;
		}
		// ret : 핵심 메서드의 리턴 값
		Object ret = joinPoint.proceed(); // 다음 메서드 호출
		System.out.printf("[ArticleCache]%s(%d) 호출 후\n"
				, joinPoint.getSignature().getName(), id);
		if(ret != null && ret instanceof Article) {
			cache.put(id, (Article)ret);
			System.out.printf("[ArticleCache] cache에 Article[%d] 추가\n", id);
		}
		return ret;
	}
}
