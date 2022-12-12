package oit.is.jinro.jinrogame.model;

public class VoteManager {
  int id;
  int vote;
  UserMapper uMapper;

  public VoteManager() {
    this.id = -1;
    this.vote = 0;
  }

  public VoteManager(int id) {
    this.id = id;
    this.vote = 0;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void setVote(int vote) {
    this.vote = vote;
  }

  public int getVote() {
    return vote;
  }

  public void vote() {
    uMapper.updateVoteNum(this.id);
    setVote(uMapper.selectByIdGetVoted(this.id));
  }

}
