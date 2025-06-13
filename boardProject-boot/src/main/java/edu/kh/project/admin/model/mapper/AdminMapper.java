package edu.kh.project.admin.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.member.model.dto.Member;

@Mapper
public interface AdminMapper {



	Member login(String memberEmail);

	Board maxCommentCount();

	Board maxLikeCount();

	Board maxReadCount();

	List<Member> getNewMember();

	List<Member> selectWithdrawnMemberList();

	List<Board> selectWithdrawnboardList();

	int restoreMember(int memberNo);

	int restoreBoard(int boardNo);

	int checkEmail(String memberEmail);

	int createAdminAccount(Member member);

	List<Member> getadminList();
	
	
	
}
