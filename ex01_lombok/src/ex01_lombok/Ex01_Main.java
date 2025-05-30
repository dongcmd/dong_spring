package ex01_lombok;

public class Ex01_Main {
	public static void main(String[] args) {
		Ex01_User user = new Ex01_User();
		user.setId("hong");
		user.setPw("1234");
		System.out.println(user);
		Ex01_User user2 = new Ex01_User("kim", "5678");
		System.out.println(user2);
	}
}
