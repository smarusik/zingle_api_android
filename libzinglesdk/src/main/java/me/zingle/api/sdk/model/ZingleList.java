package me.zingle.api.sdk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleList<Model> {
    public String sortField;
    public ZingleSortOrder sortDirection;
    public Integer page;
    public Integer pageSize;
    public Integer totalPages;
    public Integer totalRecords;

    public List<Model> objects=new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder result=new StringBuilder();
        result.append("\nsortField=");
        result.append(sortField);
        result.append("\nsortDirection=");
        result.append(sortDirection);
        result.append("\npage=");
        result.append(page);
        result.append("\npageSize=");
        result.append(pageSize);
        result.append("\ntotalPages=");
        result.append(totalPages);
        result.append("\ntotalRecords=");
        result.append(totalRecords);
        result.append("\nList:");

        for(Model t:objects) {
            result.append(t.toString());
            result.append("\n");
        }

        return result.toString();
    }
}
