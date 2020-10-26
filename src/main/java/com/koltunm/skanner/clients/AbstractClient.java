package com.koltunm.skanner.clients;

import com.koltunm.skanner.util.Config;
import com.koltunm.skanner.util.Log;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONObject;

import java.util.Properties;

public abstract class AbstractClient
{
    protected Properties config;
    protected String apiKey;
    protected String host;

    protected AbstractClient(String configPrefix)
    {
        config = Config.getProperties();
        host = config.getProperty(configPrefix + ".host");
        apiKey = config.getProperty(configPrefix + ".apiKey");
    }

    protected String constructUrl(String operation)
    {
        return String.format("https://%s%s", host, operation);
    }

    protected void validateResponse(HttpResponse<JsonNode> response, JSONObject body, String errorField)
    {
        if (response.getStatus() != 200)
        {
            StringBuilder sb = new StringBuilder()
                    .append("ValidatingResponse has failed.\n")
                    .append(String.format("%s\n%s\n%s : %s\n",
                            body.getString(errorField),
                            response.getHeaders(),
                            response.getStatusText(),
                            response.getStatus()));
            Log.w(sb.toString());
            throw new RuntimeException(sb.toString());
        }
    }
}
