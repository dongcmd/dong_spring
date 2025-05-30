package ex01_lombok;

public class Ex02_Main {
	public static void main(String[] args) {
		Ex02_User user = Ex02_User.builder()
				.id("hong")
				.build();
		System.out.println(user);
	}
}
