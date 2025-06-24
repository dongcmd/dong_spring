package ex01_hash;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Main03 {
	public static void main(String[] args) throws Exception {
		Class.forName("org.mariadb.jdbc.Driver");
		Connection conn = DriverManager.getConnection(
				"jdbc:mariadb://localhost:3306/gdjdb", "gduser", "1234");
		PreparedStatement pstmt = conn.prepareStatement(
				"select password, username from usercipher where userid=?");
		Scanner sc = new Scanner(System.in);
		System.out.println("아디 입력");
		String id = sc.nextLine();
		System.out.println("비번 입력");
		String pw = sc.nextLine();
		pstmt.setString(1, id);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String hashpass = "";
			byte[] plain = pw.getBytes();
			byte[] hash = md.digest(plain);
			for(byte b : hash) hashpass += String.format("%02X", b);
			
			System.out.println(rs.getString("password"));
			System.out.println(hashpass);
			
			if(rs.getString("password").equals(hashpass)) {
				System.out.println("반갑습니다" + rs.getString("username"));
			} else {
				System.out.println("비번 오류");
			}
		} else {
			System.out.println("아디 없");
		}
	}
}
