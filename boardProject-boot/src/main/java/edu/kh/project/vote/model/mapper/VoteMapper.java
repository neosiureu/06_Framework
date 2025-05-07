package edu.kh.project.vote.model.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.vote.model.dto.UserVote;


@Mapper
public interface VoteMapper {

	int checkUserVoted(int userId);

	void insertUserVote(UserVote userVote);

	void incrementVoteCount(String choice);

	int getVoteCount(String string);

}
