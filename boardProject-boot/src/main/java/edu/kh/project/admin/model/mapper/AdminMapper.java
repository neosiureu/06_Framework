package edu.kh.project.admin.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;


@Mapper
public interface AdminMapper {

	Member login(String memberEmail);

}
