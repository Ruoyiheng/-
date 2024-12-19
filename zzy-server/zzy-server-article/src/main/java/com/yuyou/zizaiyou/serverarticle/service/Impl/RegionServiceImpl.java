package com.yuyou.zizaiyou.serverarticle.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuyou.zizaiyou.article.domain.Region;
import com.yuyou.zizaiyou.commoncore.qo.QueryObject;
import com.yuyou.zizaiyou.serverarticle.mapper.RegionMapper;
import com.yuyou.zizaiyou.serverarticle.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.management.Query;

@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    private final RegionMapper regionMapper;

    public RegionServiceImpl(RegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    @Override
    public Page queryPage(QueryObject queryObject) {
        Page<Region> page = new Page<>(queryObject.getCurrentPage(), queryObject.getPageSize());
        QueryWrapper<Region> regionQueryWrapper = new QueryWrapper<>();
        regionQueryWrapper.eq(StringUtils.hasText(queryObject.getKeyWord()),"name",queryObject.getKeyWord());
        return regionMapper.selectPage(page,regionQueryWrapper);
    }
}
