package kr.gd.ex02_maven;

import org.springframework.stereotype.Component;

@Component
public class Worker {
	public void work(WorkUnit unit) {
		System.out.println(this + " : " + unit);
	}
}
