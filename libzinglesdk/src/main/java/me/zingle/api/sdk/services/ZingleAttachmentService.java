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
 * Created by SLAVA 10 2015.
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
        return super.get(id);
    }

    @Override
    public ZingleList<ZingleAttachment> list() {
        return super.list();
    }

    @Override
    protected ZingleList<ZingleAttachment> list(List<QueryPart> conditions) {
        return super.list(conditions);
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
