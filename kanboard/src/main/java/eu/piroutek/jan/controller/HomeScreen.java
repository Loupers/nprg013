package eu.piroutek.jan.controller;

import eu.piroutek.jan.database.ProjectDbHandler;
import eu.piroutek.jan.model.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HomeScreen extends JPanel {
    private JPanel mainPanel;
    private JPanel projectsPanel;
    private JButton createProjectBtn;
    private JLabel projectLbl;
    private JPanel headPanel;
    private JToolBar toolbar;
    private JPanel wrapper;
    private JScrollPane paneScroll;

    private List<Project> projectList;
    private ProjectDbHandler db;

    private FrameController parent;

    /**
     *
     * @param parent frame
     */
    public HomeScreen(FrameController parent) {

        setLayout(new BorderLayout());
        wrapper = new JPanel();
        mainPanel.add(new JScrollPane(wrapper));

        add(mainPanel);
        this.parent = parent;

        try {
            db = new ProjectDbHandler();
            projectList = db.getAllProjects();
            createRows();
        } catch (Exception e) {
            ErrorDialogUtils.showUnRecoverableErrorDialog(this);
        }

        setButtons();
        setVisible(true);

    }

    /**
     * lists all projects
     */
    private void createRows() {
        projectsPanel.removeAll();
        projectsPanel.setLayout(new GridLayout(0, 1));
        for (Project p : projectList) {
            projectsPanel.add(new ProjectRow(p, parent, this));
        }
        projectsPanel.setVisible(true);

        wrapper.removeAll();
        wrapper.add(projectsPanel);
        wrapper.setVisible(true);
        wrapper.revalidate();
        wrapper.repaint();
    }

    /**
     * sets button actions
     */
    private void setButtons() {
        createProjectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialog();
            }
        });
    }

    /**
     * show dialog when creating new project
     */
    private void showDialog() {
        String name = (String) JOptionPane.showInputDialog(this, "Set project name", "kanboard", JOptionPane.PLAIN_MESSAGE, null, null, "");
        if (name != null && name.length() >= 1 && name.length() <= 50) {
            Project p = new Project();
            p.setName(name);
            try {
                if (db.insertProject(p)) {
                    projectList = db.getAllProjects();
                    createRows();
                } else {
                    ErrorDialogUtils.showRecoverableErrorDialog("Project couldn't be inserted", this);
                }
            } catch (Exception e) {
                ErrorDialogUtils.showUnRecoverableErrorDialog(this);
            }
        } else if (name != null) {
            ErrorDialogUtils.showRecoverableErrorDialog("Project name has to have min 1 and maximal 50 characters ", this);
        }
    }

    /**
     * request database for deleting project
     *
     * @param p project to be deleted
     */
    public void deleteProject(Project p) {
        try {
            db.deleteProject(p.getId());
            projectList.remove(p);
            createRows();
        } catch (Exception e) {
            System.out.println(e);
            ErrorDialogUtils.showRecoverableErrorDialog("Project couldn't be deleted", this);
        }
    }

    /**
     * updated project and refreshes view
     * @param p project to be updated
     */
    public void updateProject(Project p) {
        try {
            db.updateProject(p);
            projectList = db.getAllProjects();
            createRows();
        } catch (Exception e) {
            ErrorDialogUtils.showRecoverableErrorDialog("Project couldn't be updated", this);
        }
    }


}