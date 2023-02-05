package eu.piroutek.jan.controller;

import eu.piroutek.jan.database.TagDbHandler;
import eu.piroutek.jan.model.Project;
import eu.piroutek.jan.model.Tag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * screen for showing all tags, that belong to some project
 */
public class TagScreen extends JPanel {
    private JPanel mainPanel;
    private JPanel headPanel;
    private JPanel tagsPanel;
    private JButton createTagBtn;
    private JLabel tagLbl;
    private JButton backBtn;
    private JPanel wrapper;


    private Project project;
    private FrameController parent;
    private TagDbHandler db;
    private List<Tag> tagList;

    /**
     *
     * @param p project which tasks should be displayed
     * @param parent frame
     */
    public TagScreen(Project p, FrameController parent) {
        this.project = p;
        this.parent = parent;

        wrapper = new JPanel();
        tagsPanel.setLayout(new GridLayout(0, 1));
        setLayout(new BorderLayout());
        mainPanel.add(new JScrollPane(wrapper));

        add(mainPanel);

        try {
            db = new TagDbHandler();
            tagList = db.getAllTags(this.project.getId());
            createRows();
        } catch (Exception e) {
            ErrorDialogUtils.showUnRecoverableErrorDialog(this);
        }

        tagLbl.setText("tags for project " + project.getName());
        setButtons();
        setVisible(true);
    }

    /**
     * lists all tags data as rows
     */
    private void createRows() {
        tagsPanel.removeAll();
        tagsPanel.setLayout(new GridLayout(0, 1));
        for (Tag t : tagList) {
            tagsPanel.add(new TagRow(t, project, parent, this));
        }
        tagsPanel.setVisible(true);

        wrapper.removeAll();
        wrapper.add(tagsPanel);
        wrapper.setVisible(true);
        wrapper.revalidate();
        wrapper.repaint();

    }

    /**
     * set button actions
     */
    private void setButtons() {
        createTagBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDialog();
            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.goToProjects();
            }
        });
    }

    /**
     * show dialog with name input, when creating new tag
     * default tag color is black
     */
    private void showDialog() {
        String name = (String) JOptionPane.showInputDialog(this, "Set tag name", "kanboard", JOptionPane.PLAIN_MESSAGE, null, null, "");
        if (name != null && name.length() >= 1 && name.length() <= 50) {
            Tag t = new Tag();
            t.setProjectId(project.getId());
            t.setName(name);
            t.setRed(0);
            t.setBlue(0);
            t.setGreen(0);
            try {
                int id = db.insertTag(t);
                if (id != -1) {
                    Tag newTag = db.getTag(id);
                    parent.goToTagEdit(newTag, project);
                } else {
                    ErrorDialogUtils.showRecoverableErrorDialog("Tag couldn't be created", this);
                }
            } catch (Exception e) {
                ErrorDialogUtils.showUnRecoverableErrorDialog(this);
            }
        } else if (name != null) {
            ErrorDialogUtils.showRecoverableErrorDialog("Tag name has to have min 1 and maximal 50 characters ", this);
        }
    }

    /**
     * delete tag from database and refresh view
     * @param tag to be deleted
     */
    public void deleteTag(Tag tag) {
        try {
            db.deleteTag(tag.getId());
            tagList.remove(tag);
            createRows();
        } catch (Exception e) {
            ErrorDialogUtils.showRecoverableErrorDialog("Tag couldn't be deleted", this);
        }
    }
}