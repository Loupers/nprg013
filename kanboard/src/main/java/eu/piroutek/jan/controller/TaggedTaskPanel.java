package eu.piroutek.jan.controller;

import eu.piroutek.jan.database.TaskDbHandler;
import eu.piroutek.jan.model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * list of tasks for project with same tag
 */
public class TaggedTaskPanel extends JPanel {
    private Tag tag;
    private List<Task> listOfTasks;
    private Project project;
    private FrameController parent;
    private TaskScreen layout;

    private JPanel mainPanel;
    private JPanel tasksPanel;
    private JLabel tagNameLbl;

    private TaskDbHandler db;

    /**
     *
     * @param tasks list of task for this box
     * @param tag tag of tasks
     * @param p project to which tasks belong
     * @param parent frame
     * @param layout panel in which is this displayed
     */
    public TaggedTaskPanel(List<Task> tasks, Tag tag, Project p, FrameController parent, TaskScreen layout) {

        this.listOfTasks = tasks;
        this.tag = tag;
        this.parent = parent;
        this.layout = layout;
        this.project = p;

        add(mainPanel);
        if (tag == null) {
            tagNameLbl.setText("Undefined");
            mainPanel.setBackground(new Color(255, 255, 255));
            tasksPanel.setBackground(new Color(255, 255, 255));
        } else {
            tagNameLbl.setText(tag.getName());
            mainPanel.setBackground(new Color(tag.getRed(), tag.getGreen(), tag.getBlue()));
            tasksPanel.setBackground(new Color(tag.getRed(), tag.getGreen(), tag.getBlue()));
            if (tag.sumOfColors() < 255) {
                mainPanel.setForeground(new Color(255, 255, 255));
            }
        }

        try {
            db = new TaskDbHandler();
        } catch (Exception e) {
            ErrorDialogUtils.showUnRecoverableErrorDialog(this);
        }

        createRows();
        setVisible(true);
    }

    /**
     * lists all tasks as rows
     */
    private void createRows() {
        tasksPanel.removeAll();
        tasksPanel.setLayout(new GridLayout(0, 1));

        for (Task t : listOfTasks) {
            tasksPanel.add(new TaskRow(t, project, parent, this));
        }
        tasksPanel.setVisible(true);
        tasksPanel.setAlignmentX(Component.TOP_ALIGNMENT);
        JPanel wrapper = new JPanel();
        wrapper.add(tasksPanel);

        mainPanel.add(new JScrollPane(wrapper));
        mainPanel.setVisible(true);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * delete task from database
     *
     * @param t task to be deleted
     */
    public void deleteTask(Task t) {
        try {
            if (db.deleteTask(t.getId())) {
                mainPanel.remove(tasksPanel);
                listOfTasks.remove(t);
                createRows();
            } else {
                ErrorDialogUtils.showRecoverableErrorDialog("Task couldn't be deleted", this);
            }
        } catch (Exception e) {
            ErrorDialogUtils.showUnRecoverableErrorDialog(this);
        }
    }

}
