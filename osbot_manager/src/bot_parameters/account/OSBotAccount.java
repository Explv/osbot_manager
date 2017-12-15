package bot_parameters.account;

import bot_parameters.interfaces.BotParameter;
import bot_parameters.interfaces.Copyable;

public final class OSBotAccount extends Account implements BotParameter, Copyable<OSBotAccount> {

    private static final long serialVersionUID = 3206668253057580659L;

    public OSBotAccount(final String username, final String password) {
        super(username, password);
    }

    @Override
    public final String[] toParameter() {
        return new String[]{ "-login", String.format("%s:%s", getUsername(), getPassword()) };
    }

    @Override
    public OSBotAccount createCopy() {
        return new OSBotAccount(getUsername(), getPassword());
    }
}
