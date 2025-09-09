package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private List<Employee> employees = new ArrayList<>();
    private int id = 0;

    public void clear() {
        employees.clear();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        int id = ++this.id;
        Employee newEmployee = new Employee(id, employee.name(), employee.age(), employee.gender(), employee.salary());
        employees.add(newEmployee);
        return newEmployee;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee get(@PathVariable int id) {
        return employees.stream()
                .filter(employee -> employee.id() == id)
                .findFirst()
                .orElse(null);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> get(@RequestParam(required = false) String gender,
                              @RequestParam(required = false) Integer page,
                              @RequestParam(required = false) Integer size) {
        if (gender != null) {
            return employees.stream()
                    .filter(e -> e.gender().compareToIgnoreCase(gender) == 0)
                    .toList();
        }
        if (page != null && size != null) {
            int start = (page - 1) * size;
            int end = Math.min(start + size, employees.size());
            if (start >= employees.size()) {
                return new ArrayList<>();
            }
            return employees.subList(start, end);
        }
        return employees;
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee updateEmployee(@PathVariable Integer id, @RequestBody Employee updatedEmployee) {
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            if (Objects.equals(employee.id(), id)) {
                Employee newEmployee = new Employee(
                        id,
                        updatedEmployee.name(),
                        updatedEmployee.age(),
                        updatedEmployee.gender(),
                        updatedEmployee.salary()
                );
                employees.set(i, newEmployee);
                return newEmployee;
            }
        }
        return null;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Integer id) {
        for (int i = 0; i < employees.size(); i++) {
            Employee employee = employees.get(i);
            if (Objects.equals(employee.id(), id)) {
                employees.remove(i);
                break;
            }
        }
    }
}
