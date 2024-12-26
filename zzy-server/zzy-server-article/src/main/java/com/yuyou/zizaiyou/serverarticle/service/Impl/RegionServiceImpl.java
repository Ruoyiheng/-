package com.yuyou.zizaiyou.serverarticle.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuyou.zizaiyou.article.domain.Destination;
import com.yuyou.zizaiyou.article.domain.Region;
import com.yuyou.zizaiyou.commoncore.qo.QueryObject;
import com.yuyou.zizaiyou.serverarticle.mapper.RegionMapper;
import com.yuyou.zizaiyou.serverarticle.query.RegionQuery;
import com.yuyou.zizaiyou.serverarticle.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.management.Query;
import java.util.List;

@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

    private final RegionMapper regionMapper;

    public RegionServiceImpl(RegionMapper regionMapper) {
        this.regionMapper = regionMapper;
    }

    @Override
    public Page queryPage(RegionQuery regionQuery) {
        Page<Region> page = new Page<>(regionQuery.getCurrentPage(), regionQuery.getPageSize());
        QueryWrapper<Region> regionQueryWrapper = new QueryWrapper<>();
        regionQueryWrapper.like(StringUtils.hasText(regionQuery.getKeyWord()),"name",regionQuery.getKeyWord());
        return regionMapper.selectPage(page,regionQueryWrapper);
    }

    @Override
    public List<Region> getHotRegion() {
        return regionMapper.selectList(new QueryWrapper<Region>().eq("ishot",Region.STATE_HOT));
    }


}
