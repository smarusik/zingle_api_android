package me.zingle.api.sdk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SLAVA 09 2015.
 */
public class ZingleList<Model extends ZingleBaseModel> {
    public String sortField;
    public ZingleSortOrder sortDirection;
    public Integer page;
    public Integer pageSize;
    public Integer totalPages;
    public Integer totalRecords;

    public List<Model> objects=new ArrayList<>();

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("\nstatus:{");
        sb.append("\n    sortField='").append(sortField).append('\'');
        sb.append("\n    sortDirection=").append(sortDirection);
        sb.append("\n    page=").append(page);
        sb.append("\n    pageSize=").append(pageSize);
        sb.append("\n    totalPages=").append(totalPages);
        sb.append("\n    totalRecords=").append(totalRecords);
        sb.append("}\n");

        sb.append("\nresult:[").append(objects).append("]\n");
        return sb.toString();
    }
}
