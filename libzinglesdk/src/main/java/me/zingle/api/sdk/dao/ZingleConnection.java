package me.zingle.api.sdk.dao;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import me.zingle.api.sdk.Exceptions.UninitializedConnectionEx;
import me.zingle.api.sdk.dto.ResponseDTO;
import me.zingle.api.sdk.logger.Log;
//import sun.net.www.protocol.https.HttpsURLConnectionImpl;

public class ZingleConnection {
    private final String apiPath;
    private final String apiVersion;
    private final String encryptedAuthString;

    private static ZingleConnection instance;

    private ZingleConnection(String apiPath, String apiVersion, String encryptedAuthString) {

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

        this.encryptedAuthString=encryptedAuthString;
    }


    public static ZingleConnection getInstance() throws UninitializedConnectionEx {
        if(instance!=null){
            return instance;
        }
        else {
            Log.err("ZingleConnection need to be initialized by static init() function before use.");
            throw new UninitializedConnectionEx();
        }
    }

    public static boolean init (String apiPath, String apiVersion, String token, String key){

        //if(instance!=null) return false;

        //System.setProperty("jsse.enableSNIExtension", "false");

        ZingleConnection temp = new ZingleConnection(apiPath, apiVersion, generateEncryptedAuthString(token, key));

        try {
            URL url = new URL(temp.apiPath + temp.apiVersion);
        } catch (MalformedURLException e) {
            Log.err(ZingleConnection.class,"init()",temp.apiPath + temp.apiVersion+" is not a proper URL.");
            return false;
        }

        instance = temp;
        return true;
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

/*
    public void setEncryptedAuthString (String encryptedAuthString){
        this.encryptedAuthString = encryptedAuthString;
    }

    public void setEncryptedAuthString(String token, String key) {
        this.encryptedAuthString = generateEncryptedAuthString(token, key);
    }
*/

    public static String generateEncryptedAuthString(String token, String key) {
        String authString = token + ":" + key;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        return new String(authEncBytes);
    }

    public ResponseDTO send(ZingleQuery query){
        ResponseDTO result=new ResponseDTO();
        URL url;

        try {
            url = new URL(apiPath + apiVersion + query.getResourcePath() + query.getQueryStr());
        }catch (MalformedURLException e){

            result.setErrorStackTrace(e.getStackTrace().toString());
            result.setErrorString(e.getMessage());

            Log.err(this.getClass(), "send()", apiPath + apiVersion + query.getResourcePath() + query.getQueryStr() + " is not a proper URL name.");

            return result;
        }


        HttpsURLConnection connection=null;

        try{
            connection = (HttpsURLConnection) url.openConnection();

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

            InputStream dataStream;

            if (result.getResponseCode() == 200) {
                dataStream = connection.getInputStream();
            }
            else {
                dataStream = connection.getErrorStream();
            }

            Scanner channel = new Scanner(dataStream);
            channel.useDelimiter("\\A");

            StringBuilder res = new StringBuilder();

            while (channel.hasNext()) {
                res.append(channel.next());
            }

            result.setDataWithStr(res.toString());

            channel.close();
            dataStream.close();

            if (result.getResponseCode() == 200) {
                Log.info(ZingleConnection.class, "send()", connection.getRequestMethod() + ": " + connection.getURL().toString());
                if(query.getPayload()!=null)
                    Log.info(ZingleConnection.class, "send()", "Payload transmitted:\n" + query.getPayload().toString());
                Log.info(ZingleConnection.class, "send()", "Receive data:\n" + res);
            }
            else {
                Log.err(ZingleConnection.class, "send()", connection.getRequestMethod() + ": " + connection.getURL().toString());
                if(query.getPayload()!=null)
                    Log.err(ZingleConnection.class, "send()", "Payload transmitted:\n" + query.getPayload().toString());
                Log.err(ZingleConnection.class, "send()", "Responce:\n" + connection.getResponseCode() + ":" + connection.getResponseMessage());
                Log.err(ZingleConnection.class, "send()", "Receive data:\n" + res);
            }

            connection.disconnect();
            return result;


        }catch (IOException e){
            result.setErrorStackTrace(e.getStackTrace().toString());
            result.setErrorString(e.getMessage());
            return result;
        }
        finally {
            if(connection!=null)
                connection.disconnect();
        }
    }
}
