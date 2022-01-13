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
public class DepartmentService {


    @Autowired
    private DepartmentRepository departmentRepository;


    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Collection<Department> getAllDepartments() {
        return (Collection<Department>) departmentRepository.findAll();
    }

    public Optional<Department> findDepartmentById(long id){
        return departmentRepository.findById(id);
    }

    public void saveDepartment (Department department) {
        departmentRepository.save(department);
    }

    public void deleteDepartment (Department department) {
        departmentRepository.delete(department);
    }

}
