package kr.gd.ex02_maven;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/*
 * @Component처리된 클래스의 객체가 @ComponentScan처리된 클래스에 저장됨
 */
@Component
public class Executor {
	// 컨테이너에서 Worker 타입의 객체를 worker에 주입.
	// 의존성 주입(Dependency Injection)
	@Autowired
	private Worker worker;
	public void addUnit(WorkUnit unit) {
		worker.work(unit);
	}
}
