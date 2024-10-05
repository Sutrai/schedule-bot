package bot.schedulebot.dao;

import bot.schedulebot.domain.dto.user.User;
import bot.schedulebot.domain.exception.UserNotFoundException;

import java.util.List;

public interface UserDao {

    void insertNewUser(User user);
    User findByUserId(long userId) throws UserNotFoundException;
    void setSelectedFile(long userId, String selectedFile);
    void setGroupId(long userId, Integer groupId);
    List<User> getAllUsers();
    int getCountUsers();
}
