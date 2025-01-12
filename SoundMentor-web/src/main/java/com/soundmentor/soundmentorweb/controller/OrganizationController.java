package com.soundmentor.soundmentorweb.controller;

import com.soundmentor.soundmentorbase.enums.OrganizationRole;
import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.organization.*;
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
    public ResponseDTO<List<OrganizationListDTO>> list(@RequestParam OrganizationRole role){
        return ResponseDTO.OK(organizationService.OrganizationList(role));
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

    /**
     * 查询组织成员列表
     * @param organizationId
     * @return
     */
    @GetMapping("/userList/{organizationId}")
    public ResponseDTO<List<OrganizationUserListDTO>> userList(@PathVariable Integer organizationId){
        return ResponseDTO.OK(organizationService.userList(organizationId));
    }

    /**
     * 更新组织内的用户角色
     * @param dto
     * @return
     */
    @PutMapping("/updateRole")
    public ResponseDTO updateRole(@RequestBody @Valid UpdateOrgUserRoleDTO dto){
        organizationService.updateRole(dto);
        return ResponseDTO.OK();
    }

    /**
     * 将用户踢出组织
     * @param dto
     * @return
     */
    @DeleteMapping("/remove")
    public ResponseDTO remove(@RequestBody @Valid RemoveOrganizationUserDTO dto)
    {
        organizationService.removeUserFromOrg(dto);
        return ResponseDTO.OK();
    }

    @DeleteMapping("/remove/{organizationId}")
    public ResponseDTO remove(@PathVariable Integer organizationId)
    {
        organizationService.removeOrganization(organizationId);
        return ResponseDTO.OK();
    }
}
