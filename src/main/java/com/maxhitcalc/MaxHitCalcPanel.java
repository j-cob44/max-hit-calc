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
import java.awt.image.BufferedImage;
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

    // NPC Selection Data
    private JComboBox npcList = new JComboBox();
    private JLabel npcNameHeader = new JLabel("No NPC Selected", SwingConstants.LEFT);
    private JLabel npcIcon = new JLabel("---");
    private JLabel weaknessIcon = new JLabel("---");
    private JLabel weaknessPercentLabel = new JLabel("---");


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


        // Content section
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        contentPanel.setLayout(new GridLayout(0, 1));

        // Construct main panel
        JPanel panel = new JPanel();
        panel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 0;

        // Blowpipe Dart Setting
        JLabel dartSettingLabel = new JLabel("Blowpipe Dart:");
        dartSettingLabel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 0;
        c.weightx = 0.7;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;

        panel.add(dartSettingLabel, c);


        // Create Dropdown menu for selecting Dart type
        dartList = new JComboBox(BlowpipeDartType.values());
        dartList.setPrototypeDisplayValue("123456789");
        BlowpipeDartType selectedDart = (BlowpipeDartType)configManager.getConfiguration("MaxHitCalc", "blowpipeDartType", BlowpipeDartType.class);
        dartList.setSelectedIndex(selectedDart.ordinal()); // 0 = Mithril
        dartList.addActionListener(e -> onDartSwitched());

        c.gridx = 1;
        c.gridy = 0;
        c.ipady = 0;
        c.weightx = 0.3;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
//        c.anchor = GridBagConstraints.LINE_END;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;

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
        c.weightx = 0.7;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = 1;

        panel.add(cbladeSettingLabel, c);

        // Dropdown for NPC Size
        colossalBladeList = new JComboBox(ColossalBladeSizeBonus.values());
        colossalBladeList.setPrototypeDisplayValue("12345");
        colossalBladeList.setSelectedIndex(0);
        colossalBladeList.addActionListener(e -> onCBladeSettingSwitched());

        c.gridx = 1;
        c.gridy = 2;
        c.ipady = 0;
        c.weightx = 0.3;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.LINE_END;
        c.gridwidth = 1;

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


        // Select NPC
        JLabel npcWeaknessLabel = new JLabel("Select NPC:");
        npcWeaknessLabel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        npcWeaknessLabel.setLayout(new GridLayout(2, 1));

        c.gridx = 0;
        c.gridy = 4;
        c.ipady = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.LINE_START;
        c.gridwidth = 1;

        panel.add(npcWeaknessLabel, c);

        // Dropdown for NPC selection
        npcList = new JComboBox(NPCTypeWeakness.values());
        npcList.setPrototypeDisplayValue("1234567890123");
        npcList.setSelectedIndex(0);
        npcList.addActionListener(e -> onNpcSelected());

        c.gridx = 1;
        c.gridy = 4;
        c.ipady = 0;
        c.weightx = 0.5;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.LINE_END;
        c.gridwidth = 1;

        panel.add(npcList, c);

        c.gridx = 0;
        c.gridy = 5;
        c.ipady = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(10,0,0,0);  //top padding
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 2;

        panel.add(Box.createGlue(), c);

        // Selected NPC info panel
        JPanel selectedNPCPanel = new JPanel(new BorderLayout(10, 10));
        selectedNPCPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);
        selectedNPCPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        npcNameHeader = new JLabel("No NPC Selected", SwingConstants.LEFT);
        npcNameHeader.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        selectedNPCPanel.add(npcNameHeader, BorderLayout.NORTH);

        JPanel imagePanel = new JPanel(new GridLayout(1, 3, 0, 0));

        // Selected NPC icon
        npcIcon = new JLabel("---");
        npcIcon.setHorizontalAlignment(SwingConstants.CENTER);
        npcIcon.setVerticalAlignment(SwingConstants.CENTER);
        imagePanel.add(npcIcon);

        // NPC weakness icon
        weaknessIcon = new JLabel("---");
        weaknessIcon.setHorizontalAlignment(SwingConstants.CENTER);
        weaknessIcon.setVerticalAlignment(SwingConstants.CENTER);
        imagePanel.add(weaknessIcon);

        weaknessPercentLabel = new JLabel("---");
        weaknessPercentLabel.setHorizontalAlignment(SwingConstants.CENTER);
        weaknessPercentLabel.setVerticalAlignment(SwingConstants.CENTER);
        imagePanel.add(weaknessPercentLabel);

        selectedNPCPanel.add(imagePanel, BorderLayout.CENTER);

        c.gridx = 0;
        c.gridy = 6;
        c.ipady = 0;
        c.weightx = 2;
        c.weighty = 0;
//        c.insets = new Insets(0,0,0,0);  //top padding
//        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;

        panel.add(selectedNPCPanel, c);

        c.gridx = 0;
        c.gridy = 7;
        c.ipady = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(10,0,0,0);  //top padding
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 2;

        panel.add(Box.createGlue(), c);

        JButton resetButton = new JButton("Reset");
        resetButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                resetNPCviaPanel();
            }
        });

        c.gridx = 0;
        c.gridy = 8;
        c.ipady = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.insets = new Insets(0,0,0,0);  //top padding
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 1;
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

    void onNpcSelected(){
        if(npcList.getSelectedIndex() == 0)
        {
            // handle as reset, means NONE is selected
            resetNpcInfoPanel();
        }
        else {
            NPCTypeWeakness selectedNPCWeakness = NPCTypeWeakness.values()[npcList.getSelectedIndex()];
            setNpcInfoPanel(selectedNPCWeakness);

            // Recalculate plugin
            plugin.selectedNPCName = selectedNPCWeakness.getNPCName();
            plugin.npcSelectedByPanel = true;
        }
    }

    void setNpcInfoPanel(NPCTypeWeakness selectedNPCWeakness){
        npcNameHeader.setText(selectedNPCWeakness.getNPCName());

        npcIcon.setText("");
        if(selectedNPCWeakness.getIcon() != null)
        {
            npcIcon.setIcon(selectedNPCWeakness.getIcon());
        }
        else
        {
            npcIcon.setText("No pic.");
            npcIcon.setIcon(null);
        }

        weaknessIcon.setText("");
        switch (selectedNPCWeakness.getElementalWeakness())
        {
            case Air: weaknessIcon.setIcon(NPCIcons.AIR_RUNE_ICON); break;
            case Water: weaknessIcon.setIcon(NPCIcons.WATER_RUNE_ICON); break;
            case Earth: weaknessIcon.setIcon(NPCIcons.EARTH_RUNE_ICON); break;
            case Fire: weaknessIcon.setIcon(NPCIcons.FIRE_RUNE_ICON); break;
            case NoType: weaknessIcon.setIcon(NPCIcons.EMPTY_RUNE_ICON); break;
        }

        weaknessPercentLabel.setText(selectedNPCWeakness.getWeaknessPercent() + "%");
    }


    void resetNpcInfoPanel()
    {
        npcNameHeader.setText("No NPC Selected");

        npcIcon.setIcon(null);
        npcIcon.setText("---");

        weaknessIcon.setIcon(null);
        weaknessIcon.setText("---");

        weaknessPercentLabel.setText("---");
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

        // Reset Npc selection panel
        resetNpcInfoPanel();
    }

    // Set panel info automatically from plugin
    void setNPCviaPlugin()
    {
        // Set Size
        int size = Math.min(plugin.NPCSize, 5);
        colossalBladeList.setSelectedIndex(size-1);

        // Set NPC Info panel
        NPCTypeWeakness selectedNpc = NPCTypeWeakness.findWeaknessByName(plugin.selectedNPCName);
        if (selectedNpc != null) {
            setNpcInfoPanel(selectedNpc);
        }
        else {
            resetNpcInfoPanel();

            // Set name
            npcNameHeader.setText(plugin.selectedNPCName);

            weaknessIcon.setText("");
            weaknessIcon.setIcon(NPCIcons.EMPTY_RUNE_ICON);

            weaknessPercentLabel.setText("0%");
        }
    }
}
