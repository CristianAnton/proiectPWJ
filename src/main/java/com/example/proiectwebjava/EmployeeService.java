package com.example.proiectwebjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository ;


    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Collection<Employee> getAllEmployees() {
        return (Collection<Employee>) employeeRepository.findAll();
    }

    public Optional<Employee> findEmployeeById(long id){
        return employeeRepository.findById(id);
    }

    public void saveEmployee (Employee employee) {
        employeeRepository.save(employee);
    }

    public void deleteEmployee (Employee employee) {
        employeeRepository.delete(employee);
    }

}
