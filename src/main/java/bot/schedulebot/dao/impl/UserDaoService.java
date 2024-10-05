package bot.schedulebot.dao.impl;

import bot.schedulebot.dao.UserDao;
import bot.schedulebot.domain.dto.user.User;
import bot.schedulebot.domain.dto.user.UserRowMapper;
import bot.schedulebot.domain.exception.UserNotFoundException;
import bot.schedulebot.service.bot.MessageService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional
public class UserDaoService extends JdbcDaoSupport implements UserDao {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
        log.info("DataSource has been set.");
    }

    public void insertNewUser(User user) {
        log.info("Inserting new user: {}", user);
        jdbcTemplate.update("INSERT INTO users(user_name, selected_file, telegram_id) VALUES (?, ?, ?) ON CONFLICT (telegram_id) DO NOTHING;",
                user.getUsername(), user.getSelectedFile(), user.getTelegramId());
        log.info("User inserted: {}", user.getUsername());
    }

    public User findByUserId(long userId) {
        log.info("Finding user by telegram_id: {}", userId);
        try {
            User user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE telegram_id = ?;", new UserRowMapper(), userId);
            log.info("User found: {}", user);
            return user;
        } catch (EmptyResultDataAccessException ex) {
            log.error("User not found for telegram_id: {}", userId);
            throw new UserNotFoundException();
        }
    }

    public void setSelectedFile(long userId, String selectedFile) {
        log.info("Updating selected_file for userId: {}, new selected_file: {}", userId, selectedFile);
        jdbcTemplate.update("UPDATE users SET selected_file = ? WHERE telegram_id = ?", selectedFile, userId);
        log.info("Updated selected_file for userId: {}", userId);
    }

    public void setGroupId(long userId, Integer groupId) {
        log.info("Updating group_id for userId: {}, new group_id: {}", userId, groupId);
        jdbcTemplate.update("UPDATE users SET group_id = ? WHERE telegram_id = ?", groupId, userId);
        log.info("Updated group_id for userId: {}", userId);
    }

    public List<User> getAllUsers() {
        log.info("Retrieving all users.");
        List<User> users = jdbcTemplate.query("SELECT * FROM users;", new UserRowMapper());
        log.info("Number of users retrieved: {}", users.size());
        return users;
    }

    public int getCountUsers() {
        log.info("Counting total number of users.");
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        log.info("Total number of users: {}", count);
        return count;
    }
}
