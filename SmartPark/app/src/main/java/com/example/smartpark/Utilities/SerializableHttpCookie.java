package com.example.smartpark.Utilities;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpCookie;

//This class is not mine. Credit goes to saiaspire on GitHub. Repo link: https://gist.github.com/saiaspire/d02e1c9dc332db7b5305

public class SerializableHttpCookie implements Serializable {
    private static final long serialVersionUID = -6051428667568260064L;

    private transient HttpCookie cookie;

    public SerializableHttpCookie(HttpCookie cookie) {
        this.cookie = cookie;
    }

    public HttpCookie getCookie() {
        return cookie;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(cookie.getName());
        out.writeObject(cookie.getValue());
        out.writeObject(cookie.getComment());
        out.writeObject(cookie.getCommentURL());
        out.writeBoolean(cookie.getDiscard());
        out.writeObject(cookie.getDomain());
        out.writeLong(cookie.getMaxAge());
        out.writeObject(cookie.getPath());
        out.writeObject(cookie.getPortlist());
        out.writeBoolean(cookie.getSecure());
        out.writeInt(cookie.getVersion());
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        cookie = new HttpCookie(name, value);
        cookie.setComment((String) in.readObject());
        cookie.setCommentURL((String) in.readObject());
        cookie.setDiscard(in.readBoolean());
        cookie.setDomain((String) in.readObject());
        cookie.setMaxAge(in.readLong());
        cookie.setPath((String) in.readObject());
        cookie.setPortlist((String) in.readObject());
        cookie.setSecure(in.readBoolean());
        cookie.setVersion(in.readInt());
    }
}
