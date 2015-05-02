
package chatserver.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "join", namespace = "http://chatserver/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "join", namespace = "http://chatserver/")
public class Join {

    @XmlElement(name = "user", namespace = "")
    private String user;

    /**
     * 
     * @return
     *     returns String
     */
    public String getUser() {
        return this.user;
    }

    /**
     * 
     * @param user
     *     the value for the user property
     */
    public void setUser(String user) {
        this.user = user;
    }

}
