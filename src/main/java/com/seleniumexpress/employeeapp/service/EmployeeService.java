package com.seleniumexpress.employeeapp.service;

import com.seleniumexpress.employeeapp.dto.AddressDTO;
import com.seleniumexpress.employeeapp.dto.EmployeeDTO;
import com.seleniumexpress.employeeapp.entity.Employee;
import com.seleniumexpress.employeeapp.openfeightclients.AddressClient;
import com.seleniumexpress.employeeapp.repo.EmployeeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepo employeeRepo;

    private final ModelMapper modelMapper;
    //    private final DiscoveryClient discoveryClient;
    private final LoadBalancerClient loadBalancerClient;

    private final WebClient webClient;
    private final AddressClient addressClient;

//    private final RestTemplate restTemplate;
//
//    @Value("${address.service.base.url}")
//    private String addressBase;

    @Autowired
    public EmployeeService(EmployeeRepo employeeRepo, ModelMapper modelMapper, LoadBalancerClient loadBalancerClient, WebClient webClient, AddressClient addressClient) {
        this.employeeRepo = employeeRepo;
        this.modelMapper = modelMapper;
        this.loadBalancerClient = loadBalancerClient;
        this.webClient = webClient;
        this.addressClient = addressClient;
    }

    public EmployeeDTO getEmployeeById(int id) {

        Employee employee = employeeRepo.findById(id).get();

        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);

//        List<ServiceInstance> instances = discoveryClient.getInstances("address-service");
//        ServiceInstance serviceInstance = instances.get(0);

        ServiceInstance instance = loadBalancerClient.choose("address-service");
        String uri = instance.getUri().toString();
        String contextRoot = instance.getMetadata().get("configPath");

        System.out.println("uri >>>>> " + uri + contextRoot);


        ResponseEntity<AddressDTO> addressResponseEntity = addressClient.getAddressByEmployeeId(id);
        AddressDTO addressDTO = addressResponseEntity.getBody();

        employeeDTO.setAddressDTO(addressDTO);

        return employeeDTO;
    }

    public List<EmployeeDTO> getAllEmployees() {

        List<Employee> employeeList = employeeRepo.findAll();
        List<EmployeeDTO> employeesResponse = Arrays.asList(modelMapper.map(employeeList, EmployeeDTO[].class));
        List<AddressDTO> addressList = addressClient.getAllAddress().getBody();
        employeesResponse.forEach(employeeDTO -> {
            for (AddressDTO addressDTO : addressList) {
                if (addressDTO.getId() == employeeDTO.getId()) {
                    employeeDTO.setAddressDTO(addressDTO);
                }
            }
        });
        return employeesResponse;
    }

    public void createEmployee(EmployeeDTO employee) {
        employeeRepo.saveAndFlush(modelMapper.map(employee, Employee.class));
        addressClient.createAddress(employee.getAddressDTO());
    }

//    private AddressDTO callingAddressServiceUsingRestTemplate(int id) {
//        return restTemplate.getForObject(
//                addressBase + "/address/{id}",
//                AddressDTO.class, id
//        );
//    }
}
