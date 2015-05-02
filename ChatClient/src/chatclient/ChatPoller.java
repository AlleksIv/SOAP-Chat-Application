package chatclient;

import java.util.List;

/**
 * ChatPoller
 * Responsible for periodically requesting new messages from the chat server.
 * Any new messages that are received are sent to the ChatPanel to be displayed.
 * 
 * @author Nick
 */
public class ChatPoller extends Thread {
    // How often the server will be polled for new messages (milliseconds)
    private static final int POLL_TIME_MS = 500;
    private final ChatServer m_server;
    private final ChatPanel m_panel;
    // Specifies if the polling is currently active
    // Note: once the polling has been stopped, it can not be resumed
    private boolean m_active;
    // Unix timestamp specifying when the list of users was last changed
    private long m_lastUserUpdate;
    
    public ChatPoller(ChatServer server, ChatPanel panel) {
        m_server = server;
        m_panel = panel;
        m_active = true;
        m_lastUserUpdate = -1;
    }
    
    @Override
    public void run() {
        while(isActive()) {
            // Update with recent messages
            List<Message> messages = m_server.getMessages(m_panel.getUser());
            m_panel.addMessages(messages);
            
            // Check if the user list has changed
            long serverLastUserUpdate = m_server.getLastUserUpdate();
            if(serverLastUserUpdate > m_lastUserUpdate) {
                // Retrieve the current list of users
                List<String> userList = m_server.getClientList();
                // Update the user list with the list we just obtained from the server
                m_panel.setUserList(userList);
                // The last user change time has updated
                m_lastUserUpdate = serverLastUserUpdate;
            }
            
            try {
                Thread.sleep(POLL_TIME_MS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Returns true if this thread is still polling the chat for updates
    public boolean isActive() {
        return m_active;
    }
    
    // Stops the thread from executing
    public void finish() {
        m_active = false;
    }
}
