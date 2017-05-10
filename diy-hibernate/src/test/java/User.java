import net.liuchenfei.diyhibernate.Table;

/**
 * Created by liuchenfei on 2017/5/9.
 */
@Table(tableName = "user")
public class User {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Table(columnName = "username")
    private String username;

    @Table(columnName = "userid",isId = true)
    private String userid;
}
