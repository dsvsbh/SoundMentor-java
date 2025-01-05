package com.soundmentor.soundmentorbase.utils;

import com.soundmentor.soundmentorbase.constants.PPTConstant;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.xslf.usermodel.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.List;
@Slf4j
public class PPTUtil {
    /**
     * 加载 PPT 文件
     *
     * @param filePath PPT 本地临时文件路径
     * @throws IOException 如果文件读取失败
     */
    public static XMLSlideShow loadPPT(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return new XMLSlideShow(fis);
        } catch (Exception e) {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), ResultCodeEnum.FILE_ERROR.getMsg());
        }
    }

    /**
     * 获取ppt的页列表
     */
    public static List<XSLFSlide> getSlides(XMLSlideShow ppt) {
        return ppt.getSlides();
    }

    /**
     * 获取指定页幻灯片信息
     *
     * @param slideIndex 幻灯片页码（从 0 开始）
     * @return 幻灯片标题和内容
     */
    public static String getSlideInfo(XMLSlideShow ppt, int slideIndex) {
        if (ppt == null) {
            throw new IllegalStateException("PPT 未加载，请先调用 loadPPT 方法加载 PPT 文件！");
        }
        List<XSLFSlide> slides = ppt.getSlides();
        if (slideIndex < 0 || slideIndex >= slides.size()) {
            throw new IndexOutOfBoundsException("幻灯片页码超出范围！");
        }

        XSLFSlide slide = slides.get(slideIndex);
        StringBuilder slideInfo = new StringBuilder();
        if (StringUtils.isNotEmpty(slide.getTitle())) {
            slideInfo.append("Slide Title: ").append(slide.getTitle()).append("\n");
        }
        slide.getShapes().forEach(shape -> {
            if (shape instanceof XSLFTextShape) {
                slideInfo.append(((XSLFTextShape) shape).getText()).append("\n");
            }
        });

        return slideInfo.toString();
    }

    /**
     * 向指定页导入音频
     */
    public static void addAudioToSlide(XMLSlideShow ppt,int slideIndex, String mp3FilePath) {
        if (ppt == null) {
            throw new IllegalStateException("PPT 未加载，请先调用 loadPPT 方法加载 PPT 文件！");
        }

        List<XSLFSlide> slides = ppt.getSlides();
        if (slideIndex < 0 || slideIndex >= slides.size()) {
            throw new IndexOutOfBoundsException("幻灯片页码超出范围！");
        }

        XSLFSlide slide = slides.get(slideIndex);
        log.info("开始向{}页ppt导入音频", slideIndex);
        // 读取图片文件
        File imageFile = new File(PPTConstant.PPT_VOICE_PICTURE_PATH);
        byte[] imageBytes = null;
        try {
            imageBytes = Files.readAllBytes(imageFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 添加图片到 PPT
        XSLFPictureData pictureData = ppt.addPicture(imageBytes, PictureData.PictureType.PNG); // 这里假设图片格式是 PNG
        XSLFPictureShape pictureShape = slide.createPicture(pictureData);

        // 设置图片的位置
        pictureShape.setAnchor(new java.awt.Rectangle(PPTConstant.PPT_VOICE_PICTURE_X, PPTConstant.PPT_VOICE_PICTURE_Y, PPTConstant.PPT_VOICE_PICTURE_WIDTH, PPTConstant.PPT_VOICE_PICTURE_HEIGHT)); // 可以调整图片的位置和大小

        // 设置图片的超链接
        URI linkUri = null;  // 这里可以是本地文件路径或网络 URL
        try {
            linkUri = new URI(mp3FilePath);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        XSLFHyperlink hyperlink = pictureShape.createHyperlink();// 为图片设置超链接
        hyperlink.setAddress(linkUri.toString());
    }
    /**
     * 将ppt转存为文件
     */
    public static void savePPT(XMLSlideShow ppt,String outputPath){
        if (ppt == null) {
            throw new IllegalStateException("PPT 未加载，请先调用 loadPPT 方法加载 PPT 文件！");
        }
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            ppt.write(fos);
        } catch (Exception e)
        {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), ResultCodeEnum.FILE_ERROR.getMsg());
        }
    }
}
class PPTUtilTest {
    public static void main(String[] args) {
        XMLSlideShow ppt = PPTUtil.loadPPT("SoundMentor-base/src/main/resources/test.ppt");
        List<XSLFSlide> slides = ppt.getSlides();
        System.out.println(slides.size());
        System.out.println(PPTUtil.getSlideInfo(ppt, 32));
        PPTUtil.addAudioToSlide(ppt, 0, "test.mp3");
        PPTUtil.savePPT(ppt, "SoundMentor-base/src/main/resources/test.ppt");
    }
}
