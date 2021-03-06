package com.example.proiectwebjava;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="department")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( name = "team", joinColumns = @JoinColumn(name = "department_id"), inverseJoinColumns = @JoinColumn(name = "company_id") )
    private Set<Company> companies;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id")
    private Set<Employee> employees;

    public Department() {

    }

    public Department(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee){
        employees.add(employee);
    }

    public void removeEmployee(Employee employee){
        employees.remove(employee);
    }

    public void update(Department department){
        this.name = department.getName();
        this.description = department.getDescription();
    }

}
