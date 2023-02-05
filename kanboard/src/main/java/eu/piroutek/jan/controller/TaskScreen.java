package eu.piroutek.jan.controller;

import eu.piroutek.jan.database.TagDbHandler;
import eu.piroutek.jan.database.TaskDbHandler;
import eu.piroutek.jan.model.Project;
import eu.piroutek.jan.model.Tag;
import eu.piroutek.jan.model.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * show all tasks for project in boxes by their tags
 */
public class TaskScreen extends JPanel {
    private Project project;
    private List<Task> taskList;
    private List<Tag> projectTags;
    private TaskDbHandler dbTask;
    private TagDbHandler dbTag;
    private FrameController parent;
    private JPanel mainPanel;
    private JButton createBtn;
    private JButton backBtn;
    private JLabel screenLbl;
    private JPanel dataPanel;
    private JPanel headPanel;

    /**
     *
     * @param p project to which tasks belong
     * @param parent frame
     */
    public TaskScreen(Project p, FrameController parent) {
        this.project = p;
        this.parent = parent;


        try {
            dbTask = new TaskDbHandler();
            dbTag = new TagDbHandler();
            taskList = dbTask.getAllTasks(project.getId());
            projectTags = dbTag.getAllTags(project.getId());
            generateTagPanels();
        } catch (Exception e) {
            ErrorDialogUtils.showUnRecoverableErrorDialog(this);
        }
        setLayout(new BorderLayout());
        add(mainPanel);
        screenLbl.setText("Tasks for project " + project.getName());
        setButtons();
        setVisible(true);
    }

    /**
     * generates all panel which tasks sorted by tag
     */
    private void generateTagPanels() {
        dataPanel.removeAll();
        dataPanel.setLayout(new GridLayout(1 ,0));

        dataPanel.add(new TaggedTaskPanel(
           this.filterTasks(0),
                null,
                project,
                parent,
                this
        ));

        for (Tag tag : projectTags) {
            dataPanel.add(new TaggedTaskPanel(
                    this.filterTasks(tag.getId()),
                    tag,
                    project,
                    parent,
                    this
            ));
        }

        dataPanel.setVisible(true);
        mainPanel.add(new JScrollPane(dataPanel));
        mainPanel.setVisible(true);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * this method is substitution for stream.filter.collect
     * because Intelij UI doesn't work on java8 with lambdas
     *
     * @param tagId by which should be filtered
     * @return list of tasks with same tag
     */
    private List<Task> filterTasks(Integer tagId) {
        List<Task> filtered = new ArrayList<>();
        for (Task t : taskList) {
            if (t.getTag() == tagId) {
                filtered.add(t);
            }
        }
        return filtered;
    }

    /**
     * set action of buttons
     */
    private void setButtons() {
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.goToProjects();
            }
        });

        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialog();
            }
        });
    }

    /**
     * show dialog for creating new task
     */
    private void showDialog() {
        String name = (String) JOptionPane.showInputDialog(this, "Set tag name", "kanboard", JOptionPane.PLAIN_MESSAGE, null, null, "");
        if (name != null && name.length() >= 1 && name.length() <= 50) {
            Task t = new Task();
            t.setName(name);
            t.setProjectId(project.getId());
            try {
                int id = dbTask.insertTask(t);
                if (id != -1) {
                    Task newTask = dbTask.getTask(id);
                    parent.goToTaskEdit(newTask, project);
                } else {
                    ErrorDialogUtils.showRecoverableErrorDialog("task couldn't be created", this);
                }
            } catch (Exception e) {
                ErrorDialogUtils.showUnRecoverableErrorDialog(this);
            }
        } else if (name != null) {
            ErrorDialogUtils.showRecoverableErrorDialog("Task name has to have min 1 and maximal 50 characters ", this);
        }
    }
}