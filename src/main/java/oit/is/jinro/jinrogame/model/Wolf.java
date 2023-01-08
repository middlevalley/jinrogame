package oit.is.jinro.jinrogame.model;

public class Wolf {
  UserMapper uMapper;

  public void biteYou(int id) {
    uMapper.updateKillFragUpById(id);
  }
}
