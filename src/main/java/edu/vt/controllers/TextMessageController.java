package edu.vt.controllers;

import edu.vt.globals.Methods;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


// Text Message Controller
@Named(value = "textMessageController")

@RequestScoped

/**
 * This class sends a Multimedia Messaging Service (MMS) Text Message to a cellular (mobile) phone.
 */
public class TextMessageController {

    Properties emailServerProperties;   // java.util.Properties
    Session emailSession;               // javax.mail.Session
    MimeMessage mimeEmailMessage;       // javax.mail.internet.MimeMessage


    // Instance Variables (Properties)

    private String cellPhoneNumber;         // Cell phone number to which MMS Text Message to be sent.
    private String cellPhoneCarrierDomain;  // Cell phone carrier company's MMS gateway domain name.
    private String mmsTextMessage;          // MMS text message content.

    // Constructor Method
    public TextMessageController() {
    }

    // Getter and Setter Methods
    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String enteredCellPhoneNumber) {
        this.cellPhoneNumber = enteredCellPhoneNumber.replaceAll("[^0-9.]", "");
    }

    public String getCellPhoneCarrierDomain() {
        return cellPhoneCarrierDomain;
    }

    public void setCellPhoneCarrierDomain(String cellPhoneCarrierDomain) {
        this.cellPhoneCarrierDomain = cellPhoneCarrierDomain;
    }

    public String getMmsTextMessage() {
        return mmsTextMessage;
    }

    public void setMmsTextMessage(String mmsTextMessage) {
        this.mmsTextMessage = mmsTextMessage;
    }

    // Instance Methods
    public void clearTextMessage() {
        cellPhoneNumber = "";
        cellPhoneCarrierDomain = null;
        mmsTextMessage = "";
    }

    // Create Email Sesion and Transport Email in Plain Text Format

    public void sendTextMessage() throws AddressException, MessagingException {

        // Set Email Server Properties
        emailServerProperties = System.getProperties();
        emailServerProperties.put("mail.smtp.port", "587");
        emailServerProperties.put("mail.smtp.auth", "true");
        emailServerProperties.put("mail.smtp.starttls.enable", "true");

        try {
            // Create an email session using the email server properties set above
            emailSession = Session.getDefaultInstance(emailServerProperties, null);

            /*
            Create a Multi-purpose Internet Mail Extensions (MIME) style email
            message from the MimeMessage class under the email session created.
             */
            mimeEmailMessage = new MimeMessage(emailSession);



            mimeEmailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(cellPhoneNumber + "@" + cellPhoneCarrierDomain));

            /*
             Since some cell phones may not be able to process text messages in the HTML format,
             send the email message containing the text message in Plain Text format.
             */
            mimeEmailMessage.setContent(mmsTextMessage, "text/plain");

            // Create a transport object that implements the Simple Mail Transfer Protocol (SMTP)
            Transport transport = emailSession.getTransport("smtp");

            /*
            Connect to Gmail's SMTP server using the username and password provided.
            For the Gmail's SMTP server to accept the unsecure connection, the
            Cloud.Software.Email@gmail.com account's "Allow less secure apps" option is set to ON.
             */
            transport.connect("smtp.gmail.com", "mayurdhepe@gmail.com", "zjmavuzvrphglmsq");

            // Send the email message containing the text message to the specified email address
            transport.sendMessage(mimeEmailMessage, mimeEmailMessage.getAllRecipients());

            // Close this service and terminate its connection
            transport.close();

            //Methods.showMessage("Information", "Success!", "MMS Text Message is Sent!");
            clearTextMessage();

        } catch (AddressException ae) {
            Methods.showMessage("Fatal Error", "Email Address Exception Occurred!",
                    "See: " + ae.getMessage());

        } catch (MessagingException me) {
            Methods.showMessage("Fatal Error",
                    "Email Messaging Exception Occurred! Internet Connection Required!",
                    "See: " + me.getMessage());
        }
    }

}
