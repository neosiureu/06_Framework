package edu.kh.project.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.mapper.BoardMapper;
import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.Pagination;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional (rollbackFor = Exception.class)
@Slf4j
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardMapper mapper;

	// 게시판 종류 조회 서비스
	@Override
	public List<Map<String, Object>> selectBoardTypeList() {
		
		return mapper.selectBoardTypeList();
	}

	// 특정 게시판의 지정된 페이지 목록 조회 서비스
		@Override
		public Map<String, Object> selectBoardList(int boardCode, int cp) {
			
			//1. 지정된 게시판 (boardCode)에서 
			// 삭제되지 않은 게시글 수를 조회
			int listCount = mapper.getListCount(boardCode);
			
			//2. 1번의 결과 + cp 를 이용해서 
			// Pagination 객체를 생성
			// * Pagination 객체 : 게시글 목록 구성에 필요한 값을 저장한 객체
			Pagination pagination = new Pagination(cp, listCount);
			
			//3. 특정 게시판의 지정된 페이지 목록 조회
			/*
			 * ROWBOUNDS 객체 (MyBatis 제공 객체)
			 * : 지정된 크기만큼 건너띄고(offset) 
			 * 제한된 크기만큼(limit)의 행을 조회하는 객체
			 * 
			 * --> 페이징 처리가 굉장히 간단해짐.
			 * */
			
			int limit = pagination.getLimit(); // 10개씩 조회
			
			int offset = (cp - 1) * limit;
			
			RowBounds rowBounds = new RowBounds(offset,limit); 
			
			//Mapper 메서드 호출 시 원래 전달 할수 있는 매개변수 1개
			// -> 2개를 전달할 수 있는 경우가있음
			// rowBounds를 이용할때!
			// -> 첫번째 매개변수 -> sql 에 전달할 파라미터
			// -> 두번째 매개변수 -> RowBounds 객체 전달
			
			List<Board> boardList = mapper.selectBoardList(boardCode,rowBounds);
			
			//log.debug("boardList 결과 : {}", boardList);
			
			//4. 목록 조회 결과 + Pagination 객체를 Map으로 묶어서 반환
			
			Map<String, Object> map = new HashMap<>();
			
			map.put("pagination", pagination);
			map.put("boardList", boardList);
			
			//결과반환
			return map;
		}


	    @Override
        public Board selectOne(Map<String, Integer> map) {
        		
        	//여러 SQL 을 실행하는 방법
        	
        	// 1. 하나의 Service 메서드에서
        	//     여러 mapper 메서드를 호출 하는 방법
        	
        	//2. 수행하려는 SQL이 
        	//1) 모두 SELECT 이면서
        	//2) 먼저 조회된 결과 중 일부를 이용해서
        	//   나중에 수행되는 SQL의 조건으로 삼을수 있는 경우
        	// -> Mybatis의 <resultMap> , <collection> 태그를 이용해서 
        	//   mapper 메서드 1회 호출로 여러 SELECT 한번에 수행 가능

        	
        	
        return mapper.selectOne(map);
             
        }

	    

	    //좋아요 체크 / 해제
	  		@Override
	  		public int boardLike(Map<String, Integer> map) {
	  			
	  			int result = 0;
	  			
	  			
	  			
	  			
	  			
	  			if(map.get("likeCheck") == 1) {
	  				//1. 좋아요가 체크된 상태인 경우 (likeCheck == 1)
	  				// -> Board_LIKE 테이블에 DELETE 수행
	  				
	  				result = mapper.deleteBoardLike(map);
	  				
	  				log.debug("deleteBoardLike result {}", result);
	  				
	  			}else {
	  				//2. 좋아요가 체크가 해제된상태인 경우 (likeCheck == 0)
	  				// -> Board_LIKE 테이블에 INSERT 수행
	  				result = mapper.insertBoardLike(map);
	  				log.debug("insertBoardLike result {}", result);
	  			}
	  			
	  			//해당 게시글의 좋아요 개수를 조회해서 반환
	  			if(result > 0) {
	  				
	  				return mapper.selectLikeCount(map.get("boardNo"));
	  			}
	  			
	  			return -1; //좋아요 처리 실패
	  		}

	  		//조회수 1증가 서비스
	  		@Override
	  		public int updateReadCount(int boardNo) {
	  			
	  			//1. 조회수 1증가 (UPDATE)
	  			int result = mapper.updateReadCount(boardNo);
	  			
	  			//2. 현재 조회 수 조회
	  			
	  			if(result > 0) {
	  				return mapper.selectReadCount(boardNo);
	  			}
	  				
	  			// 실패한 경우 -1 반환
	  			return -1;
	  		}
	  		
	  		// 검색 서비스 (게시글 목록 조회 참고)
	  		@Override
	  		public Map<String, Object> searchList(Map<String, Object> paramMap, int cp) {
	  			// paramMap (key, query, boardCdoe)
	  			
	  			//1. 지정된 게시판 (boardCode)에서
	  			// 검색조건에 맞으면서
	  			// 삭제되지 않은 게시글 수를 조회

	  			int listCount = mapper.getSearchCount(paramMap);
	  			
	  			//2. 1번의 결과 + cp 를 이용해서
	  			// Pagination 객체를 생성
	  			Pagination pagination = new Pagination(cp, listCount);
	  			
	  			//3. 특정 게시판의 지정된 페이지 목록 조회
	  			int limit = pagination.getLimit(); // 10개씩 조회
				
				int offset = (cp - 1) * limit;
				
				RowBounds rowBounds = new RowBounds(offset,limit); 
				
				//mapper 메서드 호출 코드 수행
				// -> Mapper 메서드 호출 시 전달 할 수 있는 매개변수 1개
				// -> 2개를 전달할수 있는경우가있음
				// RowBounds 를 이용할 때
				// 1번쨰 : sql 에 전달할 파라미터
				// 2번째 : RowBounds 객체   (1번쨰 아무것도없으면 null 이래도 해야됨)
				List<Board> boardList = mapper.selectSearchList(paramMap,rowBounds);
	  			
	  			//4. 목록 조회 결과 + Pagination 객체를 Map으로 묶음
				Map<String, Object> map = new HashMap<>();
				map.put("pagination", pagination);
				map.put("boardList", boardList);
				
	  			return map;
	  		}
	  		
	  		
}









































