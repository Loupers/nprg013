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
import java.util.List;

/**
 * screen for editing task
 */
public class TaskEdit extends JPanel {
    private JPanel mainPanel;
    private JPanel taskHeaderPanel;
    private JPanel taskParamsPanel;
    private JLabel editTaskLbl;
    private JButton BackBtn;
    private JButton SaveBtn;
    private JTextField nameInput;
    private JLabel nameLbl;
    private JComboBox<Tag> tagInput;
    private JLabel tagLbl;
    private JTextArea descriptionInput;
    private JLabel descriptionLbl;

    private Task task;
    private Project project;
    private FrameController parent;
    private TaskDbHandler dbTask;
    private TagDbHandler dbTag;
    private List<Tag> listOfTags;

    /**
     *
     * @param t task to be edited
     * @param p project ot which task belongs
     * @param parent frame
     */
    public TaskEdit(Task t, Project p, FrameController parent) {
        this.task = t;
        this.project = p;
        this.parent = parent;

        setLayout(new BorderLayout());
        add(mainPanel);
        nameInput.setText(task.getName());
        if (task.getDescription() != null) {
            descriptionInput.setText(task.getDescription());
        } else {
            descriptionInput.setText("");
        }

        try {
            dbTag = new TagDbHandler();
            dbTask = new TaskDbHandler();
            listOfTags = dbTag.getAllTags(project.getId());
            loadCombo();
        } catch (Exception e) {
            ErrorDialogUtils.showUnRecoverableErrorDialog(this);
        }

        setButtons();
        setVisible(true);
    }

    /**
     * loads tags for project and displays them as select options including undefined tag
     */
    private void loadCombo() {
        Tag empty = new Tag();
        empty.setName("Undefined");
        empty.setId(0);
        tagInput.addItem(empty);
        tagInput.setSelectedItem(empty);
        for (Tag t : listOfTags) {
            if (task.getTag() == t.getId()) {
                tagInput.setSelectedItem(task);
            }
            tagInput.addItem(t);
        }
    }

    /**
     * set button actions
     */
    private void setButtons() {
        BackBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               parent.goToTasks(project);
            }
        });

        SaveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    task.setName(nameInput.getText());
                    task.setDescription(descriptionInput.getText());
                    task.setTag(((Tag) tagInput.getSelectedItem()).getId());
                    if (dbTask.updateTask(task)) {
                        parent.goToTasks(project);
                    } else {
                        //TODO error
                    }
                } catch (Exception exception) {

                }
            }
        });
    }
}
