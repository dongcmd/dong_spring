package ex01_hash;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
 *  1. unsercipher 테이블 생성. useraccount와 같은 테이블로 생성
 *  	create table usercipher select * from useraccount
 *  2. usercipher의 password 컬럼의 크기를 300
 *  	alter table usercipher modify column password varchar(300)
 *  3. userid 컬럼을 기본키로 설정
 *  	ALTER TABLE usercipher ADD CONSTRAINT PRIMARY KEY (userid)
 *  
 *  useraccount 테이블 읽어서 usercipher 테이블의 password를
 *  sha-256로 저장
 *  
 */
public class Main02 {
	public static void main(String[] args) throws Exception {
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:mariadb://localhost:3306/gdjdb", "gduser", "1234");
		PreparedStatement pstmt = conn.prepareStatement(
				"select userid, password from useraccount");
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			String id = rs.getString("userid");
			String pass = rs.getString("password");
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String hashpass = "";
			byte[] plain = pass.getBytes();
			byte[] hash = md.digest(plain);
			for(byte b : hash) hashpass += String.format("%02X", b);
			pstmt.close();
			pstmt = conn.prepareStatement(
					"update usercipher set password=? where userid=?");
			pstmt.setString(1, hashpass);
			pstmt.setString(2, id);
			pstmt.executeUpdate();
		}
	}

}
