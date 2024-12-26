package com.yuyou.zizaiyou.serverarticle.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuyou.zizaiyou.article.domain.Destination;
import com.yuyou.zizaiyou.article.domain.Region;
import com.yuyou.zizaiyou.commoncore.qo.QueryObject;
import com.yuyou.zizaiyou.serverarticle.query.DestinationQuery;

import java.util.List;

public interface DestinationService extends IService<Destination> {
    List<Destination> getDestnationByRid(Long id);

    Page<Destination> pageList(DestinationQuery destinationQuery);
}
