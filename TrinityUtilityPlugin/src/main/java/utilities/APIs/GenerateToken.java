package utilities.APIs;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GenerateToken {
    String accessToken = null;
    public String GenerateAccessToken(
            String grant_type,
            String access_token_url,
            String client_id,
            String client_secret,
            String scope
            ) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost authRequest = new HttpPost(access_token_url);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", grant_type));
        params.add(new BasicNameValuePair("access_token_url", access_token_url));
        params.add(new BasicNameValuePair("client_id", client_id));
        params.add(new BasicNameValuePair("client_secret", client_secret));
        params.add(new BasicNameValuePair("scope", scope));

        authRequest.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(authRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Parse the JSON response to get the access token
        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(EntityUtils.toString(response.getEntity()));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        accessToken = jsonResponse.getString("access_token");
        System.out.println("AccessToken is :" + accessToken);
        try {
            httpClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return accessToken;
    }
}
