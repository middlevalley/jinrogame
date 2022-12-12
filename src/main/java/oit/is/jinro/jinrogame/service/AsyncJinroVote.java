package oit.is.jinro.jinrogame.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.jinro.jinrogame.model.Users;
import oit.is.jinro.jinrogame.model.UserMapper;

@Service
public class AsyncJinroVote {

  private final Logger logger = LoggerFactory.getLogger(AsyncJinroVote.class);

  @Autowired
  UserMapper uMapper;

  @Transactional
  public ArrayList<Users> syncShowUserList() {
    return uMapper.selectAll();
  }

  public Users syncKillUser(int id) {
    Users user = uMapper.selectById(id);
    uMapper.deleteById(id);

    return user;
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

}
