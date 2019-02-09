package bot_parameters.configuration;

import java.io.Serializable;

public enum WorldType implements Serializable {
    F2P("F2P"),
    MEMBERS("Members");

    private String name;

    WorldType(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
