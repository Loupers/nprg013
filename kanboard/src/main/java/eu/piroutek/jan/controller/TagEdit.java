package eu.piroutek.jan.controller;

import eu.piroutek.jan.database.TagDbHandler;
import eu.piroutek.jan.model.Project;
import eu.piroutek.jan.model.Tag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * screen for editing tasks
 */
public class TagEdit extends JPanel {


    private Tag tag;
    private FrameController parent;
    private Project project;
    private JPanel mainPanel;
    private JPanel tagHeaderPanel;
    private JPanel tagParamsPanel;
    private JButton SaveBtn;
    private JButton BackBtn;
    private JLabel editTagLbl;
    private JTextField nameInput;
    private JLabel nameLbl;
    private JButton chooseColorBtn;

    private TagDbHandler db;

    /**
     *
     * @param t tag for this row
     * @param p project of that tag
     * @param parent frame
     */
    public TagEdit(Tag t, Project p, FrameController parent) {
        this.tag = t;
        this.project = p;
        this.parent = parent;

        setLayout(new BorderLayout());
        add(mainPanel);

        this.nameInput.setText(tag.getName());
        setButtons();
        setVisible(true);
    }

    /**
     * set button actions
     */
    private void setButtons() {
        BackBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.goToTags(project);
            }
        });

        SaveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        this.chooseColorBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JColorChooser chooser = new JColorChooser();
                JDialog dialog = JColorChooser.createDialog(parent, "tag color", true, chooser, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        chooseColorBtn.setBackground(chooser.getColor());
                        tag.setGreen(chooser.getColor().getGreen());
                        tag.setRed(chooser.getColor().getRed());
                        tag.setBlue(chooser.getColor().getBlue());
                    }
                }, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //cancel do nothing
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    /**
     * save updated values into database
     */
    private void save() {
        try {
            tag.setName(nameInput.getText());
            this.db = new TagDbHandler();
            if (db.updateTag(tag)) {
                parent.goToTags(project);
            } else {
                ErrorDialogUtils.showRecoverableErrorDialog("Tag couldn't be updated", this);
            }
        } catch (Exception e) {
            ErrorDialogUtils.showUnRecoverableErrorDialog(this);
        }
    }
}
