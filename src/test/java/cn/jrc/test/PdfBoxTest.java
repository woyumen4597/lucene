package cn.jrc.test;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Ref;
import java.util.Iterator;

/**
 * @author Created By Jrc
 * @version v.0.1
 * @date 2018/1/23 16:27
 */
public class PdfBoxTest {
    @Test
    public void createHellPdf() throws IOException {
        PDDocument doc = null;
        PDPage page = null;

        doc = new PDDocument();
        page = new PDPage();
        doc.addPage(page);
        PDFont font = PDType1Font.COURIER_BOLD;
        PDPageContentStream content = new PDPageContentStream(doc,page);
        content.beginText();
        content.setFont(font,12);
        content.newLineAtOffset(100,700);
        content.showText("Hello");
        content.endText();
        content.close();
        doc.save("./files/hello.pdf");
        doc.close();
    }


    @Test
    public void readPdf() throws IOException {
        PDDocument document = null;
        document = PDDocument.load(new File("./files/hello.pdf"));
        PDFTextStripper textStripper = new PDFTextStripper();
        System.out.println(textStripper.getText(document));
        document.close();
    }

    @Test
    public void PdfToImage() throws IOException {
        PDDocument document = PDDocument.load(new File("./files/hello.pdf"));
        int pageCount = document.getNumberOfPages();
        System.out.println(pageCount);
        PDFRenderer renderer = new PDFRenderer(document);
        for (int i = 0; i < pageCount; i++) {
            BufferedImage image = renderer.renderImageWithDPI(i, 300, ImageType.RGB);
            ImageIOUtil.writeImage(image,"./files/hello.png",300);
        }
        document.close();

    }
}
