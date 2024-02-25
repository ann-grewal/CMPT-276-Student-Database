package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.models.User;
import com.example.demo.models.UserRepository;

import io.micrometer.core.instrument.Meter.Id;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class UsersController {
    @Autowired
    private UserRepository userRepo; 
    @GetMapping("/")
    public RedirectView process(){
        return new RedirectView("/users/view");
    }
    @GetMapping("/users/view")
    public String getAllUsers(Model model){
        System.out.println("Getting all users");
        List<User> users = userRepo.findAll(); 
        model.addAttribute("us", users);
        return "users/showAll";
    }
    @PostMapping("/users/add")
    public String addUser (@RequestParam Map<String, String> newuser, HttpServletResponse response){
        System.out.println("Add User"); 
        String newName = newuser.get("name");
        String newColor = newuser.get("color"); 
        String newMajor = newuser.get("major"); 
        int newWeight = Integer.parseInt(newuser.get("weight")); 
        int newHeight = Integer.parseInt(newuser.get("height")); 
        double newGpa = Double.parseDouble(newuser.get("gpa")); 
        List<User> nameUsed = userRepo.findByName(newName); 
        if (nameUsed.isEmpty()) {
            userRepo.save(new User(newName,newColor, newMajor, newWeight, newHeight, newGpa)); 
            response.setStatus(201);
            return "users/addedUser"; 
        }
        return "users/failedUser"; 
    }
    @GetMapping("/users/edit")
    public String editUser (@RequestParam Map<String, String> edituser, HttpServletResponse response, Model model){
        System.out.println("Edit User"); 
        String editLabel = edituser.get("label");
        int divider = editLabel.indexOf(",");
        String editName = editLabel.substring(0, divider); 
        List<User> users = userRepo.findByName(editName); 
        model.addAttribute("us", users);
        return "users/editUser"; 
    } 
    @PostMapping("/users/update") 
    public String updateUser (@RequestParam Map<String, String> edituser, HttpServletResponse response){
        System.out.println("Update User"); 

        // find old version by name 
        String editName = edituser.get("editName"); 
        User editStudent = userRepo.findByName(editName).get(0);
        if (editStudent == null) {
            return "users/showAll"; 
        } 
        String editColor = edituser.get("color"); 
        if (editStudent.getColor() != editColor) {
            editStudent.setColor(editColor);
        }
        String editMajor = edituser.get("major"); 
        if (editStudent.getMajor() != editMajor) {
            editStudent.setMajor(editMajor);
        }
        int editWeight = Integer.parseInt(edituser.get("weight"));  
        if (editStudent.getWeight() != editWeight) {
            editStudent.setWeight(editWeight);
        }
        int editHeight = Integer.parseInt(edituser.get("height")); 
        if (editStudent.getHeight() != editHeight) {
            editStudent.setHeight(editHeight);
        }
        double editGpa = Double.parseDouble(edituser.get("gpa")); 
        if (editStudent.getGpa() != editGpa) {
            editStudent.setGpa(editGpa);
        }
        return "users/updatedUser"; 
    }
    @PostMapping("/users/delete")
    public String deleteUser (@RequestParam Map<String, String> deleteuser, HttpServletResponse response){
        System.out.println("Delete User"); 
        String deleteName = deleteuser.get("removeName");
        User deleteStudent = userRepo.findByName(deleteName).get(0);
        if (deleteStudent != null) {
            userRepo.delete(deleteStudent); 
        } 
        return "users/deletedUser"; 
    }
}
