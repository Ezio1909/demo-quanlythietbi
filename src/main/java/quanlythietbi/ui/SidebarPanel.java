package quanlythietbi.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SidebarPanel extends JPanel {
    private static final Color SIDEBAR_BG = new Color(51, 51, 51);
    private static final Color HOVER_BG = new Color(70, 70, 70);
    private static final Color SELECTED_BG = new Color(80, 80, 80);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final int SIDEBAR_WIDTH = 220;
    private static final Font MENU_FONT = new Font("Arial", Font.PLAIN, 14);
    
    private final List<MenuButton> buttons = new ArrayList<>();
    private MenuButton selectedButton;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public SidebarPanel(JPanel contentPanel, CardLayout cardLayout) {
        this.contentPanel = contentPanel;
        this.cardLayout = cardLayout;
        setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));
        setBackground(SIDEBAR_BG);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(20, 0, 0, 0));

        // Add logo/title
        JLabel titleLabel = new JLabel("Device Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        add(titleLabel);

        // Add menu items
        addMenuItem("📱 Devices", "devices");
        addMenuItem("👥 Assignments", "assignments");
        addMenuItem("🔧 Maintenance", "maintenance");
        addMenuItem("📊 Dashboard", "dashboard");
        addMenuItem("📋 Reports", "reports");
        addMenuItem("⚙️ Settings", "settings");

        // Select first button by default
        if (!buttons.isEmpty()) {
            selectButton(buttons.get(0));
        }
    }

    private void addMenuItem(String text, String cardName) {
        MenuButton button = new MenuButton(text, cardName);
        buttons.add(button);
        add(button);
        add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void selectButton(MenuButton button) {
        if (selectedButton != null) {
            selectedButton.setSelected(false);
            selectedButton.setBackground(SIDEBAR_BG);
        }
        selectedButton = button;
        button.setSelected(true);
        button.setBackground(SELECTED_BG);
        cardLayout.show(contentPanel, button.getCardName());
    }

    private class MenuButton extends JPanel {
        private boolean isSelected = false;
        private boolean isHovered = false;
        private final String cardName;
        private final String text;

        MenuButton(String text, String cardName) {
            this.text = text;
            this.cardName = cardName;
            setOpaque(true);
            setBackground(SIDEBAR_BG);
            setMaximumSize(new Dimension(SIDEBAR_WIDTH, 40));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(8, 20, 8, 20));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!isSelected) {
                        isHovered = true;
                        setBackground(HOVER_BG);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!isSelected) {
                        isHovered = false;
                        setBackground(SIDEBAR_BG);
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    selectButton(MenuButton.this);
                }
            });
        }

        public String getCardName() {
            return cardName;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
            if (selected) {
                isHovered = false;
                setBackground(SELECTED_BG);
            } else {
                setBackground(isHovered ? HOVER_BG : SIDEBAR_BG);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(MENU_FONT);
            g2.setColor(TEXT_COLOR);
            g2.drawString(text, 20, getHeight() / 2 + 5);
            g2.dispose();
        }
    }
} 