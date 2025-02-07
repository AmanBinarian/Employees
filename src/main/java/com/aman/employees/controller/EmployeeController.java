package com.aman.employees.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aman.employees.model.Employee;
import com.aman.employees.service.EmployeeService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {
	 @Autowired
	    private EmployeeService employeeService;

	    // Create or Update Employee
	    @PostMapping("/employee/add")
	    public Employee createOrUpdateEmployee(@RequestBody Employee employee) throws IOException {
	        return employeeService.createOrUpdateEmployee(employee);
	    }

	    // Get Employee by ID
	    @GetMapping("/employee/{id}")
	    public Employee getEmployeeById(@PathVariable Long id) {
	        return employeeService.getEmployeeById(id);
	    }
	    
	    // Get all Employee Data
	    @GetMapping("/getallemployee")
	    public List<Employee> getEmployeeDetails() {
	        return employeeService. getEmployeeDetails();
	    }
}



