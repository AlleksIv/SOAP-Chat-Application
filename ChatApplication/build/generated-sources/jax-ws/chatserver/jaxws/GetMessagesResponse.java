
package chatserver.jaxws;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import chatserver.Message;

@XmlRootElement(name = "getMessagesResponse", namespace = "http://chatserver/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getMessagesResponse", namespace = "http://chatserver/")
public class GetMessagesResponse {

    @XmlElement(name = "return", namespace = "")
    private List<Message> _return;

    /**
     * 
     * @return
     *     returns List<Message>
     */
    public List<Message> getReturn() {
        return this._return;
    }

    /**
     * 
     * @param _return
     *     the value for the _return property
     */
    public void setReturn(List<Message> _return) {
        this._return = _return;
    }

}
