package oit.is.jinro.jinrogame.controller;

import java.io.IOException;
import java.lang.reflect.Array;
import java.security.Principal;
import java.util.ArrayList;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import oit.is.jinro.jinrogame.model.UserMapper;
import oit.is.jinro.jinrogame.model.Users;
import oit.is.jinro.jinrogame.service.AsyncJinroCounter;
import oit.is.jinro.jinrogame.model.Role;
import oit.is.jinro.jinrogame.model.RoleMapper;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/discus")
public class JinroDiscusController {

  @Autowired
  UserMapper UMapper;

  @Autowired
  RoleMapper RMapper;

  @Autowired
  AsyncJinroCounter Jcounter;

  @GetMapping("step1")
  public String Discus1(ModelMap model, Principal prin) {
    UMapper.InsertJoinUser(prin.getName());
    System.out.println("joinUserName: " + prin.getName());
    return "preCounter.html";
  }

  @GetMapping("step2")
  public String Discus2(ModelMap model) {
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
    return "counter.html";
  }

  @GetMapping("step3")
  public String Discus3(ModelMap model) {
    ArrayList<Users> users = UMapper.selectAll();
    ArrayList<Role> roles = RMapper.selectAll();

    model.addAttribute("users", users);
    model.addAttribute("roles", roles);
    return "counter.html";
  }

  @GetMapping("step4")
  public SseEmitter Discus4() throws IOException, InterruptedException {
    final SseEmitter sseEmitter = new SseEmitter();
    this.Jcounter.count(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("step5")
  public String Discus5(ModelMap model) {
    if (UMapper.selectCountAliveOfWolves() == 0) {
      model.addAttribute("winner", "村人陣営");
      return "gameSet.html";
    } else if (UMapper.selectCountAliveOfWolves() >= UMapper.selectCountAliveOfVillagers()) {
      model.addAttribute("winner", "人狼陣営");
      return "gameSet.html";
    } else {
      
      return "preDiscus.html";
    }

  }
}
