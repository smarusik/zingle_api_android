package me.zingle.api.sdk.dao;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.dto.RequestDTO;

/**
 * Created by SLAVA 08 2015.
 */
public class ZingleQuery {
    private RequestMethods requestMethod;
    private String resourcePath;
    private List<QueryPart> params;

    private RequestDTO payload;

    public ZingleQuery(RequestMethods requestMethod, String resourcePath) {
        this.requestMethod = requestMethod;
        setResourcePath(resourcePath);
    }

    public ZingleQuery(RequestMethods requestMethod, String resourcePath, List<QueryPart> params) {
        this(requestMethod,resourcePath);
        this.params = params;
    }

    public ZingleQuery(RequestMethods requestMethod, String resourcePath, List<QueryPart> params, RequestDTO payload) {
        this(requestMethod,resourcePath, params);
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
        else
            this.resourcePath="";

        if(resourcePath.charAt(resourcePath.length()-1)=='/'){
            resourcePath=resourcePath.substring(0,resourcePath.length()-1);
        }

            this.resourcePath += resourcePath;
    }

    public List<QueryPart> getParams() {
        return params;
    }

    public String getQueryStr(){
        if(params !=null) {
            StringBuilder res = new StringBuilder();
            for (QueryPart part : params) {
                res.append("?").append(part.key).append("=").append(part.value);
            }
            return res.toString();
        }
        else
            return new String();
    }

    public void setParams(List<QueryPart> params) {
        this.params = params;
    }

    public void addParam(QueryPart param){
        if(this.params==null)
            this.params=new ArrayList<>();

        this.params.add(param);
    }

    public void addParam(String key,String value){
        addParam(new QueryPart(key, value));
    }

    public void addParam(String key,int value){
        addParam(new QueryPart(key,value));
    }

    public JSONObject getPayload() {
        if(payload!=null)
            return payload.getData();
        else
            return null;
    }

    public void setPayload(RequestDTO payload) {
        this.payload = payload;
    }

}
