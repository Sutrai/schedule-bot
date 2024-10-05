package bot.schedulebot.domain.dto.user;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .username(rs.getString("user_name"))
                .selectedFile(rs.getString("selected_file"))
                .telegramId(rs.getLong("telegram_id"))
                .groupId(rs.getInt("group_id"))
                .build();
    }
}
