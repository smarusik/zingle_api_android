package me.zingle.api.sdk.model;

import java.net.URL;
import java.util.Map;

import me.zingle.api.sdk.dao.RequestMethods;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleWebServiceLog {
    private int id;
    private Integer message_id;
    private URL request_url;
    private RequestMethods request_method;
    private Map<String,String> request_headers;
    private int response_code;
    private Map<String,String> response_headers;
    private String response_body;

    public ZingleWebServiceLog() {
    }

    public ZingleWebServiceLog(int id, Integer message_id, URL request_url, RequestMethods request_method, Map<String, String> request_headers,
                               int response_code, Map<String, String> response_headers, String response_body) {
        this.id = id;
        this.message_id = message_id;
        this.request_url = request_url;
        this.request_method = request_method;
        this.request_headers = request_headers;
        this.response_code = response_code;
        this.response_headers = response_headers;
        this.response_body = response_body;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Integer message_id) {
        this.message_id = message_id;
    }

    public URL getRequest_url() {
        return request_url;
    }

    public void setRequest_url(URL request_url) {
        this.request_url = request_url;
    }

    public RequestMethods getRequest_method() {
        return request_method;
    }

    public void setRequest_method(RequestMethods request_method) {
        this.request_method = request_method;
    }

    public Map<String, String> getRequest_headers() {
        return request_headers;
    }

    public void setRequest_headers(Map<String, String> request_headers) {
        this.request_headers = request_headers;
    }

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public Map<String, String> getResponse_headers() {
        return response_headers;
    }

    public void setResponse_headers(Map<String, String> response_headers) {
        this.response_headers = response_headers;
    }

    public String getResponse_body() {
        return response_body;
    }

    public void setResponse_body(String response_body) {
        this.response_body = response_body;
    }
}
