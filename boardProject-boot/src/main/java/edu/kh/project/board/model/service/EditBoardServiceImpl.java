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
// import jakarta.mail.FolderClosedException; // 사용되지 않는 import문은 제거하는 것이 좋습니다.
import lombok.extern.slf4j.Slf4j;

@PropertySource("classpath:/config.properties")
@Transactional(rollbackFor = Exception.class) // 런타임 예외 발생 시 rollback
@Service
@Slf4j
public class EditBoardServiceImpl implements EditBoardService {

	@Autowired
	private EditBoardMapper mapper;

	@Value("${my.board.web-path}")
	private String webPath; // /images/board/

	@Value("${my.board.folder-path}")
	private String folderPath; // C:/uploadFiles/board/

	// 게시글 작성 (기존 코드 그대로 유지)
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) throws Exception {

		// 1. 게시글 부분을 먼저 BOARD 테이블 INSERT 하기
		int result = mapper.boardInsert(inputBoard);

		if (result == 0) {
			return 0;
		}

		int boardNo = inputBoard.getBoardNo();

		// 2. 업로드된 이미지가 실제로 존재할 경우 BOARD_IMG 테이블에 삽입 준비
		List<BoardImg> uploadList = new ArrayList<>();

		for (int i = 0; i < images.size(); i++) {
			if (!images.get(i).isEmpty()) {
				String originalName = images.get(i).getOriginalFilename();
				String rename = Utility.fileRename(originalName);

				BoardImg img = BoardImg.builder().imgOriginalName(originalName).imgRename(rename).imgPath(webPath)
						.boardNo(boardNo).imgOrder(i).uploadFile(images.get(i)).build();

				uploadList.add(img);
			}
		}

		// 선택한 파일이 전부 없을 경우
		if (uploadList.isEmpty()) {
			return boardNo;
		}

		// 3. 선택한 파일이 존재할 경우 BOARD_IMG 테이블에 insert
		result = mapper.insertUploadList(uploadList);

		// 다중 INSERT 성공 확인 (uploadList의 모든 이미지가 정상 삽입되었는지)
		if (result == uploadList.size()) {
			// 서버에 파일 저장
			for (BoardImg img : uploadList) {
				img.getUploadFile().transferTo(new File(folderPath + img.getImgRename()));
			}
		} else {
			// 부분적으로 삽입 실패 시 롤백
			log.error("Failed to batch insert images. Expected: {}, Inserted: {}", uploadList.size(), result);
			throw new RuntimeException("Failed to insert images.");
		}

		return boardNo;
	}

	/**
	 * 게시글 수정 서비스 (수정된 로직)
	 */
	@Override
	public int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrderList) throws Exception {

		// 1. 게시글 부분(제목/내용) 수정
		// 성공하면 result = 1
		int result = mapper.boardUpdate(inputBoard);

		// 수정 실패 시 바로 리턴 (Optimistic Lock 등을 사용하는 경우 0이 될 수 있음)
		if (result == 0) {
			log.warn("Board update (text) failed for boardNo: {}", inputBoard.getBoardNo());
			return 0;
		}

		// 2. 기존 O -> 삭제된 이미지(deleteOrderList)가 있는 경우
		// 매퍼 XML에서 deleteOrder -> deleteOrderList로 수정되었다는 가정 하에 진행
		if (deleteOrderList != null && !deleteOrderList.equals("")) {

			Map<String, Object> map = new HashMap<>();
			map.put("deleteOrderList", deleteOrderList); // 매퍼 XML과 키 이름 일치 확인!
			map.put("boardNo", inputBoard.getBoardNo());

			// 실제로 삭제된 이미지 개수
			int deleteCount = mapper.deleteImage(map);

			// 삭제하려는 이미지가 있었는데 하나도 삭제되지 않았다면 문제 발생으로 간주하고 롤백
			// (deleteOrderList가 비어있지 않은데 deleteCount가 0인 경우)
			// 더 정확하게는 deleteOrderList의 개수와 deleteCount를 비교해야 할 수도 있으나,
			// 여기서는 0이면 문제로 간주하여 롤백합니다.
			// 만약 삭제할 대상 이미지들이 DB에 원래 없었다면 deleteCount는 0이 될 수 있습니다.
			// 이 경우는 예외로 처리하지 않아야 할 수도 있으니 비즈니스 로직에 맞게 조정 필요.
			// 현재 코드에서는 deleteOrderList가 있으면 최소 하나 이상 삭제되어야 정상으로 판단.
			// if (deleteCount == 0) { // 이 체크는 deleteOrderList에 실제로 존재하는 순서가 없을 때도 발동함
            //     log.warn("Image deletion attempted for boardNo {} but no images deleted with orders: {}", inputBoard.getBoardNo(), deleteOrderList);
            //     // throw new RuntimeException("Failed to delete specified images."); // 엄격하게 하려면 롤백
			// }
             log.debug("Deleted {} images for boardNo {} with orders: {}", deleteCount, inputBoard.getBoardNo(), deleteOrderList);


		}

		// --- 이미지 수정/삽입 대상 목록 (uploadList) 생성 ---
		// 이 루프는 파일이 비어있지 않은 경우에만 BoardImg 객체를 생성하여 uploadList에 추가하는 역할만 합니다.
		List<BoardImg> uploadList = new ArrayList<>();

		for (int i = 0; i < images.size(); i++) {
			// 실제 선택된 파일이 존재하는 경우 (!= 비어있는 input type="file" )
			if (!images.get(i).isEmpty()) {

				// 원본명
				String originalName = images.get(i).getOriginalFilename();

				// 변경명
				String rename = Utility.fileRename(originalName);

				// 모든 값을 저장할 DTO 객체 생성 (BoardImg)
				BoardImg img = BoardImg.builder().imgOriginalName(originalName).imgRename(rename).imgPath(webPath)
						.boardNo(inputBoard.getBoardNo()).imgOrder(i) // 파일이 업로드된 인덱스 == 이미지 순서
						.uploadFile(images.get(i)).build();

				// 해당 BoardImg를 uploadList에 추가
				uploadList.add(img);
			}
		}

		// --- uploadList에 있는 이미지 정보들을 이용하여 DB 수정/삽입 수행 ---
		// 이 루프는 DB에 실제로 반영하는 역할만 합니다.
		if (!uploadList.isEmpty()) {
			int insertUpdateCount = 0; // 성공적으로 DB에 반영된 이미지 수 카운트

			for (BoardImg img : uploadList) {
				// 해당 순서(imgOrder)의 이미지가 DB에 이미 있다면 수정(UPDATE), 없다면 삽입(INSERT) 수행

				// 1) 기존 이미지가 있었는데 새 이미지로 수정한 경우 => update 매퍼 호출 시도
				int imageResult = mapper.updateImage(img); // 해당 순서의 이미지 정보 업데이트 시도

				if (imageResult == 0) {
					// 2) update 실패 (해당 순서에 이미지가 없었거나 문제 발생). 삽입 시도.
					// 기존에 해당 imgOrder에 이미지가 없었으므로 새로 삽입합니다.
					imageResult = mapper.insertImg(img);
				}

				// update 또는 insert 둘 중 하나라도 성공했다면 카운트 증가
				if (imageResult > 0) {
					insertUpdateCount++;
				} else {
					// 만약 update도 실패하고 insert도 실패했다면 심각한 문제 -> 롤백
					// 어떤 이미지 때문에 실패했는지 로그에 남기면 디버깅에 도움됨
					log.error("Image DB operation failed (update or insert) for boardNo: {}, imgOrder: {}",
							img.getBoardNo(), img.getImgOrder());
					throw new RuntimeException("Image DB operation failed"); // 예외 발생 -> 롤백
				}
			} // end of uploadList DB loop

			// DB 업데이트/삽입이 uploadList의 모든 이미지에 대해 성공했는지 최종 확인
			// (위 루프에서 실패 시 즉시 예외를 던지므로 이 체크는 사실상 항상 true이거나 도달하지 않음)
			if (insertUpdateCount != uploadList.size()) {
				// 이 곳에 도달했다면 예상치 못한 로직 오류일 수 있음
				log.error("Mismatch in successful image DB operations count. Expected: {}, Actual: {}", uploadList.size(), insertUpdateCount);
				throw new RuntimeException("Mismatch in successful image DB operations count"); // 예외 발생 -> 롤백
			}


            // --- DB에 성공적으로 반영된 이미지 파일을 서버에 저장 ---
            // DB 작업이 모두 성공한 후에 파일 저장 (DB 롤백 시 파일만 남는 것 방지)
			for (BoardImg img : uploadList) {
				try {
					img.getUploadFile().transferTo(new File(folderPath + img.getImgRename()));
				} catch (Exception e) {
					// 파일 저장 실패 시 로그를 남기고 예외를 던져 DB 트랜잭션도 롤백되게 함
					log.error("Failed to save file to server for boardNo: {}, imgRename: {}", img.getBoardNo(),
							img.getImgRename(), e);
					throw new RuntimeException("Failed to save image file"); // 예외 발생 -> 롤백
				}
			}
		} // end of if (!uploadList.isEmpty())

		// 게시글 부분 수정, 이미지 삭제, 이미지 수정/삽입, 파일 저장이
		// 중간에 예외 발생 없이 모두 완료되었다면 게시글 수정은 성공으로 간주.
		// 최초의 게시글 제목/내용 수정 결과를 반환하거나 단순히 성공 의미의 1을 반환.
		// 최초 result (게시글 제목/내용 수정 결과)는 1이므로 result를 반환해도 무방합니다.
		return result; // result는 최초 게시글 수정 성공 여부 (1)을 담고 있습니다.
	}

	// 게시글 삭제 (기존 코드 그대로 유지)
	@Override
	public int boardDelete(Map<String, Integer> map) {
		// TODO Auto-generated method stub
		return mapper.boardDelete(map);
	}

}