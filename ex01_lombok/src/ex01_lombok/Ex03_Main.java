package ex01_lombok;

public class Ex03_Main {
	public static void main(String[] args) {
		Ex03_User user = Ex03_User.builder()
				.id("hong")
				.build();
		System.out.println(user);
		System.out.println(user.getId());
	}
}
