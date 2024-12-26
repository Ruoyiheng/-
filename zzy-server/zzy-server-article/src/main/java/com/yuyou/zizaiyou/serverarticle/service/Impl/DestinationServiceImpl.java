package com.yuyou.zizaiyou.serverarticle.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuyou.zizaiyou.article.domain.Destination;
import com.yuyou.zizaiyou.article.domain.Region;
import com.yuyou.zizaiyou.serverarticle.mapper.DestinationMapper;

import com.yuyou.zizaiyou.serverarticle.mapper.RegionMapper;
import com.yuyou.zizaiyou.serverarticle.query.DestinationQuery;
import com.yuyou.zizaiyou.serverarticle.service.DestinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements DestinationService {
    private final DestinationMapper destinationMapper;
    private final RegionMapper regionMapper;

    public DestinationServiceImpl(DestinationMapper destinationMapper, RegionMapper regionMapper) {
        this.destinationMapper = destinationMapper;
        this.regionMapper = regionMapper;
    }

    @Override
    public List<Destination> getDestnationByRid(Long regionId) {
        Region region = regionMapper.selectById(regionId);
        if (regionId == null) {
            return null;
        }
        List<Long> refIds = region.parseRefIds();
        if (refIds.isEmpty()){
            return Collections.emptyList();
        }
        return destinationMapper.selectBatchIds(refIds);
    }

    @Override
    public Page<Destination> pageList(DestinationQuery destinationQuery) {
        Page<Destination> page = new Page<>(destinationQuery.getCurrentPage(), destinationQuery.getPageSize());
        QueryWrapper<Destination> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull(destinationQuery.getParentId() == null,"parent_id");
        queryWrapper.eq(destinationQuery.getParentId() != null,"parent_id",destinationQuery.getParentId());
        queryWrapper.like(!StringUtils.isEmpty(destinationQuery.getKeyWord()),"name",destinationQuery.getKeyWord());
        return destinationMapper.selectPage(page,queryWrapper);
    }
}
