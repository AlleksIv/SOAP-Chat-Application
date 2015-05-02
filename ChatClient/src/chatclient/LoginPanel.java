package chatclient;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * LoginPanel
 * Responsible for allowing the user to enter a username and join the chat.
 * 
 * @author Nick
 */
public class LoginPanel extends JPanel {
    // The size of the login components
    private static final int PANEL_WIDTH = 160;
    private static final int PANEL_HEIGHT = 80;
    
    private final JTextField m_txtName;
    private final JButton m_btnJoin;
    
    public LoginPanel() {
        // Create all of the required components
        JLabel lblName = new JLabel("Name:");
        m_txtName = new JTextField();
        m_btnJoin = new JButton("Join");
        
        // Use GridBagLayout as the main layout, as it centers all components
        setLayout(new GridBagLayout());
        
        // Create an inner JPanel that will be centered
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        mainPanel.setBorder(new TitledBorder("Join Chat"));
        
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 4, 2, 4);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 1;
        
        mainPanel.add(lblName, c);
        
        c.gridx = 1;
        c.weightx = 1;
        mainPanel.add(m_txtName, c);
        
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        mainPanel.add(m_btnJoin, c);
        
        add(mainPanel);
    }
    
    // Retrieves the username that the user has entered
    // Avoid `getName` as that is a member of JComponent
    public String getEnteredName() {
        return m_txtName.getText();
    }
    
    // Allow outside classes to access the join button,
    // to attach event listeners etc..
    public JButton getJoinButton() {
        return m_btnJoin;
    }
}
