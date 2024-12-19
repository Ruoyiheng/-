package com.yuyou.zizaiyou.serverarticle.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuyou.zizaiyou.article.domain.Region;
import com.yuyou.zizaiyou.commoncore.qo.QueryObject;
import com.yuyou.zizaiyou.serverarticle.mapper.RegionMapper;

public interface RegionService extends IService<Region> {
    Page queryPage(QueryObject queryObject);
}
