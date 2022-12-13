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
  int count = 0;
  private final Logger logger = LoggerFactory.getLogger(AsyncJinroCounter.class);

  @Async
  public void count(SseEmitter emitter) {
    logger.info("count start");
    try {
      while (count < 10) {
        logger.info("send:" + count);
        emitter.send(count);
        count++;
        TimeUnit.SECONDS.sleep(1);
      }
    } catch (Exception e) {
      logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
    }
  }

}
