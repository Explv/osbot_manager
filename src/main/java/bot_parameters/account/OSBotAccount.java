package bot_parameters.account;

import bot_parameters.interfaces.BotParameter;

public final class OSBotAccount extends Account implements BotParameter {

    private static final long serialVersionUID = 3206668253057580659L;

    private static final OSBotAccount instance = new OSBotAccount();

    private OSBotAccount(){}

    public static OSBotAccount getInstance() {
        return instance;
    }

    @Override
    public final String[] toParameter() {
        return new String[]{ "-login", String.format("\"%s:%s\"", getUsername(), getPassword()) };
    }
}
