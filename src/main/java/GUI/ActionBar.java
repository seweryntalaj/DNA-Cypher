package GUI;

import buisness.model.Message;
import buisness.MessageProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ActionBar extends JDesktopPane implements ActionListener {

    private JButton encodeButton;
    private JButton decodeButton;
    private JCheckBox toggleDeletions;
    private JCheckBox toggleDuplications;
    private JCheckBox toggleInsertions;
    private JCheckBox toggleInvertions;
    private JCheckBox toggleSubstitutions;
    private JCheckBox toggleTranslocations;
    private PropertiesManager propertiesManager;

    public ActionBar(PropertiesManager propertiesManager) {
        this.propertiesManager = propertiesManager;
        toggleDeletions = new JCheckBox("Deletions");
        toggleDuplications = new JCheckBox("Duplications");
        toggleInsertions = new JCheckBox("Insertions");
        toggleInvertions = new JCheckBox("Inversions");
        toggleSubstitutions = new JCheckBox("Substitutions");
        toggleTranslocations = new JCheckBox("Translocations");
        toggleTranslocations.setEnabled(false);
        java.util.List<JCheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(toggleDeletions);
        checkBoxes.add(toggleDuplications);
        checkBoxes.add(toggleInsertions);
        checkBoxes.add(toggleInvertions);
        checkBoxes.add(toggleSubstitutions);
        checkBoxes.add(toggleTranslocations);
        checkBoxes.forEach(this::add);
        setLayout(new GridLayout(4, 2));
        setPreferredSize(new Dimension(400, 100));
        encodeButton = new JButton("Encode");
        decodeButton = new JButton("Decode");
        add(decodeButton);
        encodeButton.addActionListener(this);
        decodeButton.addActionListener(this);
        add(encodeButton);
        this.propertiesManager = propertiesManager;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source == encodeButton) {
            propertiesManager.init();

            if (DataManager.getFile() != null) {
                if (toggleDeletions.isSelected()) propertiesManager.enableDeletions();
                if (toggleDuplications.isSelected()) propertiesManager.enableDuplications();
                if (toggleInsertions.isSelected()) propertiesManager.enableInsertions();
                if (toggleInvertions.isSelected()) propertiesManager.enableInversions();
                if (toggleSubstitutions.isSelected()) propertiesManager.enableSubstitutions();
                MessageProcessor messageProcessor = new MessageProcessor();
                Message message = messageProcessor.processEncoding(DataManager.getFile());
                FileInfoBox.setMutationKeyValue(message.getKey());
                Popup.popupInfo("Mutation key copied to clipboard!", "", 1);
                StringSelection selection = new StringSelection(message.getKey());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            } else {
                Popup.popupInfo("No file was chosen!", "Input error", 0);
            }
        }

        if (source == decodeButton) {
            if (DataManager.getFile() != null) {

                String mutationKey = FileInfoBox.getMutationKeyValue();
                MessageProcessor messageProcessor = new MessageProcessor();
                Message message = messageProcessor.processDecoding(DataManager.getFile(), mutationKey);
            } else {
                Popup.popupInfo("No file was chosen!", "Input error", 0);
            }
        }
    }
}
