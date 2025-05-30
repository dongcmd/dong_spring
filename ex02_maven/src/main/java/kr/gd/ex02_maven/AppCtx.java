package kr.gd.ex02_maven;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration // spring 환경을 설정하는 프로그램
@ComponentScan(basePackages = {"kr.gd.ex02_maven"})
public class AppCtx {
	
}
