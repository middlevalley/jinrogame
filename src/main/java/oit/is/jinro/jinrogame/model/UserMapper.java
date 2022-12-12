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

  @Select("SELECT voted from users where id = #{id}")
  int selectByIdGetVoted(int id);

  @Update("UPDATE USERS SET USERROLE=#{userRole} where id = #{id}")
  void updateById(Users users);

  @Update("UPDATE USERS SET voted = voted + 1 where id = #{id}")
  void updateVoteNum(int id);

  @Update("UPDATE USERS SET voted = 0")
  void voteInit();

  @Delete("DELETE FROM users WHERE ID =#{id}")
  void deleteById(int id);
}
