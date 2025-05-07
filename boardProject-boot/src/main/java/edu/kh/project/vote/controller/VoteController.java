package edu.kh.project.vote.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.board.controller.EditBoardController;
import edu.kh.project.vote.model.dto.UserVote;
import edu.kh.project.vote.model.service.VoteService;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("vote")
@Controller
@Slf4j
public class VoteController {
	@Autowired
    private VoteService service;

    @PostMapping
    @ResponseBody
    public Map<String, Integer> castVote(@RequestBody UserVote userVote) {
        log.info("투표 요청 수신: {}", userVote);
        return service.processVote(userVote);
    }
    
    @GetMapping("/result") // ✅ 수정된 경로
    @ResponseBody
    public Map<String, Integer> getVoteResult() {
        int countA = service.countVotes("A");
        int countB = service.countVotes("B");
        int total = countA + countB;

        int percentA = total == 0 ? 0 : (int)((countA * 100.0) / total);
        int percentB = total == 0 ? 0 : (int)((countB * 100.0) / total);

        Map<String, Integer> result = new HashMap<>();
        result.put("percentA", percentA);
        result.put("percentB", percentB);
        return result;
    }
}
