package com.example.proiectwebjava;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "boss", nullable = false)
    private String boss;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( name = "work", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "employee_id") )
    private Set<Employee> employees;

    public Task() {

    }

    public Task(String description, String boss) {
        this.description = description;
        this.boss = boss;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
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

    public void update(Task task){
        this.description = task.getDescription();
        this.boss = task.getBoss();
    }

}
