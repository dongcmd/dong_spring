package dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

	@Update("update useraccount set username=#{username}, phoneno=#{phoneno},"
	+ " postcode=#{postcode}, address=#{address}, email=#{email},"
	+ " birthday=#{birthday} where userid=#{userid}")
	void update(User user);

	@Delete("delete from useraccount where userid=#{value}")
	void delete(String userid);

	@Update("update useraccount set password=#{chgpass} where userid=#{userid}")
	void chgpass(Map<String, Object> param);

	@Select({"<script>",
	"select ${col} from useraccount ",
	"where email=#{email} and phoneno=#{phoneno} ",
	"<if test='userid != null'> and userid=#{userid}</if>",
	"</script>"})
	String search(Map<String, Object> param);

	@Update("update useraccount set password=#{newPw} where userid=#{userid}")
	Integer resetPw(Map<String, Object> param);

	@Select("select * from useraccount")
	List<User> list();


}
