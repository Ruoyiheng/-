package com.yuyou.zizaiyou.commoncore.qo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryObject {
    private int pageSize = 10;
    private int currentPage = 1;

    private String keyWord;
}
