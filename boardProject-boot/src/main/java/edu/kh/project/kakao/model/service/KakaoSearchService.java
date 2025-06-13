package edu.kh.project.kakao.model.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import edu.kh.project.kakao.model.dto.KakaoDocument;
import edu.kh.project.kakao.model.dto.KakaoResp;
import edu.kh.project.kakao.model.dto.SearchReq;
import edu.kh.project.kakao.model.dto.SearchRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * rect 를 SPLIT × SPLIT 으로 분할하고 각 sub‑rect 마다
 * page 1 ~ 3, size 15 를 호출해 총합을 집계한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoSearchService {

    private static final int SPLIT      = 3;    // 3×3 분할
    private static final int MAX_PAGE   = 3;    // Kakao 제한
    private static final int PAGE_SIZE  = 15;   // 고정

    private final WebClient kakaoClient;

    public SearchRes search(SearchReq req) {
        double swLat = req.sw().lat();
        double swLng = req.sw().lng();
        double neLat = req.ne().lat();
        double neLng = req.ne().lng();

        double latStep = (neLat - swLat) / SPLIT;
        double lngStep = (neLng - swLng) / SPLIT;

        int total = 0;
        List<KakaoDocument> sample = new ArrayList<>();

        for (int i = 0; i < SPLIT; i++) {
            for (int j = 0; j < SPLIT; j++) {
                double sLat = swLat + latStep * i;
                double sLng = swLng + lngStep * j;
                double nLat = sLat + latStep;
                double nLng = sLng + lngStep;

                String subRect = "%f,%f,%f,%f".formatted(sLng, sLat, nLng, nLat);

                for (int p = 1; p <= MAX_PAGE; p++) {
                    final int currentPage = p;   

                    KakaoResp resp = kakaoClient.get()
                        .uri(uri -> uri
                            .path("/v2/local/search/keyword.json")
                            .queryParam("query", req.keyword())
                            .queryParam("rect",  subRect)
                            .queryParam("page",  currentPage)
                            .queryParam("size",  PAGE_SIZE)
                            .build())
                        .retrieve()
                        .bodyToMono(KakaoResp.class)
                        .block();

                    if (resp == null) break;

                    log.info("rect[{}/{}] page {} docs={}"
                            , (i * SPLIT) + j + 1
                            , SPLIT * SPLIT
                            , currentPage
                            , resp.documents().size());

                    total += resp.documents().size();

                    if (sample.size() < 10) {
                        sample.addAll(resp.documents()
                                           .stream()
                                           .limit(10 - sample.size())
                                           .toList());
                    }

                    if (resp.meta().is_end()) break;
                }
            }
        }
        return new SearchRes(total, sample);
    }
}