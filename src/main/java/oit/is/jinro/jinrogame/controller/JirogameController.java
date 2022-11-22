package oit.is.jinro.jinrogame.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import oit.is.jinro.jinrogame.model.UserMapper;
import oit.is.jinro.jinrogame.model.Users;
import oit.is.jinro.jinrogame.model.Role;
import oit.is.jinro.jinrogame.model.RoleMapper;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vote")
public class JirogameController {
  @Autowired
  UserMapper UMapper;
  @Autowired
  RoleMapper RMapper;

  @GetMapping("step1")
  public String vote01(ModelMap model) {
    ArrayList<Users> users = UMapper.selectAll();
    ArrayList<Role> roles = RMapper.selectAll();
    model.addAttribute("users", users);
    model.addAttribute("roles", roles);
    return "vote.html";
  }
}
