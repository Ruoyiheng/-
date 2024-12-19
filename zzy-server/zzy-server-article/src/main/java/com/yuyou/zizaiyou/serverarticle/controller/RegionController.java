package com.yuyou.zizaiyou.serverarticle.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuyou.zizaiyou.article.domain.Region;
import com.yuyou.zizaiyou.commoncore.exception.BaseResponse;
import com.yuyou.zizaiyou.commoncore.exception.ResultUtils;
import com.yuyou.zizaiyou.commoncore.qo.QueryObject;
import com.yuyou.zizaiyou.serverarticle.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/regions")
public class RegionController {
    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @GetMapping("/detail")
    public BaseResponse<Region> getById(Long id) {
        Region region = regionService.getById(id);
        return ResultUtils.success(region);
    }

    @GetMapping("/page")
    public BaseResponse<Page<Region>> pageList(QueryObject queryObject) {
        return ResultUtils.success(regionService.queryPage(queryObject));
    }
    @PostMapping("/add")
    public BaseResponse<Boolean> addRegion(Region region){
        return ResultUtils.success(regionService.save(region));
    }
    @PostMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteRegion(Long id){
        return ResultUtils.success(regionService.removeById(id));
    }
    @PostMapping("/update")
    public BaseResponse<Boolean> updateRegion(Region region){
        return ResultUtils.success(regionService.updateById(region));
    }
}
