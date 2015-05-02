package chatserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.jws.Oneway;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * ChatServer
 * Manages multiple clients, allowing them to connect, leave, and send & receive
 * messages, when connected.
 * 
 * @author Nick
 */
@WebService(serviceName = "ChatServer")
public class ChatServer {
    // Read write lock to stop concurrent modification of the client list
    private final ReadWriteLock rwl_clients = new ReentrantReadWriteLock(true);
    // Read write lock to stop concurrent modification of the message list
    private final ReadWriteLock rwl_messages = new ReentrantReadWriteLock(true);
    // Stores a list of usernames of the connected clients
    private final List<String> m_clients = new ArrayList<>();
    // A map from username, to a list of messages intended for that user
    private final ConcurrentHashMap<String, List<Message>> m_userMessages = new ConcurrentHashMap<>();
    // UNIX Timestamp in milliseconds, indicating when the last user joined/left
    private volatile long m_lastUserUpdate = 0;
    
    /**
     * Allows a new client to join the chat server
     * @param user the username of the client that wishes to join
     * @return true if the username is not taken, false otherwise
     */
    @WebMethod(operationName = "join")
    public boolean join(@WebParam(name = "user") String user) {
        // We will obtain a write lock instantly, as if the username is not taken
        // we would have to release the read lock before obtaining the write lock.
        // In that case, someone may take the username during the lock swap and this 
        // would result in two people with the same name.
        rwl_clients.writeLock().lock();
        
        try {
            // If there is an existing connected client with the provided name
            // Do not allow the user to join with the provided name
            if(m_clients.contains(user)) {
                return false;
            } else {
                m_clients.add(user);

                rwl_messages.writeLock().lock();
                try {
                    m_userMessages.put(user, new ArrayList<>());
                } finally {
                    rwl_messages.writeLock().unlock();
                }

                // The user list has changed
                m_lastUserUpdate = System.currentTimeMillis();
                return true;
            }
        } finally {
            rwl_clients.writeLock().unlock();
        }
        
    }
    
    /**
     * Allows a connected client to leave the server
     * @param user The username of the client that is leaving the chat
     */
    @WebMethod(operationName = "leave")
    @Oneway
    public void leave(@WebParam(name = "user") String user) {
        // Obtain the write lock before attempting to remove the user
        rwl_clients.writeLock().lock();
        
        try {
            // Only remove the client, if a client with the provided name exists
            if(m_clients.remove(user)) {
                
                rwl_messages.writeLock().lock();
                try {
                    m_userMessages.remove(user);
                } finally {
                    rwl_messages.writeLock().unlock();
                }
                
                // The user list has changed
                m_lastUserUpdate = System.currentTimeMillis();
            }
        } finally {
            rwl_clients.writeLock().unlock();
        }
    }
    
    /**
     * Allows a connected client to send a message to other clients
     * @param message A message object specifying the receiver, sender, message, and time
     */
    @WebMethod(operationName = "sendMessage")
    @Oneway
    public void sendMessage(@WebParam(name = "message") final Message message) {
        // If the receiver is null, this means the message is intended for all
        // connected users
        if(message.getReceiver() == null) {
            // We are only reading from the client list
            rwl_clients.readLock().lock();
            // We will be adding new messages to the message map
            rwl_messages.writeLock().lock();
            
            try {
                 // Provide the message to all connected users
                for(String client : m_clients) {
                    m_userMessages.get(client).add(message);
                }
            } finally {
                rwl_messages.writeLock().unlock();
                rwl_clients.readLock().unlock();
            }
            
        // Otherwise this is a personal message
        } else {
            rwl_clients.readLock().lock();
            try {
                // Validate that the intended recipient of the message exists
                if(m_clients.contains(message.getReceiver())) {
                    rwl_messages.writeLock().lock();
                    
                    try {
                        // Provide the message to the receiver
                        m_userMessages.get(message.getReceiver()).add(message);
                        // Also send the message back to the sender, so they know
                        // the message was successfully received by the server
                        m_userMessages.get(message.getSender()).add(message);
                    } finally {
                        rwl_messages.writeLock().unlock();
                    }
                }
            } finally {
                rwl_clients.readLock().unlock();
            }
        }
    }

    /**
     * Obtains all messages intended for the specified user
     * @param user The recipient of the messages you wish to receive
     * @return A list of messages for the specified user
     */
    @WebMethod(operationName = "getMessages")
    public List<Message> getMessages(@WebParam(name = "user") String user) {
        rwl_clients.readLock().lock();
        
        try {
            // Ensure that the specified user exists
            if(m_clients.contains(user)) {
                rwl_messages.writeLock().lock();
                try {
                    // Make a copy of the message list before emptying it
                    List<Message> messages = new ArrayList<>(m_userMessages.get(user));
                    // Empty the message list for this user, to avoid sending the same
                    // messages more than once
                    m_userMessages.get(user).clear();
                    
                    return messages;
                } finally {
                    rwl_messages.writeLock().unlock();
                }
            } else {
                return null;
            }
        } finally {
            rwl_clients.readLock().unlock();
        }
    }
    
    /**
     * Gets the list of connected clients
     * @return A list of the usernames of the connected clients
     */
    @WebMethod(operationName = "getClientList")
    public List<String> getClientList() {
        rwl_clients.readLock().lock();
        try {
            return m_clients;
        } finally {
            rwl_clients.readLock().unlock();
        }
        
    }

    /**
     * Gets a UNIX timestamp in milliseconds of when a user most recently joined
     * or left the chat. This is useful if you wish to only update something
     * in the event that the list of clients has changed, without the need to
     * query for the list of clients.
     * @return timestamp of when the user list most recently changed
     */
    @WebMethod(operationName = "getLastUserUpdate")
    public long getLastUserUpdate() {
        return m_lastUserUpdate;
    }
}
