package ex01_lombok;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ex03_User {
	private String id;
	private String pw;
	
//	public Ex03_User(Builder builder) {
//		this.id = builder.id;
//		this.pw = builder.pw;
//	}
//	public static Builder builder() {
//		return new Builder();
//	}
//	@Override
//	public String toString() {
//		return "Ex02_User [id=" + id + ", pw=" + pw + "]";
//	}
//	public static class Builder {
//		private String id;
//		private String pw;
//		public Builder id(String id) {
//			this.id = id;
//			return this;
//		}
//		public Builder pw(String pw) {
//			this.pw = pw;
//			return this;
//		}
//		public Ex03_User build() {
//			return new Ex03_User(this);
//		}
//		
//	}
	
}
