package oit.is.jinro.jinrogame.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
public class AsyncJinroVote {

  int deathFlag = 0;
  int count = 0;
  ArrayList<Users> killedUsers = new ArrayList<Users>();
  private final Logger logger = LoggerFactory.getLogger(AsyncJinroVote.class);

  @Autowired
  UserMapper uMapper;

  public void countInit() {
    count = 0;
  }

  @Transactional
  public ArrayList<Users> syncShowUserList() {
    return uMapper.selectAll();
  }

  public Users syncKillUser(int id) {
    Users user = uMapper.selectById(id);
    uMapper.deleteById(id);

    return user;
  }

  public void syncUpdateVoteNum(int id) {
    uMapper.updateVoteNum(id);
  }

  /**
   * @param emitter
   */
  @Async
  public void asyncShowUsersList(SseEmitter emitter) {
    try {
      ArrayList<Users> users1 = this.syncShowUserList();
      emitter.send(users1);
      TimeUnit.MILLISECONDS.sleep(1000);
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowUsersList complete");
  }

  @Async
  public void asyncShowVoted(SseEmitter emitter) {
    try {
      count++;
      logger.info("send:" + count);
      logger.info(String.valueOf(uMapper.selectGetAlive()));
      if (uMapper.selectGetAlive() <= count) {
        if (killedUsers.size() == 0) {
          killedUsers = uMapper.selectKilledUser(uMapper.selectMaxVote());
        }
        System.out.println(killedUsers.size());
        if (deathFlag == 0) {
          if (killedUsers.size() != 1) {
            emitter.send("noDeath");
          } else {
            uMapper.deleteById(killedUsers.get(0).getId());
            emitter.send(killedUsers.get(0).getId());
          }
          deathFlag++;
        } else {
          if (killedUsers.size() != 1) {
            emitter.send("noDeath");
          } else {
            uMapper.deleteById(killedUsers.get(0).getId());
            emitter.send(killedUsers.get(0).getUserName());
          }
          logger.info("killedUserID:" + killedUsers.get(0).getUserRole());
        }
      } else {
        deathFlag = 0;
        emitter.send("stay");
        TimeUnit.SECONDS.sleep(10);
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
  }

}
