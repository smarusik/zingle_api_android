package me.zingle.api.sdk.dao;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import me.zingle.api.sdk.dto.ResponseDTO;
import sun.net.www.protocol.https.HttpsURLConnectionImpl;

public class ZingleConnection {
    private final String apiPath;
    private final String apiVersion;
    private String encryptedAuthString;
    private ZingleQuery query;

    private URL url;
    private HttpsURLConnection connection = null;

    public ZingleConnection(String apiPath, String apiVersion) {
        System.setProperty("jsse.enableSNIExtension", "false");

        if(apiPath.charAt(apiPath.length()-1)=='/'){
            apiPath=apiPath.substring(0,apiPath.length()-1);
        }
        this.apiPath = apiPath;

        if(apiVersion.charAt(apiVersion.length()-1)=='/'){
            apiVersion=apiVersion.substring(0,apiVersion.length()-1);
        }

        if(apiVersion.charAt(0)!='/')
            this.apiVersion="/"+apiVersion;
        else
            this.apiVersion = apiVersion;
    }

    public ZingleConnection(String apiPath, String apiVersion, String token, String key) {
        this(apiPath,apiVersion);
        setEncryptedAuthString(token, key);
    }

    @Override
    protected void finalize() throws Throwable {
        if(connection!=null){
            connection.disconnect();
        }
    }

    public String getApiPath() {
        return apiPath;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getEncryptedAuthString() {
        return encryptedAuthString;
    }

    public void setEncryptedAuthString(String encryptedAuthString) {
        this.encryptedAuthString = encryptedAuthString;
    }

    public void setEncryptedAuthString(String token, String key) {
        String authString = token + ":" + key;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        encryptedAuthString = new String(authEncBytes);
    }

    public ZingleQuery getQuery() {
        return query;
    }

    public void setQuery(ZingleQuery query) {
        this.query = query;
    }

    public ResponseDTO send() {
        ResponseDTO result=new ResponseDTO();

        try {

            url = new URL(apiPath + apiVersion + query.getResourcePath() + query.getQueryStr());
        }catch (MalformedURLException e){
            result.setErrorStackTrace(e.getStackTrace().toString());
            result.setErrorString(e.getMessage());
            return result;
        }

        try{
            connection = (HttpsURLConnectionImpl) url.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + encryptedAuthString);
            connection.setRequestProperty("Content-Type", "application/json");

            connection.setRequestMethod(query.getRequestMethodStr());
            connection.setDoInput(true);

            if(query.getRequestMethod()==RequestMethods.PUT || query.getRequestMethod()==RequestMethods.POST)
                connection.setDoOutput(true);
            else
                connection.setDoOutput(false);

            connection.connect();

            if(query.getPayload()!=null){
                OutputStream os = connection.getOutputStream();
                os.write(query.getPayload().toString().getBytes());
                os.flush();
                os.close();
            }

            result.setResponseCode(connection.getResponseCode());
            result.setResponseStr(connection.getResponseMessage());

            if (result.getResponseCode() == 200) {

                InputStream dataStream;
                dataStream = connection.getInputStream();

                Scanner channel = new Scanner(dataStream);

                StringBuilder res = new StringBuilder();

                while (channel.hasNext())
                    res.append(channel.next());

                result.setDataWithStr(res.toString());

                dataStream.close();
                channel.close();


            }

            connection.disconnect();
            return result;


        }catch (ProtocolException e){
            e.printStackTrace();
        }catch (SocketTimeoutException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            connection.disconnect();
            return result;
        }
    }
}
