package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    private List<Company> companies = new ArrayList<>();
    private int id = 0;

    public void clear() {
        companies.clear();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company create(@RequestBody Company company) {
        int id = ++this.id;
        Company newCompany = new Company(id, company.name(), company.address());
        companies.add(newCompany);
        return newCompany;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Company get(@PathVariable int id) {
        return companies.stream()
                .filter(company -> company.id() == id)
                .findFirst()
                .orElse(null);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Company> get(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        List<Company> result = new ArrayList<>(companies);
        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, companies.size());
            if (start >= companies.size()) {
                return new ArrayList<>();
            }
            result = companies.subList(start, end);
        }
        return result;
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Company updateEmployee(@PathVariable Integer id, @RequestBody Company company) {
        return companies.stream()
                .filter(c -> Objects.equals(c.id(), id))
                .findFirst()
                .map(originalCompany -> {
                    Company newCompany = new Company(
                            id,
                            company.name(),
                            company.address()
                    );
                    companies.set(companies.indexOf(originalCompany), newCompany);
                    return newCompany;
                })
                .orElse(null);
    }

}
