package ex02_aes;

/*
 * AES 암호 : 대칭키 암호화 알고리즘 (암호화, 복호화에 사용되는 키 동일)
 */
public class Main01 {
	public static void main(String[] args) {
		String plain1 = "ㅎㅇ 홍길동";
		String cipher1 = CipherUtil.encrypt(plain1); // 암호화
		System.out.println("암호문 : " + cipher1);
		
		String plain2 = CipherUtil.decrypt(cipher1); // 복호화
		System.out.println("복호문 : " + plain2);
	}
}
