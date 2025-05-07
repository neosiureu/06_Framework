package edu.kh.project.vote.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVote {
    private int userId;      // 회원 ID
    private String choice;   // 사용자가 선택한 항목 ('A' or 'B')
}
