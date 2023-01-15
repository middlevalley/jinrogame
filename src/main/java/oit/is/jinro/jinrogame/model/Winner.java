package oit.is.jinro.jinrogame.model;

public class Winner {
  int id;
  String userName;
  int count;
  String camp;

  public Winner() {

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

    public int getCount(){
    return count;
  }

  public void setCount(int count){
    this.count = count;
  }


  public String getCamp() {
    return camp;
  }

  public void setCamp(String camp) {
    this.camp = camp;
  }
}
