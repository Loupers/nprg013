package eu.piroutek.jan.database;

import eu.piroutek.jan.model.Project;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import eu.piroutek.jan.exception.NotFoundException;

/**
 * bridge between application and database handling requests for projects;
 */
public class ProjectDbHandler extends DbHandlerBase {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS projects (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name CHAR(50) NOT NULL)";

    private static final String GET_ALL_PROJECTS = "SELECT * FROM projects";
    private static final String GET_PROJECT =  "SELECT * FROM projects WHERE id = ?";
    private static final String CREATE_PROJECT = "INSERT INTO projects (name) VALUES (?)";
    private static final String UPDATE_PROJECT = "UPDATE projects SET name = ? WHERE id = ?";
    private static final String DELETE_PROJECT = "DELETE FROM projects WHERE id = ?";
    private static final String DELETE_TASKS_ASOC = "DELETE FROM tasks WHERE project_id = ?";
    private static final String DELETE_TAGS_ASOC = "DELETE FROM tags WHERE project_id = ?";

    public ProjectDbHandler() throws SQLException, ClassNotFoundException {
        super();
        createTable();
    }

    /**
     * creates table for projects, if it doesn't exist yet
     * @throws SQLException
     */
    private void createTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE);) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.error("cannot create table for projects", exception);
            throw exception;
        }
    }

    /**
     * @return list of all projects stored in database
     * @throws SQLException
     */
    public List<Project> getAllProjects() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(GET_ALL_PROJECTS)) {
            List<Project> projects = new ArrayList<>();
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    projects.add(parseSetToProject(set));
                }
            } catch (Exception e) {
                throw e;
            }
            return projects;
        } catch (SQLException exception) {
            LOGGER.error("couldn't fetch all projects", exception);
            throw exception;
        }
    }

    /**
     *
     * @param projectId - id of project to get
     * @return project in database
     * @throws NotFoundException if no project is found
     * @throws SQLException
     */
    public Project getProject(int projectId) throws SQLException, NotFoundException {
        try (PreparedStatement statement = connection.prepareStatement(GET_PROJECT)) {
            statement.setInt(1, projectId);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return parseSetToProject(set);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException exception) {
            LOGGER.error("project not found", exception);
            throw exception;
        } catch (SQLException exception) {
            LOGGER.error("couldn't get data of project", exception);
            throw exception;
        }
    }

    private Project parseSetToProject(ResultSet set) throws SQLException {
        Project project = new Project();
        project.setId(set.getInt("id"));
        project.setName(set.getString("name"));
        return project;
    }

    /**
     * insert new project into database
     *
     * @param project to insert
     * @return true if successfully inserted
     * @throws SQLException
     */
    public boolean insertProject(Project project) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_PROJECT)) {
            statement.setString(1, project.getName());
            int rows = statement.executeUpdate();
            if (rows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException exception) {
            LOGGER.error("cannot insert new project", exception);
            throw exception;
        }
    }

    /**
     * updates already existing project
     *
     * @param project to update
     * @return true if project was successfully updated
     * @throws SQLException
     */
    public boolean updateProject(Project project) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PROJECT)) {
            statement.setString(1, project.getName());
            statement.setInt(2, project.getId());
            int rows = statement.executeUpdate();
            if (rows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException exception) {
            LOGGER.error("cannot update project", exception);
            throw exception;
        }
    }

    /**
     * deletes project from database
     *
     * @param projectID to be deleted
     * @return true if successfully deleted
     * @throws SQLException
     */
    public boolean deleteProject(int projectID) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_PROJECT); PreparedStatement deleteTasks = connection.prepareStatement(DELETE_TASKS_ASOC); PreparedStatement deleteTags = connection.prepareStatement(DELETE_TAGS_ASOC)) {
            statement.setInt(1, projectID);
            int rows = statement.executeUpdate();
            if (rows == 1) {
                deleteTasks.setInt(1, projectID);
                deleteTasks.executeUpdate();
                deleteTags.setInt(1, projectID);
                deleteTags.executeUpdate();
               return true;
            } else {
                return false;
            }
        } catch (SQLException exception) {
            LOGGER.error("cannot delete project", exception);
            throw exception;
        }
    }
}
