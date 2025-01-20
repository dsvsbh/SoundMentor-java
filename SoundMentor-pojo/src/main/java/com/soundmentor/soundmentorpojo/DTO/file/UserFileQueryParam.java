package com.soundmentor.soundmentorpojo.DTO.file;

import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFileQueryParam {
    private Integer userId;
    private List<Integer> fileTypes;
    private String fileName;
}
