package oit.is.jinro.jinrogame.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {
  @Select("SELECT * from users")
  ArrayList<Users> selectAll();

  @Select("SELECT * from users where id = #{id}")
  Users selectById(int id);

  @Select("SELECT count(id) from users")
  int selectGetAlive();

  @Select("SELECT * from users")
  ArrayList<Users> selectGetAliveMember();

  @Select("SELECT * from roles join users on roles.roleName = users.userRole where camp = 'villagers'")
  ArrayList<Users> selectGetAliveVillagersMember();

  @Select("Select * from users where not(userRole = 'villager')")
  Users selectNotVillager();

  @Select("Select min(id) from users")
  int selectGetMinId();

  @Select("SELECT * FROM users where userName = #{name}")
  Users selectByName(String name);

  @Select("SELECT voted from users where id = #{id}")
  int selectByIdGetVoted(int id);

  @Select("SELECT userRole from users where id = #{id}")
  String selectGetUserRoleById(int id);

  @Select("SELECT userRole from users where userName = #{name}")
  String selectGetUserRoleByName(String name);

  @Select("SELECT count(*) from roles join users on roles.roleName = users.userRole")
  int selectCountAlive();

  @Select("SELECT count(*) from roles join users on roles.roleName = users.userRole where camp = 'wolves'")
  int selectCountAliveOfWolves();

  @Select("SELECT count(*) from roles join users on roles.roleName = users.userRole where users.userRole = 'knight'")
  int selectCountAliveOfKnights();

  @Select("SELECT count(*) from roles join users on roles.roleName = users.userRole where users.userRole = 'necro'")
  int selectCountAliveOfNecro();

  @Select("SELECT count(*) from roles join users on roles.roleName = users.userRole where camp = 'villagers'")
  int selectCountAliveOfVillagers();

  @Select("select camp from roles join users on roles.roleName = users.userRole where userName = #{name}")
  String SelectCampByName(String name);

  @Select("SELECT * from users where voted = #{num}")
  ArrayList<Users> selectKilledUser(int num);

  @Insert("INSERT INTO users (userName) VALUES (#{name})")
  void InsertJoinUser(String name);

  @Update("UPDATE USERS SET USERROLE=#{userRole} where id = #{id}")
  void updateById(Users users);

  @Update("UPDATE USERS SET voted = voted + 1 where id = #{id}")
  void updateVoteNum(int id);

  @Update("UPDATE USERS SET voted = 0")
  void voteInit();

  @Update("UPDATE USERS SET killFlag = 0")
  void killFlagInit();

  @Update("UPDATE USERS SET useFlag = 0")
  void useFlagInit();

  @Update("UPDATE USERS SET useFlag = useFlag + 1 where id = #{id}")
  void useSkill(int id);

  @Select("SELECT userRole from users where id = #{id}")
  String useNecroSkill(int id);

  @Select("SELECT useFlag from users where userName = #{name}")
  int selectGetUseFlag(String name);

  @Select("SELECT sum(useFlag) from users")
  int selectGetUseFlagSum();

  @Select("SELECT Max(voted) from users;")
  int selectMaxVote();

  @Delete("DELETE FROM users WHERE ID =#{id}")
  void deleteById(int id);

  @Update("UPDATE USERS SET killFlag = killFlag + 1 where id = #{id}")
  void updateKillFlagUpById(int id);

  @Update("UPDATE USERS SET killFlag = killFlag - 1 where id = #{id}")
  void updateKillFlagDownById(int id);

  @Select("SELECT * FROM users where killFlag >= 1 ")
  ArrayList<Users> selectKilledUsers();

  @Update("Update USERS SET userRole=NULL")
  void userRoleInit();

  @Delete("DELETE from users")
  void usersTableInit();

}
