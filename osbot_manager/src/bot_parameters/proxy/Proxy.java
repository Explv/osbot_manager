package bot_parameters.proxy;

import bot_parameters.interfaces.BotParameter;
import bot_parameters.interfaces.Copyable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Proxy implements BotParameter, Copyable<Proxy>, Serializable {

    private static final long serialVersionUID = -6367454010350132779L;

    SimpleStringProperty ipAddress;
    SimpleIntegerProperty port;

    public Proxy(final String ipAddress, final int port) {
        this.ipAddress = new SimpleStringProperty(ipAddress);
        this.port = new SimpleIntegerProperty(port);
    }

    public final String getIpAddress() {
        return ipAddress.get();
    }

    public final int getPort() {
        return port.get();
    }

    public final void setIP(final String ip) {
        this.ipAddress.set(ip);
    }

    public final void setPort(final int port) { this.port.set(port); }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(getIpAddress());
        stream.writeInt(getPort());
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        ipAddress = new SimpleStringProperty((String) stream.readObject());
        port = new SimpleIntegerProperty(stream.readInt());
    }

    @Override
    public String[] toParameter() {
        return new String[] { "-proxy", String.format("%s:%d", ipAddress.get(), port.get()) };
    }

    @Override
    public String toString() {
        return ipAddress.get() + ":" + port.get();
    }

    @Override
    public Proxy createCopy() {
        return new Proxy(getIpAddress(), getPort());
    }
}
