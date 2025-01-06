package com.soundmentor.soundmentorpojo.DTO.organization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinOrganizationDTO {
    private Integer organizationId;
    private String shareCode;
}
