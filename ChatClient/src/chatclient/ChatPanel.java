package chatclient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * ChatPanel
 * Responsible for dealing with the sending and receiving of messages to and
 * from the server.
 * 
 * @author Nick
 */
public class ChatPanel extends JPanel {
    private static final int USER_LIST_WIDTH = 120;
    // Attribute to store bold text (used to display username)
    private final SimpleAttributeSet ATT_BOLD;
    // Attribute to signify the message is a private message
    private final SimpleAttributeSet ATT_PM;
    
    // Stores a reference to the chat server
    private final ChatServer m_server;
    // Stores the name of the user that is running this client
    private String m_user;
    // Lists all users that are in the chat
    private final JList<String> m_lstUsers;
    // List model for manipulating the user list
    private final DefaultListModel<String> m_lmUsers;
    
    // Lists all messages that have been sent
    private final JTextPane m_txtMsgList;
    // Create a document that will store the messages (so we can stylize them)
    private final StyledDocument m_docMsgList;
    
    
    // Allows the user to enter a message
    private final JTextField m_txtMsgInput;
    // Button to send the message input
    private final JButton m_btnSend;
    // Button for the user to leave the chat
    private final JButton m_btnLeave;
    
    public ChatPanel(ChatServer server) {
        // Store a reference to the server we will be using to send & receive
        m_server = server;
        
        // Components responsible for storing the list of active users
        m_lmUsers = new DefaultListModel<>();
        m_lstUsers = new JList<>(m_lmUsers);
        m_lstUsers.setBorder(new EmptyBorder(0, 5, 0, 0));
        m_lstUsers.setPreferredSize(new Dimension(USER_LIST_WIDTH, 0));
        
        // Components responsible for storing the messages
        m_txtMsgList = new JTextPane();
        m_txtMsgList.setEditable(false);
        m_docMsgList = m_txtMsgList.getStyledDocument();
        JScrollPane spMsgList = new JScrollPane(m_txtMsgList);
        spMsgList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        spMsgList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        // Allows users to enter their message
        m_txtMsgInput = new JTextField();
        
        m_btnSend = new JButton("Send");
        m_btnLeave = new JButton("Leave Chat");
        
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        add(m_lstUsers, c);
        
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.BOTH;
        add(m_btnLeave, c);
        
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        add(spMsgList, c);
        
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 0;
        add(m_txtMsgInput, c);
        
        c.gridx = 2;
        c.weightx = 0;
        c.weighty = 0;
        add(m_btnSend, c);
        
        // Set up the action listener for the send button
        getSendButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        
        // Add a mouse listener to automatically set up the PM to a user
        m_lstUsers.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // If the user has double clicked
                if(e.getClickCount() == 2) {
                    String toUser = m_lstUsers.getSelectedValue();
                    
                    if(!toUser.isEmpty()) {
                        m_txtMsgInput.setText("/pm \"" + toUser + "\" " + m_txtMsgInput.getText());
                        // Restore focus to the text input field, so the user can
                        // type the message without having to click the text field first
                        m_txtMsgInput.requestFocusInWindow();
                    }
                }
            }
        });
        
        // Define attributes for stylizing messages
        ATT_BOLD = new SimpleAttributeSet();
        ATT_PM = new SimpleAttributeSet();
        StyleConstants.setBold(ATT_BOLD, true);
        StyleConstants.setForeground(ATT_PM, Color.GREEN);
    }
    
    // Displays the list of active users specified in the left sidebar
    // This will replace the existing list with the specified list of users
    public void setUserList(List<String> users) {
        m_lmUsers.clear();
        
        for(String user : users) {
            addUser(user);
        }
    }
    
    // Appends a single user to the list of users
    public void addUser(String user) {
        m_lmUsers.addElement(user);
    }
    
    // Appends a list of messages to the chat log
    public void addMessages(List<Message> messages) {
        for(Message message : messages) {
            addMessage(message);
        }
    }
    
    // Appends a single message to the chat log
    public void addMessage(Message message) {
        // Convert the timestamp to a string (HH:MM:SS)
        Date msgDate = new Date(message.getTimestamp());
        String sTimestamp = new SimpleDateFormat("[HH:mm:ss]").format(msgDate);

        try {
            // Display the formatted timestamp of the message
            m_docMsgList.insertString(m_docMsgList.getLength(), sTimestamp + " ", null);
            
            // Check if this message is a private message
            // i.e if a receiver was specified
            if(message.getReceiver() != null) {
                // Output the appropriate "from" or "to" with respect to whether
                // the current user was the receiver or sender
                if(this.getUser().equals(message.getReceiver())) {
                    m_docMsgList.insertString(m_docMsgList.getLength(), "PM From ", ATT_PM);
                    m_docMsgList.insertString(m_docMsgList.getLength(), message.getSender() + ": ", ATT_BOLD);
                } else {
                    m_docMsgList.insertString(m_docMsgList.getLength(), "PM To ", ATT_PM);
                    m_docMsgList.insertString(m_docMsgList.getLength(), message.getReceiver() + ": ", ATT_BOLD);
                }  
            } else {
                // Otherwise just output the sender of the message
                m_docMsgList.insertString(m_docMsgList.getLength(), message.getSender() + ": ", ATT_BOLD);
            }

            // Output the message that was sent
            m_docMsgList.insertString(m_docMsgList.getLength(), message.getMessage() + "\n", null);
            
            // Scroll to the bottom of the message list
            m_txtMsgList.select(m_docMsgList.getLength(), m_docMsgList.getLength());
        // Exception thrown if we specify a position out of range in the chat log
        // (Should not ever be thrown with the existing code)
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    
    // Sends the existing message that has been entered to the server,
    // if it is appropriately formatted
    public void sendMessage() {
        // Remove unnecessary white space from the message
        String inputMsg = this.getInputMessage().trim();
        // Only send a non-empty message
        if(inputMsg.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a message");
        } else {
            Message msg;
            // If this is a private message
            if(inputMsg.startsWith("/pm ")) {
                msg = generatePrivateMessage(inputMsg);
            } else {
                msg = generateGlobalMessage(inputMsg);
            }
            
            // If the message was well formed (the receiver exists), send it
            if(msg != null) {
                m_server.sendMessage(msg);
                m_txtMsgInput.setText("");
            }
            
            // Have the text input request focus, as if the user clicked the send
            // button, it will have lost focus to the button
            m_txtMsgInput.requestFocusInWindow();
        }
        
    }
    
    // Generates a message that is to be sent to everyone in the chat, via the input string provided
    public Message generateGlobalMessage(String inputMsg) {
        Message msg = new Message();
        
        msg.setSender(this.getUser());
        msg.setMessage(inputMsg);
        msg.setTimestamp(System.currentTimeMillis());
        
        return msg;
    }
    
    // Generates a message that is to be sent to a single user, via the input string provided
    // If the format of the message is incorrect, the intended recipient does not exist,
    // or the user is trying to send a message to themselves, then a null message will be returned.
    public Message generatePrivateMessage(String inputMsg) {
        // Splits up the username of the receiver and the message itself
        // in to two separate groups
        String regex = "\\/pm\\s\"([\\w\\d\\s]+)\"\\s(.+)";
        
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(inputMsg);
        
        Message msg = new Message();
        msg.setSender(this.getUser());
        msg.setTimestamp(System.currentTimeMillis());
        
        // If our RegEx found a match
        if(m.find()) {
            msg.setReceiver(m.group(1));
            msg.setMessage(m.group(2));
            
            // Stop the user from sending a message to themselves
            if(msg.getReceiver().equals(this.getUser())) {
                JOptionPane.showMessageDialog(this, "Unable to send message to self");
                return null;
            // If the recipient specified does not exist
            } else if(!m_lmUsers.contains(msg.getReceiver())) {
                JOptionPane.showMessageDialog(this, "The user provided does not exist");
                return null;
            } else {
                return msg;
            }
        } else {
            JOptionPane.showMessageDialog(this, "There was an error with the format of your message\n"
                    + "Please follow the format /pm \"user\" message");
            return null;
        }
    }
    
    // Empties the user list and the message list
    public void clear() {
        // Clear the user list
        m_lmUsers.clear();
        
        try {
            // Clear the text message list
            m_docMsgList.remove(0, m_docMsgList.getLength());
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }
    
    public JButton getSendButton() {
        return m_btnSend;
    }
    public JButton getLeaveButton() {
        return m_btnLeave;
    }
    
    public String getInputMessage() {
        return m_txtMsgInput.getText();
    }
    
    // Sets the username of the user that is currently using the chat client
    public void setUser(String user) {
        m_user = user;
    }
    // Returns the usernanme of the user that is currently using the chat client
    public String getUser() {
        return m_user;
    }
}
