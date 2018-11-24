package com.wf.store;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.transaction.Transactional;
import java.util.Arrays;

@SpringBootApplication(scanBasePackageClasses = {Controller.class})
@Log4j2
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Transactional
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {

        return args -> {

            log.info("Let's inspect the beans provided by Spring Boot:");
            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                log.info(beanName);
            }

        };
    }

//    @Bean
//    public CommandLineRunner demo(CustomerRepository repository) {
//        return (args) -> {
//            // save a couple of customers
//            repository.save(new Customer("Jack", "Bauer"));
//            repository.save(new Customer("Chloe", "O'Brian"));
//            repository.save(new Customer("Kim", "Bauer"));
//            repository.save(new Customer("David", "Palmer"));
//            repository.save(new Customer("Michelle", "Dessler"));
//
//            // fetch all customers
//            log.info("Customers found with findAll():");
//            log.info("-------------------------------");
//            for (Customer customer : repository.findAll()) {
//                log.info(customer.toString());
//            }
//            log.info("");
//
//            // fetch an individual customer by ID
//            repository.findById(1L)
//                    .ifPresent(customer -> {
//                        log.info("Customer found with findById(1L):");
//                        log.info("--------------------------------");
//                        log.info(customer.toString());
//                        log.info("");
//                    });
//
//            // fetch customers by last name
//            log.info("Customer found with findByLastName('Bauer'):");
//            log.info("--------------------------------------------");
//            repository.findCustomerBySurname("Bauer").forEach(bauer -> {
//                log.info(bauer.toString());
//            });
//        };
//    }
}
