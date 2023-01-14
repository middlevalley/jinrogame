package oit.is.jinro.jinrogame.controller;

import java.lang.reflect.Array;
import java.security.Principal;
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
import oit.is.jinro.jinrogame.model.VoteManager;
import oit.is.jinro.jinrogame.model.Wolf;
import oit.is.jinro.jinrogame.service.AsyncJinroNight;
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

  @Autowired
  AsyncJinroNight JNight;

  @GetMapping("step1")
  public String vote01(ModelMap model) {
    UMapper.voteInit();
    UMapper.useFlagInit();
    JVote.countInit();
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

  @GetMapping("step5")
  public String vote05(@RequestParam String userName, ModelMap model, Principal prin) {
    UMapper.killFlagInit();
    if (!(userName.equals("-1"))) {
      if (userName.equals(prin.getName())) {
        return "gameOver.html";
      } else {
        model.addAttribute("killedUser", userName + "が処刑されました。");
        return "result.html";
      }
    } else {
      model.addAttribute("killedUser", "無効な投票となりました");
      return "result.html";
    }
  }

  @GetMapping("step6")
  public SseEmitter vote06() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.JVote.asyncShowVoted(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("step7")
  public String night01(Principal prin, ModelMap model) {
    System.out.println(UMapper.selectCountAliveOfWolves() + prin.getName());
    if (UMapper.selectCountAliveOfWolves() == 0) {
      model.addAttribute("winner", "村人陣営");
      return "gameSet.html";
    } else if (UMapper.selectCountAliveOfWolves() >= UMapper.selectCountAliveOfVillagers()) {
      model.addAttribute("winner", "人狼陣営");
      return "gameSet.html";
    } else {
      if (UMapper.selectGetUserRoleByName(prin.getName()).equals("wolf")
          && UMapper.selectGetUseFlag(prin.getName()) == 0) {
        ArrayList<Users> users = UMapper.selectGetAliveVillagersMember();
        model.addAttribute("kill_list", users);
      } else if (UMapper.selectGetUserRoleByName(prin.getName()).equals("villager")) {
        UMapper.useSkill(UMapper.selectByName(prin.getName()).getId());
      } else if (UMapper.selectGetUserRoleByName(prin.getName()).equals("knight")
          && UMapper.selectGetUseFlag(prin.getName()) == 0) {
        ArrayList<Users> users = UMapper.selectGetAliveMember();
        model.addAttribute("gird_list", users);
      } else if (UMapper.selectGetUserRoleByName(prin.getName()).equals("necro")
          && UMapper.selectGetUseFlag(prin.getName()) == 0) {
        ArrayList<Users> users = UMapper.selectGetAliveMember();
        model.addAttribute("arive_list", users);
      }

      return "night.html";
    }
  }

  @GetMapping("step8")
  public String night02(@RequestParam Integer id, Principal prin, ModelMap model) {
    UMapper.useSkill(UMapper.selectByName(prin.getName()).getId());
    if (UMapper.selectByName(prin.getName()).getUserRole().equals("wolf")) {
      UMapper.updateKillFlagUpById(id);
    } else if (UMapper.selectByName(prin.getName()).getUserRole().equals("knight")) {
      UMapper.updateKillFlagDownById(id);
    } else if (UMapper.selectByName(prin.getName()).getUserRole().equals("necro")) {
      String name = UMapper.useNecroSkill(id);
      model.addAttribute("role_name", "選んだ人の役職は" + name + "です");
    }
    return "night.html";
  }

  @GetMapping("step9")
  public SseEmitter night03() {
    final SseEmitter sseEmitter = new SseEmitter();
    this.JNight.asyncShowUseSkill(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("step10")
  public SseEmitter night04(ModelMap model) {
    final SseEmitter sseEmitter = new SseEmitter();
    this.JNight.asyncKilledUsers(sseEmitter);
    return sseEmitter;
  }

  @GetMapping("step11")
  public String night05() {
    return "nightResult.html";
  }
}
