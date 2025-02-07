package com.aman.employees.model;
import jakarta.persistence.*;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate ID in MySQL
    private Long id;

    private String username;
    private String password;
    private String role;
    private String department;
    private String message;

    public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	// Getters and Setters
    public Long getId() {
        return id;
    }
    
    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}
	
	public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
