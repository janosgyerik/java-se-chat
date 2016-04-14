package com.janosgyerik.examples.stackexchange.chat;

import com.gistlabs.mechanize.Resource;
import com.gistlabs.mechanize.document.html.HtmlDocument;
import com.gistlabs.mechanize.document.html.HtmlElement;
import com.gistlabs.mechanize.impl.MechanizeAgent;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Browser {

    public static final String SE_OPENID = "https://openid.stackexchange.com";
    public static final String SE_OPENID_LOGIN_ROOT = SE_OPENID + "/account/login";
    public static final String SE_LOGIN_ROOT = "http://stackexchange.com/users";

    public static final String CHAT_ROOT = "http://chat.stackexchange.com";
    public static final String KEY_NAME = "fkey";

    private final MechanizeAgent agent;
    private String chatKey;

    public Browser() {
        agent = new MechanizeAgent();

        // Note: very shaky!
        // AbstractHttpClient is deprecated as of version 4.4.1 of org.apache.httpcomponents:httpclient
        // The custom redirect strategy is needed, because of the 302 redirect in SE_OPENID_LOGIN_ROOT/submit
        AbstractHttpClient client = agent.getClient();
        client.setRedirectStrategy(new LaxRedirectStrategy());
    }

    protected String getKey(String url) {
        HtmlDocument page = agent.get(url);
        HtmlElement keyField = page.find("input[name=\"fkey\"]");
        return keyField.getAttribute("value");
    }

    public void loginToStackExchange(String email, String password) throws UnsupportedEncodingException {
        String keyURL = SE_OPENID_LOGIN_ROOT;
        String postURL = SE_OPENID_LOGIN_ROOT + "/submit";

        Map<String, String> data = new HashMap<>();
        data.put("email", email);
        data.put("password", password);
        data.put(KEY_NAME, getKey(keyURL));

        agent.post(postURL, data);
    }

    public void loginToSite() throws UnsupportedEncodingException {
        String keyURL = SE_LOGIN_ROOT + "/login";
        String postURL = SE_LOGIN_ROOT + "/authenticate";

        Map<String, String> data = new HashMap<>();
        data.put("openid_identifier", SE_OPENID);
        data.put(KEY_NAME, getKey(keyURL));

        agent.post(postURL, data);
    }

    public void loginToChat() {
        String keyURL = CHAT_ROOT + "/chats/join/favorite";
        chatKey = getKey(keyURL);
    }

    public void sendMessage(int roomID, String text) throws UnsupportedEncodingException {
        String postURL = String.format("%s/chats/%s/messages/new", CHAT_ROOT, roomID);

        Map<String, String> data = new HashMap<>();
        data.put("text", text);
        data.put(KEY_NAME, chatKey);

        agent.post(postURL, data);
    }

    // for debugging
    private void debugResponse(Resource resource) {
        try {
            InputStream in = resource.getResponse().getEntity().getContent();
            int length = in.available();
            byte[] bytes = new byte[length];
            in.read(bytes, 0, length);
            System.out.println(resource.getResponse().getStatusLine());
            System.out.println(new String(bytes, UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
