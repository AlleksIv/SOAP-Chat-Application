package chatserver;

/**
 * Message
 * Stores a single message that is to be sent to a user. If the receiver specified
 * is null, this signifies that the message is to be sent to all connected clients.
 * 
 * @author Nick
 */
public class Message {
    private String sender;
    private String receiver;
    private String message;
    // Unix timestamp in milliseconds of when the message was sent
    private long timestamp;
    
    public Message() {
        
    }
    
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
