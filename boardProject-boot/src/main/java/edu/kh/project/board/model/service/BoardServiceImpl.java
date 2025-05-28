package edu.kh.project.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.Pagination;
import edu.kh.project.board.model.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardMapper mapper;

	/**
	 * 게시판 종류 조회 서비스
	 */
	@Override
	public List<Map<String, Object>> selectBoardTypeList() {
		// TODO Auto-generated method stub
		return mapper.selectBoardTypeList();
	}

	
	// 특정 게시판에 지정된 페이지 목록 조회 서비스
	@Override
	public Map<String, Object> selectBoardList(int boardCode, int cp) {

		// 페이지네이션 >의 의미: 다음 목록의 첫 페이지로 이동하기 위함
		// 맨 끝 목록으로 이동하려면 >>>를 쓰곤 한다
		
		// 1. 지정된 게시판 (boardCode)에서 삭제되지 않은 게시글 수를 조회 (pagination에 쓰기 위함이다.)
		
		int listCount = mapper.getListCount(boardCode);
		// 몇 번 게시판에 있는 게시글 수를 조회
		
		
		// 2. 페이지네이션의 생성 => 1번의 결과와 컨트롤러에서 전달받은 cp를 이용하여 
		// 페이지네이션 객체 자체를 생성한다
		
		// Pagination 객체는 게시글 목록 구성에 필요한 값들을 저장하는 객체
		
		Pagination pagination = new Pagination(cp, listCount); // 현재 목록, 전체 게시글

		
		// 3. 특정 게시판만의 지정된 페이지 목록만 조회 => 딱 limit인 10개씩만
		
		/* 
		
		mybatis framework가 제공하는 객체 
		=> 지정된 크기만큼 건너 뛰고 제한된 크기만큼의 행을 조회하는 객체
		
		가령 31~40이면 1~30은 건너뛰고 10개만
		
		지정된 크기만큼 건너 뛰는 인자 offset
		제한된 크기 = limit
		
		*/
		
		int limit = pagination.getLimit(); //10이 반환
		int offset = (cp-1) * limit;
		
		RowBounds rowBounds = new RowBounds(offset,limit);
		
		
		// Mapper 메서드 호출 시에는 매개변수로 하나만 전달 가능 
		// => rowBounds를 이용할 때는 둘 다 이용가능
		// 첫 인자 = SQL에 전달할 파라미터, 두번째 인자  = rowBounds
		
		// 게시판의 종류
		List<Board> boardList = mapper.selectBoardList(boardCode, rowBounds);
		
		//log.debug("boardList결과"+boardList);
		
		// log.debug("boardList결과 : {}", boardList);
		
		
		
		// 컨트롤러에게 boardList와 pagenation을 돌려줘야 한다
		
		
		// 4. 목록 조회결과인 boardList와 Pagenation 객체를 map으로 묶어 반환
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("pagination", pagination);
		map.put("boardList", boardList);

		
		return map ;
	
	}


	// 게시글 상세조회
	@Override
	public Board selectOne(Map<String, Integer> map) {
		
		// 총 3개의 SQL문을 실행해야 한다 => 어떻게 실행할래?
		
		// 1) 한 서비스 단에서 여러 매퍼 메서드를 호출
		
		/*
		
		
		2) 만일 수행하려는 SQL들이 모두 SELECT만 있으며
		먼저 조회된 결과 중 일부를 이용해 
		나중에 수행하는 SQL의 조건으로 삼을 수 있는 경우
		
		
		Mybatis 태그 중 <resultMap>과 <collection>태그를 이용하여 
		mapper메서드 1회 호출을 통해 여러 SELECT를 한 번에 수행할 수 있다
		
		
		*/
		
		return mapper.selectOne(map);
	}


	@Override
	public int boardLike(Map<String, Integer> map) {
		// 넘어온 likeCheck가 1인지 0인지
		
		int result=0; 

		if(map.get("likeCheck")==1) { 		
			// 좋아요 체크된 상태인 경우 likecheck가 1인경우 BOARD_LIKE테이블에 DELETE

			result = mapper.deleteBoardLike(map);
			
		}

		else {		
			// 좋아요 체크가 해제된 상태인 경우 likecheck가 0인경우 BOARD_LIKE테이블에 INTSERT
			result = mapper.insertBoardLike(map);

		}
		
		
		// 해제나 삽입 시 result의 숫자가 변경되곤 함  
		//   <span th:text="*{likeCount}">1</span> ← 좋아요 개수      
		// 3. 해당 게시글의 좋아요 개수를 조회해서 반환

		if(result>0) {
			return mapper.selectLikeCount(map.get("boardNo"));
			
		}
		
		return -1; // insert delete 좋아요 처리는 무조건 실패한 셈
	}


	/**
	 * 조회수 1 증가 서비스
	 */
	@Override
	public int updateReadCount(int boardNo) {
		// 조회수 1 증가시키는 업데이트문을 호출
		int result = mapper.updateReadCount(boardNo);

		
		// 그 후 변경한 게시글의 조회수 자체를 전체 조회
		
		if(result>0) {
			return mapper.selectReadCount(boardNo);
			
		}
		
		
		
		// 조회수 증가가 실패한 경우 -1을 반환
		return -1;
	}


	// 게시글 완료/미완료 상태 수정 서비스 구현
		@Override 
		public int updateCompletion(Map<String, Object> paramMap) { // Map을 인자로 받음
			log.debug("BoardServiceImpl.updateCompletion 실행, paramMap: " + paramMap); // 메소드 실행 확인

			// BoardMapper의 해당 메소드를 호출하여 DB 업데이트 수행
			
			return mapper.updateCompletion(paramMap); // BoardMapper에 정의할 메소드 호출
		}

		
		
		

		/**
		 * 검색서비스이지만 게시글 목록 서비스를 배끼면 된다.
		 */
		@Override
		public Map<String, Object> searchList(Map<String, Object> paraMap, int cp) {
			// paramMap(key, query, boardCode)

			// selectBoardList에서 삭제되지 않은 게시글 수를 이랃ㄴ getListCount()로 가져와서 페이지네이션을 만들었었음
			
			
			// 1. boardCode에 해당하는 게시판에서 검색되지 않으면서 삭제되지 않은 게시글수를 조회하겠다
			
			
			int listCount = mapper.getSearchCount(paraMap); // paramMap안에 boardCode, 검색조건에 따른 키 쿼리등이 다 있음
			
			
			// 2. 1번의 결과와 cp를 이용하여 페이지네이션 객체를 생성
			Pagination pagination = new Pagination(cp, listCount);

			
			
			// 3. 진짜 페이지 목록 조회

			int limit = pagination.getLimit(); //10이 반환
			int offset = (cp-1) * limit;
			
			RowBounds rowBounds = new RowBounds(offset,limit);
			
			
			// mapper 메서드 호출하여 원하는 코드 수행
			// mapper메서드 호출 시 전달할 수 있는 경우는 하나 뿐
			// 둘을 전달할 수 있는 경우: RowBounds 이용할 때
			
			// 인자의 종류
			// 1번째: SQL에 전달할 파라미터 (없으면 null이라도)
			// 2번째: RowBounds 객체


			List<Board> boardList = mapper.selectSearchList(paraMap, rowBounds);

			
			// 4. boardList와 pagination 객체를 map으로 묶어 위로 간다
			Map<String, Object> map = new HashMap<>();
			map.put("pagination", pagination);
			map.put("boardList", boardList);
			
			
			return map;
		}


		@Override
		public List<String> selectDBImageList() {
			// TODO Auto-generated method stub
			return mapper.selectDBImageList();
		}
	
	
	

}
