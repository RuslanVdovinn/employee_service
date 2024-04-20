package com.seleniumexpress.employeeapp.controllers;

import com.seleniumexpress.employeeapp.dto.AddressDTO;
import com.seleniumexpress.employeeapp.dto.EmployeeDTO;
import com.seleniumexpress.employeeapp.entity.Employee;
import com.seleniumexpress.employeeapp.repo.EmployeeRepo;
import com.seleniumexpress.employeeapp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeRepo1) {
        this.employeeService = employeeRepo1;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable("id") int id) {

        EmployeeDTO employee = employeeService.getEmployeeById(id);

        return ResponseEntity.status(OK).body(employee);
    }

    @GetMapping()
    public ResponseEntity<List<EmployeeDTO>> getEmployee() {

        List<EmployeeDTO> employeeList = employeeService.getAllEmployees();


        return ResponseEntity.status(OK).body(employeeList);
    }

    @PostMapping
    public ResponseEntity createEmployee(@RequestBody EmployeeDTO employee) {
        employeeService.createEmployee(employee);
        return ResponseEntity.status(OK).build();
    }
}
