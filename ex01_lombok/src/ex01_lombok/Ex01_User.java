package ex01_lombok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Setter
//@Getter
//@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ex01_User {
	private String id;
	private String pw;
}
