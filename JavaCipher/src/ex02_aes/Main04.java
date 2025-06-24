package ex02_aes;

/*
 * useraccount 테이블 eamil을 읽어 usercipher 테이블에 암호화 하여 저장
 * 1. usercipher 테이블의 eamil 컬럼 크기 1000으로 변경
 * 	ALTER TABLE usercipher MODIFY COLUMN email VARCHAR(1000)
 * 2. key는 userid의 해쉬값(SHA-256)의 앞16자리로 설정
 */

public class Main04 {
	public static void main(String[] args) {
		
	}

}
