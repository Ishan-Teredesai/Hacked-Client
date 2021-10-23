package net.spookysquad.spookster.gui.accountmanager.auth.util;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HTTPRequester {

	/**
	 * Gson instance
	 */
	private static final Gson gson = new Gson();
	/**
	 * The URL to send requests to
	 */
	private final URL url;

	/**
	 * Creates a new HTTPRequester
	 *
	 * @param url URL to send requests to
	 */
	public HTTPRequester(URL url) {
		this.url = url;
	}

	/**
	 * Sends a request by parsing the object using gson.
	 *
	 * @param requestObj    Object to send as a request after being parsed with gson.
	 * @param responseClass Class to parse the response to with gson.
	 * @param <T>           Class to parse the response to with gson.
	 * @return A result object, using the specified class as a template and parsed by gson.
	 * @throws IOException
	 */
	public <T extends Object> T gsonRequest(Object requestObj, Class<T> responseClass) throws IOException {
		//System.out.println(requestObj + " ||| " + gson.toJson(requestObj));
		String response = postRequestOld(gson.toJson(requestObj), "application/json");

		T result = gson.fromJson(response, responseClass);
		return result;
	}

    public String postRequestOld(String post, String contentType) throws IOException {
        HttpURLConnection connection = this.createConnection();
        byte[] postBytes = post.getBytes(Charset.forName("UTF-8"));
        connection.setRequestProperty("Content-Length", String.valueOf(postBytes.length));
        connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8"); //defined type with utf-8 encoding
        connection.setDoOutput(true);
        String response = null;

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(postBytes);
        outputStream.close();

        InputStream inputStream = connection.getInputStream();
        try {
            response = new Scanner(inputStream).useDelimiter("\\A").next();
        } catch (NoSuchElementException e) {
            return response;
        }
        inputStream.close();

        connection.disconnect();

        return response;
    }


    /**
	 * Sends a post request to a webserver
	 *
	 * @param post        Post data
	 * @param contentType Type of content
	 * @return Response from the request
	 * @throws IOException
	 */
	public String postRequest(String post, String contentType) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url.toString());
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        String[] postSplit = post.split("&");
        for(String s : postSplit) {
            String[] postParts = s.split("=");
            postParams.add(new BasicNameValuePair(postParts[0], postParts[1]));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(postParams));
        CloseableHttpResponse response = httpclient.execute(httpPost);

        HttpEntity entity = response.getEntity();
        BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
        String out = "";

        String line = "";
        while((line = reader.readLine()) != null) {
            out = out.concat(line);
        }

        EntityUtils.consume(entity);

		return out;
	}

	/**
	 * Creates a new URL connection with the settings required for what we need.
	 *
	 * @return A new URL connection
	 * @throws IOException
	 */
	private HttpURLConnection createConnection() throws IOException {
		HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
		connection.setConnectTimeout(20000); //20 seconds
		connection.setReadTimeout(20000); //20 seconds
		return connection;
	}

}
