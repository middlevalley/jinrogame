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

  @Select("Select min(id) from users")
  int selectGetMinId();

  @Select("SELECT * FROM users where userName = #{name}")
  Users selectByName(String name);

  @Select("SELECT voted from users where id = #{id}")
  int selectByIdGetVoted(int id);

  @Insert("INSERT INTO users (userName) VALUES (#{name})")
  void InsertJoinUser(String name);

  @Update("UPDATE USERS SET USERROLE=#{userRole} where id = #{id}")
  void updateById(Users users);

  @Update("UPDATE USERS SET voted = voted + 1 where id = #{id}")
  void updateVoteNum(int id);

  @Update("UPDATE USERS SET voted = 0")
  void voteInit();

  @Select("SELECT id, Max(voted) from users group by id;")
  ArrayList<VoteManager> selectMaxVote();

  @Delete("DELETE FROM users WHERE ID =#{id}")
  void deleteById(int id);
}
