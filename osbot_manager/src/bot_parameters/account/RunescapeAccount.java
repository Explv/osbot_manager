package bot_parameters.account;

import bot_parameters.interfaces.BotParameter;
import bot_parameters.interfaces.Copyable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RunescapeAccount extends Account implements BotParameter, Copyable<RunescapeAccount> {

    private static final long serialVersionUID = 4730705293691635049L;

    private SimpleIntegerProperty pin;
    private SimpleBooleanProperty isBanned;

    public RunescapeAccount(final String username, final String password, final int pin) {
        super(username, password);
        this.pin = new SimpleIntegerProperty(pin);
        this.isBanned = new SimpleBooleanProperty();
    }

    public final int getPin() {
        return pin.get();
    }

    public final void setPin(final int pin) {
        this.pin = new SimpleIntegerProperty(pin);
    }

    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.writeObject(getUsername());
        stream.writeObject(getPassword());
        stream.writeInt(getPin());
        stream.writeBoolean(isBanned());
    }

    private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
        username = new SimpleStringProperty((String) stream.readObject());
        password = new SimpleStringProperty((String) stream.readObject());
        pin = new SimpleIntegerProperty(stream.readInt());
        try {
            isBanned = new SimpleBooleanProperty(stream.readBoolean());
        } catch (Exception e) {
            isBanned = new SimpleBooleanProperty();
        }
    }

    @Override
    public final String toParameterString() {
        return String.format("-bot \"%s:%s:%d\"", getUsername(), getPassword(), pin.get());
    }

    @Override
    public RunescapeAccount createCopy() {
        return new RunescapeAccount(getUsername(), getPassword(), getPin());
    }

    public boolean isBanned() {
        return isBanned.get();
    }

    public void checkIsBanned() {
        HttpClient httpClient = HttpClientBuilder.create().build();

        Optional<HttpResponse> loginResponse = login(httpClient);

        if (!loginResponse.isPresent()) {
            return;
        }

        Optional<HttpResponse> accountSettingsResponse = getAccountSettings(httpClient, loginResponse.get());

        if (!accountSettingsResponse.isPresent()) {
            return;
        }

        Optional<HttpResponse> accountHistoryResponse = getAccountStatus(httpClient, accountSettingsResponse.get());

        if (!accountHistoryResponse.isPresent()) {
            return;
        }

        this.isBanned.set(isAccountStatusBanned(accountHistoryResponse.get()));
    }

    private Optional<HttpResponse> login(final HttpClient httpClient) {
        HttpPost loginRequest = new HttpPost("https://secure.runescape.com/m=weblogin/login.ws");

        loginRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        loginRequest.addHeader("Accept-Encoding", "gzip, deflate, br");
        loginRequest.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
        loginRequest.addHeader("Cache-Control", "max-age=0");
        loginRequest.addHeader("Connection", "keep-alive");
        loginRequest.addHeader("Content-Type", "application/x-www-form-urlencoded");
        loginRequest.addHeader("Host", "secure.runescape.com");
        loginRequest.addHeader("Origin", "https://secure.runescape.com");
        loginRequest.addHeader("Referer", "https://secure.runescape.com/m=weblogin/loginform.ws?mod=www&ssl=1&expired=0&dest=account_settings.ws");
        loginRequest.addHeader("Upgrade-Insecure-Requests", "1");
        loginRequest.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

        List<NameValuePair> loginParameters = new ArrayList<>();
        loginParameters.add(new BasicNameValuePair("username", getUsername()));
        loginParameters.add(new BasicNameValuePair("password", getPassword()));
        loginParameters.add(new BasicNameValuePair("mod", "www"));
        loginParameters.add(new BasicNameValuePair("ssl", "1"));
        loginParameters.add(new BasicNameValuePair("dest", "account_settings.ws"));

        try {
            loginRequest.setEntity(new UrlEncodedFormEntity(loginParameters));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }

        try {
            return Optional.of(httpClient.execute(loginRequest));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<HttpResponse> getAccountSettings(final HttpClient httpClient, final HttpResponse loginResponse) {
        HttpGet accountSettingsGetRequest = new HttpGet(loginResponse.getFirstHeader("Location").getValue());
        try {
            return Optional.of(httpClient.execute(accountSettingsGetRequest));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<HttpResponse> getAccountStatus(final HttpClient httpClient, final HttpResponse accountSettingsResponse) {
        Pattern accountHistoryLinkPattern = Pattern.compile("src=\"([^\"]+account_history.ws)\"");

        try (BufferedReader settingsReader = new BufferedReader(new InputStreamReader(accountSettingsResponse.getEntity().getContent()))) {
            String settingsLine;
            while ((settingsLine = settingsReader.readLine()) != null) {
                Matcher accountHistoryLinkMatcher = accountHistoryLinkPattern.matcher(settingsLine);
                if (accountHistoryLinkMatcher.find()) {
                    String accountHistoryLink = accountHistoryLinkMatcher.group(1);

                    HttpGet accountStatusRequest = new HttpGet(accountHistoryLink);
                    return Optional.of(httpClient.execute(accountStatusRequest));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }



    private boolean isAccountStatusBanned(final HttpResponse accountStatusResponse) {
        try (BufferedReader accountStatusReader = new BufferedReader(new InputStreamReader(accountStatusResponse.getEntity().getContent()))) {
            String accountStatusLine;
            while ((accountStatusLine = accountStatusReader.readLine()) != null) {
                if (accountStatusLine.contains("<b>Banned:</b>")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
