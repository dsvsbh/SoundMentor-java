package com.soundmentor.soundmentorbase.utils;

import cn.hutool.json.XML;
import com.soundmentor.soundmentorbase.constants.PPTConstant;
import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.*;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.List;

@Slf4j
public class PPTXUtil {
    /**
     * 通过 url下载并加载 pptx
     * @param fileUrl
     * @param
     * @return
     */
    public static XMLSlideShow loadPPTX(String fileUrl) {
        if(!FileTypeEnum.PPTX.getSuffix().equals(fileUrl.substring(fileUrl.lastIndexOf("."))))
        {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "文件格式需为pptx");
        }
        HttpURLConnection connection = null;
        try {
            URL url = new URL(fileUrl);
            // 打开连接
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000); // 设置连接超时10秒
            connection.setReadTimeout(10000); // 设置读取超时10秒
            // 检查响应码
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "Failed to download file. HTTP code: " + responseCode);
            }
            try(InputStream inputStream = connection.getInputStream()) {
                return new XMLSlideShow(inputStream);
            }
        } catch (Exception e)
        {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "文件下载失败");
        }
    }


    /**
     * 获取 pptx 的页列表
     */
    public static List<XSLFSlide> getSlides(XMLSlideShow pptx) {
        return pptx.getSlides();
    }

    /**
     * 获取pptx的页信息
     * @param slide
     * @return
     */
    public static String getSlideInfo(XSLFSlide slide) {

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
    public static void addAudioToSlide(XMLSlideShow pptx,int slideIndex, String mp3FilePath) {
        if (pptx == null) {
            throw new IllegalStateException("PPTX 未加载，请先调用 loadPPTX 方法加载 PPTX 文件！");
        }

        List<XSLFSlide> slides = pptx.getSlides();
        if (slideIndex < 0 || slideIndex >= slides.size()) {
            throw new IndexOutOfBoundsException("幻灯片页码超出范围！");
        }

        XSLFSlide slide = slides.get(slideIndex);
        log.info("开始向{}页pptx导入音频", slideIndex);
        // 读取图片文件
        File imageFile = new File(PPTConstant.PPT_VOICE_PICTURE_PATH);
        byte[] imageBytes = null;
        try {
            imageBytes = Files.readAllBytes(imageFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 添加图片到 PPTX
        XSLFPictureData pictureData = pptx.addPicture(imageBytes, PictureData.PictureType.PNG); // 这里假设图片格式是 PNG
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
     * 将 pptx 转存为文件
     */
    public static void savePPT(XMLSlideShow pptx,String outputPath){
        if (pptx == null) {
            throw new IllegalStateException("PPT 未加载，请先调用 loadPPT 方法加载 PPT 文件！");
        }
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            pptx.write(fos);
        } catch (Exception e)
        {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), ResultCodeEnum.FILE_ERROR.getMsg());
        }
    }
}

class PPTUtilTest {
    public static void main(String[] args) {
        String url = "http://121.43.62.36:9000/ppt/1736923625533_5e5bd4d73cbd451c0cfd346dc96a5d9b.pptx";
        XMLSlideShow ppt = PPTXUtil.loadPPTX(url);
        List<XSLFSlide> slides = ppt.getSlides();
        System.out.println(slides.size());
        System.out.println(PPTXUtil.getSlideInfo(slides.get(0)));
        PPTXUtil.addAudioToSlide(ppt, 0, "test.mp3");
        PPTXUtil.savePPT(ppt, "SoundMentor-base/src/main/resources/test.ppt");
    }
}
