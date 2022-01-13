package com.example.proiectwebjava;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    private static Optional<Company> company;
    private static long id;
    private static long size;

    @BeforeAll
    public static void setup(){
        company = Optional.of(new Company("Ipsos","Bucuresti", "Market Research"));
        id = 3;
        size=6;
    }

    /*
    @Test
    @DisplayName("Test get companies")
    public void testGetAllCompanies() {
        Assert.assertEquals(size, companyService.getAllCompanies().size());
    }

    @Test
    @DisplayName("Test find by id")
    public void testFindCompanyById() {
        Assert.assertEquals(company, companyService.findCompanyById(id));
    }

 */
}
