package edu.kh.project.share.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Share {

    private int    boardNo;                       // 게시판 번호 BOARD_NO (PK)
    private String boardTitle;                    // 게시판 제목 BOARD_TITLE
    private String boardContent;                  // 게시판 내용 BOARD_CONTENT
    private String boardWriteDate;                // 게시판 작성일 BOARD_WRITE_DATE
    private String boardUpdateDate;               // 게시판 수정일 BOARD_UPDATE_DATE
    private int    readCount;                     // 조회수 READ_COUNT
    private String boardDelFl;                    // 보드삭제여부 BOARD_DEL_FL  (Y/N)
    private int    memberNo;                      // 게시글 작성한 사람의 멤버넘버 MEMBER_NO     (FK)
    private int    boardCode;                     // 어느 게시판인지 BOARD_CODE    (FK)
    private String shareStatus;                   // 나눔이 완료인지 아닌지 SHARE_STATUS  (Y/N)  
    
    
    
    private int  shareBoardCategoryDetailCode;  // 소분류 카테고리 이름 SHARE_BOARD_CATEGORY_DETAIL_CODE (FK)- 
    
    
    private String shareBoardCategoryDetailName;  // 소분류 카테고리 번호 (물품이나 재능의 상세 종류) SHARE_BOARD_CATEGORY_DETAIL_NAME
    private int shareBoardCategoryCode; // 대분류 카테고리 번호 (물품0 재능1) SHARE_BOARD_CATEGORY_CODE (FK) 
    private String shareBoardCategoryName;   // 대분류 카테고리 이름 SHARE_BOARD_CATEGORY_NAME

}


/*
 
 
 1) 나눔게시판 쿼리 shareBoardList 
 


 <SELECT>

 SELECT BOARD_CODE, BOARD_NO, BOARD_TITLE, READ_COUNT, MEMBER_NICKNAME,  
 COMPLETION_STATUS,
(SELECT COUNT(*) FROM "COMMENT" C WHERE C.BOARD_NO = B.BOARD_NO  AND C.COMMENT_DEL_FL = 'N') COMMENT_COUNT,
(SELECT COUNT(*) FROM "BOARD_LIKE" L WHERE L.BOARD_NO = B.BOARD_NO ) LIKE_COUNT,


<![CDATA[

	CASE 	
	WHEN SYSDATE -  BOARD_WRITE_DATE < (1/24/60)
	THEN  FLOOR( (SYSDATE - BOARD_WRITE_DATE) * 24*60*60) || '초 전'
	
	WHEN SYSDATE -  BOARD_WRITE_DATE < (1/24)
	THEN  FLOOR( (SYSDATE - BOARD_WRITE_DATE) * 24*60) || '분 전'
	
	WHEN SYSDATE - BOARD_WRITE_DATE < 1
	THEN  FLOOR( (SYSDATE - BOARD_WRITE_DATE) * 24) || '시간 전'

ELSE TO_CHAR(BOARD_WRITE_DATE,'YYYY-MM-DD') 
END BOARD_WRITE_DATE -- 컬럼의 별칭 지정

]]>

FROM BOARD B 
JOIN "MEMBER" M ON (B.MEMBER_NO = M.MEMBER_NO) 
WHERE BOARD_DEL_FL = 'N' AND BOARD_CODE = #{boardCode} ORDER BY BOARD_NO DESC
 
 </SELECT>

 
 2) 나눔게시판 상세 쿼리 shareBoardDetail
 
 <SELECT >
 
 SELECT BOARD_NO, BOARD_TITLE, BOARD_CONTENT, BOARD_CODE, READ_COUNT, MEMBER_NO, MEMBER_NICKNAME, PROFILE_IMG, 
TO_CHAR(BOARD_WRITE_DATE,'YYYY"년"MM"월"DD"일" HH24:MI:SS') BOARD_WRITE_DATE,
TO_CHAR(BOARD_UPDATE_DATE,'YYYY"년"MM"월"DD"일" HH24:MI:SS') BOARD_UPDATE_DATE,
COMPLETION_STATUS,
(SELECT COUNT(*) FROM "BOARD_LIKE" WHERE BOARD_NO =#{boardNo}) LIKE_COUNT,

(SELECT IMG_PATH || IMG_RENAME FROM "BOARD_IMG" WHERE BOARD_NO = #{boardNo} AND IMG_ORDER=0) THUMBNAIL, 

(SELECT COUNT(*) FROM "BOARD_LIKE" WHERE BOARD_NO = #{boardNo} AND MEMBER_NO=#{memberNo}) LIKE_CHECK 

FROM "BOARD" 
JOIN "MEMBER" 
USING(MEMBER_NO) 
WHERE BOARD_DEL_FL = 'N' AND BOARD_NO = #{boardNo} AND BOARD_CODE= #{boardCode}
 </SELECT>
 
 + resultMap 필수
 
 
 
 
2-2) 글삭제 눌렀을 시 쿼리
	
 	<UPDATE>
 
    UPDATE "BOARD" SET BOARD_DEL_FL = 'Y' 
	WHERE BOARD_CODE = #{boardCode}
	AND BOARD_NO = #{boardNo}
	AND MEMBER_NO = #{memberNo}
 
 	</UPDATE>

 

 
	2-3) 나눔완료 눌렀을 시 쿼리

	<UPDATE>
 
	UPDATE "BOARD" SET
	SHARE_STATUS = #{shareStatus}
	WHERE BOARD_NO = #{boardNo}
 
 	</UPDATE>



	2-4) 찜 눌렀을 시 쿼리

	<UPDATE>
    INSERT INTO "BOARD_LIKE" VALUES(#{memberNo}, #{boardNo})
	</UPDATE>
 
 
 
	2-5) 찜 취소 시 쿼리

	<UPDATE>
    DELETE FROM "BOARD_LIKE" WHERE MEMBER_NO  = #{memberNo} AND BOARD_NO = #{boardNo}
	</UPDATE>
 	
 	
 	2-6) 현재 찜 개수 쿼리
 	
 <SELECT>
SELECT COUNT(*) FROM "BOARD_LIKE" WHERE BOARD_NO = #{boardNo}
</SELECT>

	2-7) 조회수 증가 쿼리

<UPDATE>

UPDATE "BOARD" SET READ_COUNT = READ_COUNT+1 WHERE BOARD_NO = #{boardNo}

</UPDATE>

	2-8) 현재 조회수 조회 쿼리
	
	<SELECT>

SELECT READ_COUNT FROM "BOARD" WHERE BOARD_NO = #{boardNo}

	</SELECT>
 
3) 글 삽입 쿼리 shareBoardInsert  
 
 
<insert id="boardInsert" useGeneratedKeys="true" parameterType="Board">
		<selectKey keyProperty="boardNo" resultType="_int" order="BEFORE">
			SELECT SEQ_BOARD_NO.NEXTVAL FROM DUAL
		</selectKey>

		INSERT INTO "BOARD" (
			BOARD_NO,
			BOARD_TITLE,
			BOARD_CONTENT,
			BOARD_CODE,
			MEMBER_NO
		) VALUES (
			#{boardNo},
			#{boardTitle},
			#{boardContent},
			#{boardCode},
			#{memberNo}
		)
	</insert>


 
 
  3-2) 이미지 여러 장 삽입 시 쿼리
  
   <INSERT>
     INSERT INTO "BOARD_IMG" (IMG_NO, IMG_PATH, IMG_ORIGINAL_NAME, IMG_RENAME, IMG_ORDER, BOARD_NO)
     <foreach collection="list" item="img" separator=" UNION ALL ">
         SELECT NEXT_IMG_NO(), #{img.imgPath}, #{img.imgOriginalName}, #{img.imgRename}, #{img.imgOrder}, #{img.boardNo} FROM DUAL
     </foreach>
  </INSERT>
 
   3-3) 카테고리 설정시 쿼리
   
   <INSERT>
  INSERT INTO "SHARE_CATEGORY_DETAIL"
    (BOARD_NO, SHARE_BOARD_CATEGORY_CODE, SHARE_BOARD_CATEGORY_DETAIL_CODE)
  VALUES
    (#{boardNo}, #{shareBoardCategoryCode}, #{shareBoardCategoryDetailCode})
 
   </INSERT>

 
 
 
 
 4) 글 수정 쿼리 shareBoardUpdate
 

 
 <UPDATE>
 UPDATE "BOARD" SET BOARD_TITLE = #{boardTitle}, BOARD_CONTENT= #{boardContent} 
WHERE BOARD_CODE = #{boardCode} AND BOARD_NO = #{boardNo} AND MEMBER_NO = #{memberNo}
 </UPDATE>
  
  
  
  	4-1) 업데이트 중 이미지 삭제 쿼리
  	<DELETE>
		DELETE FROM "BOARD_IMG" WHERE BOARD_NO = #{boardNo} AND IMG_ORDER IN (${deleteOrderList})
	</DELETE>
  
  
  	4-2) 업데이트 중 이미지 수정 쿼리
  	<UPDATE id="updateImage">
	UPDATE "BOARD_IMG" SET IMG_ORIGINAL_NAME = #{imgOriginalName},
	IMG_RENAME = #{imgRename}
	WHERE BOARD_NO = #{boardNo}
	AND IMG_ORDER = #{imgOrder}
	</UPDATE>
  	
  	
  	4-3) 업데이트 중 새로 이미지 한장 추가 시 쿼리
  	
    <INSERT>
	INSERT INTO "BOARD_IMG" VALUES(
	NEXT_IMG_NO(), 
	#{imgPath}, 
	#{imgOriginalName},
	#{imgRename},
	#{imgOrder},
	#{boardNo})
	</INSERT>
  
    4-4) 업데이트 중 기존 이미지 가운데 삭제하는 쿼리
  		<DELETE>
		DELETE FROM "BOARD_IMG" WHERE BOARD_NO = #{boardNo} AND IMG_ORDER IN (${deleteOrderList})
		</DELETE>
  
    4-5) 카테고리 변경 눌렀을 시 쿼리
	  
	 <UPDATE>
	  UPDATE "SHARE_CATEGORY_DETAIL"
  	SET SHARE_BOARD_CATEGORY_CODE = #{shareBoardCategoryCode},
      SHARE_BOARD_CATEGORY_DETAIL_CODE = #{shareBoardCategoryDetailCode}
  	WHERE BOARD_NO = #{boardNo}
	  
	 </UPDATE>

  
 */
 