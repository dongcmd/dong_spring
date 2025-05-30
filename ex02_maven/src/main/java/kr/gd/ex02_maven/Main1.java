package kr.gd.ex02_maven;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main1 {
	public static void main(String[] args) {
		ApplicationContext ctx =
				new AnnotationConfigApplicationContext(AppCtx.class);
		Executor exec = ctx.getBean("executor", Executor.class);
		exec.addUnit(new WorkUnit());
		exec.addUnit(new WorkUnit());
		
		Worker worker = ctx.getBean("worker", Worker.class);
		System.out.println(worker);
	}
}
