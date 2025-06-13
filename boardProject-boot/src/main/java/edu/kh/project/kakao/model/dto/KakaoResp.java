package edu.kh.project.kakao.model.dto;


import java.util.List;

public record KakaoResp(
        KakaoMeta meta,
        List<KakaoDocument> documents
) { }