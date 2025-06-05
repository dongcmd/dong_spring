package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import logic.User;

public interface UserMapper {

	@Insert("insert into useraccount(userid, password, username, phoneno, postcode, address, email, birthday)"
			+ " values(#{userid}, #{password}, #{username}, #{phoneno}, #{postcode}, #{address}, #{email}, #{birthday})")
	void insert(User user);

	@Select({"<script>",
		"select * from useraccount "
		+ "<if test='userid != null'>where userid = #{userid}</if>"
		,"</script>"})
	List<User> select(Map<String, Object> param);

}
