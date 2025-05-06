package edu.kh.project.email.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface EmailMapper {

	/** 처음에 기존 이메일은 그냥 두고 인증키 부분만 수정한다
	 * @param map (authKey,email)
	 * @return
	 */
	int updateAuthKey(Map<String, String> map);

	/** 이메일이 없었다면 이메일과 인증키를 모두 새로 삽입한다.
	 * @param map (authKey,email)
	 * @return 
	 */
	int insertAuthKey(Map<String, String> map);

	
	
	/** 입력받은 이메일과 인증번호가 DB가 있는 지 조회
	 * @param map (email, authKey)
	 * @return
	 */
	int checkAuthKey(Map<String, String> map);
	

}
