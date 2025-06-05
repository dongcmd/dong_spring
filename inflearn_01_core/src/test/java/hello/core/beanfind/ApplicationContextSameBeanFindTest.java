package hello.core.beanfind;

import hello.core.AppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextSameBeanFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SameBeanConfig.class);

    @Test
    @DisplayName("타입으로 조회시 같은 타입 둘 이상이면 중복 오류")
    void findBeanByTypeDuplicate() {
//        MemberRepository bean =  ac.getBean(MemberRepository.class);
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(MemberRepository.class));
    }

    @Test
    @DisplayName("타입으로 조회시 같은 타입 둘 이상 일 경우 빈 이름으로 지정")
    void findBeanByName() {
        MemberRepository bean = ac.getBean("mr1", MemberRepository.class);
        assertThat(bean).isInstanceOf(MemberRepository.class);
    }

    @Test
    @DisplayName("특정 타입 모두 조회")
    void findAllBeanByType() {
        Map<String, MemberRepository> map = ac.getBeansOfType(MemberRepository.class);
        for (String key : map.keySet()) {
            System.out.println("key = " + key + ", V = " + map.get(key));
        }
        System.out.println(map);
        assertThat(map.size()).isEqualTo(2);
    }


    @Configuration
    static class SameBeanConfig {
        @Bean
        public MemberRepository mr1() {
            return new MemoryMemberRepository();
        }

        @Bean
        public MemberRepository mr2() {
            return new MemoryMemberRepository();
        }
    }
}
