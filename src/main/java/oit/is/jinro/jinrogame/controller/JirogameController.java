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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.jinro.jinrogame.model.UserMapper;
import oit.is.jinro.jinrogame.model.Users;
import oit.is.jinro.jinrogame.service.AsyncJinroVote;
import oit.is.jinro.jinrogame.model.Role;
import oit.is.jinro.jinrogame.model.RoleMapper;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;

@Controller
@RequestMapping("/vote")
public class JirogameController {
  @Autowired
  UserMapper UMapper;
  @Autowired
  RoleMapper RMapper;

  @Autowired
  AsyncJinroVote JVote;

  @GetMapping("step1")
  public String vote01(ModelMap model) {
    UMapper.voteInit();
    ArrayList<Users> users = UMapper.selectAll();
    model.addAttribute("users", users);
    return "vote.html";
  }

  @GetMapping("step2")
  public String vote02(ModelMap model) {
    ArrayList<Users> users = UMapper.selectAll();
    ArrayList<Role> roles = RMapper.selectAll();
    int roleDicide;

    for (Role role : roles) {
      if (role.getRoleName().equals("villager")) {
        role.setMAX_num(3);
      } else {
        if (role.getMAX_num() == 0) {
          role.setMAX_num(1);
        }
      }
    }
    for (Users user : users) {
      roleDicide = (int) (Math.random() * 10 % 2);
      if (roles.get(roleDicide).getRoleName().equals("villager")) {
        user.setUserRole(roles.get(roleDicide).getRoleName());
      } else if (roles.get(roleDicide).getMAX_num() != 0) {
        user.setUserRole(roles.get(roleDicide).getRoleName());
        roles.get(roleDicide).setMAX_num(0);
      } else {
        user.setUserRole("villager");
      }
      UMapper.updateById(user);
    }
    model.addAttribute("users", users);
    model.addAttribute("roles", roles);
    return "vote.html";
  }

  @GetMapping("step3")
  public String vote03(@RequestParam Integer id, ModelMap model) {
    JVote.syncUpdateVoteNum(id);
    return "standby.html";
  }

  @GetMapping("step4")
  public SseEmitter vote04() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.JVote.asyncShowUsersList(sseEmitter);
    return sseEmitter;
  }
}
