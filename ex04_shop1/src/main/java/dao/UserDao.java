package dao;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dao.mapper.UserMapper;
import logic.User;

@Repository
public class UserDao {
	@Autowired
	private SqlSessionTemplate template;
	private Map<String, Object> param = new HashMap<>();
	private final Class<UserMapper> cls = UserMapper.class;
	
	public void insert(User user) {
		template.getMapper(cls).insert(user);
	}

	public User select(String userid) {
		param.clear();
		param.put("userid", userid);
		return template.selectOne("dao.mapper.UserMapper.select", param);
	}

	public void update(User user) {
		template.getMapper(cls).update(user);
	}

	public void delete(String userid) {
		template.getMapper(cls).delete(userid);
	}

	public void chgpass(String userid, String chgpass) {
		param.clear();
		param.put("userid", userid);
		param.put("chgpass", chgpass);
		template.getMapper(cls).chgpass(param);
	}

	public String search(User user) {
		String col = "userid";
		if(user.getUserid() != null) col = "password";
		param.clear();
		param.put("col", col);
		param.put("userid", user.getUserid());
		param.put("email", user.getEmail());
		param.put("phoneno", user.getPhoneno());
		return template.getMapper(cls).search(param);
	}

	public String resetPw(User user) {
		final String CHAR_POOL = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    final SecureRandom random = new SecureRandom();
	    StringBuilder newPw = new StringBuilder();
	    for(int i = 0; i < 6; i++) {
	    	int idx = random.nextInt(CHAR_POOL.length());
	    	newPw.append(CHAR_POOL.charAt(idx));
	    }
		param.clear();
		param.put("userid", user.getUserid());
		param.put("newPw", newPw.toString());
		if(template.getMapper(cls).resetPw(param) == 1) {
			return newPw.toString();
		}
		return null;
	}

	public List<User> list() {
		return template.getMapper(cls).list();
	}
}
