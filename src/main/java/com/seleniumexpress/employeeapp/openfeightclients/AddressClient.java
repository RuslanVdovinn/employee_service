package com.seleniumexpress.employeeapp.openfeightclients;

import com.seleniumexpress.employeeapp.dto.AddressDTO;
import com.seleniumexpress.employeeapp.dto.EmployeeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient(name = "ADDRESS-SERVICE", path = "/address-app/api")
public interface AddressClient {

    @GetMapping("/address/{employeeId}")
    public ResponseEntity<AddressDTO> getAddressByEmployeeId(@PathVariable("employeeId") int id);

    @GetMapping("/address")
    public ResponseEntity<List<AddressDTO>> getAllAddress();

    @PostMapping("/address")
    @ResponseBody
    ResponseEntity createAddress(AddressDTO map);
}
