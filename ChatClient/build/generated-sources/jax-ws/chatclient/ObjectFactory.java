
package chatclient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the chatclient package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetClientList_QNAME = new QName("http://chatserver/", "getClientList");
    private final static QName _GetMessages_QNAME = new QName("http://chatserver/", "getMessages");
    private final static QName _GetMessagesResponse_QNAME = new QName("http://chatserver/", "getMessagesResponse");
    private final static QName _GetClientListResponse_QNAME = new QName("http://chatserver/", "getClientListResponse");
    private final static QName _GetLastUserUpdate_QNAME = new QName("http://chatserver/", "getLastUserUpdate");
    private final static QName _SendMessage_QNAME = new QName("http://chatserver/", "sendMessage");
    private final static QName _Join_QNAME = new QName("http://chatserver/", "join");
    private final static QName _JoinResponse_QNAME = new QName("http://chatserver/", "joinResponse");
    private final static QName _Leave_QNAME = new QName("http://chatserver/", "leave");
    private final static QName _GetLastUserUpdateResponse_QNAME = new QName("http://chatserver/", "getLastUserUpdateResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: chatclient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetLastUserUpdateResponse }
     * 
     */
    public GetLastUserUpdateResponse createGetLastUserUpdateResponse() {
        return new GetLastUserUpdateResponse();
    }

    /**
     * Create an instance of {@link Leave }
     * 
     */
    public Leave createLeave() {
        return new Leave();
    }

    /**
     * Create an instance of {@link SendMessage }
     * 
     */
    public SendMessage createSendMessage() {
        return new SendMessage();
    }

    /**
     * Create an instance of {@link Join }
     * 
     */
    public Join createJoin() {
        return new Join();
    }

    /**
     * Create an instance of {@link JoinResponse }
     * 
     */
    public JoinResponse createJoinResponse() {
        return new JoinResponse();
    }

    /**
     * Create an instance of {@link GetClientListResponse }
     * 
     */
    public GetClientListResponse createGetClientListResponse() {
        return new GetClientListResponse();
    }

    /**
     * Create an instance of {@link GetClientList }
     * 
     */
    public GetClientList createGetClientList() {
        return new GetClientList();
    }

    /**
     * Create an instance of {@link GetMessages }
     * 
     */
    public GetMessages createGetMessages() {
        return new GetMessages();
    }

    /**
     * Create an instance of {@link GetMessagesResponse }
     * 
     */
    public GetMessagesResponse createGetMessagesResponse() {
        return new GetMessagesResponse();
    }

    /**
     * Create an instance of {@link GetLastUserUpdate }
     * 
     */
    public GetLastUserUpdate createGetLastUserUpdate() {
        return new GetLastUserUpdate();
    }

    /**
     * Create an instance of {@link Message }
     * 
     */
    public Message createMessage() {
        return new Message();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetClientList }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://chatserver/", name = "getClientList")
    public JAXBElement<GetClientList> createGetClientList(GetClientList value) {
        return new JAXBElement<GetClientList>(_GetClientList_QNAME, GetClientList.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessages }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://chatserver/", name = "getMessages")
    public JAXBElement<GetMessages> createGetMessages(GetMessages value) {
        return new JAXBElement<GetMessages>(_GetMessages_QNAME, GetMessages.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessagesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://chatserver/", name = "getMessagesResponse")
    public JAXBElement<GetMessagesResponse> createGetMessagesResponse(GetMessagesResponse value) {
        return new JAXBElement<GetMessagesResponse>(_GetMessagesResponse_QNAME, GetMessagesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetClientListResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://chatserver/", name = "getClientListResponse")
    public JAXBElement<GetClientListResponse> createGetClientListResponse(GetClientListResponse value) {
        return new JAXBElement<GetClientListResponse>(_GetClientListResponse_QNAME, GetClientListResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLastUserUpdate }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://chatserver/", name = "getLastUserUpdate")
    public JAXBElement<GetLastUserUpdate> createGetLastUserUpdate(GetLastUserUpdate value) {
        return new JAXBElement<GetLastUserUpdate>(_GetLastUserUpdate_QNAME, GetLastUserUpdate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://chatserver/", name = "sendMessage")
    public JAXBElement<SendMessage> createSendMessage(SendMessage value) {
        return new JAXBElement<SendMessage>(_SendMessage_QNAME, SendMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Join }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://chatserver/", name = "join")
    public JAXBElement<Join> createJoin(Join value) {
        return new JAXBElement<Join>(_Join_QNAME, Join.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link JoinResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://chatserver/", name = "joinResponse")
    public JAXBElement<JoinResponse> createJoinResponse(JoinResponse value) {
        return new JAXBElement<JoinResponse>(_JoinResponse_QNAME, JoinResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Leave }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://chatserver/", name = "leave")
    public JAXBElement<Leave> createLeave(Leave value) {
        return new JAXBElement<Leave>(_Leave_QNAME, Leave.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetLastUserUpdateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://chatserver/", name = "getLastUserUpdateResponse")
    public JAXBElement<GetLastUserUpdateResponse> createGetLastUserUpdateResponse(GetLastUserUpdateResponse value) {
        return new JAXBElement<GetLastUserUpdateResponse>(_GetLastUserUpdateResponse_QNAME, GetLastUserUpdateResponse.class, null, value);
    }

}
