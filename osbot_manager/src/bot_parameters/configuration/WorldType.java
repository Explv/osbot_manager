package bot_parameters.configuration;

import java.io.Serializable;

public enum WorldType implements Serializable {
    F2P (301, 308, 316, 326, 335, 382, 383, 384, 393, 394),
    MEMBERS (302, 303, 304, 305, 306, 309, 310, 311, 312, 313, 314, 317, 318, 319, 320,
            321, 322, 327, 328, 329, 330, 333, 334, 336, 338, 341, 342, 343, 344,
            346, 350, 351, 352, 354, 357, 358, 359, 360, 362, 365,
            367, 368, 369, 370, 375, 376, 377, 386),
    PVP (325, 337),
    DEADMAN (345, 374, 378);

    public final Integer[] worlds;

    WorldType(final Integer... worlds) {
        this.worlds= worlds;
    }

    @Override
    public String toString() {
        char[] name = name().toLowerCase().toCharArray();
        name[0] = Character.toUpperCase(name[0]);
        return new String(name);
    }
}
