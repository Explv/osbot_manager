package bot_parameters.configuration;

import bot_parameters.interfaces.BotParameter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class World implements BotParameter, Serializable {
    private static final long serialVersionUID = -9046100616950752889L;

    private static List<World> worlds;

    private static Comparator<World> worldComparator = (w1, w2) -> {
        int typeComparison = w1.getType().compareTo(w2.getType());

        if (typeComparison != 0) {
            return typeComparison;
        }

        return Integer.compare(w1.getNumber(), w2.getNumber());
    };

    private WorldType type;
    private int number;
    private String detail;

    public World(final WorldType type, final int number, final String detail) {
        this.type = type;
        this.number = number;
        this.detail = detail;
    }

    public final WorldType getType() {
        return type;
    }

    public final int getNumber() {
        return number;
    }

    public final String getDetail() {
        return detail;
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(getType());
        stream.writeInt(getNumber());
        stream.writeObject(getDetail());
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        type = (WorldType) stream.readObject();
        number = stream.readInt();
        detail = (String) stream.readObject();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof World)) {
            return false;
        }

        World otherWorld = (World) other;

        return getType() == otherWorld.getType() && getNumber() == otherWorld.getNumber();
    }

    @Override
    public String toString() {
        String worldStr = getType().toString() + " " + getNumber();

        if (!getDetail().isEmpty()) {
            worldStr += " - " + getDetail();
        }

        return worldStr;
    }

    public static List<World> getWorlds() {
        if (worlds == null) {
            loadWorlds();
        }
        return worlds;
    }

    public static Comparator<World> getWorldComparator() {
        return worldComparator;
    }

    private static void loadWorlds() {
        worlds = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("http://oldschool.runescape.com/slu").get();
            Elements servers = doc.select("tr.server-list__row");
            for (Element server : servers) {
                Element serverLink = server.selectFirst(".server-list__world-link");
                String worldIDStr = serverLink.id().replaceAll("slu-world-", "");
                int worldNum = Integer.parseInt(worldIDStr);

                Element membershipType = server.selectFirst(".server-list__row-cell--type");
                boolean members = membershipType.html().equals("Members");

                WorldType worldType = members ? WorldType.MEMBERS : WorldType.F2P;

                String worldDetail = membershipType.nextElementSibling().html();

                if (worldDetail.equals("-")) {
                    worldDetail = "";
                }

                worlds.add(new World(worldType, worldNum, worldDetail));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        worlds.sort(worldComparator);
    }

    @Override
    public String[] toParameter() {
        return new String[] { "-world",  String.valueOf(getNumber()) };
    }
}
