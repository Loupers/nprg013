package eu.piroutek.jan.controller;

import eu.piroutek.jan.model.Project;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * single row with project data
 */
public class ProjectRow extends JPanel{
    private JPanel row;
    private JButton editProjectBtn;
    private JLabel projectNameLbl;
    private JButton deleteProjectBtn;
    private JButton gotoTasksBtn;
    private JButton gotoTagBtn;
    private Project project;


    private FrameController parent;
    private HomeScreen layout;

    /**
     *
     * @param p project for this row
     * @param parent frame
     * @param layout panel in which row lays
     */
    public ProjectRow(Project p, FrameController parent, HomeScreen layout) {
        this.project = p;
        this.layout = layout;

        add(row);
        projectNameLbl.setText(p.getName());
        setButtons();
        setVisible(true);
        this.parent = parent;
    }

    /**
     * sets button actions in the row
     */
    private void setButtons() {
        gotoTasksBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.goToTasks(project);
            }
        });

        deleteProjectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layout.deleteProject(project);
            }
        });

        gotoTagBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.goToTags(project);
            }
        });

        editProjectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showEditDialog();
            }
        });
    }

    /**
     *  show dialog for editing project
     */
    private void showEditDialog() {
        String name = (String) JOptionPane.showInputDialog(this, "Set project name", "kanboard", JOptionPane.PLAIN_MESSAGE, null, null, project.getName());
        if (name != null && name.length() >= 1 && name.length() <= 50) {
            project.setName(name);
            layout.updateProject(project);
        } else if (name != null){
            ErrorDialogUtils.showRecoverableErrorDialog("Project name has to have min 1 and maximal 50 characters ", this);
        }
    }
}