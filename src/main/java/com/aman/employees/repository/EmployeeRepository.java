package com.aman.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aman.employees.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}
