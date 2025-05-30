/* MaxHitCalcPanel.java
 * Code for plugin panel which changes calculation settings
 *
 *
 * Copyright (c) 2024, Jacob Burton <https://github.com/j-cob44>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.maxhitcalc;

import com.google.inject.Inject;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;


public class MaxHitCalcPanel extends PluginPanel
{
    @Inject
    @Nullable
    private Client client;

    @Inject
    private EventBus eventBus;

    @Inject
    private ClientThread clientThread;

    @Inject
    private ConfigManager configManager;

    private MaxHitCalcPlugin plugin;
    private MaxHitCalcConfig config;

    // UI Settings
    private JComboBox dartList = new JComboBox();
    private JComboBox colossalBladeList = new JComboBox();

    private JLabel selectionNotice;
    private JLabel[] allNPCLabels;
    private Map<JLabel, String> npcLabels = new HashMap<>();

    private String panelSelectedNPCName = "";


    enum ColossalBladeSizeBonus
    {
        ONE(1) {

            @Override
            public String toString() {
                return "1x1";
            }
        },
        TWO(2) {
            @Override
            public String toString() {
                return "2x2";
            }
        },
        THREE(3) {
            @Override
            public String toString() {
                return "3x3";
            }
        },
        FOUR(4) {
            @Override
            public String toString() {
                return "4x4";
            }
        },
        FIVE(5) {
            @Override
            public String toString() {
                return "5x5 +";
            }
        };

        public final int monsterSize;

        ColossalBladeSizeBonus(int monsterSize) {
            this.monsterSize = monsterSize;
        }
    }

    void init(MaxHitCalcPlugin plugin, MaxHitCalcConfig config)
    {
        this.plugin = plugin;
        this.config = config;

        // Initial Settings
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        titlePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        titlePanel.setLayout(new GridLayout(0, 1));

        JLabel titleLabel = new JLabel();
        titleLabel.setText("Calculation Settings");

        titlePanel.add(titleLabel);
        //titlePanel.add(Box.createGlue()); // Adds a break


        // Content section
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        contentPanel.setLayout(new GridLayout(0, 1));

        // Blowpipe Dart Setting
        JPanel panel = new JPanel();
        panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 0;

        JLabel dartSettingLabel = new JLabel("Blowpipe Dart:");
        dartSettingLabel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        //dartSettingLabel.setLayout(new GridLayout(2, 1));

        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = 2;

        panel.add(dartSettingLabel, c);


        // Create Dropdown menu for selecting Dart type
        dartList = new JComboBox(BlowpipeDartType.values());
        BlowpipeDartType selectedDart = (BlowpipeDartType)configManager.getConfiguration("MaxHitCalc", "blowpipeDartType", BlowpipeDartType.class);
        dartList.setSelectedIndex(selectedDart.ordinal()); // 0 = Mithril
        dartList.addActionListener(e -> onDartSwitched());

        c.gridx = 2;
        c.gridy = 0;
        c.ipady = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.LINE_END;
        c.gridwidth = 2;

        panel.add(dartList, c);


        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(10,0,0,0);  //top padding
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 2;

        panel.add(Box.createGlue(), c);

        // Colossal Blade NPC Size Setting
        JLabel cbladeSettingLabel = new JLabel("NPC Size:");
        cbladeSettingLabel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        cbladeSettingLabel.setLayout(new GridLayout(2, 1));

        c.gridx = 0;
        c.gridy = 2;
        c.ipady = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = 2;

        panel.add(cbladeSettingLabel, c);

        // Dropdown for NPC Size
        colossalBladeList = new JComboBox(ColossalBladeSizeBonus.values());
        colossalBladeList.setSelectedIndex(0);
        colossalBladeList.addActionListener(e -> onCBladeSettingSwitched());

        c.gridx = 2;
        c.gridy = 2;
        c.ipady = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.LINE_END;
        c.gridwidth = 2;

        panel.add(colossalBladeList, c);

        c.gridx = 0;
        c.gridy = 3;
        c.ipady = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(10,0,0,0);  //top padding
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 2;

        panel.add(Box.createGlue(), c);

        // NPC Selection Title
        JLabel npcSelectionTitleLabel = new JLabel("Selected NPC:");
        npcSelectionTitleLabel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        c.gridx = 0;
        c.gridy = 4;
        c.ipady = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = 2;

        panel.add(npcSelectionTitleLabel, c);

        // NPC Selection
        selectionNotice = new JLabel("");
        selectionNotice.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        c.gridx = 2;
        c.gridy = 4;
        c.ipady = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.LINE_END;
        c.gridwidth = 2;

        panel.add(selectionNotice, c);

        int totalCells = NPCTypeWeakness.values().length;
        allNPCLabels = new JLabel[totalCells];

        int columns = 4;
        int rows = (int) Math.ceil(((double) totalCells/(double)columns));
        int index = 0;

        int finalRow = 6;

        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < columns; j++)
            {
                if(index >= totalCells)
                {


                    //panel.add(new JLabel(""), c);
                }
                else
                {
                    NPCTypeWeakness currentNPC = NPCTypeWeakness.values()[index];
                    JLabel createLabel = createIcon(currentNPC.getIcon(), currentNPC.getNPCName());

                    allNPCLabels[index] = createLabel;
                    npcLabels.put(createLabel, currentNPC.getNPCName());

                    c.gridx = 0+j;
                    c.gridy = 5+i;
                    c.ipady = 0;
                    c.weightx = 0.5;
                    c.weighty = 0;
                    c.insets = new Insets(0,0,0,0);  //top padding
                    c.anchor = GridBagConstraints.CENTER;
                    c.gridwidth = 1;
                    c.gridheight = 1;

                    panel.add(createLabel, c);
                    index++;
                }
            }
            finalRow = 6+i; // needed for last button
        }

        JButton resetButton = new JButton("Reset");
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetNPCviaPanel();
            }
        });

        c.gridx = 0;
        c.gridy = finalRow;
        c.ipady = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 2;
        c.gridheight = 0;

        panel.add(resetButton, c);

        contentPanel.add(panel);

        // Final,
        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        eventBus.register(this);
    }

    void deinit()
    {
        eventBus.unregister(this);
    }

    private JLabel createIcon(ImageIcon icon, String npcName)
    {
        ImageIcon resizedImage = new ImageIcon(icon.getImage().getScaledInstance(-1, 39, Image.SCALE_DEFAULT));

        JLabel iconLabel = new JLabel(resizedImage);
        iconLabel.setLayout(new BorderLayout());
        iconLabel.setBorder(new EmptyBorder(1,1,1,1));
        iconLabel.setToolTipText(npcName);
        iconLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                JLabel currentLabel = (JLabel) e.getSource();

                clearCurrentPanelNPC(); // remove border from previous npc

                currentLabel.setBorder(new LineBorder(Color.ORANGE, 1));

                panelSelectedNPCName = npcLabels.get(currentLabel);

                selectionNotice.setText(panelSelectedNPCName);

                // Recalculate plugin
                plugin.selectedNPCName = panelSelectedNPCName;
                plugin.npcSelectedByPanel = true;
            }
        });

        return iconLabel;
    }

    void onDartSwitched()
    {
        BlowpipeDartType selectedDart = BlowpipeDartType.values()[dartList.getSelectedIndex()];
        configManager.setConfiguration("MaxHitCalc", "blowpipeDartType", selectedDart);
        plugin.selectedDartType = selectedDart;
        plugin.dartSettingChanged = true;
    }

    void configDartSwitched()
    {
        dartList.setSelectedIndex(config.blowpipeDartType().ordinal());
        plugin.selectedDartType = config.blowpipeDartType();
    }

    void onCBladeSettingSwitched()
    {
        plugin.NPCSize = Math.max(1, colossalBladeList.getSelectedIndex()+1); // index 0 == 1, index 1 == 2, etc...
        plugin.npcSizeSettingChanged = true;
    }

    void resetNPCviaPanel(){
        // Reset size
        colossalBladeList.setSelectedIndex(0);

        // Reset border around npc
        clearCurrentPanelNPC();

        // Clear Texts
        panelSelectedNPCName = "";
        selectionNotice.setText(panelSelectedNPCName);
    }

    // Set panel info automatically from plugin
    void setNPCviaPlugin()
    {
        // Set Size
        int size = Math.min(plugin.NPCSize, 5);
        colossalBladeList.setSelectedIndex(size-1);

        // Clear currently bordered npc on panel
        clearCurrentPanelNPC();

        // Set NPC
        selectionNotice.setText(plugin.selectedNPCName);
        if(plugin.selectedNPCName != null)
        {
            for(JLabel label : allNPCLabels)
            {
                String searchedNPC = npcLabels.get(label);

                // Full and Correct name found
                if(searchedNPC.equals(plugin.selectedNPCName))
                {
                    label.setBorder(new LineBorder(Color.ORANGE, 1));
                    panelSelectedNPCName = plugin.selectedNPCName; // Full name
                    break;
                }

                // Partial name found, e.g. baby (black dragon)
                if(plugin.selectedNPCName.toLowerCase().contains(searchedNPC.toLowerCase()))
                {
                    label.setBorder(new LineBorder(Color.ORANGE, 1));
                    panelSelectedNPCName = searchedNPC.toLowerCase(); // Partial name
                    break;
                }
            }
        }
    }

    // Clears currently Selected in the panel
    void clearCurrentPanelNPC()
    {
        for(JLabel label : allNPCLabels)
        {
            String searchedNPC = npcLabels.get(label);

            if (!panelSelectedNPCName.isEmpty())
            {
                // Find Full correct name
                if (searchedNPC.equals(panelSelectedNPCName))
                {
                    label.setBorder(new EmptyBorder(0, 0, 0, 0));
                    break;
                }

                // Find Partial name , e.g. baby (black dragon)
                if (panelSelectedNPCName.toLowerCase().contains(searchedNPC.toLowerCase()))
                {
                    label.setBorder(new EmptyBorder(0, 0, 0, 0));
                    break;
                }
            }
            else
            {
                break; // no npc currently selected in panel
            }
        }
    }
}
