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

}
