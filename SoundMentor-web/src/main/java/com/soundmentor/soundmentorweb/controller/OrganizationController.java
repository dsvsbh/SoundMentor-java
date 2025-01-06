package com.soundmentor.soundmentorweb.controller;

import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.organization.JoinOrganizationDTO;
import com.soundmentor.soundmentorpojo.DTO.organization.OrganizationListDTO;
import com.soundmentor.soundmentorpojo.DTO.user.req.CreateOrganizationDTO;
import com.soundmentor.soundmentorweb.annotation.RepeatSubmit;
import com.soundmentor.soundmentorweb.service.IOrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 组织相关接口
 */
@RestController
@Slf4j
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final IOrganizationService organizationService;

    /**
     * 用户创建组织
     * @param dto
     * @return
     */
    @PostMapping("/create")
    @RepeatSubmit(limitTime = 10)
    public ResponseDTO<Integer> createOrganization(@RequestBody @Valid CreateOrganizationDTO dto){
        return ResponseDTO.OK(organizationService.createOrganization(dto));
    }

    /**
     * 获取组织列表
     * @return
     */
    @GetMapping("/list")
    public ResponseDTO<List<OrganizationListDTO>> list(){
        return ResponseDTO.OK(organizationService.OrganizationList());
    }

    /**
     * 获取组织分享码
     * @param organizationId
     * @return
     */
    @GetMapping("/shareCode/{organizationId}")
    public ResponseDTO<String> shareCode(@PathVariable Integer organizationId){
        return ResponseDTO.OK(organizationService.getShareCode(organizationId));
    }

    /**
     * 用户通过分享码加入组织
     * @param dto
     * @return
     */
    @PostMapping("/join")
    public ResponseDTO join(@RequestBody JoinOrganizationDTO dto){
        organizationService.join(dto);
        return ResponseDTO.OK();
    }
}
