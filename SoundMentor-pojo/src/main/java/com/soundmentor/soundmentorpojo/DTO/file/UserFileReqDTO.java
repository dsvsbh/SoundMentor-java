package com.soundmentor.soundmentorpojo.DTO.file;

import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFileReqDTO {
    /**
     * 文件类型，查ppt用"PPTX", 查mp3用"MP3",查图片用"JPG","PNG",文档"DOC","DOCX","PDF"
     */
    private List<FileTypeEnum> fileTypes;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 页码
     */
    @NotNull(message = "pageNum不能为空")
    private Integer pageNum;
    /**
     * 每页大小
     */
    @NotNull(message = "pageSize不能为空")
    private Integer pageSize;
}
