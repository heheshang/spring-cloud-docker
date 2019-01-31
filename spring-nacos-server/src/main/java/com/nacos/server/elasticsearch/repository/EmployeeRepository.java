package com.nacos.server.elasticsearch.repository;

import com.nacos.server.elasticsearch.entity.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * @author ssk www.8win.com Inc.All rights reserved
 * @version v1.0
 * @date 2019-01-31-上午 10:05
 */
@Component
public interface EmployeeRepository extends ElasticsearchRepository<Employee, String> {

    Employee queryEmployeeById(String id);
}
