package edu.kh.project.board.model.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.mapper.EditBoardMapper;
import edu.kh.project.common.util.Utility;
import jakarta.mail.FolderClosedException;

@PropertySource("classpath:/config.properties")
@Transactional(rollbackFor = Exception.class)
@Service
public class EditBoardServiceImpl implements EditBoardService {

	@Autowired
	private EditBoardMapper mapper;

	@org.springframework.beans.factory.annotation.Value("${my.board.web-path}")
	private String webPath;

	@org.springframework.beans.factory.annotation.Value("${my.board.folder-path}")
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

}
