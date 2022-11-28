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
import org.yaml.snakeyaml.emitter.Emitter;

import oit.is.jinro.jinrogame.model.Users;
import oit.is.jinro.jinrogame.model.UserMapper;

@Service
public class AsyncJinroGame {
  boolean dbUpdated = false;

  private final Logger logger = LoggerFactory.getLogger(AsyncJinroGame.class);

  @Autowired
  UserMapper uMapper;

  public ArrayList<Users> syncShowUserList() {
    return uMapper.selectAll();
  }

  @param emitter

  @Async
  public void asyncShowUsersList(SseEmitter emitter) {
    dbUpdated = true;
    try {
      while  (true) {
        if (false == dbUpdated) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }

        ArrayList<Users> users1 = this.syncShowUserList();
        emitter.send(users1);
        TimeUnit.MILLISECONDS.sleep(1000);
        dbUpdated = false;
      }

      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    } finally {
      emitter.complete();
    }
    System.out.println("asyncShowUsersList complete");
  }

}
