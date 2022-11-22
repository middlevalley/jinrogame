package oit.is.jinro.jinrogame.model;

public class Users {
  int id;
  String userName;
  int voted;

  public Users() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public int getVoted() {
    return voted;
  }

  public void setVoted(int voted) {
    this.voted = voted;
  }
}
