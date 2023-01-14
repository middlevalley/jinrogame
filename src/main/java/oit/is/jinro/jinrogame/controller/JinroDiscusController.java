package oit.is.jinro.jinrogame.controller;

import java.io.IOException;
import java.lang.reflect.Array;
import java.security.Principal;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
    return "ready.html";
  }

  @GetMapping("step2")
  public String Discus2(@RequestParam Integer id, ModelMap model, Principal prin) {
    ArrayList<Users> users = UMapper.selectAll();
    ArrayList<Role> roles = RMapper.selectAll();
    int roleDicide;
    System.out.println(id);
    for (Users user : users) {
      user.setUserRole("");
    }
    try {
      if (id == 1) {
        for (Role role : roles) {
          System.out.println(role.getRoleName());
          if (role.getRoleName().equals("villager")) {
          } else {
            roleDicide = (int) (Math.random() * 100 % users.size());
            while (true) {
              if (users.get(roleDicide).getUserRole().isEmpty()) {
                users.get(roleDicide).setUserRole(role.getRoleName());
                break;
              } else {
                if (roleDicide + 1 != users.size()) {
                  roleDicide++;
                } else {
                  roleDicide = 0;
                }
              }
            }
            System.out.println("dicideNum: " + roleDicide);
          }
        }
        for (Users user : users) {
          if (user.getUserRole().isEmpty()) {
            user.setUserRole("villager");
          }
          UMapper.updateById(user);
        }
        model.addAttribute("role", UMapper.selectGetUserRoleByName(prin.getName()));
      } else {
        while (UMapper.selectGetUserRoleByName(prin.getName()) == null) {
          TimeUnit.SECONDS.sleep(2);
        }
        model.addAttribute("role", UMapper.selectGetUserRoleByName(prin.getName()));
      }
    } catch (Exception e) {
      System.out.println("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    }
    return "preCounter.html";
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
  public String Discus5(ModelMap model, Principal prin) {
    if (UMapper.selectCountAliveOfWolves() == 0) {
      model.addAttribute("winner", "村人陣営");
      return "gameSet.html";
    } else if (UMapper.selectCountAliveOfWolves() >= UMapper.selectCountAliveOfVillagers()) {
      model.addAttribute("winner", "人狼陣営");
      return "gameSet.html";
    } else {
      if (UMapper.selectByName(prin.getName()) == null) {
        return "gameOver.html";
      }
      return "preDiscus.html";
    }

  }
}
