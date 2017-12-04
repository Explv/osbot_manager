package bot_parameters.bot;

import bot_parameters.interfaces.BotParameter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class Bot implements BotParameter, Serializable {

    private static final long serialVersionUID = 3351956474698861300L;

    private String osbotPath;

    public final String getOsbotPath() {
        return osbotPath;
    }

    public final void setOsbotPath(final String osbotPath) {
        this.osbotPath = osbotPath;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(getOsbotPath());
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        setOsbotPath((String) stream.readObject());
    }

    @Override
    public String toParameterString() {
        return "java -jar " + osbotPath;
    }
}
