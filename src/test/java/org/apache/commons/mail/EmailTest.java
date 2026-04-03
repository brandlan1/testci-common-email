package org.apache.commons.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;

public class EmailConcrete extends Email {

    // Store from address
    private String from;
    private final Map<String, String> headers = new HashMap<>();
    private final List<InternetAddress> toList = new ArrayList<>();
    private final List<InternetAddress> bccList = new ArrayList<>();
    private final List<InternetAddress> ccList = new ArrayList<>();
    private final List<InternetAddress> replyToList = new ArrayList<>();
    private String msg;
    private String hostName;

    @Override
    public Email setFrom(String email) throws EmailException {
        this.from = email;
        return this;
    }

    @Override
    public Email addReplyTo(String email, String name) throws EmailException {
        try {
            this.replyToList.add(new InternetAddress(email, name));
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

    // Getters to satisfy EmailTest assertions
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

    public String getMsg() {
        return msg;
    }
}


