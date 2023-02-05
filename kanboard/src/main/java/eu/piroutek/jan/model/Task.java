package eu.piroutek.jan.model;

import java.util.Objects;

/**
 * model of task
 */
public class Task {
    private int id;
    private String name;
    private String description;

    private int projectId;
    private int tagId;

    public Task() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTag() {
        return tagId;
    }

    public void setTag(int tag) {
        this.tagId = tag;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, tagId, projectId);
    }
}
