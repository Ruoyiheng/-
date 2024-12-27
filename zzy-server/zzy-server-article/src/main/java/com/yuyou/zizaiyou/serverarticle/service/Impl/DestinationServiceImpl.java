package com.yuyou.zizaiyou.serverarticle.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements DestinationService {
    private final DestinationMapper destinationMapper;
    private final RegionMapper regionMapper;
    public final ThreadPoolExecutor businessThreadPoolExecutor;

    public DestinationServiceImpl(DestinationMapper destinationMapper, RegionMapper regionMapper, ThreadPoolExecutor businessThreadPoolExecutor) {
        this.destinationMapper = destinationMapper;
        this.regionMapper = regionMapper;
        this.businessThreadPoolExecutor = businessThreadPoolExecutor;
    }

    /**
     * @param regionId
     * @return 查询区域下的目的地
     */
    @Override
    public List<Destination> getDestnationByRid(Long regionId) {
        // 1。通过区域id查询区域信息
        Region region = regionMapper.selectById(regionId);
        if (regionId == null) {
            return null;
        }
        // 2.通过区域信息得到目的地id列表
        List<Long> refIds = region.parseRefIds();
        if (refIds.isEmpty()) {
            return Collections.emptyList();
        }
        // 3 查询目的地并返回
        return destinationMapper.selectBatchIds(refIds);
    }

    @Override
    public Page<Destination> pageList(DestinationQuery destinationQuery) {
        Page<Destination> page = new Page<>(destinationQuery.getCurrentPage(), destinationQuery.getPageSize());
        QueryWrapper<Destination> queryWrapper = new QueryWrapper<>();
        // 如果parent_id为null，则按null查询
        queryWrapper.isNull(destinationQuery.getParentId() == null, "parent_id");
        // 如果parent_id不为null，则按parent_id查询
        queryWrapper.eq(destinationQuery.getParentId() != null, "parent_id", destinationQuery.getParentId());
        // 关键字查询
        queryWrapper.like(!StringUtils.isEmpty(destinationQuery.getKeyWord()), "name", destinationQuery.getKeyWord());
        return destinationMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Boolean updateInfo(Long id, String info) {
        UpdateWrapper<Destination> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id);
        wrapper.set("info", info);
        return super.update(wrapper);
    }

    @Override
    public List<Destination> toasts(Long destId) {
        List<Destination> list = new ArrayList<>();
        while (destId != null) {
            Destination destination = destinationMapper.selectById(destId);
            list.add(destination);
            if (destination == null) {
                break;
            }
            destId = destination.getParentId();
        }
        Collections.reverse(list);
        return list;
    }

    @Override
    public List<Destination> hotList(Long regionId) {
        List<Destination> destinations = new ArrayList<>();
        QueryWrapper<Destination> wrapper = new QueryWrapper<>();
        if (regionId < 0) {
            destinations = destinationMapper.selectList(wrapper.eq("parent_id", 1));
        } else {
            Region region = regionMapper.selectById(regionId);
            if (region == null) {
                return Collections.emptyList();
            }
            destinations = destinationMapper.selectBatchIds(region.parseRefIds());
        }
        CountDownLatch countDownLatch = new CountDownLatch(destinations.size());
        for (Destination destination : destinations) {
            businessThreadPoolExecutor.execute(() ->{
                List<Destination> list = destinationMapper.selectList(wrapper.eq("parent_id", destination.getId()).last("limit 10"));
                destination.setChildren(list);
            });
            countDownLatch.countDown();
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return destinations;
    }
}
