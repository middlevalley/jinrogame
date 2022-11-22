package oit.is.jinro.jinrogame.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleMapper {
  @Select("SELECT * from roles")
  ArrayList<Role> selectAll();
}
