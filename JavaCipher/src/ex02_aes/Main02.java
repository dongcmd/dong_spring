package ex02_aes;

public class Main02 {
	public static void main(String[] args) {
		String plain1 = "ㅎㅇ 난 홍길";
		String key = "abc1234567";
		String cipher1 = CipherUtil.encrypt(plain1, key);
		System.out.println("암호문 : " + cipher1);
		
		key = "abc1234567";
		String plain2 = CipherUtil.decrypt(cipher1, key);
		System.out.println("암호문 : " + plain2);
	}

}
