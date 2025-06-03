package com.project.ecommerce.controllers;

import com.project.ecommerce.dtos.ChangePasswordRequest;
import com.project.ecommerce.dtos.RegisterUserRequest;
import com.project.ecommerce.dtos.UpdateUserRequest;
import com.project.ecommerce.dtos.UserDto;
import com.project.ecommerce.mapper.UserMapper;
import com.project.ecommerce.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    public Iterable<UserDto> getAllUsers(
            @RequestParam(required = false,defaultValue = "name") String sort
    ) {
        if(Set.of("name","email").contains(sort))
            sort="name";


        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        var user=userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<?> registerUser(
            UriComponentsBuilder uriBuilder,
            @Valid @RequestBody RegisterUserRequest request){
       if(userRepository.existsByEmail(request.getEmail())){
           return ResponseEntity.badRequest().body(
                   Map.of("email","Email already register")
           );
       };
        var user=userMapper.toEntity(request);
       userRepository.save(user);

       var userDto=userMapper.toDto(user);
       var uri=uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
       return  ResponseEntity.created(uri).body(userDto);

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest request
    ){
        var user=userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }

        userMapper.update(request,user);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.toDto(user));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id){
        var user=userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable(name = "id") Long id,
            @RequestBody ChangePasswordRequest request
    ){
        var user=userRepository.findById(id).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        if (!user.getPassword().equals(request.getOldPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();

    }

}
