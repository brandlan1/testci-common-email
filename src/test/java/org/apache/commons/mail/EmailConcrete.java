package org.apache.commons.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailConcrete extends Email {

    // Keep internal storage for testing
    private InternetAddress from;
    private final Map<String, String> headers = new HashMap<>();
    private final List<InternetAddress> toList = new ArrayList<>();
    private final List<InternetAddress> bccList = new ArrayList<>();
    private final List<InternetAddress> ccList = new ArrayList<>();
    private final List<InternetAddress> replyToList = new ArrayList<>();
    private String msg;
    private String hostName;
    private Date sentDate;

    @Override
    public void setFrom(InternetAddress address) throws EmailException {
        this.from = address;
    }

    @Override
    public void addReplyTo(String email, String name) throws EmailException {
        try {
            this.replyToList.add(new InternetAddress(email, name));
        } catch (Exception e) {
            throw new EmailException(e);
        }
    }

    @Override
    public void addHeader(String name, String value) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name can not be null or empty");
        }
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value cannot be null or empty");
        }
        headers.put(name, value);
    }

    @Override
    public void addBcc(String... emails) throws EmailException {
        for (String email : emails) {
            try {
                bccList.add(new InternetAddress(email));
            } catch (Exception e) {
                throw new EmailException(e);
            }
        }
    }

    @Override
    public void addCc(String email) throws EmailException {
        try {
            ccList.add(new InternetAddress(email));
        } catch (Exception e) {
            throw new EmailException(e);
        }
    }

    @Override
    public void setMsg(String msg) throws EmailException {
        this.msg = msg;
    }

    @Override
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public void setSentDate(Date date) {
        this.sentDate = date;
    }

    @Override
    public Date getSentDate() {
        return sentDate;
    }

    @Override
    public InternetAddress getFromAddress() {
        return from;
    }

    @Override
    public void buildMimeMessage() throws EmailException {
        // For testing, just create an empty MimeMessage if host & from are set
        if (from == null) {
            throw new EmailException("From address required");
        }
        if (toList.isEmpty()) {
            throw new EmailException("receiver address required");
        }
        try {
            Session session = Session.getDefaultInstance(System.getProperties());
            MimeMessage msgObj = new MimeMessage(session);
            this.setMimeMessage(msgObj);
        } catch (Exception e) {
            throw new EmailException(e);
        }
    }

    // Helper getters for test assertions
    public List<InternetAddress> getBccAddresses() {
        return bccList;
    }

    public List<InternetAddress> getCcAddresses() {
        return ccList;
    }

    public List<InternetAddress> getReplyToAddresses() {
        return replyToList;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMsg() {
        return msg;
    }

    public void addTo(String email) throws EmailException {
        try {
            toList.add(new InternetAddress(email));
        } catch (Exception e) {
            throw new EmailException(e);
        }
    }
}
