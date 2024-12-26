package com.yuyou.zizaiyou.serverarticle.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.yuyou.zizaiyou.article.domain.Destination;
import com.yuyou.zizaiyou.commoncore.exception.BaseResponse;
import com.yuyou.zizaiyou.commoncore.exception.ResultUtils;
import com.yuyou.zizaiyou.serverarticle.query.DestinationQuery;
import com.yuyou.zizaiyou.serverarticle.service.DestinationService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("destinations")
public class DestinationController {
    private final DestinationService destinationService;


    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    /**
     * @param destinationQuery
     * @return
     * 分页查询
     */
    @GetMapping("list")
    public BaseResponse<Page<Destination>> pageList(DestinationQuery destinationQuery){
        return ResultUtils.success(destinationService.pageList(destinationQuery));
    }
}
