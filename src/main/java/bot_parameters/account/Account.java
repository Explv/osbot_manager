package bot_parameters.account;

import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class Account implements Serializable {

    private static final long serialVersionUID = 7601621618280835775L;

    protected SimpleStringProperty username, password;

    public Account() {
        this.username = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
    }

    public Account(final String username, final String password) {
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }

    public final String getUsername() {
        return username.get();
    }

    public final void setUsername(String username) {
        this.username.set(username);
    }

    public final String getPassword() {
        return password.get();
    }

    public final void setPassword(String password) {
        this.password.set(password);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(getUsername());
        stream.writeObject(getPassword());
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        username = new SimpleStringProperty((String) stream.readObject());
        password = new SimpleStringProperty((String) stream.readObject());
    }

    @Override
    public final String toString() {
        return getUsername();
    }
}
