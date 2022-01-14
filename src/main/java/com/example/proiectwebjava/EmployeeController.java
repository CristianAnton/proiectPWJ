package com.example.proiectwebjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private TaskService taskService;


    @GetMapping(path = "/employees",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Employee>> getEmployees(){
        Collection<Employee> employees = employeeService.getAllEmployees();
        if(!employees.isEmpty()) { return ResponseEntity.ok(employees); }
        else { return ResponseEntity.noContent().build(); }
    }

    @GetMapping(path = "/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long id) {
        Optional<Employee> employee= employeeService.findEmployeeById(id);
        if(employee.isPresent()) { return ResponseEntity.ok(employee.get()); }
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping(path = "/employees", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addEmployee(String firstName, String lastName){
        Employee employee = new Employee(firstName,lastName);
        employeeService.saveEmployee(employee);
        URI uri = WebMvcLinkBuilder.linkTo(EmployeeController.class).slash("employees").slash(employee.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Void> changeEmployee(@PathVariable("id") long id, @RequestBody Employee entity) {
        Optional<Employee> existingEmployee = employeeService.findEmployeeById(id);
        if(existingEmployee.isPresent())
        {
            existingEmployee.get().update(entity);
            employeeService.saveEmployee(existingEmployee.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping(path = "/employees/{id}")
    public ResponseEntity<Void> removeEmployee(@PathVariable("id") long id){
        Optional<Employee> existingEmployee = employeeService.findEmployeeById(id);
        if(existingEmployee.isPresent())
        {
            employeeService.deleteEmployee(existingEmployee.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @GetMapping(path = "/employees/{employeesId}/departments")
    public ResponseEntity<Department> getEmployeeDepartment(@PathVariable("employeesId") long employeesId){
        Optional<Employee> existingEmployee = employeeService.findEmployeeById(employeesId);
        if(existingEmployee.isPresent())
        {
            Department department = existingEmployee.get().getDepartment();
            if(existingEmployee.get().getDepartment().toString().isEmpty()) { return ResponseEntity.ok(department); }
            else { return ResponseEntity.noContent().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping("/employees/{employeesId}/departments/{departmentsId}")
    public ResponseEntity<List<String>> addEmployeeDepartment(@PathVariable("employeesId") long employeesId, @PathVariable("departmentsId") long departmentsId) {
        Optional<Employee> existingEmployee = employeeService.findEmployeeById(employeesId);
        if(existingEmployee.isPresent())
        {
            Optional<Department> existingDepartment = departmentService.findDepartmentById(departmentsId);
            if(existingDepartment.isPresent())
            {
                existingEmployee.get().setDepartment(existingDepartment.get());
                employeeService.saveEmployee(existingEmployee.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/employees/{employeesId}/departments/{departmentsId}")
    public ResponseEntity<List<String>> removeEmployeeDepartment(@PathVariable("employeesId") long employeesId, @PathVariable("departmentsId") long departmentsId) {
        Optional<Employee> existingEmployee = employeeService.findEmployeeById(employeesId);
        if(existingEmployee.isPresent())
        {
            Optional<Department> existingDepartment = departmentService.findDepartmentById(departmentsId);
            if(existingDepartment.isPresent())
            {
                existingEmployee.get().setDepartment(null);
                employeeService.saveEmployee(existingEmployee.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @GetMapping(path = "/employees/{employeeId}/tasks")
    public ResponseEntity<Set<Task>> getEmployeeTasks(@PathVariable("employeeId") long employeeId){
        Optional<Employee> existingEmployee = employeeService.findEmployeeById(employeeId);
        if(existingEmployee.isPresent())
        {
            Set<Task> tasks = existingEmployee.get().getTasks();
            if(!tasks.isEmpty()) { return ResponseEntity.ok(tasks); }
            else { return ResponseEntity.noContent().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping("/employees/{employeeId}/tasks/{tasksId}")
    public ResponseEntity<List<String>> addEmployeeTasks(@PathVariable("employeeId") long employeeId, @PathVariable("tasksId") long tasksId) {
        Optional<Employee> existingEmployee = employeeService.findEmployeeById(employeeId);
        if(existingEmployee.isPresent())
        {
            Optional<Task> existingTask = taskService.findTaskById(tasksId);
            if(existingTask.isPresent())
            {
                existingEmployee.get().addTask(existingTask.get());
                employeeService.saveEmployee(existingEmployee.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/employees/{employeeId}/tasks/{tasksId}")
    public ResponseEntity<List<String>> removeEmployeeTasks(@PathVariable("employeeId") long employeeId, @PathVariable("tasksId") long tasksId) {
        Optional<Employee> existingEmployee = employeeService.findEmployeeById(employeeId);
        if(existingEmployee.isPresent())
        {
            Optional<Task> existingTask = taskService.findTaskById(tasksId);
            if(existingTask.isPresent())
            {
                existingEmployee.get().removeTask(existingTask.get());
                employeeService.saveEmployee(existingEmployee.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

}
