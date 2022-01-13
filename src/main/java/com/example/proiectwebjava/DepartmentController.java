package com.example.proiectwebjava;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequestMapping("/api")
public class DepartmentController {

    private DepartmentService departmentService;
    private EmployeeService employeeService;

    @GetMapping(path = "/departments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Department>> getDepartments() {
        Collection<Department> departments = departmentService.getAllDepartments();
        if(!departments.isEmpty()){ return ResponseEntity.ok(departments); }
        else { return ResponseEntity.noContent().build(); }
    }

    @GetMapping(path = "/departments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Department> getDepartmentById(@PathVariable("id") long id){
        Optional<Department> existingDepartment = departmentService.findDepartmentById(id);
        if(existingDepartment.isPresent()){ return ResponseEntity.ok(existingDepartment.get());}
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping(path = "/departments",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addDepartment (String name,String description) {
        Department department = new Department(name,description);
        departmentService.saveDepartment(department);
        URI uri = WebMvcLinkBuilder.linkTo(MainController.class).slash("departments").slash(department.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(path = "/departments/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeDepartment(@PathVariable("id") long id, @RequestBody Department department){
        Optional<Department> existingDepartment = departmentService.findDepartmentById(id);
        if(existingDepartment.isPresent()) {
            existingDepartment.get().update(department);
            departmentService.saveDepartment(existingDepartment.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping(path = "/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("id") long id){
        Optional<Department> existingDepartment = departmentService.findDepartmentById(id);
        if(existingDepartment.isPresent()) {
            departmentService.deleteDepartment(existingDepartment.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @GetMapping(path = "/departments/{id}/companies")
    public ResponseEntity<Set<Company>> getDepartmentCompanies(@PathVariable("id") long id){
        Optional<Department> existingDepartment = departmentService.findDepartmentById(id);
        if(existingDepartment.isPresent()) {
            Set<Company> companies = existingDepartment.get().getCompanies();
            if(!companies.isEmpty()){ return ResponseEntity.ok(companies); }
            else { return ResponseEntity.noContent().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @GetMapping(path = "/departments/{departmentId}/employees")
    public ResponseEntity<Set<Employee>> getDepartmentsEmployees(@PathVariable("departmentId") long departmentId){
        Optional<Department> existingDepartment = departmentService.findDepartmentById(departmentId);
        if(existingDepartment.isPresent())
        {
            Set<Employee> employees = existingDepartment.get().getEmployees();
            if(!employees.isEmpty()) { return ResponseEntity.ok(employees); }
            else { return ResponseEntity.noContent().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping("/departments/{departmentId}/employees/{employeesId}")
    public ResponseEntity<List<String>> addDepartmentsEmployees(@PathVariable("departmentId") long departmentId, @PathVariable("employeesId") long employeesId) {
        Optional<Department> existingDepartment = departmentService.findDepartmentById(departmentId);
        if(existingDepartment.isPresent())
        {
            Optional<Employee> existingEmployee = employeeService.findEmployeeById(employeesId);
            if(existingEmployee.isPresent())
            {
                existingDepartment.get().addEmployee(existingEmployee.get());
                departmentService.saveDepartment(existingDepartment.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/departments/{departmentId}/employees/{employeesId}")
    public ResponseEntity<List<String>> removeDepartmentsEmployees(@PathVariable("departmentId") long departmentId, @PathVariable("employeesId") long employeesId) {
        Optional<Department> existingDepartment = departmentService.findDepartmentById(departmentId);
        if(existingDepartment.isPresent())
        {
            Optional<Employee> existingEmployee = employeeService.findEmployeeById(employeesId);
            if(existingEmployee.isPresent())
            {
                existingDepartment.get().removeEmployee(existingEmployee.get());
                departmentService.saveDepartment(existingDepartment.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }


}
