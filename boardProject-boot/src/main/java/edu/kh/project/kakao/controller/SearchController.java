package edu.kh.project.kakao.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.project.kakao.model.dto.SearchReq;
import edu.kh.project.kakao.model.dto.SearchRes;
import edu.kh.project.kakao.model.service.KakaoSearchService;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final KakaoSearchService service;

    @PostMapping("/search")
    public SearchRes search(@RequestBody SearchReq req) {
        return service.search(req);
    }
}