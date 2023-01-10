package oit.is.jinro.jinrogame.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.jinro.jinrogame.model.Users;
import oit.is.jinro.jinrogame.model.VoteManager;
import oit.is.jinro.jinrogame.model.UserMapper;

@Service
public class AsyncJinroNight {
  private final Logger logger = LoggerFactory.getLogger(AsyncJinroNight.class);

  int killFlag = 0;

  @Autowired
  UserMapper uMapper;

  ArrayList<Users> killedUsers;

  public void killFlagInit() {
    killFlag = 0;
  }

  @Async
  public void asyncShowUseSkill(SseEmitter emitter) {
    try {
      if (uMapper.selectGetUseFlagSum() >= uMapper.selectGetAlive()) {
        emitter.send("complete");
      } else {
        emitter.send("stay");
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
  }

  @Async
  public void asyncKilledUsers(SseEmitter emitter) {
    try {
      if (killFlag == 0) {
        killedUsers = uMapper.selectKilledUsers();
        for (Users u : killedUsers) {
          uMapper.deleteById(u.getId());
        }
        if (killedUsers.size() != 0) {
          emitter.send(killedUsers);
        } else {
          emitter.send("殺された人はいませんでした。");
        }
        killFlag++;
      } else {
        if (killedUsers.size() != 0) {
          emitter.send(killedUsers);
        } else {
          emitter.send("殺された人はいませんでした。");
        }
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
  }
}
