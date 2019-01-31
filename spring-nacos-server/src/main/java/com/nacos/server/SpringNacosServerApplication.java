package com.nacos.server;

import com.nacos.server.elasticsearch.entity.Employee;
import com.nacos.server.elasticsearch.repository.EmployeeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringNacosServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringNacosServerApplication.class, args);
    }


    @Log4j2
    @RestController
    static class TestController {

        @Autowired
        private EmployeeRepository employeeRepository;

        @GetMapping("/hello")
        public String hello(@RequestParam String name) {

            log.info("invoked name = " + name);
            return "hello " + name;
        }

        @PostMapping("/insertELK")
        public String insertElk(@RequestParam String id,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam String age,
                                @RequestParam String about
        ) {

            Employee employee = new Employee();
            employee.setId(id);
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setAge(Integer.parseInt(age));
            employee.setAbout(about);
            log.info("保存数据到ELK 中" + employee.toString());
            this.employeeRepository.save(employee);

            return "suc";


        }
    }


}

