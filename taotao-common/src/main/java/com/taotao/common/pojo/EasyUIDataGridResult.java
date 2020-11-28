package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * datagrid 展示数据的pojo，包括商品的pojo
 * @author wwt
 * @date 2019/9/17 16:54
 */
public class EasyUIDataGridResult implements Serializable {
    private long total;

    private List rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
