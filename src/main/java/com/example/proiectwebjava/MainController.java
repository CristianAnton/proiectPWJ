package com.example.proiectwebjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private TaskRepository taskRepository;

    @GetMapping(path = "/companies",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Company>> getCompanies(){
        Collection<Company> companies = (Collection<Company>) companyRepository.findAll();
        if(!companies.isEmpty()) { return ResponseEntity.ok(companies); }
        else { return ResponseEntity.noContent().build(); }
    }

    @GetMapping(path = "/companies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<Company> getCompany(@PathVariable("id") long id) {
     Optional<Company> company= companyRepository.findById(id);
        if(company.isPresent()) { return ResponseEntity.ok(company.get()); }
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping(path = "/companies", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addCompany(String name, String city, String specialty){
    Company company = new Company(name,city,specialty);
    companyRepository.save(company);
    URI uri = WebMvcLinkBuilder.linkTo(MainController.class).slash("companies").slash(company.getId()).toUri();
    return ResponseEntity.created(uri).build();
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<Void> changeCompany(@PathVariable("id") long id, @RequestBody Company entity) {
     Optional<Company> existingCompany = companyRepository.findById(id);
     if(existingCompany.isPresent())
     {
         existingCompany.get().update(entity);
         companyRepository.save(existingCompany.get());
         return ResponseEntity.noContent().build();
     }
     else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping(path = "/companies/{id}")
    public ResponseEntity<Void> removeCompany(@PathVariable("id") long id){
        Optional<Company> existingCompany = companyRepository.findById(id);
        if(existingCompany.isPresent())
        {
            companyRepository.delete(existingCompany.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @GetMapping(path = "/companies/{companyId}/departments")
    public ResponseEntity<Set<Department>> getCompanyDepartments(@PathVariable("companyId") long companyId){
        Optional<Company> existingCompany = companyRepository.findById(companyId);
        if(existingCompany.isPresent())
        {
            Set<Department> departments = existingCompany.get().getDepartments();
            if(!departments.isEmpty()) { return ResponseEntity.ok(departments); }
            else { return ResponseEntity.noContent().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping("/companies/{companyId}/departments/{departmentId}")
    public ResponseEntity<List<String>> addCompanyDepartment(@PathVariable("companyId") long companyId, @PathVariable("departmentId") long departmentId) {
        Optional<Company> existingCompany = companyRepository.findById(companyId);
        if(existingCompany.isPresent())
        {
            Optional<Department> existingDepartment = departmentRepository.findById(departmentId);
            if(existingDepartment.isPresent())
            {
                existingCompany.get().addDepartment(existingDepartment.get());
                companyRepository.save(existingCompany.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/companies/{companyId}/departments/{departmentId}")
    public ResponseEntity<List<String>> removeCompanyDepartment(@PathVariable("companyId") long companyId, @PathVariable("departmentId") long departmentId) {
        Optional<Company> existingCompany = companyRepository.findById(companyId);
        if(existingCompany.isPresent())
        {
            Optional<Department> existingDepartment = departmentRepository.findById(departmentId);
            if(existingDepartment.isPresent())
            {
                existingCompany.get().removeDepartment(existingDepartment.get());
                companyRepository.save(existingCompany.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @GetMapping(path = "/departments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Department>> getDepartments() {
     Collection<Department> departments = (Collection<Department>) departmentRepository.findAll();
     if(!departments.isEmpty()){ return ResponseEntity.ok(departments); }
     else { return ResponseEntity.noContent().build(); }
    }

    @GetMapping(path = "/departments/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Department> getDepartmentById(@PathVariable("id") long id){
        Optional<Department> existingDepartment = departmentRepository.findById(id);
        if(existingDepartment.isPresent()){ return ResponseEntity.ok(existingDepartment.get());}
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping(path = "/departments",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
   public ResponseEntity<Void> addDepartment (String name,String description) {
        Department department = new Department(name,description);
        departmentRepository.save(department);
        URI uri = WebMvcLinkBuilder.linkTo(MainController.class).slash("departments").slash(department.getId()).toUri();
        return ResponseEntity.created(uri).build();
   }

   @PutMapping(path = "/departments/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
   public ResponseEntity<Void> changeDepartment(@PathVariable("id") long id, @RequestBody Department department){
       Optional<Department> existingDepartment = departmentRepository.findById(id);
       if(existingDepartment.isPresent()) {
           existingDepartment.get().update(department);
           departmentRepository.save(existingDepartment.get());
           return ResponseEntity.noContent().build();
       }
       else { return ResponseEntity.notFound().build(); }
   }

    @DeleteMapping(path = "/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable("id") long id){
        Optional<Department> existingDepartment = departmentRepository.findById(id);
        if(existingDepartment.isPresent()) {
            departmentRepository.delete(existingDepartment.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @GetMapping(path = "/departments/{id}/companies")
    public ResponseEntity<Set<Company>> getDepartmentCompanies(@PathVariable("id") long id){
        Optional<Department> existingDepartment = departmentRepository.findById(id);
        if(existingDepartment.isPresent()) {
            Set<Company> companies = existingDepartment.get().getCompanies();
            if(!companies.isEmpty()){ return ResponseEntity.ok(companies); }
            else { return ResponseEntity.noContent().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    // employee

    @GetMapping(path = "/employees",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Employee>> getEmployees(){
        Collection<Employee> employees = (Collection<Employee>) employeeRepository.findAll();
        if(!employees.isEmpty()) { return ResponseEntity.ok(employees); }
        else { return ResponseEntity.noContent().build(); }
    }

    @GetMapping(path = "/employees/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") long id) {
        Optional<Employee> employee= employeeRepository.findById(id);
        if(employee.isPresent()) { return ResponseEntity.ok(employee.get()); }
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping(path = "/employees", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addEmployee(String firstName, String lastName){
        Employee employee = new Employee(firstName,lastName);
        employeeRepository.save(employee);
        URI uri = WebMvcLinkBuilder.linkTo(MainController.class).slash("employees").slash(employee.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Void> changeEmployee(@PathVariable("id") long id, @RequestBody Employee entity) {
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if(existingEmployee.isPresent())
        {
            existingEmployee.get().update(entity);
            employeeRepository.save(existingEmployee.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping(path = "/employees/{id}")
    public ResponseEntity<Void> removeEmployee(@PathVariable("id") long id){
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if(existingEmployee.isPresent())
        {
            employeeRepository.delete(existingEmployee.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    // task

    @GetMapping(path = "/tasks",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Task>> getTasks(){
        Collection<Task> tasks = (Collection<Task>) taskRepository.findAll();
        if(!tasks.isEmpty()) { return ResponseEntity.ok(tasks); }
        else { return ResponseEntity.noContent().build(); }
    }

    @GetMapping(path = "/tasks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> getTaskById(@PathVariable("id") long id) {
        Optional<Task> task= taskRepository.findById(id);
        if(task.isPresent()) { return ResponseEntity.ok(task.get()); }
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping(path = "/tasks", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addTask(String description, String boss){
        Task task = new Task(description,boss);
        taskRepository.save(task);
        URI uri = WebMvcLinkBuilder.linkTo(MainController.class).slash("tasks").slash(task.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Void> changeTask(@PathVariable("id") long id, @RequestBody Task entity) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if(existingTask.isPresent())
        {
            existingTask.get().update(entity);
            taskRepository.save(existingTask.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping(path = "/tasks/{id}")
    public ResponseEntity<Void> removeTask(@PathVariable("id") long id){
        Optional<Task> existingTask = taskRepository.findById(id);
        if(existingTask.isPresent())
        {
            taskRepository.delete(existingTask.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    //remaining relations

    @GetMapping(path = "/departments/{departmentId}/employees")
    public ResponseEntity<Set<Employee>> getDepartmentsEmployees(@PathVariable("departmentId") long departmentId){
        Optional<Department> existingDepartment = departmentRepository.findById(departmentId);
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
        Optional<Department> existingDepartment = departmentRepository.findById(departmentId);
        if(existingDepartment.isPresent())
        {
            Optional<Employee> existingEmployee = employeeRepository.findById(employeesId);
            if(existingEmployee.isPresent())
            {
                existingDepartment.get().addEmployee(existingEmployee.get());
                departmentRepository.save(existingDepartment.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/departments/{departmentId}/employees/{employeesId}")
    public ResponseEntity<List<String>> removeDepartmentsEmployees(@PathVariable("departmentId") long departmentId, @PathVariable("employeesId") long employeesId) {
        Optional<Department> existingDepartment = departmentRepository.findById(departmentId);
        if(existingDepartment.isPresent())
        {
            Optional<Employee> existingEmployee = employeeRepository.findById(employeesId);
            if(existingEmployee.isPresent())
            {
                existingDepartment.get().removeEmployee(existingEmployee.get());
                departmentRepository.save(existingDepartment.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    //employee department

    @GetMapping(path = "/employees/{employeesId}/departments")
    public ResponseEntity<Department> getEmployeeDepartment(@PathVariable("employeesId") long employeesId){
        Optional<Employee> existingEmployee = employeeRepository.findById(employeesId);
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
        Optional<Employee> existingEmployee = employeeRepository.findById(employeesId);
        if(existingEmployee.isPresent())
        {
            Optional<Department> existingDepartment = departmentRepository.findById(departmentsId);
            if(existingDepartment.isPresent())
            {
                existingEmployee.get().setDepartment(existingDepartment.get());
                employeeRepository.save(existingEmployee.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/employees/{employeesId}/departments/{departmentsId}")
    public ResponseEntity<List<String>> removeEmployeeDepartment(@PathVariable("employeesId") long employeesId, @PathVariable("departmentsId") long departmentsId) {
        Optional<Employee> existingEmployee = employeeRepository.findById(employeesId);
        if(existingEmployee.isPresent())
        {
            Optional<Department> existingDepartment = departmentRepository.findById(departmentsId);
            if(existingDepartment.isPresent())
            {
                existingEmployee.get().setDepartment(null);
                employeeRepository.save(existingEmployee.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    //employee tasks

    @GetMapping(path = "/employees/{employeeId}/tasks")
    public ResponseEntity<Set<Task>> getEmployeeTasks(@PathVariable("employeeId") long employeeId){
        Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);
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
        Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);
        if(existingEmployee.isPresent())
        {
            Optional<Task> existingTask = taskRepository.findById(tasksId);
            if(existingTask.isPresent())
            {
                existingEmployee.get().addTask(existingTask.get());
                employeeRepository.save(existingEmployee.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/employees/{employeeId}/tasks/{tasksId}")
    public ResponseEntity<List<String>> removeEmployeeTasks(@PathVariable("employeeId") long employeeId, @PathVariable("tasksId") long tasksId) {
        Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);
        if(existingEmployee.isPresent())
        {
            Optional<Task> existingTask = taskRepository.findById(tasksId);
            if(existingTask.isPresent())
            {
                existingEmployee.get().removeTask(existingTask.get());
                employeeRepository.save(existingEmployee.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    //tasks employees

    @GetMapping(path = "/tasks/{taskId}/employees")
    public ResponseEntity<Set<Employee>> getTaskEmployees(@PathVariable("taskId") long taskId){
        Optional<Task> existingTask = taskRepository.findById(taskId);
        if(existingTask.isPresent())
        {
            Set<Employee> employees = existingTask.get().getEmployees();
            if(!employees.isEmpty()) { return ResponseEntity.ok(employees); }
            else { return ResponseEntity.noContent().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping("/tasks/{taskId}/employees/{employeeId}")
    public ResponseEntity<List<String>> addTaskEmployees(@PathVariable("taskId") long taskId, @PathVariable("employeeId") long employeeId) {
        Optional<Task> existingTask = taskRepository.findById(taskId);
        if(existingTask.isPresent())
        {
            Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);
            if(existingEmployee.isPresent())
            {
                existingTask.get().addEmployee(existingEmployee.get());
                taskRepository.save(existingTask.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/tasks/{taskId}/employees/{employeeId}")
    public ResponseEntity<List<String>> removeTaskEmployees(@PathVariable("taskId") long taskId, @PathVariable("employeeId") long employeeId) {
        Optional<Task> existingTask = taskRepository.findById(taskId);
        if(existingTask.isPresent())
        {
            Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);
            if(existingEmployee.isPresent())
            {
                existingTask.get().removeEmployee(existingEmployee.get());
                taskRepository.save(existingTask.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

}
