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
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private DepartmentService departmentService;

    @GetMapping(path = "/companies",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Company>> getCompanies(){
        Collection<Company> companies = companyService.getAllCompanies();
        if(!companies.isEmpty()) { return ResponseEntity.ok(companies); }
        else { return ResponseEntity.noContent().build(); }
    }

    @GetMapping(path = "/companies/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Company> getCompany(@PathVariable("id") long id) {
        Optional<Company> company= companyService.findCompanyById(id);
        if(company.isPresent()) { return ResponseEntity.ok(company.get()); }
        else { return ResponseEntity.notFound().build(); }
    }

    @PostMapping(path = "/companies", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addCompany(String name, String city, String specialty){
        Company company = new Company(name,city,specialty);
        companyService.saveCompany(company);
        URI uri = WebMvcLinkBuilder.linkTo(CompanyController.class).slash("companies").slash(company.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<Void> changeCompany(@PathVariable("id") long id, @RequestBody Company entity) {
        Optional<Company> existingCompany = companyService.findCompanyById(id);
        if(existingCompany.isPresent())
        {
            existingCompany.get().update(entity);
            companyService.saveCompany(existingCompany.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping(path = "/companies/{id}")
    public ResponseEntity<Void> removeCompany(@PathVariable("id") long id){
        Optional<Company> existingCompany = companyService.findCompanyById(id);
        if(existingCompany.isPresent())
        {
            companyService.deleteCompany(existingCompany.get());
            return ResponseEntity.noContent().build();
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @GetMapping(path = "/companies/{companyId}/departments")
    public ResponseEntity<Set<Department>> getCompanyDepartments(@PathVariable("companyId") long companyId){
        Optional<Company> existingCompany = companyService.findCompanyById(companyId);
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
        Optional<Company> existingCompany = companyService.findCompanyById(companyId);
        if(existingCompany.isPresent())
        {
            Optional<Department> existingDepartment = departmentService.findDepartmentById(departmentId);
            if(existingDepartment.isPresent())
            {
                existingCompany.get().addDepartment(existingDepartment.get());
                companyService.saveCompany(existingCompany.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

    @DeleteMapping("/companies/{companyId}/departments/{departmentId}")
    public ResponseEntity<List<String>> removeCompanyDepartment(@PathVariable("companyId") long companyId, @PathVariable("departmentId") long departmentId) {
        Optional<Company> existingCompany = companyService.findCompanyById(companyId);
        if(existingCompany.isPresent())
        {
            Optional<Department> existingDepartment = departmentService.findDepartmentById(departmentId);
            if(existingDepartment.isPresent())
            {
                existingCompany.get().removeDepartment(existingDepartment.get());
                companyService.saveCompany(existingCompany.get());
                return ResponseEntity.noContent().build();
            }
            else { return ResponseEntity.badRequest().build(); }
        }
        else { return ResponseEntity.notFound().build(); }
    }

}
