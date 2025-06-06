package hello.core.beanfind;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextExtendsFindTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    @DisplayName("부모 타입 조회시, 자식 둘 이상이면 중복 오류")
    void findBeanByParentTypeDuplicate() {
        assertThrows(NoUniqueBeanDefinitionException.class,
                () -> ac.getBean(DiscountPolicy.class));
    }
    @Test
    @DisplayName("부모 타입 조회시, 자식 둘 이상이면 빈 이름 지정")
    void findBeanByParentTypeBeanName() {
        assertThat(ac.getBean("rateDP", DiscountPolicy.class))
                .isInstanceOf(RateDiscountPolicy.class);
    }
    @Test
    @DisplayName("하위 타입으로 조회")
    void findBeanBySubType() {
        assertThat(ac.getBean(RateDiscountPolicy.class))
                .isInstanceOf(RateDiscountPolicy.class);
    }
    @Test
    @DisplayName("부모 타입으로 모두 조회")
    void findAllBeanByParentType() {
        Map<String, DiscountPolicy> map = ac.getBeansOfType(DiscountPolicy.class);
        assertThat(map.size()).isEqualTo(2);
        for (String key : map.keySet()) {
            System.out.println("key = " + key + ", value = " + map.get(key));
        }
    }

    @Test
    @DisplayName("부모 타입 모두 조회- Object")
    void findAllBeanByObjectType() {
        Map<String, Object> map = ac.getBeansOfType(Object.class);
        for (String key : map.keySet()) {
            System.out.println("key = " + key + ", value = " + map.get(key));
        }
    }

    @Configuration
    static class TestConfig {
        @Bean
        public DiscountPolicy rateDP() {
            return new RateDiscountPolicy();
        }
        @Bean
        public DiscountPolicy fixDP() {
            return new FixDiscountPolicy();
        }

    }
}
