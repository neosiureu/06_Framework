


SELECT * FROM "MESSAGE" m ; 
CREATE TABLE "MEMBER" (
	"MEMBER_NO"	NUMBER		NOT NULL,
	"MEMBER_EMAIL"	NVARCHAR2(50)		NOT NULL,
	"MEMBER_PW"	NVARCHAR2(100)		NOT NULL,
	"MEMBER_NICKNAME"	NVARCHAR2(10)		NOT NULL,
	"MEMBER_TEL"	CHAR(11)		NOT NULL,
	"MEMBER_ADDRESS"	NVARCHAR2(300)		NULL,
	"PROFILE_IMG"	VARCHAR2(300)		NULL,
	"ENROLL_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"MEMBER_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"AUTHORITY"	NUMBER	DEFAULT 1	NOT NULL
);

SELECT MEMBER_NO, MEMBER_EMAIL, MEMBER_NICKNAME, AUTHORITY FROM "MEMBER" 
WHERE MEMBER_EMAIL = #{memberEmail} AND MEMBER_DEL_FL = 'N' AND AUTHORITY = 2

COMMENT ON COLUMN "MEMBER"."MEMBER_NO" IS '회원 번호(PK)';

COMMENT ON COLUMN "MEMBER"."MEMBER_EMAIL" IS '회원 이메일(ID 역할)';

COMMENT ON COLUMN "MEMBER"."MEMBER_PW" IS '회원 비밀번호(암호화)';

COMMENT ON COLUMN "MEMBER"."MEMBER_NICKNAME" IS '회원 닉네임';

COMMENT ON COLUMN "MEMBER"."MEMBER_TEL" IS '회원 전화 번호';

COMMENT ON COLUMN "MEMBER"."MEMBER_ADDRESS" IS '회원 주소';

COMMENT ON COLUMN "MEMBER"."PROFILE_IMG" IS '프로필 이미지';

COMMENT ON COLUMN "MEMBER"."ENROLL_DATE" IS '회원 가입일';

COMMENT ON COLUMN "MEMBER"."MEMBER_DEL_FL" IS '탈퇴 여부(Y,N)';

COMMENT ON COLUMN "MEMBER"."AUTHORITY" IS '권한(1:일반, 2:관리자)';

-- 회원 번호 시퀀스 만들기
CREATE SEQUENCE SEQ_MEMBER_NO NOCACHE;


SELECT * FROM "MEMBER";

UPDATE "MEMBER" SET MEMBER_DEL_FL = 'N' WHERE  MEMBER_NO =1;

COMMIT;


SELECT * FROM MEMBER;


SELECT * FROM MEMBER WHERE MEMBER_EMAIL = 'user01@kh.or.kr';

SELECT MEMBER_EMAIL 
FROM MEMBER 
WHERE MEMBER_NICKNAME = '유저일'
  AND MEMBER_TEL = '01012341235';

SELECT COUNT(*) FROM "MEMBER" 
WHERE MEMBER_EMAIL =  'user01@kh.or.kr' 
AND MEMBER_DEL_FL = 'N'; 

-- 샘플 회원 데이터 삽입
INSERT INTO "MEMBER"
VALUES(SEQ_MEMBER_NO.NEXTVAL, 
			 'admin03@kh.or.kr',
			 '$2a$10$SC0KYLzAdtkZT12h5wTWT.csuiG.boiRC4ct6Mioe5puIo4W7GZmW',
			 '관리자삼',
			 '01012341234',
			 NULL,
			 NULL,
			 DEFAULT,
			 DEFAULT,
			2
);

COMMIT;

SELECT * FROM "MEMBER";

SELECT MEMBER_PW FROM MEMBER WHERE MEMBER_EMAIL = 'admin02@kh.or.kr';


SELECT MEMBER_NO, MEMBER_EMAIL, MEMBER_NICKNAME, AUTHORITY FROM "MEMBER" 
WHERE AUTHORITY = 2;





SELECT * FROM "MEMBER";


UPDATE "MEMBER" SET MEMBER_PW = '$2a$10$HF.3Lx9EGBmpkWgwvd3oNerAp94oZjzY7jRB043w6zkuBHtCeOkru' WHERE MEMBER_NO =1;

SELECT MEMBER_NO, MEMBER_EMAIL, MEMBER_NICKNAME, MEMBER_PW, MEMBER_TEL, MEMBER_ADDRESS, PROFILE_IMG, AUTHORITY, TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일" MI"분" SS"초"') ENROLL_DATE
FROM "MEMBER" WHERE MEMBER_EMAIL  = 'user01@kh.or.kr' AND MEMBER_DEL_FL = 'N'; 

COMMIT;
-- 25년 4월 21일 여기까지 수행

UPDATE MEMBER
SET MEMBER_PW = '$2a$10$OgOsV0ERsTBilppi70cL2eJMXorRULWDcVrcM4EO2ooozvoL/FF.G'
WHERE MEMBER_EMAIL = 'user01@kh.or.kr';
-----------------------------------------

/* 이메일, 인증키 저장 테이블 생성 */
CREATE TABLE "TB_AUTH_KEY"(
	"KEY_NO"    NUMBER PRIMARY KEY,
	"EMAIL"	    NVARCHAR2(50) NOT NULL,
	"AUTH_KEY"  CHAR(6)	NOT NULL,
	"CREATE_TIME" DATE DEFAULT SYSDATE NOT NULL
);

COMMENT ON COLUMN "TB_AUTH_KEY"."KEY_NO"      IS '인증키 구분 번호(시퀀스)';
COMMENT ON COLUMN "TB_AUTH_KEY"."EMAIL"       IS '인증 이메일';
COMMENT ON COLUMN "TB_AUTH_KEY"."AUTH_KEY"    IS '인증 번호';
COMMENT ON COLUMN "TB_AUTH_KEY"."CREATE_TIME" IS '인증 번호 생성 시간';

CREATE SEQUENCE SEQ_KEY_NO NOCACHE; -- 인증키 구분 번호 시퀀스


SELECT * FROM "TB_AUTH_KEY";

COMMIT;


-- 25년 4월 22일 여기까지 수행

------------------------------------------


-- 파일 
CREATE TABLE "UPLOAD_FILE" (
	"FILE_NO"	NUMBER		NOT NULL,
	"FILE_PATH"	VARCHAR2(500)		NOT NULL,
	"FILE_ORIGINAL_NAME"	VARCHAR2(300)		NOT NULL,
	"FILE_RENAME"	VARCHAR2(100)		NOT NULL,
	"FILE_UPLOAD_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_NO" IS '파일 번호(PK)';

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_PATH" IS '파일 요청 경로';

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_ORIGINAL_NAME" IS '파일 원본명';

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_RENAME" IS '파일 변경명';

COMMENT ON COLUMN "UPLOAD_FILE"."FILE_UPLOAD_DATE" IS '업로드 날짜';

COMMENT ON COLUMN "UPLOAD_FILE"."MEMBER_NO" IS '업로드한 회원 번호';
		
-- 시퀀스 SEQ_FILE_NO 생성		
CREATE SEQUENCE SEQ_FILE_NO NOCACHE;

SELECT * FROM "UPLOAD_FILE";


SELECT FILE_NO, FILE_PATH, FILE_ORIGINAL_NAME, FILE_RENAME, MEMBER_NICKNAME, TO_CHAR(FILE_UPLOAD_DATE, 'YYYY-MM-DD') FILE_UPLOAD_DATE FROM "UPLOAD_FILE" JOIN "MEMBER" ON ("UPLOAD_FILE".MEMBER_NO = "MEMBER".MEMBER_NO)
WHERE "UPLOAD_FILE".MEMBER_NO =1 ORDER BY FILE_NO DESC;




-- 25년 4월 24일 여기까지 수행



------------------------------------------

/* 게시판 테이블 생성 */
CREATE TABLE "BOARD" (
	"BOARD_NO"	NUMBER		NOT NULL,
	"BOARD_TITLE"	NVARCHAR2(100)		NOT NULL,
	"BOARD_CONTENT"	VARCHAR2(4000)		NOT NULL,
	"BOARD_WRITE_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"BOARD_UPDATE_DATE"	DATE		NULL,
	"READ_COUNT"	NUMBER	DEFAULT 0	NOT NULL,
	"BOARD_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"BOARD_CODE"	NUMBER		NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL
);


COMMENT ON COLUMN "BOARD"."BOARD_NO" IS '게시글 번호(PK)';

COMMENT ON COLUMN "BOARD"."BOARD_TITLE" IS '게시글 제목';

COMMENT ON COLUMN "BOARD"."BOARD_CONTENT" IS '게시글 내용';

COMMENT ON COLUMN "BOARD"."BOARD_WRITE_DATE" IS '게시글 작성일';

COMMENT ON COLUMN "BOARD"."BOARD_UPDATE_DATE" IS '게시글 마지막 수정일';

COMMENT ON COLUMN "BOARD"."READ_COUNT" IS '조회수';

COMMENT ON COLUMN "BOARD"."BOARD_DEL_FL" IS '게시글 삭제 여부(Y/N)';

COMMENT ON COLUMN "BOARD"."BOARD_CODE" IS '게시판 종류 코드 번호';

COMMENT ON COLUMN "BOARD"."MEMBER_NO" IS '작성한 회원 번호(FK)';


SELECT * FROM "BOARD_TYPE";


-- 게시판 종류 테이블
CREATE TABLE "BOARD_TYPE" (
	"BOARD_CODE"	NUMBER		NOT NULL,
	"BOARD_NAME"	NVARCHAR2(20)		NOT NULL
);



COMMENT ON COLUMN "BOARD_TYPE"."BOARD_CODE" IS '게시판 종류 코드 번호';
COMMENT ON COLUMN "BOARD_TYPE"."BOARD_NAME" IS '게시판명';


SELECT * FROM "BOARD_TYPE";



-- 게시판 좋아요 테이블
CREATE TABLE "BOARD_LIKE" (
	"MEMBER_NO"	NUMBER		NOT NULL,
	"BOARD_NO"	NUMBER		NOT NULL
);

--  PK 복합키: 둘다 같은 숫자가 들어갈 순 없다
-- 1 1000 이후 1 2000은 할 수 있지만, 2 1000도 할 수 있지만
-- 1 1000 이후 1 1000을 또 하지는 못한다.

COMMENT ON COLUMN "BOARD_LIKE"."MEMBER_NO" IS '회원 번호(PK)';
COMMENT ON COLUMN "BOARD_LIKE"."BOARD_NO" IS '게시글 번호(PK)';


SELECT * FROM BOARD_LIKE;

INSERT INTO "BOARD_LIKE" VALUES(1,1998);
-- 1번회원이 1998번 게시글에 좋아요를 누름 => 하트가 채워져야
--  1번회원이 1998번 게시글에 좋아요를 다시 누름 => 하트가 없어져야

COMMIT;

-- 좋아요 여부 확인 = 개수 세기 (눌렀으면 1또는 안 눌렀으면 0으로 결과가 나옴) 

SELECT COUNT(*) FROM "BOARD_LIKE" WHERE BOARD_NO = 2000 AND MEMBER_NO=1 ;



SELECT * FROM BOARD_IMG bi ;
-- 게시판 이미지 테이블
CREATE TABLE "BOARD_IMG" (
	"IMG_NO"	NUMBER		NOT NULL,
	"IMG_PATH"	VARCHAR2(200)		NOT NULL,
	"IMG_ORIGINAL_NAME"	NVARCHAR2(50)		NOT NULL,
	"IMG_RENAME"	NVARCHAR2(50)		NOT NULL,
	"IMG_ORDER"	NUMBER		NULL,
	"BOARD_NO"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "BOARD_IMG"."IMG_NO" IS '이미지 번호(PK)';

COMMENT ON COLUMN "BOARD_IMG"."IMG_PATH" IS '이미지 요청 경로';

COMMENT ON COLUMN "BOARD_IMG"."IMG_ORIGINAL_NAME" IS '이미지 원본명';

COMMENT ON COLUMN "BOARD_IMG"."IMG_RENAME" IS '이미지 변경명';

COMMENT ON COLUMN "BOARD_IMG"."IMG_ORDER" IS '이미지 순서';

COMMENT ON COLUMN "BOARD_IMG"."BOARD_NO" IS '게시글 번호(PK)';




-- 댓글 테이블
CREATE TABLE "COMMENT" (
	"COMMENT_NO"	NUMBER		NOT NULL,
	"COMMENT_CONTENT"	VARCHAR2(4000)		NOT NULL,
	"COMMENT_WRITE_DATE"	DATE	DEFAULT SYSDATE	NOT NULL,
	"COMMENT_DEL_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"BOARD_NO"	NUMBER		NOT NULL,
	"MEMBER_NO"	NUMBER		NOT NULL,
	"PARENT_COMMENT_NO"	NUMBER
);

COMMENT ON COLUMN "COMMENT"."COMMENT_NO" IS '댓글 번호(PK)';

COMMENT ON COLUMN "COMMENT"."COMMENT_CONTENT" IS '댓글 내용';

COMMENT ON COLUMN "COMMENT"."COMMENT_WRITE_DATE" IS '댓글 작성일';

COMMENT ON COLUMN "COMMENT"."COMMENT_DEL_FL" IS '댓글 삭제 여부(Y/N)';

COMMENT ON COLUMN "COMMENT"."BOARD_NO" IS '게시글 번호(PK)';

COMMENT ON COLUMN "COMMENT"."MEMBER_NO" IS '회원 번호(PK)';

COMMENT ON COLUMN "COMMENT"."PARENT_COMMENT_NO" IS '부모 댓글 번호';

-- SELECT * FROM "BOARD";



--------------------- PK -----------------------

ALTER TABLE "MEMBER" ADD CONSTRAINT "PK_MEMBER" PRIMARY KEY (
	"MEMBER_NO"
); -- 수행함


ALTER TABLE "UPLOAD_FILE" ADD CONSTRAINT "PK_UPLOAD_FILE" PRIMARY KEY (
	"FILE_NO"
);  -- 수행함

ALTER TABLE "BOARD" ADD CONSTRAINT "PK_BOARD" PRIMARY KEY (
	"BOARD_NO"
); -- 수행함

ALTER TABLE "BOARD_TYPE" ADD CONSTRAINT "PK_BOARD_TYPE" PRIMARY KEY (
	"BOARD_CODE"
); -- 수행함

ALTER TABLE "BOARD_LIKE" ADD CONSTRAINT "PK_BOARD_LIKE" PRIMARY KEY (
	"MEMBER_NO",
	"BOARD_NO"
); -- 수행함

ALTER TABLE "BOARD_IMG" ADD CONSTRAINT "PK_BOARD_IMG" PRIMARY KEY (
	"IMG_NO"
); -- 수행함

ALTER TABLE "COMMENT" ADD CONSTRAINT "PK_COMMENT" PRIMARY KEY (
	"COMMENT_NO"
); -- 수행함

-------------------- FK -------------------------


ALTER TABLE "UPLOAD_FILE" ADD CONSTRAINT "FK_MEMBER_TO_UPLOAD_FILE_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);  
-- 수행함 
--MEMBER테이블의 PK에 실제로 있는 데이터만 넣을 수 있음



ALTER TABLE "BOARD" ADD CONSTRAINT "FK_BOARD_TYPE_TO_BOARD_1" FOREIGN KEY (
	"BOARD_CODE"
)
REFERENCES "BOARD_TYPE" (
	"BOARD_CODE"
);









ALTER TABLE "BOARD" ADD CONSTRAINT "FK_MEMBER_TO_BOARD_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);



ALTER TABLE "BOARD_LIKE" ADD CONSTRAINT "FK_MEMBER_TO_BOARD_LIKE_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);











ALTER TABLE "BOARD_LIKE" ADD CONSTRAINT "FK_BOARD_TO_BOARD_LIKE_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "BOARD" (
	"BOARD_NO"
);










ALTER TABLE "BOARD_IMG" ADD CONSTRAINT "FK_BOARD_TO_BOARD_IMG_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "BOARD" (
	"BOARD_NO"
);






ALTER TABLE "COMMENT" ADD CONSTRAINT "FK_BOARD_TO_COMMENT_1" FOREIGN KEY (
	"BOARD_NO"
)
REFERENCES "BOARD" (
	"BOARD_NO"
);




ALTER TABLE "COMMENT" ADD CONSTRAINT "FK_MEMBER_TO_COMMENT_1" FOREIGN KEY (
	"MEMBER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);




ALTER TABLE "COMMENT" ADD CONSTRAINT "FK_COMMENT_TO_COMMENT_1" FOREIGN KEY (
	"PARENT_COMMENT_NO"
)
REFERENCES "COMMENT" (
	"COMMENT_NO"
);

-- 댓글 뿐 아니라 해당 댓글에 대한 대댓글을 달기 위해 댓글의 부모를 알아야 함















---------------------- CHECK -----------------------

-- 이미 CHAR(1)인 회원 탈퇴 여부를 Y나 N으로만 추가적 제약을 걸기로 한다.
ALTER TABLE "MEMBER" ADD CONSTRAINT "MEMBER_DEL_CHECK" CHECK("MEMBER_DEL_FL" IN ('Y','N')); 
-- 수행됨

-- 게시글 삭제 여부
ALTER TABLE "BOARD" ADD
CONSTRAINT "BOARD_DEL_CHECK"
CHECK("BOARD_DEL_FL" IN ('Y', 'N') );

-- 댓글 삭제 여부
ALTER TABLE "COMMENT" ADD
CONSTRAINT "COMMENT_DEL_CHECK"
CHECK("COMMENT_DEL_FL" IN ('Y', 'N') );
	

-- 4/28 수행함

	
/* 게시판 종류(BOARD_TYPE) 추가 */
CREATE SEQUENCE SEQ_BOARD_CODE NOCACHE;

INSERT INTO "BOARD_TYPE" VALUES(SEQ_BOARD_CODE.NEXTVAL, '공지 게시판');
INSERT INTO "BOARD_TYPE" VALUES(SEQ_BOARD_CODE.NEXTVAL, '정보 게시판');
INSERT INTO "BOARD_TYPE" VALUES(SEQ_BOARD_CODE.NEXTVAL, '자유 게시판');

COMMIT;

SELECT * FROM "MEMBER";


SELECT * FROM "BOARD_TYPE";



SELECT BOARD_CODE "boardCode" , BOARD_NAME "boardName" FROM "BOARD_TYPE" ORDER BY BOARD_CODE;

-- 자바에서 사용하는 카멜 케이스로 바꿔야하기 때문에 별칭을 설정한다. 컬럼명이 Key로, 컬럼값이 Value로



---------------------------------------------


/* 게시글 번호용 시퀀스 생성 */
CREATE SEQUENCE SEQ_BOARD_NO NOCACHE; 

-- PLSQL

BEGIN
	FOR I IN 1..2000 LOOP
		
		INSERT INTO "BOARD"
		VALUES(SEQ_BOARD_NO.NEXTVAL,
					 SEQ_BOARD_NO.CURRVAL || '번째 게시글',
					 SEQ_BOARD_NO.CURRVAL || '번째 게시글 내용 입니다',
					 DEFAULT, DEFAULT, DEFAULT, DEFAULT,
					 CEIL( DBMS_RANDOM.VALUE(0,3) ),
					 1 -- 회원번호
		);
-- 네가지 DEFAULT값 = BOARD테이블에 가서 보자
-- 작성일, 수정일, 조회수, 삭제여부, 보드코드(123중 하나), 회원번호
-- 모든 글을 1번 회원이 		
	END LOOP;
END;

COMMIT;

SELECT * FROM MEMBER;

SELECT * FROM "BOARD";




---------------------------------------------------
-- 부모 댓글 번호 NULL 허용


/* 댓글 번호 시퀀스 생성 */
CREATE SEQUENCE SEQ_COMMENT_NO NOCACHE;

/* 댓글 ("COMMNET") 테이블에 샘플 데이터 추가*/

BEGIN
	FOR I IN 1..2000 LOOP
	
		INSERT INTO "COMMENT"	
		VALUES(
			SEQ_COMMENT_NO.NEXTVAL,
			SEQ_COMMENT_NO.CURRVAL || '번째 댓글 입니다', 
			DEFAULT, DEFAULT,
			CEIL( DBMS_RANDOM.VALUE(0, 2000) ), -- 어느 글에 단 댓글인가??
			2,
			NULL --parent 숫자. 즉 부모 댓글의 번호 (대댓글은 없다는 뜻)
		);
	END LOOP;
END;

COMMIT;


SELECT count(*) FROM "COMMENT";












-- 특정 게시판(BOARD_CODE=123)에서 삭제되지 않은 게시글 목록을 조회한다
-- 단 최신 글이 제일 위에 조회되도록
-- 작성일 : 몇초 - 몇분전 -  몇시간전 - 24시간 넘어갈때만 YYYY-MM-DD형식으로 조회

-- 글번호 제목 작성자 작성일 조회수 좋아요

-- 게시글번호/ 제목/ [댓글개수]/ 작성자닉네임/ 작성일/ 조회수 / 좋아요 개수


SELECT * FROM BOARD;
SELECT * FROM BOARD_LIKE;
SELECT * FROM MEMBER;
SELECT * FROM COMMENT;


--SELECT BOARD_NO, BOARD_TITLE, READ_COUNT, MEMBER_NICKNAME  FROM BOARD B 
--JOIN "MEMBER" M ON (B.MEMBER_NO = M.MEMBER_NO) 
--WHERE BOARD_DEL_FL = 'N' AND BOARD_CODE =1 ORDER BY BOARD_NO DESC;




-- 상관 서브쿼리 
	-- FLOOR( (SYSDATE - BOARD_WRITE_DATE) * 24*60) 은 항상 일을 반환함

SELECT BOARD_NO, BOARD_TITLE, READ_COUNT, MEMBER_NICKNAME,  (SELECT COUNT(*) FROM "COMMENT" C 
WHERE C.BOARD_NO = B.BOARD_NO) COMMENT_COUNT,
(SELECT COUNT(*) FROM "BOARD_LIKE" L WHERE L.BOARD_NO = B.BOARD_NO ) LIKE_COUNT,
CASE 
	
	
	
	WHEN SYSDATE -  BOARD_WRITE_DATE < (1/24/60)
	THEN  FLOOR( (SYSDATE - BOARD_WRITE_DATE) * 24*60*60) || '초 전'
	
	WHEN SYSDATE -  BOARD_WRITE_DATE < (1/24)
	THEN  FLOOR( (SYSDATE - BOARD_WRITE_DATE) * 24*60) || '분 전'
	
	WHEN SYSDATE - BOARD_WRITE_DATE < 1
	THEN  FLOOR( (SYSDATE - BOARD_WRITE_DATE) * 24) || '시간 전'

ELSE TO_CHAR(BOARD_WRITE_DATE,'YYYY-MM-DD') 
END BOARD_WRITE_DATE -- 컬럼의 별칭 지정


FROM BOARD B 
JOIN "MEMBER" M ON (B.MEMBER_NO = M.MEMBER_NO) 
WHERE BOARD_DEL_FL = 'N' AND BOARD_CODE =1 ORDER BY BOARD_NO DESC;




/*
 
 메인 쿼리 한행 조회 => 그 한 행의 조회 결과를 이용해 서브쿼리를 수행
 
*/










-- 4월 28일 => 이 이전 모든 코드를 수행함


-----------------------------------------------------

/* BOARD_IMG 테이블용 시퀀스 생성 */
CREATE SEQUENCE SEQ_IMG_NO NOCACHE;


SELECT * FROM board_img;



/* BOARD_IMG 테이블에 샘플 데이터 삽입 */
INSERT INTO "BOARD_IMG" VALUES(
	SEQ_IMG_NO.NEXTVAL, '/images/board/' /* 웹상주소 */, '원본1.jpg' /* 원본 이름 */, 'test1.jpg'  /* rename된 이름 */, 0 /* 화면상에 뿌릴 순서 0=썸네일 1=일반이미지 제일 왼쪽...*/ , 1998 /* 이 이미지가 삽입된 게시글 번호 */
);

INSERT INTO "BOARD_IMG" VALUES(
	SEQ_IMG_NO.NEXTVAL, '/images/board/', '원본2.jpg', 'test2.jpg', 1, 1998
);

INSERT INTO "BOARD_IMG" VALUES(
	SEQ_IMG_NO.NEXTVAL, '/images/board/', '원본3.jpg', 'test3.jpg', 2, 1998
);

INSERT INTO "BOARD_IMG" VALUES(
	SEQ_IMG_NO.NEXTVAL, '/images/board/', '원본4.jpg', 'test4.jpg', 3, 1998
);

INSERT INTO "BOARD_IMG" VALUES(
	SEQ_IMG_NO.NEXTVAL, '/images/board/', '원본5.jpg', 'test5.jpg', 4, 1998
);

--SELECT * FROM BOARD_IMG WHERE BOARD_NO = 1997 AND IMG_ORDER = 2;


COMMIT;


SELECT * FROM "BOARD";

SELECT * FROM "BOARD_LIKE";

SELECT * FROM "BOARD_IMG";

/* 
 
멤버1이 게시글2000번에 좋아요를 누르면 => BOARD_LIKE에 1 2000이라고 삽입 
=> pk이기 때문에 두번은 못누름 => 하트가 채워지면 삽입, 하트가 없어지면 삭제일 뿐

눌렀으면 1, 안 눌렀으면 0만 나오기 때문에 YN과 같은 컬럼이 따로 필요하지 않다는 것이 핵심


*/
SHOW ERRORS FUNCTION NEXT_IMG_NO;


SELECT * FROM "BOARD_IMG";
-- 실제 <img src = /images/board/test1.jpg></img>로 들어감



INSERT INTO "BOARD_IMG" (SELECT NEXT_IMG_NO(), '경로1', '원본1', '변경1', 1, 1998 FROM DUAL
-- 여러개의 SELECT를 한번에
UNION 
SELECT NEXT_IMG_NO(), '경로2', '원본2', '변경2', 2, 1998 FROM DUAL
UNION 
SELECT NEXT_IMG_NO(), '경로3', '원본3', '변경3', 3, 1998 FROM DUAL
);

----사실 시퀀스는 한 묶음에서 사용 불가해서 따로 함수가 필요
-- VALUES대신 SELECT의 서브쿼리

-- SEQ_IMG_NO시퀀스의 다음 값을 반환하는 함수 생성하기



CREATE OR REPLACE FUNCTION NEXT_IMG_NO
RETURN NUMBER -- 반환형

IS IMG_NO NUMBER; -- 사용할 변수

BEGIN
	SELECT SEQ_IMG_NO.NEXTVAL
	INTO IMG_NO
	FROM DUAL;

	RETURN IMG_NO;
END;

COMMIT ;



SELECT * FROM "MEMBER";

DELETE FROM BOARD_IMG WHERE IMG_NO IN (7,8,9,10);

COMMIT;
/*

SELECT * FROM "BOARD";


SELECT * FROM "BOARD_IMG";
-- 실제 <img src = /images/board/test1.jpg></img>로 들어감

SELECT * FROM "COMMENT";

이 세 테이블로부터 한 번의 명령어로 select할 게획

*/

SELECT BOARD_NO, BOARD_TITLE, BOARD_CONTENT, BOARD_CODE, READ_COUNT, MEMBER_NO, MEMBER_NICKNAME, PROFILE_IMG, 
TO_CHAR(BOARD_WRITE_DATE,'YYYY"년"MM"월"DD"일" HH24:MI:SS') BOARD_WRITE_DATE,
TO_CHAR(BOARD_UPDATE_DATE,'YYYY"년"MM"월"DD"일" HH24:MI:SS') BOARD_UPDATE_DATE,
(SELECT COUNT(*) FROM "BOARD_LIKE" WHERE BOARD_NO =1998) LIKE_COUNT,
(SELECT IMG_PATH ||IMG_RENAME FROM "BOARD_IMG" WHERE BOARD_NO = 1998 AND IMG_ORDER=0) THUMBNAIL, 
(SELECT COUNT(*) FROM "BOARD_LIKE" WHERE BOARD_NO = 1998 AND MEMBER_NO=1) LIKE_CHECK --자기가 좋아요한적이 있는지 하트가 채워지고 비워져있는 여부로
FROM "BOARD" 
JOIN "MEMBER" 
USING(MEMBER_NO) 
WHERE BOARD_DEL_FL = 'N' AND BOARD_NO = 1998 AND BOARD_CODE=1;

/*
 
"BOARD 테이블에서 삭제되지 않은 특정 게시글(BOARD_NO = 1997, BOARD_CODE = 1)에 대해 
제목, 내용, 작성자 번호, 작성자 닉네임, 작성자 프로필 이미지, 작성/수정 일자, 조회수 정보를 조회하고, 
추가로 BOARD_LIKE 테이블을 통해 해당 게시글이 받은 좋아요 수와 특정 회원이 좋아요를 눌렀는지 여부를 함께 조회하며, 
BOARD_IMG 테이블을 통해 썸네일(대표 이미지) 경로를 함께 가져오는 쿼리문 
 
*/


-- 좋아요 받은 개수를 얻으려면 board like라는 테이블을 따로 만들어야 어떤 회원이 어떤 게시글에  대해 좋아요를 눌렀는지를 알아내게 됨








-- 현재 상세조회하고 있는 게시글의 이미지 목록 조회하는 SQL문
SELECT * FROM BOARD_IMG WHERE BOARD_NO = 1998 ORDER BY IMG_ORDER;





-- 현재 상세조회하고 있는 게시글의 댓글 목록 조회하는 SQL문



-- 댓글 목록 조회 SQL




SELECT LEVEL, C.* FROM --c는 서브쿼리를 의미
	(SELECT COMMENT_NO, COMMENT_CONTENT,
	    TO_CHAR(COMMENT_WRITE_DATE, 'YYYY"년" MM"월" DD"일" HH24"시" MI"분" SS"초"') COMMENT_WRITE_DATE,
	    BOARD_NO, MEMBER_NO, MEMBER_NICKNAME, PROFILE_IMG, PARENT_COMMENT_NO, COMMENT_DEL_FL
	FROM "COMMENT"
	JOIN MEMBER USING(MEMBER_NO)
	WHERE BOARD_NO = 1998) C
WHERE COMMENT_DEL_FL = 'N' OR 0 != ( SELECT COUNT(*) FROM "COMMENT" SUB
				WHERE SUB.PARENT_COMMENT_NO = C.COMMENT_NO
				AND COMMENT_DEL_FL = 'N' ) 
				-- 상관쿼리: 삭제되지 않은 자식 댓글의 개수가 0이 아니다 => 부모 댓글을 조회댓글에 포함하겠다 => 쉽게 말해 부모가 살아있다면 자식도 삭제하지 않고 데려가겠다
				
START WITH PARENT_COMMENT_NO IS NULL -- 계층형을 만드는 것은 이것
CONNECT BY PRIOR COMMENT_NO = PARENT_COMMENT_NO -- 두 넘버가 같다면 계층을 이루는 부분
ORDER SIBLINGS BY COMMENT_NO -- 같은 레벨 안에서는 COMMENT_NO순서를 오름차순으로 정렬
;


-- oracle에서 제공하는 계층형 데이터를 조회할 때 몇 번째 레벨에 있는지 알려주는 가상의 컬럼


/*
COMMENT 테이블에서 특정 게시글(BOARD_NO = 1997)의 삭제되지 않은 댓글과, 
삭제된 댓글이더라도 삭제되지 않은 자식 댓글을 가진 댓글까지 포함하여, 
작성자 정보(MEMBER 테이블에서 닉네임과 프로필 이미지)를 함께 조회하고, 
계층 구조(부모-자식 관계)를 따라 댓글을 정렬하여 각 댓글의 깊이(LEVEL)와 함께 가져오는 쿼리문
 */


SELECT * FROM "COMMENT";
-- 이를 계층형 쿼리라 한다 LEVEL표시 + 서브쿼리 C의 내용들 => 인라인 뷰 (SQL에서 조회된 result set을 하나의 가상 테이블과 같이 쓰곤 한다) => 그 결과가 C

/*


인라인 뷰란,
SQL문 안에서 (SELECT ...) 별칭 형태로 작성하여,
하나의 SELECT 결과 집합(Result Set)을 테이블처럼 다루는 구조를 말한다.

"COMMENT" 테이블과 "MEMBER" 테이블을 조인한 뒤,

특정 게시글(BOARD_NO = 1997)의 댓글 데이터만 뽑아서

작성자 닉네임과 프로필 이미지까지 함께 가져온 서브 결과를

C라는 이름의 인라인 뷰로 만들어 사용하고 있다.
  
*/



-- 댓글 샘플데이터
INSERT INTO "COMMENT"	
VALUES( SEQ_COMMENT_NO.NEXTVAL, '부모 댓글 1' /*댓글내용*/,
			  DEFAULT, DEFAULT,	1998 /*댓글달린 게시글*/, 1 /*댓글단 회원 번호*/ , NULL /*부모댓글에 대한 정보*/);
			 
INSERT INTO "COMMENT"	
VALUES( SEQ_COMMENT_NO.NEXTVAL, '부모 댓글 2',
			  DEFAULT, DEFAULT,	1998, 1, NULL);
			 
INSERT INTO "COMMENT"	
VALUES( SEQ_COMMENT_NO.NEXTVAL, '부모 댓글 3',
			  DEFAULT, DEFAULT,	1998, 1, NULL);



-- 최상위 부모만 만든 셈 2001번, 2002번 2003번




-- 그 최상위 부모에 대해서 자식
-- 부모 댓글 1의 자식 댓글
INSERT INTO "COMMENT"	
VALUES( SEQ_COMMENT_NO.NEXTVAL, '부모 1의 자식 1',
			  DEFAULT, DEFAULT, 1998, 2, 2001);
			 
INSERT INTO "COMMENT"	
VALUES( SEQ_COMMENT_NO.NEXTVAL, '부모 1의 자식 2',
			  DEFAULT, DEFAULT,	1998 , 2, 2001);
			 
			 
-- 부모 댓글 2의 자식 댓글			 
INSERT INTO "COMMENT"	
VALUES( SEQ_COMMENT_NO.NEXTVAL, '부모 2의 자식 1',
			  DEFAULT, DEFAULT,	1998, 2, 2002);
			 

SELECT * FROM "COMMENT" c ORDER BY c.COMMENT_NO DESC; 


-- 2002가 1레벨 (댓글) 2006이 2레벨 (대댓글) , 2007이 3레벨 (대대댓글)


-- 최상위 부모의 자식의 자식

-- 부모 댓글 2의 자식 1의 자식 댓글			 
INSERT INTO "COMMENT"	
VALUES( SEQ_COMMENT_NO.NEXTVAL, '부모 2의 자식 1의 자식!!!',
			  DEFAULT, DEFAULT,	1998, 2, 2006);


SELECT * FROM "COMMENT" ORDER BY COMMENT_NO DESC; 
COMMIT;

-------------------------------------------------------

/* 좋아요 테이블(BOARD_LIKE) 샘플 데이터 추가 */
INSERT INTO "BOARD_LIKE"
VALUES(2, 2000); -- 2번 회원이 2003번 글에 좋아요를 클릭함

COMMIT;






-- 4월 29일 이 위로 다 시행






-- 1. COMPLETION_STATUS 컬럼 추가

ALTER TABLE "BOARD" ADD "COMPLETION_STATUS" CHAR(1) DEFAULT 'N' NOT NULL;

-- 2. COMPLETION_STATUS 컬럼에 'Y' 또는 'N' 값만 허용하는 제약 조건 추가 
ALTER TABLE "BOARD" ADD CONSTRAINT "BOARD_COMP_CHECK" CHECK("COMPLETION_STATUS" IN ('Y','N'));

-- 변경사항 적용
COMMIT;

SELECT * FROM "BOARD_IMG";

SELECT * FROM MEMBER;




----------------------------------------------------------

-- SEQ_IMG_NO 시퀀스의 다음 값을 반환하는 함수 생성

-- 전체 드래그 ALT+X
CREATE OR REPLACE FUNCTION NEXT_IMG_NO
-- 반환형
RETURN NUMBER
-- 사용할 변수
IS IMG_NO NUMBER;
BEGIN 
	SELECT SEQ_IMG_NO.NEXTVAL 
	INTO IMG_NO
	FROM DUAL;

	RETURN IMG_NO;
END;
-- 여기까지 긁기

SELECT NEXT_IMG_NO() FROM DUAL;











-- 하나의 SQL에서 두가지 테이블을 합쳐서 가져가겠다

/*
SELECT SUBSTR(PROFILE_IMG, INSTR(PROFILE_IMG,'/',-1)+1) AS "RENAME" FROM "MEMBER" 
WHERE PROFILE_IMG IS NOT NULL
UNION
SELECT IMG_RENAME FROM "BOARD_IMG";
*/

-- SQL Error [12704] [72000]: ORA-12704: 문자 집합이 일치하지 않습니다
-- FROM "MEMBER" 는 varchar2 300이었지만 FROM "BOARD_IMG"는 nvarchar2 50
-- 둘이 타입 자체가 맞지 않기 때문에 오류 => 형변환



SELECT SUBSTR(PROFILE_IMG, INSTR(PROFILE_IMG,'/',-1)+1) AS "RENAME" FROM "MEMBER" 
WHERE PROFILE_IMG IS NOT NULL
UNION
SELECT CAST (IMG_RENAME AS VARCHAR2(300)) "rename" FROM "BOARD_IMG";


----------------------------------------------------------
/* 채팅 */
CREATE TABLE "CHATTING_ROOM" (
	"CHATTING_ROOM_NO"	NUMBER		NOT NULL,
	"CREATE_DATE"	DATE	DEFAULT CURRENT_DATE	NOT NULL,
	"OPEN_MEMBER"	NUMBER		NOT NULL,
	"PARTICIPANT"	NUMBER		NOT NULL
);

COMMENT ON COLUMN "CHATTING_ROOM"."CHATTING_ROOM_NO" IS '채팅방 번호';
COMMENT ON COLUMN "CHATTING_ROOM"."CREATE_DATE" IS '채팅방 생성일';
COMMENT ON COLUMN "CHATTING_ROOM"."OPEN_MEMBER" IS '개설자 회원 번호';
COMMENT ON COLUMN "CHATTING_ROOM"."PARTICIPANT" IS '참여자 회원 번호';


-- 기존 'MESSAGE' 테이블의 이름을 'MESSAGES'로 변경합니다. (채팅 메시지 테이블)
CREATE TABLE "MESSAGES" (
	"MESSAGE_NO"	NUMBER		NOT NULL,
	-- 컬럼 이름은 그대로 유지합니다.
	"MESSAGE_CONTENT"	VARCHAR2(4000)		NOT NULL,
	"READ_FL"	CHAR(1)	DEFAULT 'N'	NOT NULL,
	"SEND_TIME"	DATE	DEFAULT CURRENT_DATE	NOT NULL,
	"SENDER_NO"	NUMBER		NOT NULL,
	"CHATTING_ROOM_NO"	NUMBER		NOT NULL
);




-- COMMENT ON COLUMN 구문에서도 테이블 이름을 'MESSAGES'로 변경합니다.
COMMENT ON COLUMN "MESSAGES"."MESSAGE_NO" IS '메시지 번호';
COMMENT ON COLUMN "MESSAGES"."MESSAGE_CONTENT" IS '메시지 내용';
COMMENT ON COLUMN "MESSAGES"."READ_FL" IS '읽음여부Y/N';
COMMENT ON COLUMN "MESSAGES"."SEND_TIME" IS '메시지 보낸 시간';
COMMENT ON COLUMN "MESSAGES"."SENDER_NO" IS '메시지 보낸 회원 번호';
COMMENT ON COLUMN "MESSAGES"."CHATTING_ROOM_NO" IS '채팅방 번호';

ALTER TABLE "CHATTING_ROOM" ADD CONSTRAINT "PK_CHATTING_ROOM" PRIMARY KEY (
	"CHATTING_ROOM_NO"
);

-- PRIMARY KEY 제약 조건에서 참조하는 테이블 이름을 'MESSAGES'로 변경하고, 제약 조건 이름도 변경하는 것이 일반적입니다.
ALTER TABLE "MESSAGES" ADD CONSTRAINT "PK_MESSAGES" PRIMARY KEY (
	"MESSAGE_NO"
);

ALTER TABLE "CHATTING_ROOM" ADD CONSTRAINT "FK_MEMBER_TO_CHATTING_ROOM_1" FOREIGN KEY (
	"OPEN_MEMBER"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

ALTER TABLE "CHATTING_ROOM" ADD CONSTRAINT "FK_MEMBER_TO_CHATTING_ROOM_2" FOREIGN KEY (
	"PARTICIPANT"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

-- FOREIGN KEY 제약 조건에서 참조되는 테이블 이름을 'MESSAGES'로 변경하고, 제약 조건 이름도 변경하는 것이 일반적입니다.
ALTER TABLE "MESSAGES" ADD CONSTRAINT "FK_MEMBER_TO_MESSAGES_1" FOREIGN KEY (
	"SENDER_NO"
)
REFERENCES "MEMBER" (
	"MEMBER_NO"
);

-- FOREIGN KEY 제약 조건에서 참조되는 테이블 이름을 'MESSAGES'로 변경하고, 제약 조건 이름도 변경하는 것이 일반적입니다.
ALTER TABLE "MESSAGES" ADD CONSTRAINT "FK_CHATTING_ROOM_TO_MESSAGES_1" FOREIGN KEY (
	"CHATTING_ROOM_NO"
)
REFERENCES "CHATTING_ROOM" (
	"CHATTING_ROOM_NO"
);


-- 채팅방 번호 생성 시퀀스 (변경 없음)
CREATE SEQUENCE SEQ_ROOM_NO NOCACHE;

-- 메시지 번호 생성 시퀀스 이름도 'MESSAGES' 테이블에 맞게 변경하는 것이 좋습니다.
CREATE SEQUENCE SEQ_MESSAGES_NO NOCACHE;


/* 로그인한 회원이 참여한 채팅방 목록 조회*/
-- 특정 사용자가 사용한 채팅방 목록 (각 채팅방의 최근 메시지, 관련 정보 등등이 있을텐데 그를 조회하는 용)
-- 조회 쿼리에서도 'MESSAGE' 테이블이 사용된 부분을 'MESSAGES'로 변경.
SELECT CHATTING_ROOM_NO
	-- 서브쿼리 안의 'MESSAGE' 테이블 이름을 'MESSAGES'로 변경.
	,(SELECT MESSAGE_CONTENT FROM
		(SELECT * FROM MESSAGES M2
		WHERE M2.CHATTING_ROOM_NO = R.CHATTING_ROOM_NO
		ORDER BY MESSAGE_NO DESC)
		WHERE ROWNUM = 1) LAST_MESSAGE
	-- 서브쿼리 안의 'MESSAGE' 테이블 이름을 'MESSAGES'로 변경합니다.
	,TO_CHAR(NVL((SELECT MAX(SEND_TIME) SEND_TIME
			FROM MESSAGES M -- 널 처리 함수 => 앞의 작성한 값이 null이면 뒤의 값으로 대체
			WHERE R.CHATTING_ROOM_NO  = M.CHATTING_ROOM_NO), CREATE_DATE), -- 채팅방이 개설만 됐고 오간적이 없다.
			'YYYY.MM.DD') SEND_TIME
	,NVL2((SELECT OPEN_MEMBER FROM CHATTING_ROOM R2   -- 널 처리 함수 => null이라면 세번째 인자로 대입 null이 아니라면 두번째 인자로
		WHERE R2.CHATTING_ROOM_NO = R.CHATTING_ROOM_NO
		AND R2.OPEN_MEMBER = 1),
		R.PARTICIPANT, -- null이 아닌 경우 = 로그인한 사람이 내가 채팅을 오픈한 멤버일 때는 참가자가 이 메시지를 받는 타겟의 번호이다.
		R.OPEN_MEMBER -- null인 경우 참가자인 내가 메시지를 보낸 타겟은 오픈 멤버
		) TARGET_NO
	,NVL2((SELECT OPEN_MEMBER FROM CHATTING_ROOM R2
		WHERE R2.CHATTING_ROOM_NO = R.CHATTING_ROOM_NO
		AND R2.OPEN_MEMBER = 1),
		(SELECT MEMBER_NICKNAME FROM MEMBER WHERE MEMBER_NO = R.PARTICIPANT),
		(SELECT MEMBER_NICKNAME FROM MEMBER WHERE MEMBER_NO = R.OPEN_MEMBER)
		) TARGET_NICKNAME
	,NVL2((SELECT OPEN_MEMBER FROM CHATTING_ROOM R2
		WHERE R2.CHATTING_ROOM_NO = R.CHATTING_ROOM_NO
		AND R2.OPEN_MEMBER = 1),
		(SELECT PROFILE_IMG FROM MEMBER WHERE MEMBER_NO = R.PARTICIPANT),
		(SELECT PROFILE_IMG FROM MEMBER WHERE MEMBER_NO = R.OPEN_MEMBER)
		) TARGET_PROFILE
	-- 서브쿼리 안의 'MESSAGE' 테이블 이름을 'MESSAGES'로 변경합니다.
	,(SELECT COUNT(*) FROM MESSAGES M
	WHERE M.CHATTING_ROOM_NO = R.CHATTING_ROOM_NO
	AND READ_FL = 'N' AND SENDER_NO != 1) NOT_READ_COUNT -- 내가 보낸 것을 제외한 것들 중 읽지 않은 메시지 수를 가져간다.
	-- 서브쿼리 안의 'MESSAGE' 테이블 이름을 'MESSAGES'로 변경합니다.
	,(SELECT MAX(MESSAGE_NO) SEND_TIME
	FROM MESSAGES M
	WHERE R.CHATTING_ROOM_NO  = M.CHATTING_ROOM_NO) MAX_MESSAGE_NO
FROM CHATTING_ROOM R
WHERE OPEN_MEMBER = 1
OR PARTICIPANT = 1
ORDER BY MAX_MESSAGE_NO DESC NULLS LAST;
-- 채팅방 열어놓고 아무것도 안하는 경우 마지막에 두겠다.




CREATE TABLE VOTE (
    VOTE_ID NUMBER PRIMARY KEY,
    CHOICE CHAR(1) CHECK (CHOICE IN ('A', 'B')) NOT NULL,
    COUNT NUMBER DEFAULT 0 NOT NULL
);

CREATE SEQUENCE VOTE_SEQ NOCACHE NOCYCLE;


INSERT INTO VOTE (VOTE_ID, CHOICE, COUNT)
VALUES (VOTE_SEQ.NEXTVAL, 'A', 0);

INSERT INTO VOTE (VOTE_ID, CHOICE, COUNT)
VALUES (VOTE_SEQ.NEXTVAL, 'B', 0);

SELECT * FROM VOTE;

CREATE TABLE USER_VOTE (
    USER_ID   NUMBER     PRIMARY KEY,  -- MEMBER 테이블의 PK (1인 1표이므로 PK로 사용)
    CHOICE    CHAR(1)    CHECK (CHOICE IN ('A', 'B')), -- 투표한 항목

    CONSTRAINT FK_USER_VOTE_MEMBER 
        FOREIGN KEY (USER_ID) 
        REFERENCES MEMBER(MEMBER_NO)
        ON DELETE CASCADE
);



SELECT * FROM USER_VOTE;

DELETE FROM USER_VOTE;

SELECT * FROM "MEMBER";

COMMIT;










CREATE TABLE MESSAGE (
    MESSAGE_NO      NUMBER              PRIMARY KEY,  -- 쪽지 고유 번호
    SENDER_NO       NUMBER              NOT NULL,     -- 보낸 회원 번호
    RECEIVER_NO     NUMBER              NOT NULL,     -- 받은 회원 번호
    CONTENT         NVARCHAR2(1000)     NOT NULL,     -- 쪽지 내용
    SEND_DATE       DATE DEFAULT SYSDATE NOT NULL,    -- 보낸 날짜
    READ_FL         CHAR(1) DEFAULT 'N' NOT NULL,     -- 읽음 여부 (N:안읽음, Y:읽음)

    -- 외래 키 제약 조건
    CONSTRAINT FK_MESSAGE_SENDER FOREIGN KEY (SENDER_NO)
        REFERENCES MEMBER(MEMBER_NO)
        ON DELETE CASCADE,

    CONSTRAINT FK_MESSAGE_RECEIVER FOREIGN KEY (RECEIVER_NO)
        REFERENCES MEMBER(MEMBER_NO)
        ON DELETE CASCADE
);


CREATE SEQUENCE SEQ_MESSAGE_NO NOCACHE;



COMMENT ON TABLE MESSAGE IS '쪽지 테이블';
COMMENT ON COLUMN MESSAGE.MESSAGE_NO IS '쪽지 번호(PK)';
COMMENT ON COLUMN MESSAGE.SENDER_NO IS '보낸 사람 회원 번호';
COMMENT ON COLUMN MESSAGE.RECEIVER_NO IS '받는 사람 회원 번호';
COMMENT ON COLUMN MESSAGE.CONTENT IS '쪽지 내용';
COMMENT ON COLUMN MESSAGE.SEND_DATE IS '쪽지를 보낸 날짜';
COMMENT ON COLUMN MESSAGE.READ_FL IS '읽음 여부 (Y/N)';




ALTER TABLE MESSAGE ADD (
    MESSAGE_DEL_FL  CHAR(1) DEFAULT 'N' NOT NULL
);


ALTER TABLE MESSAGE ADD (
    BOARD_NO        NUMBER     DEFAULT 1         NOT NULL
);


ALTER TABLE MESSAGE ADD CONSTRAINT FK_MESSAGE_BOARD FOREIGN KEY (BOARD_NO)
    REFERENCES BOARD(BOARD_NO);

-- 새로 추가된 컬럼에 대한 주석을 추가합니다.
COMMENT ON COLUMN MESSAGE.BOARD_NO IS '관련 게시글 번호(FK)'; -- 외래 키임을 명시


SELECT * FROM MESSAGE ORDER BY MESSAGE_NO DESC;

DELETE FROM MESSAGE;


COMMIT;

SELECT *
FROM MESSAGE -- 실제 쪽지 테이블 이름 사용 (MESSAGE 또는 MESSAGES 등)
WHERE (SENDER_NO =1 OR RECEIVER_NO = 2)
  AND MESSAGE_DEL_FL = 'N';