package kr.gdu.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.gdu.logic.Board;

@Mapper
public interface BoardMapper {
    String select = "select num,writer,pass,title,content,file1 fileurl,"
		+ " regdate, readcnt, grp, grplevel, grpstep, boardid from board";
    @Select({"<script>",
   	"select count(*) from board where boardid=#{boardid} ",
    "<if test='searchtype != null'> "
    + " and ${searchtype} like '%${searchcontent}%'</if>",
   	"</script>"})
	int count(Map<String, Object> param);
    
    @Select({"<script>",
    	select,
   	"<if test='num != null'> where num = #{num}</if>",
   	"<if test='boardid != null'> where boardid = #{boardid} </if>",
   	"<if test='searchtype != null'> "
   	+ " and ${searchtype} like '%${searchcontent}%'</if>",
   	"<if test='limit != null'> "
   	+ " order by grp desc, grpstep asc limit #{startrow}, #{limit}</if>",
   	"</script>"})    
	List<Board> select(Map<String, Object> param);

    @Select(select + " where num = #{value}")
	Board selectOne(int num);

    @Update("update board set readcnt = readcnt + 1 where num = #{value}")
	void addReadCnt(int num);
    
    @Select("select ifnull(max(num), 0) from board")
    int maxNum();

    @Insert("insert into board (num, writer, pass, title, content, file1, boardid, regdate, readcnt, grp, grplevel, grpstep)"
+ "values (#{num}, #{writer}, #{pass}, #{title}, #{content}, #{fileurl}, #{boardid}, now(), 0, #{grp}, #{grplevel}, #{grpstep})")
	void insert(Board board);
    
    @Update("update board set writer=#{writer}, title=#{title},"
+ " content=#{content}, file1=#{fileurl} where num=#{num}")
	void update(Board board);

    @Delete("delete from board where num=#{value}")
	void delete(int num);

    @Update("update board set grpstep=grpstep + 1"
    		+ " where grp=#{grp} and grpstep>#{grpstep}")
	void grpStepAdd(Map<String, Object> param);
}
