package com.soundmentor.soundmentorbase.utils;

import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PPTUtil {
    /**
     * 加载 PPT 文件
     * @param filePath PPT 本地临时文件路径
     * @throws IOException 如果文件读取失败
     */
    public static XMLSlideShow loadPPT(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
             return new XMLSlideShow(fis);
        } catch (IOException e) {
            throw new IOException("Failed to load PPT file: " + filePath, e);
        }
    }
    /**
     * 获取ppt的页列表
     */
    public static List<XSLFSlide> getSlides(XMLSlideShow ppt) {
        return ppt.getSlides();
    }

    /**
     * 向指定页导入音频
     */
    public static void insertAudio(XMLSlideShow ppt, String  mp3FilePath, int slideIndex) throws FileNotFoundException {
        if (ppt == null) {
            throw new IllegalStateException("PPT 未加载，请先调用 loadPPT 方法加载 PPT 文件！");
        }
        List<XSLFSlide> slides = ppt.getSlides();
        if (slideIndex < 0 || slideIndex >= slides.size()) {
            throw new IndexOutOfBoundsException("幻灯片页码超出范围！");
        }

        XSLFSlide slide = slides.get(slideIndex);
        File mp3File = new File(mp3FilePath);
        try (FileInputStream fis = new FileInputStream(mp3File)) {
            byte[] mp3Bytes = fis.readAllBytes();
            XSLFPictureData pictureData = ppt.addPicture(mp3Bytes, PictureData.PictureType.PNG);
            slide.createPicture(pictureData);
        } catch (IOException e) {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(),ResultCodeEnum.FILE_ERROR.getMsg());
        }
    }
}
