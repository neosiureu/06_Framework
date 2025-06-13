package edu.kh.project.kakao.model.dto;

import java.util.List;

public record SearchRes(
        int total,
        List<KakaoDocument> sample
) { }