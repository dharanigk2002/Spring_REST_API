package com.example.employee_api.rest;

import com.example.employee_api.entity.Employee;
import com.example.employee_api.entity.EmployeeNotFoundException;
import com.example.employee_api.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {
    private final EmployeeService employeeService;
    private final JsonMapper mapper;

    @Autowired
    public EmployeeRestController(EmployeeService employeeService, JsonMapper mapper) {
        this.mapper=mapper;
        this.employeeService= employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeService.findAll();
    }

    @GetMapping("/employee/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {
        Employee employee=employeeService.findById(employeeId);
        if(employee==null)
            throw new EmployeeNotFoundException("Employee with id " + employeeId + " is not found");
        return employee;
    }

    @PostMapping("/employee")
    public Employee saveEmployee(@RequestBody Employee employee) {
        employee.setId(0);
        Employee savedEmp = employeeService.save(employee);
        return savedEmp;
    }

    @PutMapping("/employee")
    public Employee updateEmployee(@RequestBody Employee employee) {
        Employee updatedEmployee=employeeService.save(employee);
        return updatedEmployee;
    }

    @PatchMapping("/employee/{employeeId}")
    public Employee updateEmployee(@PathVariable int employeeId, @RequestBody Map<String, Object> patchPayload) {
        Employee employee=employeeService.findById(employeeId);
        if(employee==null)
            throw new EmployeeNotFoundException("Employee with id "+employeeId+" is not found");
        if(patchPayload.containsKey("id"))
            throw new RuntimeException("Employee id is not allowed in request body");
        Employee patchedEmployee=mapper.updateValue(employee, patchPayload);
        Employee updatedEmp=employeeService.save(patchedEmployee);
        return updatedEmp;
    }

    @DeleteMapping("/employee/{employeeId}")
    public Map<String, String> deleteEmployee(@PathVariable int employeeId) {
        employeeService.deleteById(employeeId);
        return Map.of("status", String.valueOf(HttpStatus.NO_CONTENT.value()), "message", "Successfully deleted employee");
    }
}
