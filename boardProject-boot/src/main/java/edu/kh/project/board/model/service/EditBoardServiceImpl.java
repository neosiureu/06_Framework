package edu.kh.project.board.model.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.mapper.EditBoardMapper;
import edu.kh.project.common.util.Utility;
import jakarta.mail.FolderClosedException;
import lombok.extern.slf4j.Slf4j;

@PropertySource("classpath:/config.properties")
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class EditBoardServiceImpl implements EditBoardService {

	@Autowired
	private EditBoardMapper mapper;

    @Value("${my.board.web-path}")
	private String webPath;

    @Value("${my.board.folder-path}")
	private String folderPath;

	// 게시글부분을 일단 보드 테이블에 먼저 insert하기 (이미지는 따로)

	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) throws Exception {
		// insert 결과로 작성된 게시글의 번호를 반환 받기 (두 테이블의 공통분모라서)
		
		
		// 1. 삽입의 성패
		int result = mapper.boardInsert(inputBoard);
		
		// 현재는 삽입 성공한 행의 개수로 0이나 1이 됨
		// 반환은 inputBoard에 알아서 들어가있음
		
		if(result==0) {
			return 0;
		}
		
		// 삽입 성공 시 
		// 삽입된 현 게시글의 번호를 변수로 저장
		int boardNo  = inputBoard.getBoardNo();
		// 얕은복사 때문에 inputBoard에 주소를 이 안에 전달함
		// map파일에서 inputbBoard역시 주소를 건들고 있고 그를 수정하므로 여기서 인자로 들어온것도 수정된다
		// mapper.xml에서 <selectKey> 태그를 이용해서 생성된
		// boardNo가 inputBoard에 저장된 상태 (얕은복사)
		
		
		// 2. 업로드된 이미지가 실제로 존재할 경우
		// 업로드된 이미지 한정으로 별도로 저장하여
		// BOARD_IMG테이블에 삽입하는 코드를 작성한다
		
		
		// 실제 업로드된 이미지의 정보를 모아둘 리스트를 생성
		// 가령 리스트 내 1번과 3번에만 실제 multipart객체가 둘만 있다면 그 둘만 있는 새로운 리스트를 만든다 
		
		
		List<BoardImg> uploadList = new ArrayList<>();
		
		// 인자로 받은 images리스트에서 하나씩 꺼내어 파일이 있는지 없는지 검사
		
		
		for(int i=0; i< images.size(); i++) {
			
			// 실제 선택된 파일이 존재하는 경우
			
			if(!images.get(i).isEmpty()) 
			{
				// 원본명을 꺼내 변경명을 만든다 => 모든 값들을 저장할 BoardImg DTO를 따로 만들어 저장
				
				// 원본명
				
				String originalName = images.get(i).getOriginalFilename();
				
				
				// 변경명
				String rename = Utility.fileRename(originalName);
				
				/*
				 * public class BoardImg {
	
	
					private int imgNo;
	
	
					private String imgPath;
					private String imgOriginalName;
					private String imgRename;
	
					private int imgOrder;
					private int boardNo;

	
	
					// 게시글 이미지 삽입 및 수정 시 사용하는 필드를 미리 만들어 놓는다
					private MultipartFile uploadFile;
					}
				 * */
				
				BoardImg img = BoardImg.builder()
						.imgOriginalName(originalName)
						.imgRename(rename)
						.imgPath(webPath)
						.boardNo(boardNo)
						.imgOrder(i)
						.uploadFile(images.get(i))
						.build();
						

				// 해당 BoardImg를 uploadList에 추가
				
				uploadList.add(img);
				
				/* List<MultipartFile> images에 만일 [O X X O O]이면
				  BoardImg null null BoardImg BoardImg
				  => uploadList라는 리스트에는  [BoardImg, BoardImg, BoardImg]
				*/	
				
			}
			
		}
		
		// 선택한 파일이 아예없는 경우
		
		if(uploadList.isEmpty()) {
			
			return boardNo; // 컨트롤러 단으로 보드의 이름만 넘김
			// 제목과 상세내용만을 삽입한 보드 자체의 넘버를 위로 넘기는 것
		}
		
		// 선택한 파일이 하나라도 있는 경우
		
		// 다시 "BOARD_IMG" 테이블에 추가로 insert하고 실제로 서버에 파일을 저장하기로 한다
		
		
		result  = mapper.insertUploadList(uploadList);
		// result의 값은 삽입된 행의 개수가 넘어 옴
		// 전부다 잘 삽입 됐다면 uploadList.size()와 result가 같다.
		
		
		// 다중 insert가 전부 성공했는지 insertUploadList가 전부 정상적으로 잘 삽입됐나?
		
		if(result==uploadList.size()) {
			// 전부 DB에 정상삽입 => 일단 서버에 해당 파일들을 전부 저장
			for(BoardImg img : uploadList) {
				img.getUploadFile().transferTo(new File(folderPath+img.getImgRename()) );
			}
			
			log.debug("디버깅 결과"+images);

		}
		
		else { // 부분적으로 또는 전부다 실패
			
			// 삽입된 것이 일부만 있을 때 
			// uploadList에 2개저장 하려 했는데 1개 삽입 1개 실패여도 그냥 다 실패한거임
			// 이전에 삽입된 내용을 전부 롤백한다
			
			
			// 스스로 롤백이 되지 않기 때문에 강제로 예외를 발생시켜야 한다
			// 발생만 시키면 @Transactional이 알아서 처리해줌
			
			throw new RuntimeException();
			

		}
		
		
		return boardNo;

	}
	
	

	/**
	 * 게시글 수정 서비스
	 */
	@Override
	public int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrderList) throws Exception {
		// 1. Board에서의 게시글 부분 (제목과 내용) 수정
		int result = mapper.boardUpdate(inputBoard);
		
		// 수정 실패 시 바로 리턴
		
		if(result ==0) return 0;
		
		// 2. 기존에 있었으나 삭제된 이미지가 있는 경우
		
		if(deleteOrderList != null && !deleteOrderList.equals("") ) {
			// deleteOrderList 자체와 boardNo를 가지고 delete로 간다
			Map<String, Object> map = new HashMap<>();
			map.put("deleteOrderList",deleteOrderList);
			map.put("boardNo", inputBoard.getBoardNo());
			
			result = mapper.deleteImage(map);
			
			// 삭제 실패될 경우 롤백
			
			if(result==0) 
			{
				throw new RuntimeException();
				// 억지로 롤백시키기 위함
			}
			
			
			
			// 3. 선택한 파일이 존재할 경우에 해당 파일 정보만 모아두는 List 생성
			
			// 어쨋든 MultipartFile타입의 List객체의 요소는 5개
			
			List <BoardImg> uploadList = new ArrayList<>();
			// 왜 제한을 이렇게 함?
			// for문을 이용해 images리스트에서 multipartFile객체가 실제로 null이 아닌지 검사할 것
			// 있다면 BoardImg타입으로 빌더를 이용해 추가할 것임
			
			// images List에서 하나씩 꺼내어 파일이 있는지 검사
			
			for(int i=0; i<images.size(); i++) {
				// 실제 선택된 파일이 비어있지 않은지 (존재는 하니?)
				if(!images.get(i).isEmpty()) {
					
					// 원본명을 얻어서 rename한다
					
					String originalName = images.get(i).getOriginalFilename();
					
					// 변경명을 얻어온다 (현재 날짜 시간 .jpg)
					
					String rename = Utility.fileRename(originalName);
					
					
					// 모든 값을 저장할 DTO 객체 생성 (BoardImg)
					BoardImg img = BoardImg.builder()
							.imgOriginalName(originalName)
							.imgRename(rename)
							.imgPath(webPath)
							.boardNo(inputBoard.getBoardNo())
							.imgOrder(i)
							.uploadFile(images.get(i))
							.build(); //업로드한 이미지들에 대한 모든 정보
					
					/*
					private int imgNo;
	
					private String imgPath;
					private String imgOriginalName;
					private String imgRename;
					private int imgOrder;
					private int boardNo;

	
					// 게시글 이미지 삽입 및 수정 시 사용하는 필드를 미리 만들어 놓는다
					private MultipartFile uploadFile; 
					
					 */
					
					uploadList.add(img);
					
					// 4. 업로드하려는 이미지 정보를 이용하여 수정하거나 삽입을 수행한다
					
					// 4-1 기존 이미지가 있었는데 새 이미지로 수정한 경우 => update 매퍼 호출
					
					result = mapper.updateImage(img); // 현재 만든 이미지 객체 하나만 전달
					// for문을 돌면서 수정
					
					
					
					if(result==0) {
						// 이미지가 변경이 안 됐다 = 해당 순서의 이미지번호에 해당하는 이미지가 비어있었다.
						// =>기존 해당 순서 img_order에  이미지가 없으면  삽입해라.
						
						// 4-2) 기존에 없을 때 새 이미지를 추가
						result = mapper.insertImg(img); // 현재 다루고있는 img 객체 하나만 전달
						
						
					}
					
				}
				if(result ==0) {
					
					throw new RuntimeException(); 
					// 예외발생을 통한 롤백
				}
			} //for문 끝
			
			// 선택된 이미지 파일이 하나도 없을 경우
			if(uploadList.isEmpty()) {
				return result;
			}
			// 업로드 리스트가 비어있다 = for문 돌면서 update insert 없다
			// 마지막으로 바꾼 delete를 했을 때 result 또는 그 위 제목이나 내용 수정 시의 result
			
			
			
			// 지금까지는 DB에만 했으니 
			// 서버에 수정하거나 새로 삽입한 이미지를 저장한다
			// C uploadFiles boardImg에 저장
			
			for(BoardImg img: uploadList) {
				img.getUploadFile().transferTo(new File(
						folderPath + img.getImgRename())
						);
			}
			
			
		}
		
		return result;
	}



	@Override
	public int boardDelete(Map<String, Integer> map) {
		// TODO Auto-generated method stub
		return mapper.boardDelete(map);
	}

	
	

}
