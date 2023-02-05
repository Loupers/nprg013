package eu.piroutek.jan.controller;

import eu.piroutek.jan.model.Project;
import eu.piroutek.jan.model.Tag;
import eu.piroutek.jan.model.Task;

import javax.swing.*;

/**
 * main controller of application UI
 */
public class FrameController extends JFrame {

    private JPanel main;

    public FrameController() {
        main = new HomeScreen(this);

        setSize(1500, 1000);
        add(main);
        setVisible(true);
    }

    /**
     * show screen for list of tasks
     *
     * @param p project which tasks should be shown
     */
    public void goToTasks(Project p) {
        remove(main);
        main = new TaskScreen(p, this);
        add(main);

        revalidate();
        repaint();
    }

    /**
     * show screen for editing task
     *
     * @param t task to be edited
     * @param p project to which task belongs
     */
    public void goToTaskEdit(Task t, Project p) {
            remove(main);

            main = new TaskEdit(t, p, this);
            add(main);

            revalidate();
            repaint();
    }

    /**
     * show screen with list of tags for project
     *
     * @param p project to which tags belong
     */
    public void goToTags(Project p) {
            remove(main);
            main = new TagScreen(p, this);
            add(main);

            revalidate();
            repaint();
    }

    /**
     * show screen with list of projects
     */
    public void goToProjects() {
            remove(main);
            main = new HomeScreen(this);
            add(main);

            revalidate();
            repaint();
    }

    /**
     * show screen for editing tag
     *
     * @param t tag to be edited
     * @param p project to which tag belongs
     */
    public void goToTagEdit(Tag t, Project p) {
            remove(main);
            main = new TagEdit(t, p, this);
            add(main);

            revalidate();
            repaint();
    }
}