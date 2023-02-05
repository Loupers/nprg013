package eu.piroutek.jan.database;

import eu.piroutek.jan.exception.NotFoundException;
import eu.piroutek.jan.model.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDbHandler extends DbHandlerBase{

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS tasks (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "project_id INT NOT NULL," +
            "description TEXT," +
            "tag INT," +
            "name CHAR(50) NOT NULL)";

    private static final String GET_ALL_TASKS_FOR_PROJECT = "SELECT * FROM tasks WHERE project_id = ?";
    private static final String GET_TASK =  "SELECT * FROM tasks WHERE id = ?";
    private static final String CREATE_TASK = "INSERT INTO tasks (project_id, name, description, tag) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_TASK = "UPDATE tasks SET project_id = ?, name = ?, description = ?, tag = ? WHERE id = ?";
    private static final String DELETE_TASK = "DELETE FROM tasks WHERE id = ?";

    public TaskDbHandler() throws SQLException, ClassNotFoundException {
        super();
        this.createTable();
    }

    /**
     * creates table for tasks, if it doesn't exist yet
     * @throws SQLException
     */
    private void createTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE);) {
            statement.execute();
        } catch (SQLException exception) {
            LOGGER.error("cannot create table for projects", exception);
            throw exception;
        }
    }

    /**
     * @return list of all tasks stored in database
     * @throws SQLException
     */
    public List<Task> getAllTasks(int projectId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(GET_ALL_TASKS_FOR_PROJECT)) {
            statement.setInt(1, projectId);
            List<Task> tasks = new ArrayList<>();
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    tasks.add(parseSetToTask(set));
                }
            } catch (Exception e) {
                throw e;
            }
            return tasks;
        } catch (SQLException exception) {
            LOGGER.error("couldn't fetch all tasks", exception);
            throw exception;
        }
    }

    /**
     *
     * @param taskId- id of task to get
     * @return task from database
     * @throws NotFoundException if no such task is found
     * @throws SQLException
     */
    public Task getTask(int taskId) throws SQLException, NotFoundException {
        try (PreparedStatement statement = connection.prepareStatement(GET_TASK)) {
            statement.setInt(1, taskId);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return parseSetToTask(set);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException exception) {
            LOGGER.error("task not found", exception);
            throw exception;
        } catch (SQLException exception) {
            LOGGER.error("couldn't get data of task", exception);
            throw exception;
        }
    }

    private Task parseSetToTask(ResultSet set) throws SQLException {
        Task task = new Task();
        task.setId(set.getInt("id"));
        task.setName(set.getString("name"));
        task.setDescription(set.getString("description"));
        task.setProjectId(set.getInt("project_id"));
        task.setTag(set.getInt("tag"));
        return task;
    }

    /**
     * insert new task into database
     *
     * @param task to insert
     * @return id of newly inserted task or -1 if inserting didn't work
     * @throws SQLException
     */
    public int insertTask(Task task) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_TASK)) {
            statement.setInt(1, task.getProjectId());
            statement.setString(2, task.getName());
            statement.setString(3, task.getDescription());
            statement.setInt(4, task.getTag());
            int rows = statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (rows == 1 && keys.next()) {
                return keys.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException exception) {
            LOGGER.error("cannot insert new task", exception);
            throw exception;
        } catch (Exception e) {
            LOGGER.error("general exception", e);
            throw e;
        }
    }

    /**
     * updates already existing task
     *
     * @param task to update
     * @return true if task was successfully updated
     * @throws SQLException
     */
    public boolean updateTask(Task task) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TASK)) {
            statement.setInt(1, task.getProjectId());
            statement.setString(2, task.getName());
            statement.setString(3, task.getDescription());
            statement.setInt(4, task.getTag());
            statement.setInt(5, task.getId());
            int rows = statement.executeUpdate();
            if (rows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException exception) {
            LOGGER.error("cannot update task", exception);
            throw exception;
        }
    }

    /**
     * deletes task from database
     *
     * @param taskId to be deleted
     * @return true if successfully deleted
     * @throws SQLException
     */
    public boolean deleteTask(int taskId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_TASK)) {
            statement.setInt(1, taskId);
            int rows = statement.executeUpdate();
            if (rows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException exception) {
            LOGGER.error("cannot delete task", exception);
            throw exception;
        }
    }
}
