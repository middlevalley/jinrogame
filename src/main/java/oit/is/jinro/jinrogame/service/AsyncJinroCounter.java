package oit.is.jinro.jinrogame.service;

import java.io.IOException;
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
public class AsyncJinroCounter {

  @Autowired
  UserMapper UMapper;

  private final Logger logger = LoggerFactory.getLogger(AsyncJinroCounter.class);

  @Async
  public void count(SseEmitter emitter) {
    logger.info("count start");
    try {
      TimeUnit.SECONDS.sleep(10);
      emitter.send("complete");
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    }
  }

}
