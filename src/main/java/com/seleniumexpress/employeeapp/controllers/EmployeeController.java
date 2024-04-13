package com.seleniumexpress.employeeapp.controllers;

import com.seleniumexpress.employeeapp.dto.AddressDTO;
import com.seleniumexpress.employeeapp.dto.EmployeeDTO;
import com.seleniumexpress.employeeapp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }
}
