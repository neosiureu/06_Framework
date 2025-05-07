package edu.kh.project.vote.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.vote.model.dto.UserVote;
import edu.kh.project.vote.model.mapper.VoteMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteMapper mapper;

    @Override
    public Map<String, Integer> processVote(UserVote userVote) {

        // 1. 이미 투표했는지 확인
        int check = mapper.checkUserVoted(userVote.getUserId());

        boolean alreadyVoted = (check != 0); // 1.5단계

        
        if (check == 0) {
            // 2. USER_VOTE에 삽입
            mapper.insertUserVote(userVote);

            // 3. VOTE 테이블의 count 증가
            mapper.incrementVoteCount(userVote.getChoice());
        }

        // 4. 현재 A/B 투표 수 조회
        int countA = mapper.getVoteCount("A");
        int countB = mapper.getVoteCount("B");

        int total = countA + countB;
        int percentA = total == 0 ? 0 : (int) Math.round(countA * 100.0 / total);
        int percentB = 100 - percentA;

        // 5. 결과 반환
        Map<String, Integer> result = new HashMap<>();
        result.put("percentA", percentA);
        result.put("percentB", percentB);
        result.put("voted", alreadyVoted ? 1 : 0); // 추가

        return result;
    }

    @Override
    public int countVotes(String choice) {
        return mapper.getVoteCount(choice);
    }
}
