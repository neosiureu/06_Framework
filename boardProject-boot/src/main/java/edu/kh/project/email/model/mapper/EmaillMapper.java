package edu.kh.project.email.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmaillMapper {

	/** 기존 이메일에대한 인증키 수정
	 * @param map (authKey, email)
	 * @return
	 */
	int updateAuthKey(Map<String, String> map);

	/** 이메일과 인증번호 새로 삽입
	 * @param map(authKey, email)
	 * @return
	 */
	int insertAuthKey(Map<String, String> map);

}
