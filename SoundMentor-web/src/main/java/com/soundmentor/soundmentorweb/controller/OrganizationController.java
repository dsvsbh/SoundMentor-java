package com.soundmentor.soundmentorweb.controller;

import com.soundmentor.soundmentorpojo.DTO.ResponseDTO;
import com.soundmentor.soundmentorpojo.DTO.user.req.CreateOrganizationDTO;
import com.soundmentor.soundmentorweb.annotation.RepeatSubmit;
import com.soundmentor.soundmentorweb.service.IOrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@Slf4j
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final IOrganizationService organizationService;
    @PostMapping("/create")
    @RepeatSubmit
    public ResponseDTO<Long> createOrganization(@RequestBody @Valid CreateOrganizationDTO dto){
        return ResponseDTO.OK(organizationService.createOrganization(dto));
    }

}
