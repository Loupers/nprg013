package eu.piroutek.jan.controller;

import eu.piroutek.jan.model.Project;
import eu.piroutek.jan.model.Tag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * row for displaying data about task
 */
public class TagRow extends JPanel{
    private JButton editTagBtn;
    private JButton deleteTagBtn;
    private JLabel tagNameLbl;
    private JPanel row;

    private Tag tag;
    private Project project;
    private FrameController parent;
    private TagScreen layout;

    /**
     *
     * @param tag for this row
     * @param p project for this tag
     * @param parent frame
     * @param layout in which row is displayed
     */
    public TagRow(Tag tag, Project p, FrameController parent, TagScreen layout) {
        this.tag = tag;
        this.parent = parent;
        this.layout = layout;
        this.project = p;

        this.row.setBackground(new Color(tag.getRed(), tag.getGreen(), tag.getBlue()));
        if (tag.sumOfColors() < 400) {
            this.tagNameLbl.setForeground(new Color(255, 255, 255));
        }

        add(row);
        tagNameLbl.setText(tag.getName());
        this.setButtons();
        setVisible(true);
    }

    /**
     * set button actions
     */
    private void setButtons() {
        editTagBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.goToTagEdit(tag, project);
            }
        });

        deleteTagBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                layout.deleteTag(tag);
            }
        });
    }
}
