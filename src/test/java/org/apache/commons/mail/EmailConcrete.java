package org.apache.commons.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailConcrete extends Email {

    private String from;
    private final Map<String, String> headers = new HashMap<>();
    private final List<InternetAddress> toList = new ArrayList<>();
    private final List<InternetAddress> bccList = new ArrayList<>();
    private final List<InternetAddress> ccList = new ArrayList<>();
    private final List<InternetAddress> replyToList = new ArrayList<>();
    private String msg;
    private String hostName;
    private Date sentDate;
    private MimeMessage mimeMessage;
    private Session mailSession;

    @Override
    public Email setFrom(String email) throws EmailException {
        this.from = email;
        return this;
    }

    @Override
    public Email addTo(String email) throws EmailException {
        try {
            toList.add(new InternetAddress(email));
        } catch (Exception e) {
            throw new EmailException(e);
        }
        return this;
    }

    @Override
    public Email addBcc(String... emails) throws EmailException {
        for (String email : emails) {
            try {
                bccList.add(new InternetAddress(email));
            } catch (Exception e) {
                throw new EmailException(e);
            }
        }
        return this;
    }

    @Override
    public Email addCc(String email) throws EmailException {
        try {
            ccList.add(new InternetAddress(email));
        } catch (Exception e) {
            throw new EmailException(e);
        }
        return this;
    }

    @Override
    public Email addReplyTo(String email, String name) throws EmailException {
        try {
            replyToList.add(new InternetAddress(email, name));
        } catch (Exception e) {
            throw new EmailException(e);
        }
        return this;
    }

    @Override
    public Email addHeader(String name, String value) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name can not be null or empty");
        }
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("value cannot be null or empty");
        }
        headers.put(name, value);
        return this;
    }

    @Override
    public Email setMsg(String msg) throws EmailException {
        this.msg = msg;
        return this;
    }

    @Override
    public Email setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public Session getMailSession() throws EmailException {
        if (mailSession != null) {
            return mailSession;
        }
        if (hostName == null || hostName.isEmpty()) {
            throw new EmailException("hostname must be set");
        }
        Properties props = new Properties();
        props.put("mail.smtp.host", hostName);
        mailSession = Session.getInstance(props);
        return mailSession;
    }

    @Override
    public Email buildMimeMessage() throws EmailException {
        if (mimeMessage != null) {
            throw new IllegalStateException("already built");
        }
        if (from == null || from.isEmpty()) {
            throw new EmailException("From address required");
        }
        if (toList.isEmpty()) {
            throw new EmailException("receiver address required");
        }
        try {
            mimeMessage = new MimeMessage(getMailSession());
            mimeMessage.setFrom(new InternetAddress(from));
            for (InternetAddress to : toList) {
                mimeMessage.addRecipient(MimeMessage.RecipientType.TO, to);
            }
            for (InternetAddress cc : ccList) {
                mimeMessage.addRecipient(MimeMessage.RecipientType.CC, cc);
            }
            for (InternetAddress bcc : bccList) {
                mimeMessage.addRecipient(MimeMessage.RecipientType.BCC, bcc);
            }
            if (!replyToList.isEmpty()) {
                mimeMessage.setReplyTo(replyToList.toArray(new InternetAddress[0]));
            }
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                mimeMessage.setHeader(entry.getKey(), entry.getValue());
            }
            mimeMessage.setSentDate(sentDate != null ? sentDate : new Date());
            if (msg != null) {
                mimeMessage.setText(msg);
            }
        } catch (Exception e) {
            throw new EmailException(e);
        }
        return this;
    }

    @Override
    public Email setSentDate(Date date) {
        this.sentDate = date;
        return this;
    }

    @Override
    public Date getSentDate() {
        return sentDate;
    }

    // Getters for test assertions
    public String getFromAddress() {
        return from;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public List<InternetAddress> getBccAddresses() {
        return bccList;
    }

    public List<InternetAddress> getCcAddresses() {
        return ccList;
    }

    public List<InternetAddress> getReplyToAddresses() {
        return replyToList;
    }

    public List<InternetAddress> getToAddresses() {
        return toList;
    }

    public String getMsg() {
        return msg;
    }

    public MimeMessage getMimeMessage() {
        return mimeMessage;
    }
}
