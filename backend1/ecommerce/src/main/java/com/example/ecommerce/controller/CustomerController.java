package com.example.ecommerce.controller;

import com.example.ecommerce.dto.request.CustomerRequest;
import com.example.ecommerce.dto.request.LoginRequest;
import com.example.ecommerce.dto.response.ApiResponse;
import com.example.ecommerce.dto.response.CustomerResponse;
import com.example.ecommerce.dto.response.LoginResponse;
import com.example.ecommerce.exception.DataNotFoundException;
import com.example.ecommerce.exception.DuplicateDataException;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CustomerRequest userRequest){
        try {
            CustomerResponse userResponse = userService.registerUser(userRequest);
            return ResponseEntity.ok(new ApiResponse<>(200,userResponse));
        }catch (DuplicateDataException e){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(409,e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500,e.getMessage()));
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody LoginRequest loginRequest){
        try {

            CustomerResponse customerResponse = userService.loginUser(loginRequest);

            String token = jwtUtil.generateToken(customerResponse.getUsername());


            LoginResponse loginResponse = new LoginResponse(token, customerResponse.getRole());


            return ResponseEntity.ok(new ApiResponse<>(200, loginResponse));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, e.getMessage()));
        }
    }

    @PutMapping("update/{username}")
    public ResponseEntity<?> updateCustomer(@PathVariable String username, @RequestBody CustomerRequest customerRequest){
        try{
            CustomerResponse customerResponse = userService.updateUser(customerRequest, username);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), customerResponse));
        }catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (DuplicateDataException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findall(){
        try{
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), userService.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleted(@PathVariable("username")String username){
        try{
            userService.deleteUser(username);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "user deleted succesfully"));
        } catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(404, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete user: " + e.getMessage()));
        }
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable String username){
        try{
            CustomerResponse customerResponse = userService.findByUsername(username);
            if (customerResponse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "User not found with username: " + username));
            }
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), customerResponse));
        }catch (DataNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }
}