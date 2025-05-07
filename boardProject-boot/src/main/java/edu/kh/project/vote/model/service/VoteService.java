package edu.kh.project.vote.model.service;

import java.util.Map;

import edu.kh.project.vote.model.dto.UserVote;

public interface VoteService {

	
	// 투표의 진행
	Map<String, Integer> processVote(UserVote userVote);

	
	// 투표 수 결과를 처음에 DB에서 화면에 랜더링하려고
	int countVotes(String string);

}
