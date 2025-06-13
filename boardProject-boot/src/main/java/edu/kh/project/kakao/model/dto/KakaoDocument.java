package edu.kh.project.kakao.model.dto;

public record KakaoDocument(
        String place_name,
        String address_name,
        double x,   // 경도
        double y    // 위도
) { }
