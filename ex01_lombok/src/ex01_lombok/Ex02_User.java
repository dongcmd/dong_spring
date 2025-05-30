package ex01_lombok;

//import lombok.Builder;
//import lombok.Data;

//@Builder
//@Data
public class Ex02_User {
	private String id;
	private String pw;
	
	public Ex02_User(Builder builder) {
		this.id = builder.id;
		this.pw = builder.pw;
	}
	public static Builder builder() {
		return new Builder();
	}
	@Override
	public String toString() {
		return "Ex02_User [id=" + id + ", pw=" + pw + "]";
	}
	public static class Builder {
		private String id;
		private String pw;
		public Builder id(String id) {
			this.id = id;
			return this;
		}
		public Builder pw(String pw) {
			this.pw = pw;
			return this;
		}
		public Ex02_User build() {
			return new Ex02_User(this);
		}
		
	}
	
}
