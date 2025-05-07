package edu.kh.project.vote.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote {
    private int voteId;      // 기본키
    private String choice;   // 'A' 또는 'B'
    private int count;       // 누적 투표 수
    private int userId; 
}
