package chatclient;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * ChatFrame
 * Responsible for managing both the process of joining & leaving the chat,
 * and the process of using the chat (sending & receiving messages).
 * These two processes have been split up in to separate JPanels,
 * LoginPanel & ChatPanel. This class simply encapsulates the two panels and
 * swaps them around when necessary.
 * 
 * @author Nick
 */
public class ChatFrame extends JFrame implements ActionListener {
    // Default size of the JFrame when it is initially loaded
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 480;
    
    // The maximum length a user's name may be
    private static final int MAX_LENGTH_NAME = 15;
    
    private final LoginPanel m_loginPanel;
    private final ChatPanel m_chatPanel;
    private final ChatServer m_server;
    private ChatPoller m_poller;
    
    public ChatFrame() {
        // Connect to the server
        ChatServer_Service cs_service = new ChatServer_Service();
        m_server = cs_service.getChatServerPort();
        
        // Create the panel that allows to user to login
        m_loginPanel = new LoginPanel();
        // This frame deals with the event when a user tries to login
        m_loginPanel.getJoinButton().addActionListener(this);
        // Create the panel that allows the user to interact with the chat
        m_chatPanel = new ChatPanel(m_server);
        // Let this frame deal with a logout event too, so we know when to swap
        // back the JPanels
        m_chatPanel.getLeaveButton().addActionListener(this);
        
        // Add an event listener for when a user attempts to close the window
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                closeCalled();
            } 
        });
        
        setTitle("Nick's Chat Client");
        // Set the default close operation to nothing, as we provide the user with a
        // confirmation message when they attempt to close the window
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        // By default show the panel that enables the user to join the chat
        showLoginPane();
        
        setVisible(true);
    }
    
    private void showLoginPane() {
        setContentPane(m_loginPanel);
        // Set it so that when the enter key is pressed, the join button
        // action listener is automatically called
        getRootPane().setDefaultButton(m_loginPanel.getJoinButton());
        revalidate();
    }
    
    private void showChatPane() {
        setContentPane(m_chatPanel);
        // Set it so that when the enter key is pressed, the send button
        // action listener is automatically called
        getRootPane().setDefaultButton(m_chatPanel.getSendButton());
        revalidate();
        
        // Start polling the chat server for updates
        m_poller = new ChatPoller(m_server, m_chatPanel);
        m_poller.start();
    }
    
    // Returns true if the user is currently logged in to the chat,
    // or false otherwise
    private boolean isViewingChatPane() {
        return getContentPane() == m_chatPanel;
    }
    
    // Called when a user presses the exit button or when the user presses the 
    // leave button from the chat. If the user is currently in chat mode,
    // they will have to confirm they wish to exit before proceeding.
    private void closeCalled() {
        // If the user is in the chat
        if(isViewingChatPane()) {
            // Confirm that the user wishes to leave
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you wish to leave the chat?", 
                    "Leave Chat", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE);
            
            // The user does wish to leave
            if(confirm == JOptionPane.YES_OPTION) {
                // Stop the polling thread from requesting further updates
                // from the chat server
                m_poller.finish();
                // Let the server know that the user is leaving
                m_server.leave(m_chatPanel.getUser());

                showLoginPane();

                // Clear the chat and user list
                m_chatPanel.clear();
            }
            
        // If the user is not in the chat, we will close the window without
        // confirming that they wish to close it (nothing important may be lost
        // in this state)
        } else {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        // The user is trying to join the chat
        if(e.getSource() == m_loginPanel.getJoinButton()) {
            // Remove any unnecessary white space from the name
            String enteredName = m_loginPanel.getEnteredName().trim();
            
            // Ensure they provide a non-empty name
            if(enteredName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name");
            // Set a limit to the maximum length of a name
            } else if(enteredName.length() > MAX_LENGTH_NAME) {
                JOptionPane.showMessageDialog(this, "Name must be no more than " + MAX_LENGTH_NAME + " characters long");
            } else {
                try {
                    // Only let the user join if the name is not already taken
                    // (Server join returns boolean specifying if join is successful)
                    if(m_server.join(enteredName)) {
                        m_chatPanel.setUser(enteredName);
                        showChatPane();
                    } else {
                        JOptionPane.showMessageDialog(this, "That username is already taken");
                    }
                // Have to use Exception rather than ConnectException which is actually thrown
                // when we cannot connect to the server, as join is  not recognised
                // as throwing ConnectException for some reason
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(this, "Unable to connect to server");
                } 
            }
        // The user is trying to leave the chat
        } else if(e.getSource() == m_chatPanel.getLeaveButton()) {
            closeCalled();
        }
    }
}
