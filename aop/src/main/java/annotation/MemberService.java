package annotation;

import org.springframework.stereotype.Component;

@Component
public class MemberService {
	public void regist(Member member) {
		System.out.println("MemberService.regist(Member) method call");
	}
	public boolean update(String memberid, UpdateInfo info) {
		System.out.println("MemberService.update(String, UpdateInfo) method call");
		return true;
	}
	public boolean delete(String memberid, String name, UpdateInfo info) {
		System.out.println("MemberService.delete(String, String, UpdateInfo) method call");
		return false;
	}
}
