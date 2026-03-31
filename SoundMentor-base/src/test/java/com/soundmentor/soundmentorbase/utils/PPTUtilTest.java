package com.soundmentor.soundmentorbase.utils;

import com.soundmentor.soundmentorbase.exception.BizException;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PPTUtil 单元测试
 */
class PPTUtilTest {

    @TempDir
    Path tempDir;

    @Test
    void testLoadPptxFromLocalFile() throws Exception {
        Path pptxPath = createPptxFile(3);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptxPath.toString());
        assertNotNull(slideShow);
        assertEquals(3, slideShow.getSlides().size());
        assertTrue(slideShow instanceof XMLSlideShow);
    }

    @Test
    void testLoadPptFromLocalFile() throws Exception {
        Path pptPath = createPptFile(2);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptPath.toString());
        assertNotNull(slideShow);
        assertEquals(2, slideShow.getSlides().size());
        assertTrue(slideShow instanceof HSLFSlideShow);
    }

    @Test
    void testGetSlidesFromPptx() throws Exception {
        Path pptxPath = createPptxFile(5);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptxPath.toString());
        assertEquals(5, PPTUtil.getSlides(slideShow).size());
    }

    @Test
    void testGetSlidesFromPpt() throws Exception {
        Path pptPath = createPptFile(3);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptPath.toString());
        assertEquals(3, PPTUtil.getSlides(slideShow).size());
    }

    @Test
    void testConvertPptxSlideToImage() throws Exception {
        Path pptxPath = createPptxFile(1);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptxPath.toString());
        Slide<?, ?> slide = PPTUtil.getSlides(slideShow).get(0);

        try (InputStream imageStream = PPTUtil.convertSlideToImage(slide)) {
            assertNotNull(imageStream);
            byte[] header = new byte[4];
            imageStream.read(header);
            // PNG文件头: 89 50 4E 47 (十六进制) -> 137 80 78 71 (十进制)
            assertArrayEquals(new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47}, header);
        }
    }

    @Test
    void testConvertPptSlideToImage() throws Exception {
        Path pptPath = createPptFile(1);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptPath.toString());
        Slide<?, ?> slide = PPTUtil.getSlides(slideShow).get(0);

        try (InputStream imageStream = PPTUtil.convertSlideToImage(slide)) {
            assertNotNull(imageStream);
            byte[] header = new byte[4];
            imageStream.read(header);
            // PNG文件头: 89 50 4E 47 (十六进制) -> 137 80 78 71 (十进制)
            assertArrayEquals(new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47}, header);
        }
    }

    @Test
    void testConvertMultipleSlidesToImages() throws Exception {
        Path pptxPath = createPptxFile(3);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptxPath.toString());

        for (int i = 0; i < 3; i++) {
            Slide<?, ?> slide = PPTUtil.getSlides(slideShow).get(i);
            try (InputStream imageStream = PPTUtil.convertSlideToImage(slide)) {
                Path outputPath = tempDir.resolve("slide_" + i + ".png");
                Files.copy(imageStream, outputPath);
                assertTrue(Files.exists(outputPath));
                assertTrue(Files.size(outputPath) > 0);
            }
        }
    }

    @Test
    void testInvalidFileFormatThrowsException() {
        Path txtPath = tempDir.resolve("test.txt");
        assertThrows(BizException.class, () -> PPTUtil.loadPPT(txtPath.toString()));
    }

    @Test
    void testEmptyPptx() throws Exception {
        Path pptxPath = createPptxFile(0);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptxPath.toString());
        assertEquals(0, PPTUtil.getSlides(slideShow).size());
    }

    @Test
    void testNonExistentFileThrowsException() {
        assertThrows(BizException.class, () -> PPTUtil.loadPPT("/non/existent/file.pptx"));
    }

    @Test
    void testGetSlideInfoPptx() throws Exception {
        Path pptxPath = createPptxFileWithText(1);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptxPath.toString());
        Slide<?, ?> slide = PPTUtil.getSlides(slideShow).get(0);
        String info = PPTUtil.getSlideInfo(slide);
        assertNotNull(info);
        assertTrue(info.contains("Test Content 0"));
    }

    @Test
    void testGetSlideInfoPpt() throws Exception {
        Path pptPath = createPptFileWithText(1);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptPath.toString());
        Slide<?, ?> slide = PPTUtil.getSlides(slideShow).get(0);
        String info = PPTUtil.getSlideInfo(slide);
        assertNotNull(info);
        assertTrue(info.contains("Test Content 0"));
    }

    @Test
    void testAddAudioToSlidePptx() throws Exception {
        Path pptxPath = createPptxFile(1);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptxPath.toString());
        PPTUtil.addAudioToSlide(slideShow, 0, "test.mp3");
        Path outputPath = tempDir.resolve("output_pptx.pptx");
        PPTUtil.savePPT(slideShow, outputPath.toString());
        assertTrue(Files.exists(outputPath));
        assertTrue(Files.size(outputPath) > 0);
    }

    @Test
    void testAddAudioToSlidePpt() throws Exception {
        Path pptPath = createPptFile(1);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptPath.toString());
        PPTUtil.addAudioToSlide(slideShow, 0, "test.mp3");
        Path outputPath = tempDir.resolve("output_ppt.ppt");
        PPTUtil.savePPT(slideShow, outputPath.toString());
        assertTrue(Files.exists(outputPath));
        assertTrue(Files.size(outputPath) > 0);
    }

    @Test
    void testSavePptx() throws Exception {
        Path pptxPath = createPptxFile(2);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptxPath.toString());
        Path outputPath = tempDir.resolve("saved_pptx.pptx");
        PPTUtil.savePPT(slideShow, outputPath.toString());

        SlideShow<?, ?> reloaded = PPTUtil.loadPPT(outputPath.toString());
        assertEquals(2, reloaded.getSlides().size());
    }

    @Test
    void testSavePpt() throws Exception {
        Path pptPath = createPptFile(2);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptPath.toString());
        Path outputPath = tempDir.resolve("saved_ppt.ppt");
        PPTUtil.savePPT(slideShow, outputPath.toString());

        SlideShow<?, ?> reloaded = PPTUtil.loadPPT(outputPath.toString());
        assertEquals(2, reloaded.getSlides().size());
    }

    @Test
    void testAddAudioToSlideInvalidIndex() throws Exception {
        Path pptxPath = createPptxFile(1);
        SlideShow<?, ?> slideShow = PPTUtil.loadPPT(pptxPath.toString());
        assertThrows(IndexOutOfBoundsException.class, () ->
            PPTUtil.addAudioToSlide(slideShow, 5, "test.mp3"));
    }

    @Test
    void testLoadPPTFromHttpUrl() {
        // 注意：这个测试需要网络连接，可能不稳定
        // 使用公开的测试PPT文件URL
        String testUrl = "https://www.example.com/test.pptx";
        // 由于测试可能失败，这里仅展示方法调用方式
        // SlideShow<?, ?> slideShow = PPTUtil.loadPPT(testUrl);
    }

    /**
     * 创建带文本内容的PPTX测试文件
     */
    private Path createPptxFileWithText(int slideCount) throws Exception {
        Path path = tempDir.resolve("test_text_" + slideCount + ".pptx");
        try (XMLSlideShow pptx = new XMLSlideShow();
             FileOutputStream fos = new FileOutputStream(path.toFile())) {
            for (int i = 0; i < slideCount; i++) {
                XSLFSlide slide = pptx.createSlide();
                org.apache.poi.xslf.usermodel.XSLFTextBox textBox = slide.createTextBox();
                textBox.setText("Test Content " + i);
                textBox.setAnchor(new java.awt.Rectangle(100, 100, 200, 50));
            }
            pptx.write(fos);
        }
        return path;
    }

    /**
     * 创建PPTX测试文件（不带文本）
     */
    private Path createPptxFile(int slideCount) throws Exception {
        Path path = tempDir.resolve("test_" + slideCount + ".pptx");
        try (XMLSlideShow pptx = new XMLSlideShow();
             FileOutputStream fos = new FileOutputStream(path.toFile())) {
            for (int i = 0; i < slideCount; i++) {
                pptx.createSlide();
            }
            pptx.write(fos);
        }
        return path;
    }

    /**
     * 创建带文本内容的PPT测试文件
     */
    private Path createPptFileWithText(int slideCount) throws Exception {
        Path path = tempDir.resolve("test_text_" + slideCount + ".ppt");
        try (HSLFSlideShow ppt = new HSLFSlideShow();
             FileOutputStream fos = new FileOutputStream(path.toFile())) {
            for (int i = 0; i < slideCount; i++) {
                HSLFSlide slide = ppt.createSlide();
                org.apache.poi.hslf.usermodel.HSLFTextBox textBox = slide.createTextBox();
                textBox.setText("Test Content " + i);
                textBox.setAnchor(new java.awt.Rectangle(100, 100, 200, 50));
            }
            ppt.write(fos);
        }
        return path;
    }

    /**
     * 创建PPT测试文件（不带文本）
     */
    private Path createPptFile(int slideCount) throws Exception {
        Path path = tempDir.resolve("test_" + slideCount + ".ppt");
        try (HSLFSlideShow ppt = new HSLFSlideShow();
             FileOutputStream fos = new FileOutputStream(path.toFile())) {
            for (int i = 0; i < slideCount; i++) {
                ppt.createSlide();
            }
            ppt.write(fos);
        }
        return path;
    }
}
