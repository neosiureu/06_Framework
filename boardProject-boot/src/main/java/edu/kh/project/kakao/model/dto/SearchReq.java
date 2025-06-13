package edu.kh.project.kakao.model.dto;

public record SearchReq(
        Coord sw,
        Coord ne,
        String keyword
) { }
