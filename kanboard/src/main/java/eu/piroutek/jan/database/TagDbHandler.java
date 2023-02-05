package eu.piroutek.jan.database;

import eu.piroutek.jan.exception.NotFoundException;
import eu.piroutek.jan.model.Tag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagDbHandler extends DbHandlerBase{
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS tags (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "project_id INT NOT NULL," +
            "name CHAR(50) NOT NULL," +
            "red INT," +
            "green INT," +
            "blue INT)";

    private static final String GET_ALL_TAGS = "SELECT * FROM tags WHERE project_id = ?";
    private static final String GET_TAG =  "SELECT * FROM tags WHERE id = ?";
    private static final String CREATE_TAG = "INSERT INTO tags (name, project_id, red, green, blue) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_TAG = "UPDATE tags SET name = ?, project_id = ?, red = ?, green = ?, blue = ? WHERE id = ?";
    private static final String DELETE_TAG = "DELETE FROM tags WHERE id = ?";
    private static final String REVERT_TAG = "UPDATE tasks SET tag = 0 WHERE tag =?";

    public TagDbHandler() throws SQLException, ClassNotFoundException {
        super();
        createTable();
    }

    /**
     * creates table for tags, if it doesn't exist yet
     * @throws SQLException
     */
    private void createTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)) {
            statement.execute();
        } catch (SQLException exception) {
            LOGGER.error("cannot create table for tags", exception);
            throw exception;
        }
    }

    /**
     * @return list of all tasks stored in database
     * @throws SQLException
     */
    public List<Tag> getAllTags(int projectId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(GET_ALL_TAGS)) {
            statement.setInt(1, projectId);
            List<Tag> tags = new ArrayList<>();
            try (ResultSet set = statement.executeQuery()) {
                while (set.next()) {
                    tags.add(parseSetToTag(set));
                }
            } catch (Exception e) {
                throw e;
            }
            return tags;
        } catch (SQLException exception) {
            LOGGER.error("couldn't fetch all tags", exception);
            throw exception;
        }
    }

    /**
     *
     * @param tagId - id of tag to get
     * @return tag from database
     * @throws NotFoundException if no such tag is found
     * @throws SQLException
     */
    public Tag getTag(int tagId) throws SQLException, NotFoundException {
        try (PreparedStatement statement = connection.prepareStatement(GET_TAG)) {
            statement.setInt(1, tagId);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return parseSetToTag(set);
            } else {
                throw new NotFoundException();
            }
        } catch (NotFoundException exception) {
            LOGGER.error("tag not found", exception);
            throw exception;
        } catch (SQLException exception) {
            LOGGER.error("couldn't get data of tag", exception);
            throw exception;
        }
    }

    private Tag parseSetToTag(ResultSet set) throws SQLException {
        Tag tag = new Tag();
        tag.setId(set.getInt("id"));
        tag.setName(set.getString("name"));
        tag.setProjectId(set.getInt("project_id"));
        tag.setRed(set.getInt("red"));
        tag.setGreen(set.getInt("green"));
        tag.setBlue(set.getInt("blue"));
        return tag;
    }

    /**
     * insert new tag into database
     *
     * @param tag to insert
     * @return id of newly inserted tag or -1 if inserting didn't work
     * @throws SQLException
     */
    public int insertTag(Tag tag) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_TAG)) {
            statement.setString(1, tag.getName());
            statement.setInt(2, tag.getProjectId());
            statement.setInt(3, tag.getRed());
            statement.setInt(4, tag.getGreen());
            statement.setInt(5, tag.getBlue());
            int rows = statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (rows == 1 && keys.next()) {
                return keys.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException exception) {
            LOGGER.error("cannot insert new tag", exception);
            throw exception;
        }
    }

    /**
     * updates already existing tag
     *
     * @param tag to update
     * @return true if task was successfully updated
     * @throws SQLException
     */
    public boolean updateTag(Tag tag) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TAG)) {
            statement.setString(1, tag.getName());
            statement.setInt(2, tag.getProjectId());
            statement.setInt(3, tag.getRed());
            statement.setInt(4, tag.getGreen());
            statement.setInt(5, tag.getBlue());
            statement.setInt(6, tag.getId());
            int rows = statement.executeUpdate();
            if (rows == 1) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException exception) {
            LOGGER.error("cannot update tag", exception);
            throw exception;
        }
    }

    /**
     * deletes tag from database
     *
     * @param tagId to be deleted
     * @return true if successfully deleted
     * @throws SQLException
     */
    public boolean deleteTag(int tagId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_TAG); PreparedStatement update = connection.prepareStatement(REVERT_TAG)) {
            statement.setInt(1, tagId);
            int rows = statement.executeUpdate();
            if (rows == 1) {
                update.setInt(1, tagId);
                update.executeUpdate();
                return true;
            } else {
                return false;
            }
        } catch (SQLException exception) {
            LOGGER.error("cannot delete tag", exception);
            throw exception;
        }
    }
}
