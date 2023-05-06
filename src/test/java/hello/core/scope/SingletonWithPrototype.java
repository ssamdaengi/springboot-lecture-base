package hello.core.scope;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

public class SingletonWithPrototype {

    @Test
    void singletoWithPrototypeTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean bean1 = ac.getBean(PrototypeBean.class);
        bean1.addCount();
        Assertions.assertThat(bean1.getCount()).isEqualTo(1);

        PrototypeBean bean2 = ac.getBean(PrototypeBean.class);
        bean2.addCount();
        Assertions.assertThat(bean2.getCount()).isEqualTo(1);
    }

    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class, ClientBean.class );
        ClientBean bean1 = ac.getBean(ClientBean.class);
        int cnt1 = bean1.logic();
        Assertions.assertThat(cnt1).isEqualTo(1);

        ClientBean bean2 = ac.getBean(ClientBean.class);
        int cnt2 = bean2.logic();
        Assertions.assertThat(cnt2).isEqualTo(1);

    }

    @Scope
    @RequiredArgsConstructor
    static class ClientBean {
//        private final PrototypeBean prototypeBean;
/*

        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }
*/
/*      @Autowired
        private final ApplicationContext ac;
*/
        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanObjectProvider;

//        @Autowired
//        private Provider<PrototypeBean> prototypeBeanProvider;
        public int logic() {
//            PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
          PrototypeBean prototypeBean = prototypeBeanObjectProvider.getObject();
//            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount() {
            count++;
        }


        public int getCount() {
            return count;
        }

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init = " + this);
        }

        @PreDestroy
        public void destory() {
            System.out.println("PrototypeBean.destroy" + this);
        }
    }
}
