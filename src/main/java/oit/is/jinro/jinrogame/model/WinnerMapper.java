package oit.is.jinro.jinrogame.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface WinnerMapper {
  @Select("SELECT userName, winNum from (SELECT userName, count(userName) as winNum from winner group by userName) as a")
  ArrayList<Winner> selectAll();

  @Insert("INSERT INTO winner (userName, camp) VALUES (#{name}, #{camp})")
  void InsertWinnersName(String name, String camp);
}
