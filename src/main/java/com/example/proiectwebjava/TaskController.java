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
public class TaskController {

    private TaskService taskService;
    private EmployeeService employeeService;

    @GetMapping(path = "/tasks",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Task>> getTasks(){
        Collection<Task> tasks = taskService.getAllTasks();
        if(!tasks.isEmpty()) { return ResponseEntity.ok(tasks); }
        else { return ResponseEntity.noContent().build(); }
    }

    @GetMapping(path = "/tasks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> getTaskById(@PathVariable("id") long id) {
        Optional<Task> task= taskService.findTaskById(id);
        if(task.isPresent()) { return ResponseEntity.ok(task.get()); }
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping(path = "/tasks", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addTask(String description, String boss){
        Task task = new Task(description,boss);
        taskService.saveTask(task);
        URI uri = WebMvcLinkBuilder.linkTo(MainController.class).slash("tasks").slash(task.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<Void> changeTask(@PathVariable("id") long id, @RequestBody Task entity) {
        Optional<Task> existingTask = taskService.findTaskById(id);
        if(existingTask.isPresent())
        {
            existingTask.get().update(entity);
            taskService.saveTask(existingTask.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping(path = "/tasks/{id}")
    public ResponseEntity<Void> removeTask(@PathVariable("id") long id){
        Optional<Task> existingTask = taskService.findTaskById(id);
        if(existingTask.isPresent())
        {
            taskService.saveTask(existingTask.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @GetMapping(path = "/tasks/{taskId}/employees")
    public ResponseEntity<Set<Employee>> getTaskEmployees(@PathVariable("taskId") long taskId){
        Optional<Task> existingTask = taskService.findTaskById(taskId);
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
        Optional<Task> existingTask = taskService.findTaskById(taskId);
        if(existingTask.isPresent())
        {
            Optional<Employee> existingEmployee = employeeService.findEmployeeById(employeeId);
            if(existingEmployee.isPresent())
            {
                existingTask.get().addEmployee(existingEmployee.get());
                taskService.saveTask(existingTask.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/tasks/{taskId}/employees/{employeeId}")
    public ResponseEntity<List<String>> removeTaskEmployees(@PathVariable("taskId") long taskId, @PathVariable("employeeId") long employeeId) {
        Optional<Task> existingTask = taskService.findTaskById(taskId);
        if(existingTask.isPresent())
        {
            Optional<Employee> existingEmployee = employeeService.findEmployeeById(employeeId);
            if(existingEmployee.isPresent())
            {
                existingTask.get().removeEmployee(existingEmployee.get());
                taskService.saveTask(existingTask.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

}
