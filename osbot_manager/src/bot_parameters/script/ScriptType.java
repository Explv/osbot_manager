package bot_parameters.script;

import java.io.Serializable;

public enum ScriptType implements Serializable {
    LOCAL,
    SDN;

    @Override
    public String toString() {
        char[] name = name().toLowerCase().toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        return new String(name);
    }
}
