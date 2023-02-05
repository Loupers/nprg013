package eu.piroutek.jan.controller;

import javax.swing.*;
import eu.piroutek.jan.model.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * displaying data about task in a row
 */
public class TaskRow extends JPanel {
    private JPanel row;
    private JLabel taskNameLbl;
    private JButton deleteBtn;
    private JButton editBtn;

    private Task task;
    private Project project;
    private FrameController parent;
    private TaggedTaskPanel layout;

    /**
     *
     * @param t task for this row
     * @param p project of that task
     * @param parent frame
     * @param layout panel in which is row displayed
     */
    public TaskRow(Task t, Project p, FrameController parent, TaggedTaskPanel layout) {
        this.task = t;
        this.project = p;
        this.parent = parent;
        this.layout = layout;

        add(row);
        taskNameLbl.setText(task.getName());
        this.setButtons();
        setVisible(true);
    }

    /**
     * set button actions
     */
    private void setButtons() {
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.goToTaskEdit(task, project);
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layout.deleteTask(task);
            }
        });
    }

}
