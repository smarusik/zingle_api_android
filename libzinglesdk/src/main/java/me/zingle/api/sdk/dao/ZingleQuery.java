package me.zingle.api.sdk.dao;

import java.util.List;
import org.json.JSONObject;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleQuery {
    private RequestMethods requestMethod;
    private String resourcePath;
    private List<QueryPart> query;

    private JSONObject payload;

    public ZingleQuery(RequestMethods requestMethod, String resourcePath) {
        this.requestMethod = requestMethod;
        this.resourcePath = resourcePath;
    }

    public ZingleQuery(RequestMethods requestMethod, String resourcePath, List<QueryPart> query) {
        this.requestMethod = requestMethod;
        this.resourcePath = resourcePath;
        this.query = query;
    }

    public ZingleQuery(RequestMethods requestMethod, String resourcePath, List<QueryPart> query, JSONObject payload) {
        this.requestMethod = requestMethod;
        this.resourcePath = resourcePath;
        this.query = query;
        this.payload = payload;
    }

    public RequestMethods getRequestMethod() {
        return requestMethod;
    }

    public String getRequestMethodStr() {
        return requestMethod.method();
    }

    public void setRequestMethod(RequestMethods requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        if(resourcePath.charAt(0)!='/')
            this.resourcePath="/";

        if(resourcePath.charAt(resourcePath.length()-1)=='/'){
            resourcePath=resourcePath.substring(0,resourcePath.length()-1);
        }

            this.resourcePath += resourcePath;
    }

    public List<QueryPart> getQuery() {
        return query;
    }

    public String getQueryStr(){
        StringBuilder res=new StringBuilder();

        for(QueryPart part: query){
            res.append("?").append(part.key).append("=").append(part.value);
        }
        return res.toString();
    }

    public void setQuery(List<QueryPart> query) {
        this.query = query;
    }

    public JSONObject getPayload() {
        return payload;
    }

    public void setPayload(JSONObject payload) {
        this.payload = payload;
    }

}
