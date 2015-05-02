
package chatserver.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import chatserver.Message;

@XmlRootElement(name = "sendMessage", namespace = "http://chatserver/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendMessage", namespace = "http://chatserver/")
public class SendMessage {

    @XmlElement(name = "message", namespace = "")
    private Message message;

    /**
     * 
     * @return
     *     returns Message
     */
    public Message getMessage() {
        return this.message;
    }

    /**
     * 
     * @param message
     *     the value for the message property
     */
    public void setMessage(Message message) {
        this.message = message;
    }

}
