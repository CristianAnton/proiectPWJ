package com.example.proiectwebjava;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="company")
public class Company {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(name = "name", nullable = false)
        private String name;

        @Column(name = "city", nullable = false)
        private String city;

        @Column(name = "specialty", nullable = false)
        private String specialty;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable( name = "team", joinColumns = @JoinColumn(name = "company_id"), inverseJoinColumns = @JoinColumn(name = "department_id") )
        private Set<Department> departments;

        public Company(){

        }

        public Company(String name, String city, String specialty){
         this.name = name;
         this.city = city;
         this.specialty = specialty;
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

        public String getCity() {
                return city;
        }

        public void setCity(String city) {
                this.city = city;
        }

        public String getSpecialty() {
                return specialty;
        }

        public void setSpecialty(String specialty) {
                this.specialty = specialty;
        }

        public Set<Department> getDepartments() {
                return departments;
        }

        public void setDepartments(Set<Department> departments) {
                this.departments = departments;
        }

        public void addDepartment(Department department){
                departments.add(department);
        }

        public void removeDepartment(Department department){
                departments.remove(department);
        }

        public void update(Company company){
                this.name = company.getName();
                this.city = company.getCity();
                this.specialty = company.getSpecialty();
        }
}

