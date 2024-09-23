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
import java.util.Vector;
import javax.annotation.Nullable;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.materialtabs.MaterialTab;
import net.runelite.client.ui.components.materialtabs.MaterialTabGroup;
import net.runelite.client.util.ImageUtil;

public class MaxHitCalcPanel extends PluginPanel
{
    @Inject
    @Nullable
    private Client client;

    @Inject
    private EventBus eventBus;

    @Inject
    private ClientThread clientThread;

    private MaxHitCalcPlugin plugin;
    private MaxHitCalcConfig config;

    // tabs
    private final JPanel npcIconDisplay = new JPanel();
    private final MaterialTabGroup npcTabGroup = new MaterialTabGroup(npcIconDisplay);

    // UI Settings
    private JComboBox dartList = new JComboBox();
    private JComboBox colossalBladeList = new JComboBox();

    private JLabel[] allNPCLabels;
    private Map<JLabel, String> npcLabels = new HashMap<>();

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

        // initial setting of panel layout
        getParent().setLayout(new BorderLayout());
        getParent().add(this, BorderLayout.CENTER); // <- this allows the panel to take up entire space

        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));


        // Build Settings Panel on Top
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        settingsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        settingsPanel.setLayout(new GridLayout(0,1));

        JLabel settingsHeader = new JLabel("Calculation Settings");
        settingsPanel.add(settingsHeader);

        // Blowpipe Dart Setting
        JLabel dartSettingLabel = new JLabel("Dart Type:");
        settingsPanel.add(dartSettingLabel);

        // Create Dropdown menu for selecting Dart type
        dartList = new JComboBox(BlowpipeDartType.values());
        dartList.setSelectedIndex(0); // 0 = Mithril
        dartList.addActionListener(e -> onDartSwitched());

        settingsPanel.add(dartList);
        settingsPanel.add(Box.createGlue());

        // Colossal Blade Setting
        JLabel colossalBladeLabel = new JLabel("Colossal Blade Enemy Size:");
        settingsPanel.add(colossalBladeLabel);
        // Dropdown
        colossalBladeList = new JComboBox(ColossalBladeSizeBonus.values());
        colossalBladeList.setSelectedIndex(0);
        colossalBladeList.addActionListener(e -> onCBladeSettingSwitched());

        settingsPanel.add(colossalBladeList);
        settingsPanel.add(Box.createGlue());

        // Build next section
        JPanel selectNPCPanel = new JPanel();
        selectNPCPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        selectNPCPanel.setLayout(new BoxLayout(selectNPCPanel, BoxLayout.PAGE_AXIS));
        selectNPCPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Selected NPC setting
        JLabel selectedNPCLabel = new JLabel("Selected NPC: ");
        selectNPCPanel.add(selectedNPCLabel);


        int totalCells = NPCTypeWeakness.values().length;
        int columns = 5;
        int rows = (int) Math.ceil(((double) totalCells/(double)columns));
        int index = 0;

        JPanel frame = new JPanel();
        selectNPCPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        frame.setLayout(new GridLayout(rows, columns));

        allNPCLabels = new JLabel[totalCells];
        JLabel[][] gridLabels = new JLabel[rows][columns];
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < columns; j++)
            {
                if(index >= totalCells)
                {
                    gridLabels[i][j] = new JLabel(""); // Make empty when not cleanly divided
                }
                else
                {
                    gridLabels[i][j] = createIcon(NPCTypeWeakness.values()[index].getIcon(), NPCTypeWeakness.values()[index].getNPCName());

                    npcLabels.put(gridLabels[i][j], NPCTypeWeakness.values()[index].getNPCName());
                    allNPCLabels[index] = gridLabels[i][j];
                    index++;
                }
                gridLabels[i][j].setSize(32,32); // max size
                gridLabels[i][j].setBackground(Color.DARK_GRAY);
                frame.add(gridLabels[i][j]);
            }
        }

        JScrollPane scroller = new JScrollPane(frame);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scroller.getVerticalScrollBar().setBorder(new EmptyBorder(0, 0, 0, 0));
        scroller.setBackground(ColorScheme.DARK_GRAY_COLOR);
        scroller.setPreferredSize(new Dimension(200, 200));

        //scroller.add(frame);
        selectNPCPanel.add(scroller);

        add(settingsPanel, BorderLayout.NORTH);
        add(selectNPCPanel, BorderLayout.CENTER);

        eventBus.register(this);
    }

    void deinit()
    {
        eventBus.unregister(this);
    }

    private JLabel createIcon(ImageIcon icon, String npcName)
    {
        JLabel empty = new JLabel("");

        MaterialTab iconLabel = new MaterialTab(icon, npcTabGroup, empty);
        iconLabel.setPreferredSize(new Dimension(32, 48));
        iconLabel.setName(npcName);
        iconLabel.setToolTipText(npcName);
        iconLabel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        iconLabel.setBorder(BorderFactory.createLineBorder(ColorScheme.DARK_GRAY_COLOR, 1));

        //iconLabel.setHorizontalAlignment(JLabel.CENTER);
        //iconLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        //iconLabel.setSize(32,32);

        iconLabel.setOnSelectEvent(() ->
        {
            onNPCSelect(iconLabel);
            return true;
        });

//        iconLabel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                onNPCSelect(iconLabel);
//            }
//        });

        npcTabGroup.addTab(iconLabel);

        return iconLabel;
    }

    void onNPCSelect(JLabel clickedLabel){
        String npcName = npcLabels.get(clickedLabel);

        System.out.println(npcName);

        // Clear old selected
        for(JLabel label : allNPCLabels)
        {
            label.setBackground(Color.DARK_GRAY);
        }

        // Highlight selected
        clickedLabel.setBackground(Color.ORANGE);

        plugin.selectedNPCName = npcName;
        plugin.npcSelectedByPanel = true;
        plugin.selectedNPCExpiryTime = Integer.MAX_VALUE;
    }

    void onDartSwitched()
    {
        BlowpipeDartType selectedDart = BlowpipeDartType.values()[dartList.getSelectedIndex()];
        plugin.selectedDartType = selectedDart;
        plugin.dartSettingChanged = true;
    }

    void onCBladeSettingSwitched()
    {
        plugin.NPCSize = Math.max(1, colossalBladeList.getSelectedIndex()+1); // index 0 == 1, index 1 == 2, etc...
        plugin.npcSizeSettingChanged = true;
    }
}
