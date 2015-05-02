package chatclient;

import javax.swing.SwingUtilities;

/**
 * ChatClient
 * Main class for the client, starts up the user interface that allows the user
 * to interact with the chat.
 * @author Nick
 */
public class ChatClient {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new ChatFrame();
            }
        });
        
    }
    
}
