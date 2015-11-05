package me.zingle.api.sdk.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import me.zingle.api.sdk.Exceptions.MappingErrorEx;
import me.zingle.api.sdk.dao.QueryPart;
import me.zingle.api.sdk.model.ZingleAttachment;
import me.zingle.api.sdk.model.ZingleList;

/**
 * ZingleBaseService derivation for working with ZingleAttachment.
 * Class haven't any reflection in API, so doesn't implement any API functions.
 * Serves as helper class for parsing and handling <a href=https://github.com/Zingle/rest-api/tree/master/messages>ZingleMessage</a> attachments.
 */
public class ZingleAttachmentService extends ZingleBaseService<ZingleAttachment> {
    @Override
    protected String resourcePath(boolean specific) {
        return null;
    }

    @Override
    protected boolean checkModifier(String modifier) {
        return false;
    }

    @Override
    public ZingleAttachment mapper(JSONObject source) throws MappingErrorEx {
        return null;
    }

    public ZingleAttachment mapper(String source) throws MappingErrorEx {
        try {
            return new ZingleAttachment(source);
        }catch(MalformedURLException e){
            throw new MappingErrorEx("ZingleAttachment",source,source);
        }
    }

    @Override
    public List<ZingleAttachment> arrayMapper(JSONArray source) throws MappingErrorEx {
        try {
            List<ZingleAttachment> retList = new ArrayList<>();

            for (int i=0; i<source.length(); i++) {
                String temp = source.optString(i++);
                retList.add(mapper(temp));
            }

            return retList;
        }catch(MappingErrorEx e){
            e.setMappedSource(source.toString());
            throw e;
        }
    }

    @Override
    public ZingleAttachment get(String id) {
        return null;
    }

    @Override
    public ZingleList<ZingleAttachment> list() {
        return null;
    }

    @Override
    public ZingleList<ZingleAttachment> list(List<QueryPart> conditions) {
        return null;
    }

    @Override
    public Boolean delete(String id) {
        return false;
    }

    @Override
    public ZingleAttachment create(ZingleAttachment object) {
        return null;
    }

    @Override
    public ZingleAttachment update(ZingleAttachment object) {
        return null;
    }
}
