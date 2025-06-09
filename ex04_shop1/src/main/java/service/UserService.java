package service;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.UserDao;
import logic.User;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	public void userInsert(User user) {
		userDao.insert(user);
	}

	public User selectUser(String userid) {
		return userDao.select(userid);
	}

	public void userUpdate(User user) {
		userDao.update(user);
	}

	public void userDelete(String userid) {
		userDao.delete(userid);
	}

	public void userChgpass(String userid, String chgpass) {
		userDao.chgpass(userid, chgpass);
	}

	public String getSearch(User user) {
		return userDao.search(user);
	}

	public String resetPw(User user) {
		return userDao.resetPw(user);
	}
}
