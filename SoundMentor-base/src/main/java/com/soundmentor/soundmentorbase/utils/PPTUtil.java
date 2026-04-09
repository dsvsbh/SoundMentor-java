package com.soundmentor.soundmentorbase.utils;



import com.soundmentor.soundmentorbase.constants.PPTConstant;
import com.soundmentor.soundmentorbase.enums.FileTypeEnum;
import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hslf.usermodel.HSLFPictureData;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.*;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;

import java.util.List;

@Slf4j
public class PPTUtil {

    /**
     * 通过 url下载并加载 ppt 或 pptx，返回 SlideShow 接口
     * 支持 http/https 远程 URL 和 file:// 本地文件路径
     * @param fileUrl 文件URL
     * @return SlideShow 对象
     */
    public static SlideShow<?, ?> loadPPT(String fileUrl) {
        String suffix = fileUrl.substring(fileUrl.lastIndexOf(".")).toLowerCase();
        if (!FileTypeEnum.PPT.getSuffix().equals(suffix) && !FileTypeEnum.PPTX.getSuffix().equals(suffix)) {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "文件格式需为ppt或pptx");
        }
        try {
            InputStream inputStream;
            if (fileUrl.startsWith("http://") || fileUrl.startsWith("https://")) {
                URL url = new URL(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "Failed to download file. HTTP code: " + responseCode);
                }
                inputStream = connection.getInputStream();
            } else {
                String filePath = fileUrl.replace("file://", "");
                inputStream = new FileInputStream(filePath);
            }
            try (InputStream is = inputStream) {
                if (FileTypeEnum.PPTX.getSuffix().equals(suffix)) {
                    return new XMLSlideShow(is);
                } else {
                    return new HSLFSlideShow(is);
                }
            }
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "文件加载失败: " + e.getMessage());
        }
    }

    /**
     * 获取 ppt/pptx 的页列表（通用接口）
     */
    public static List<? extends Slide<?, ?>> getSlides(SlideShow<?, ?> slideShow) {
        return slideShow.getSlides();
    }

    /**
     * 获取幻灯片页信息（兼容 ppt 和 pptx）
     * @param slide 幻灯片对象
     * @return 幻灯片文本内容
     */
    public static String getSlideInfo(Slide<?, ?> slide) {

        StringBuilder slideInfo = new StringBuilder();
        
        // 获取标题（PPTX 支持 getTitle()，PPT 返回 null）
        String title = slide.getTitle();
        if (StringUtils.isNotEmpty(title)) {
            slideInfo.append("Slide Title: ").append(title).append("\n");
        }
        
        // 遍历所有形状提取文本
        slide.getShapes().forEach(shape -> {
            if (shape instanceof org.apache.poi.sl.usermodel.TextShape) {
                slideInfo.append(((org.apache.poi.sl.usermodel.TextShape<?, ?>) shape).getText()).append("\n");
            }
        });

        return slideInfo.toString();
    }

    /**
     * 向指定页导入音频（兼容 ppt 和 pptx）
     * @param slideShow 幻灯片对象
     * @param slideIndex 幻灯片索引（从0开始）
     * @param mp3FilePath MP3文件路径
     */
    public static void addAudioToSlide(SlideShow<?, ?> slideShow, int slideIndex, String mp3FilePath) {
        if (slideShow == null) {
            throw new IllegalStateException("PPT 未加载，请先调用 loadPPT 方法加载 PPT/PPTX 文件！");
        }

        List<? extends Slide<?, ?>> slides = slideShow.getSlides();
        if (slideIndex < 0 || slideIndex >= slides.size()) {
            throw new IndexOutOfBoundsException("幻灯片页码超出范围！");
        }

        Slide<?, ?> slide = slides.get(slideIndex);
        log.info("开始向{}页ppt导入音频", slideIndex);
        
        byte[] imageBytes;
        try {
            // 使用类加载器读取资源文件
            InputStream imageStream = PPTUtil.class.getClassLoader().getResourceAsStream("voice.png");
            if (imageStream == null) {
                throw new RuntimeException("找不到音频图标文件: voice.png");
            }
            try (InputStream is = imageStream) {
                imageBytes = IOUtils.toByteArray(is);
            }
        } catch (IOException e) {
            throw new RuntimeException("读取音频图标文件失败: " + e.getMessage(), e);
        }

        URI linkUri;
        try {
            linkUri = new URI(mp3FilePath);
        } catch (URISyntaxException e) {
            throw new RuntimeException("音频文件路径格式错误: " + mp3FilePath, e);
        }

        if (slideShow instanceof XMLSlideShow) {
            addAudioToPptxSlide((XMLSlideShow) slideShow, (XSLFSlide) slide, imageBytes, linkUri);
        } else if (slideShow instanceof HSLFSlideShow) {
            addAudioToPptSlide((HSLFSlideShow) slideShow, (HSLFSlide) slide, imageBytes, linkUri);
        } else {
            throw new UnsupportedOperationException("不支持的PPT格式: " + slideShow.getClass().getName());
        }
    }

    private static void addAudioToPptxSlide(XMLSlideShow pptx, XSLFSlide slide, byte[] imageBytes, URI linkUri) {
        XSLFPictureData pictureData = pptx.addPicture(imageBytes, PictureData.PictureType.PNG);
        XSLFPictureShape pictureShape = slide.createPicture(pictureData);
        pictureShape.setAnchor(new java.awt.Rectangle(
                PPTConstant.PPT_VOICE_PICTURE_X,
                PPTConstant.PPT_VOICE_PICTURE_Y,
                PPTConstant.PPT_VOICE_PICTURE_WIDTH,
                PPTConstant.PPT_VOICE_PICTURE_HEIGHT
        ));
        XSLFHyperlink hyperlink = pictureShape.createHyperlink();
        hyperlink.setAddress(linkUri.toString());
    }

    private static void addAudioToPptSlide(HSLFSlideShow ppt, HSLFSlide slide, byte[] imageBytes, URI linkUri) {
        HSLFPictureData pictureData = null;
        try {
            pictureData = ppt.addPicture(imageBytes, PictureData.PictureType.PNG);
        } catch (IOException e) {
            throw new BizException();
        }
        org.apache.poi.hslf.usermodel.HSLFPictureShape pictureShape = slide.createPicture(pictureData);
        pictureShape.setAnchor(new java.awt.Rectangle(
                PPTConstant.PPT_VOICE_PICTURE_X,
                PPTConstant.PPT_VOICE_PICTURE_Y,
                PPTConstant.PPT_VOICE_PICTURE_WIDTH,
                PPTConstant.PPT_VOICE_PICTURE_HEIGHT
        ));
        org.apache.poi.hslf.usermodel.HSLFHyperlink hyperlink = pictureShape.createHyperlink();
        hyperlink.setAddress(linkUri.toString());
    }

    /**
     * 将 ppt/pptx 转存为文件（兼容 ppt 和 pptx）
     * @param slideShow 幻灯片对象
     * @param outputPath 输出文件路径
     */
    public static void savePPT(SlideShow<?, ?> slideShow, String outputPath) {
        if (slideShow == null) {
            throw new IllegalStateException("PPT 未加载，请先调用 loadPPT 方法加载 PPT/PPTX 文件！");
        }
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            slideShow.write(fos);
        } catch (Exception e) {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), ResultCodeEnum.FILE_ERROR.getMsg());
        }
    }

    /**
     * 将幻灯片转换为 PNG 图片的 InputStream（通用方法，支持 ppt 和 pptx）
     * @param slide 幻灯片对象
     * @return PNG 图片的输入流（需由调用者关闭）
     * @throws IOException 如果转换失败
     */
    public static InputStream convertSlideToImage(Slide<?, ?> slide) throws IOException {
        SlideShow<?, ?> slideShow = slide.getSlideShow();
        Dimension pageSize = slideShow.getPageSize();

        BufferedImage image = new BufferedImage(
                pageSize.width,
                pageSize.height,
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D graphics = image.createGraphics();
        configureGraphicsQuality(graphics);

        graphics.setColor(Color.WHITE);
        graphics.fill(new Rectangle2D.Double(0, 0, pageSize.width, pageSize.height));

        slide.draw(graphics);
        graphics.dispose();

        return convertBufferedImageToInputStream(image, "PNG");
    }

    // 配置高质量渲染参数
    private static void configureGraphicsQuality(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    }

    // 将 BufferedImage 转换为 InputStream
    private static InputStream convertBufferedImageToInputStream(
            BufferedImage image,
            String format
    ) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (!ImageIO.write(image, format, outputStream)) {
            throw new IOException("不支持图片格式: " + format);
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}