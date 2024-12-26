package com.yuyou.zizaiyou.serverarticle.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuyou.zizaiyou.article.domain.Destination;
import com.yuyou.zizaiyou.article.domain.Region;
import com.yuyou.zizaiyou.commoncore.exception.BaseResponse;
import com.yuyou.zizaiyou.commoncore.qo.QueryObject;
import com.yuyou.zizaiyou.serverarticle.mapper.RegionMapper;
import com.yuyou.zizaiyou.serverarticle.query.RegionQuery;

import java.util.List;

public interface RegionService extends IService<Region> {
    Page queryPage(RegionQuery regionQuery);

    List<Region> getHotRegion();

}
