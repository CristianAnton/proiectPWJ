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
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Collection<Company> getAllCompanies() {
        return (Collection<Company>) companyRepository.findAll();
    }

    public Optional<Company> findCompanyById(long id){
        return companyRepository.findById(id);
    }

    public void saveCompany (Company company) {
        companyRepository.save(company);
    }

    public void deleteCompany (Company company) {
        companyRepository.delete(company);
    }

}
