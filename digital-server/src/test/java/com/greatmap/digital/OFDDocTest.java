package com.greatmap.digital;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.greatmap.digital.dto.rest.ZsDyxx;
import com.greatmap.digital.util.SnowFlakeIdWorker;
import com.greatmap.digital.util.StringUtils;
import org.dom4j.DocumentException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ofdrw.core.annotation.Annotations;
import org.ofdrw.core.annotation.pageannot.*;
import org.ofdrw.core.basicStructure.doc.Document;
import org.ofdrw.core.basicStructure.pageObj.layer.block.TextObject;
import org.ofdrw.core.basicType.ST_Box;
import org.ofdrw.core.basicType.ST_ID;
import org.ofdrw.core.basicType.ST_Loc;
import org.ofdrw.core.basicType.ST_RefID;
import org.ofdrw.core.text.TextCode;
import org.ofdrw.font.Font;
import org.ofdrw.font.FontName;
import org.ofdrw.font.FontSet;
import org.ofdrw.layout.OFDDoc;
import org.ofdrw.layout.PageLayout;
import org.ofdrw.layout.VirtualPage;
import org.ofdrw.layout.edit.AdditionVPage;
import org.ofdrw.layout.edit.Annotation;
import org.ofdrw.layout.edit.Attachment;
import org.ofdrw.layout.element.*;
import org.ofdrw.layout.element.canvas.Canvas;
import org.ofdrw.pkg.container.DocDir;
import org.ofdrw.pkg.container.OFDDir;
import org.ofdrw.pkg.container.PageDir;
import org.ofdrw.reader.OFDReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OFDDocTest {

    @Autowired
    private SnowFlakeIdWorker snowFlakeIdWorker;

    /**
     * 向文件中加入附件文件
     *
     * @throws IOException
     */
    @Test
    public void addAttachment() throws IOException {
        Path outP = Paths.get("target/AddAttachment.ofd");
        Path file = Paths.get("src/test/resources", "eg_tulip.jpg");
        Path file2 = Paths.get("src/test/resources", "NotoSerifCJKsc-Regular.otf");

        try (OFDDoc ofdDoc = new OFDDoc(outP)) {
            Paragraph p = new Paragraph();
            Span span = new Span("这是一个带有附件的OFD文件").setFontSize(10d);
            p.add(span);
            ofdDoc.add(p);

            // 加入附件文件
            ofdDoc.addAttachment(new Attachment("Gao", file));
            ofdDoc.addAttachment(new Attachment("FontFile", file2));
        }
        System.out.println("生成文档位置：" + outP.toAbsolutePath().toString());
    }

    /**
     * 替换附件文件
     *
     * @throws IOException
     */
    @Test
    public void replaceAttachment() throws IOException {
        Path srcP = Paths.get("src/test/resources/AddAttachment.ofd");
        Path outP = Paths.get("target/ReplaceAttachment.ofd");
        Path file = Paths.get("src/test/resources", "ASCII字体宽度测量.html");

        try (OFDReader reader = new OFDReader(srcP);
             OFDDoc ofdDoc = new OFDDoc(reader, outP)) {
            // 加入附件文件
            ofdDoc.addAttachment(new Attachment("Gao", file));
        }
        System.out.println("生成文档位置：" + outP.toAbsolutePath().toString());
    }

    /**
     * 加入印章类型注释对象
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void addAnnotationStamp() throws IOException, DocumentException {
        Path srcP = Paths.get("src/test/resources", "AddWatermarkAnnot.ofd");
        Path outP = Paths.get("target/AddAnnotationStamp.ofd");
        Path imgPath = Paths.get("src/test/resources", "StampImg.png");

        try (OFDReader reader = new OFDReader(srcP);
             OFDDoc ofdDoc = new OFDDoc(reader, outP)) {
            Annotation annotation = new Annotation(70d, 100d, 60d, 60d, AnnotType.Stamp, ctx -> {
                ctx.setGlobalAlpha(0.53);
                ctx.drawImage(imgPath, 0, 0, 40d, 40d);
            });
            ofdDoc.addAnnotation(1, annotation);

        }
        System.out.println("生成文档位置：" + outP.toAbsolutePath().toString());
    }


    /**
     * 加入水印类型注释对象
     *
     * @throws IOException
     * @throws DocumentException
     */
    @Test
    public void addAnnotation() throws IOException {
        Path srcP = Paths.get("src/test/resources", "拿来主义_page6.ofd");
        Path outP = Paths.get("target/AddWatermarkAnnot.ofd");
        Path imgPath = Paths.get("src/test/resources", "eg_tulip.jpg");

        try (OFDReader reader = new OFDReader(srcP);
             OFDDoc ofdDoc = new OFDDoc(reader, outP)) {
            ST_Box boundary = new ST_Box(50d, 50d, 60d, 60d);
            Annotation annotation = new Annotation(boundary, AnnotType.Watermark, ctx -> {
                ctx.setGlobalAlpha(0.53);
                ctx.drawImage(imgPath, 0, 0, 40d, 30d);
            });

            ofdDoc.addAnnotation(1, annotation);
            ofdDoc.addAnnotation(3, annotation);
            ofdDoc.addAnnotation(5, annotation);

        }
        System.out.println("生成文档位置：" + outP.toAbsolutePath().toString());
    }


    @Test
    public void addAnnot() throws IOException, DocumentException {
        Path srcP = Paths.get("src/test/resources", "helloworld.ofd");
        Path outP = Paths.get("target/AppendAnnot.ofd");
        try (OFDReader reader = new OFDReader(srcP)) {
            OFDDir ofdDir = reader.getOFDDir();
            DocDir docDir = ofdDir.obtainDocDefault();
            Document document = docDir.getDocument();
            document.setAnnotations(new ST_Loc(DocDir.AnnotationsFileName));
            Annotations annotations = new Annotations()
                    .addPage(new AnnPage()
                            .setPageID(new ST_ID(1))
                            .setFileLoc(new ST_Loc("Pages/Page_0/Annotation.xml")));
            docDir.setAnnotations(annotations);


            Annot annot = new Annot()
                    .setID(new ST_ID(5))
                    .setType(AnnotType.Stamp)
                    .setCreator("Cliven")
                    .setLastModDate(LocalDate.now());

            TextObject tObj = new TextObject(7);
            TextCode txc = new TextCode()
                    .setX(0d)
                    .setY(11d)
                    .setDeltaX(10d, 10d)
                    .setContent("嘿嘿");
            tObj.setBoundary(new ST_Box(0, 0, 50, 50))
                    .setFont(new ST_RefID(3))
                    .setSize(10d)
                    .addTextCode(txc);

            Appearance appearance = new Appearance(new ST_Box(40, 40, 50, 50))
                    .addPageBlock(tObj);
            appearance.setObjID(6);
            annot.setAppearance(appearance);

            PageAnnot pageAnnot = new PageAnnot()
                    .addAnnot(annot);
            PageDir pageDir = docDir.getPages().getByIndex(0);
            pageDir.setPageAnnot(pageAnnot);

            document.getCommonData().setMaxUnitID(7);

            ofdDir.jar(outP);
        }
        System.out.println("生成文档位置：" + outP.toAbsolutePath().toString());
    }

    /**
     * 测试加入操作系统中的字体
     *
     * @throws IOException
     */
    @Test
    public void testAddSysFont() throws IOException {
        Path outP = Paths.get("target/SystemFont1.ofd");
        try (OFDDoc doc = new OFDDoc(outP)) {
            PageLayout pageLayout = doc.getPageLayout();
            VirtualPage vPage = new VirtualPage(pageLayout);
            Font font = new Font("等线 Light", "等线 Light");
            Paragraph p = new Paragraph(60d,30d)
                    .setDefaultFont(font)
                    .setFontSize(10d)
                    .add("字体名称：等线 Light");
            //p.setMarginTop(10d);
            p.setPosition(Position.Absolute)
                    .setPadding(0d)
                    .setBorder(0.1d);
            p.setX(doc.getPageLayout().getWidth() /3 - p.getWidth() / 3).setY(20d);
            vPage.add(p);

            font = FontSet.get(FontName.KaiTi);
            Paragraph p1 = new Paragraph(60d,30d)
                    .setDefaultFont(font)
                    .setFontSize(10d)
                    .add("字体名称：楷体");
            //p.setMarginTop(10d);
            p1.setPosition(Position.Absolute)
                    .setPadding(0d)
                    .setBorder(0.1d);
            p1.setX(doc.getPageLayout().getWidth() /3 - p.getWidth() / 3 +60d).setY(20d);
            vPage.add(p1);
            doc.addVPage(vPage);

            // 注意：在使用操作系统字体时，默认采用ACSII 0.5 其余1的比例计算宽度，因此可能需要手动设置宽度比例才可以达到相应的效果
            font = new Font("Times New Roman", "Times New Roman")
                    .setPrintableAsciiWidthMap(FontSet.TIMES_NEW_ROMAN_PRINTABLE_ASCII_MAP);
            Paragraph p2 = new Paragraph(120d,30d)
                    .setDefaultFont(font)
                    .setFontSize(10d)
                    .add("Font Name: Time New Roman");
            p2.setBorder(0.1d);
            doc.add(p2);
        }
        System.out.println("生成文档位置：" + outP.toAbsolutePath().toString());
    }

    @Test
    public void appendTest() throws IOException {
        Path srcP = Paths.get("src/test/resources", "helloworld.ofd");
        Path outP = Paths.get("target/AppendNewPage3.ofd");
        //设置页面大小
        //PageLayout pageLayout = new PageLayout(167d,2420d);
        //pageLayout.setMargin(35.4D, 41.7D, 35.4D, 41.7D);
        try (OFDReader reader = new OFDReader(srcP);
             OFDDoc ofdDoc = new OFDDoc(reader, outP)) {
           // ofdDoc.setDefaultPageLayout(pageLayout);
            String plaintext = "小时候\n" +
                    "乡愁是一枚小小的邮票\n" +
                    "我在这头\n" +
                    "母亲在那头\n" +
                    "\n" +
                    "长大后\n" +
                    "乡愁是一张窄窄的船票\n" +
                    "我在这头\n" +
                    "新娘在那头\n" +
                    "\n" +
                    "后来啊\n" +
                    "乡愁是一方矮矮的坟墓\n" +
                    "我在外头\n" +
                    "母亲在里头\n" +
                    "\n" +
                    "而现在\n" +
                    "乡愁是一湾浅浅的海峡\n" +
                    "我在这头\n" +
                    "大陆在那头\n";
            Span titleContent = new Span("乡愁").setBold(true).setFontSize(13d).setLetterSpacing(20d);
            Paragraph title = new Paragraph().add(titleContent);
            title.setFloat(AFloat.center).setMarginBottom(5d).setBorder(0.1d);
            ofdDoc.add(title);
            final String[] txtCollect = plaintext.split("\\\n");
            for (String txt : txtCollect) {
                Paragraph p = new Paragraph().setFontSize(4d)
                        .setLineSpace(10d)
                        .add(txt);
                p.setFloat(AFloat.center).setBorder(0.1d);
                ofdDoc.add(p);
            }
        }
        System.out.println("生成文档位置：" + outP.toAbsolutePath().toString());
    }


    /**
     * 修改页面测试
     */
    @Test
    public void editTest() throws IOException {
        Path srcP = Paths.get("src/test/resources", "helloworld.ofd");
        Path outP = Paths.get("target/EditedDoc.ofd");
        try (OFDReader reader = new OFDReader(srcP);
             OFDDoc ofdDoc = new OFDDoc(reader, outP)) {
            AdditionVPage avPage = ofdDoc.getAVPage(1);
            Div e = new Div(10d, 10d)
                    .setPosition(Position.Absolute)
                    .setX(70d).setY(113.5)
                    .setBackgroundColor(255, 192, 203)
                    .setBorder(0.353d)
                    .setPadding(5d);

            avPage.add(e);
        }
        System.out.println("生成文档位置：" + outP.toAbsolutePath().toString());
    }


    @Test
    public void divBoxTest() throws IOException {
        Path path = Paths.get("target/VPage1.ofd").toAbsolutePath();
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            PageLayout pageLayout = ofdDoc.getPageLayout();
            VirtualPage vPage = new VirtualPage(pageLayout);
            Div e = new Div(10d, 10d)
                    .setPosition(Position.Absolute)
                    .setX(70d).setY(113.5)
                    .setBackgroundColor(30, 144, 255)
                    .setMargin(10d)
                    .setBorder(10d)
                    .setPadding(10d);
            vPage.add(e);
            ofdDoc.addVPage(vPage);
        }
        System.out.println("生成文档位置: " + path.toAbsolutePath());
    }

    @Test
    public void divBoxDiffBorderTest() throws IOException {
        Path path = Paths.get("target/VPage2.ofd").toAbsolutePath();
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            PageLayout pageLayout = ofdDoc.getPageLayout();
            VirtualPage vPage = new VirtualPage(pageLayout);
            Div e = new Div(10d, 10d)
                    .setPosition(Position.Absolute)
                    .setX(70d).setY(113.5)
                    .setBackgroundColor(30, 144, 255)
                    .setBorderColor(255, 0, 0)
                    .setMargin(10d)
                    .setBorderTop(10d).setBorderRight(7d).setBorderBottom(3d).setBorderLeft(0.5d)
                    .setPadding(10d);
            vPage.add(e);
            ofdDoc.addVPage(vPage);
        }
        System.out.println("生成文档位置: " + path.toAbsolutePath());
    }

    @Test
    public void imgTest() throws IOException {
        Path path = Paths.get("target/VPageOfPNG1111111.ofd").toAbsolutePath();
        try (OFDDoc ofdDoc = new OFDDoc(path)) {

            PageLayout pageLayout = ofdDoc.getPageLayout();
            VirtualPage vPage = new VirtualPage(pageLayout);
            Path imgPath = Paths.get("src/test/resources", "testimg.png");
            Img img = new Img(imgPath);

            double x = (pageLayout.getWidth() - img.getWidth()) / 3;
            double y = (pageLayout.getHeight() - img.getHeight()) / 3;
            img.setPosition(Position.Absolute)
                    .setX(x).setY(y);
            img.setHeight(12d);
            img.setWidth(12d);
            img.setBorder(3d);
            img.setPadding(3d);
            vPage.add(img);
            ofdDoc.addVPage(vPage);

            Paragraph p = new Paragraph(120d, 10d);
            p.add("我们无论遇到什么困难也不要怕，微笑着面对它，消除恐惧的最好办法就是直面恐惧，坚持就是胜利，加油！奥力给！ （OFD Reader & Write）");
            p.setPosition(Position.Absolute)
                    .setPadding(3d)
                    .setBorder(0.1d);
            x = pageLayout.getWidth() / 2 - p.getMarginLeft() - p.getWidth() / 2;
            p.setX(x).setY(pageLayout.getMarginTop());

            vPage.add(p);
            ofdDoc.addVPage(vPage);

            p = new Paragraph(120d, 10d);
            p.add("我们无论遇到什么困难也不要怕，微笑着面对它，消除恐惧的最好办法就是直面恐惧，坚持就是胜利，加油！奥力给！ （OFD Reader & Write）");
            p.setPosition(Position.Absolute)
                    .setPadding(3d)
                    .setBorder(0.1d);
            x = pageLayout.getWidth() / 2 - p.getMarginLeft() - p.getWidth() / 2;
            p.setX(x).setY(pageLayout.getMarginTop());

            vPage.add(p);
            ofdDoc.addVPage(vPage);

        }
    }

    @Test
    public void imgTestTif() throws IOException {
        Path path = Paths.get("target/VPageOfTIFF.ofd").toAbsolutePath();
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            PageLayout pageLayout = ofdDoc.getPageLayout();
            VirtualPage vPage = new VirtualPage(pageLayout);
            Path imgPath = Paths.get("src/test/resources", "asf-logo.tif");
            Img img = new Img(imgPath);

            double x = (pageLayout.getWidth() - img.getWidth()) / 2;
            double y = (pageLayout.getHeight() - img.getHeight()) / 2;
            img.setPosition(Position.Absolute)
                    .setX(x).setY(y);
            img.setBorder(3d);
            img.setPadding(3d);
            vPage.add(img);
            ofdDoc.addVPage(vPage);
        }
    }

    @Test
    public void paragraphTest() throws IOException {
        Path path = Paths.get("target/VPage4.ofd").toAbsolutePath();
        try (OFDDoc ofdDoc = new OFDDoc(path)) {

            PageLayout pageLayout = ofdDoc.getPageLayout();
            VirtualPage vPage = new VirtualPage(pageLayout);

            Paragraph p = new Paragraph(120d, 10d);
            p.add("我们无论遇到什么困难也不要怕，微笑着面对它，消除恐惧的最好办法就是直面恐惧，坚持就是胜利，加油！奥力给！ （OFD Reader & Write）");
            p.setPosition(Position.Absolute)
                    .setPadding(3d)
                    .setBorder(0.353);
            double x = pageLayout.getWidth() / 2 - p.getMarginLeft() - p.getWidth() / 2;
            p.setX(x).setY(pageLayout.getMarginTop());

            vPage.add(p);
            ofdDoc.addVPage(vPage);
        }
        System.out.println("生成文档位置: " + path.toAbsolutePath());
    }

    @Test
    public void paragraphTest2() throws IOException {
        Path path = Paths.get("target/VPage5.ofd").toAbsolutePath();
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            PageLayout pageLayout = ofdDoc.getPageLayout();
            VirtualPage vPage = new VirtualPage(pageLayout);

            Paragraph p = new Paragraph(120d, 10d);
            StringBuilder txt = new StringBuilder();
            for (char ch = 32; ch <= 126; ch++) {
                txt.append(ch);
            }
            p.add(txt.toString());
            p.setPosition(Position.Absolute)
                    .setPadding(3d)
                    .setBorder(0.353);
            double x = pageLayout.getWidth() / 2 - p.getMarginLeft() - p.getWidth() / 2;
            p.setX(x).setY(pageLayout.getMarginTop());
            vPage.add(p);

            ofdDoc.addVPage(vPage);
        }
    }


    @Test
    public void streamTestFloat() throws IOException {
        Path path = Paths.get("target/VPage6.ofd").toAbsolutePath();
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            Div eLeft = new Div(10d, 10d)
                    .setBackgroundColor(30, 144, 255)
                    .setFloat(AFloat.left)
                    .setBorder(10d)
                    .setPadding(10d);
            Div eCenter = new Div(10d, 10d)
                    .setBackgroundColor(30, 144, 255)
                    .setFloat(AFloat.center)
                    .setBorder(10d)
                    .setPadding(10d);
            Div eRight = new Div(10d, 10d)
                    .setBackgroundColor(30, 144, 255)
                    .setFloat(AFloat.right)
                    .setBorder(10d)
                    .setPadding(10d);

            ofdDoc.add(eLeft);
            ofdDoc.add(eCenter);
            ofdDoc.add(eRight);
        }
        System.out.println("生成文档位置: " + path.toAbsolutePath());
    }

    @Test
    public void streamTestDivPageSplit() throws IOException {
        Path path = Paths.get("target/VPage7.ofd").toAbsolutePath();
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            ofdDoc.setDefaultPageLayout(PageLayout.A4().setMargin(0d));
            for (int i = 0; i < 500; i++) {
                Div e = new Div(70d, 30d)
                        .setBackgroundColor(255, 0, 0)
                        .setFloat(AFloat.center)
                        .setMargin(5d)
                        .setBorder(0.353d);
                ofdDoc.add(e);
            }

        }
        System.out.println("生成文档位置: " + path.toAbsolutePath());
    }

    @Test
    public void streamTestParagraphPageSplit() throws IOException {
        Path path = Paths.get("target/VPage8.ofd").toAbsolutePath();
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            Span title = new Span("看云识天气").setBold(true).setFontSize(13d).setLetterSpacing(10d);
            Paragraph p = new Paragraph().add(title);
            p.setFloat(AFloat.center).setMargin(5d);
            ofdDoc.add(p);

            Span author = new Span("朱泳燚").setBold(true).setFontSize(3d);
            p = new Paragraph().add(author);
            p.setFloat(AFloat.center).setMargin(5d);
            ofdDoc.add(p);

            Paragraph p1 = new Paragraph("天上的云，姿态万千，变化无常：有的像羽毛，轻轻地飘在空中；有的像鱼鳞，一片片整整齐齐地排列着；有的像羊群，来来去去；有的像一张大棉絮，满满地盖住了天空；还有的像峰峦，像河川，像雄狮，像奔马……它们有时把天空点缀得很美丽，有时又把天空笼罩得很阴森。刚才还是白云朵朵，阳光灿烂；一霎间却又是乌云密布，大雨倾盆。云就像是天气的“招牌”，天上挂什么云，就将出现什么样的天气。 ")
                    .setFirstLineIndent(2);
            Paragraph p2 = new Paragraph("经验告诉我们：天空的薄云，往往是天气晴朗的象征；那些低而厚密的云层，常常是阴雨风雪的预兆。")
                    .setFirstLineIndent(2);
            Paragraph p3 = new Paragraph("那最轻盈、站得最高的云，叫卷云。这种云很薄，阳光可以透过云层照到地面，房屋和树木的影子依然很清晰。卷云丝丝缕缕地飘浮着，有时像一片白色的羽毛，有时像一块洁白的绫纱。如果卷云成群成行地排列在空中，好像微风吹过水面引起的粼波，这就成了卷积云。卷云和卷积云的位置很高，那里水分少，它们一般不会带来雨雪。还有一种像棉花团似的白云，叫积云，常在两千米左右的天空，一朵朵分散着，映着温和的阳光，云块四周散发出金黄的光辉。积云都在上午开始出现，午后最多，傍晚渐渐消散。在晴天，我们还会遇见一种高积云。这是成群的扁球状的云块，排列得很匀称，云块间露出碧蓝的天幕，远远望去，就像草原上雪白的羊群。卷云、卷积云、积云和高积云，都是很美丽的。 ")
                    .setFirstLineIndent(2);
            Paragraph p4 = new Paragraph("当那连绵的雨雪要来临的时候，卷云聚集着，天空渐渐出现一层薄云，仿佛蒙上了白色的绸幕。这种云叫卷层云。卷层云慢慢地向前推进，天气就要转阴。接着，云越来越低，越来越厚，隔着云看太阳和月亮，就像隔了一层毛玻璃，朦胧不清。这时的卷层云得改名换姓，该叫它高层云了。出现了高层云，往往在几个钟头内便要下雨或者下雪。最后，云压得更低，变得更厚，太阳和月亮都躲藏了起来，天空被暗灰色的云块密密层层地布满了。这种新的云叫雨层云。雨层云一形成，连绵不断的雨雪也就开始下降。 ")
                    .setFirstLineIndent(2);
            Paragraph p5 = new Paragraph("夏天，雷雨到来之前，在天空先会出现积云。积云如果迅速向上凸起，形成高大的云山，群峰争奇，耸入天顶，就变成了积雨云。积雨云越长越高，云底慢慢变黑，云峰渐渐模糊，不一会儿，整座云山崩塌了，乌云弥漫着天空，顷刻间，雷声隆隆，电光闪闪，就会哗啦哗啦地下起暴雨来，有时竟会带来冰冰雹或者龙卷风。")
                    .setFirstLineIndent(2);
            Paragraph p6 = new Paragraph("我们还可以根据云上的光彩，推测天气的情况。在太阳和月亮的周围，有时会出现一种美丽的七彩光圈，里层是红色的，外层是紫色的。这种光圈叫做晕。日晕和月晕常常出现在卷层云上，当卷层云后面有一大片高层云和雨层云时，是大风雨的征兆。所以有“日晕三更雨，月晕午时风”的说法。说明出现卷层云，并且伴有晕，天气就会变坏。另有一种比晕小的彩色光环，叫做华。颜色的排列是里紫外红，跟晕刚好相反。日华和月华大多出现在高积云的边缘。华环由小变大，天气将趋向晴好。华环由大变小，天气可能转为阴雨。夏天，雨过天晴，太阳对面的云幕上，常会挂上一条彩色的圆弧，这就是虹。人们常说：“东虹轰隆西虹雨。”意思说，虹在东方，就有雷无雨；虹在西方，将会有大雨。还有一种云彩常出现在清晨或傍晚。太阳照到天空，使云层变成红色，这种云彩叫做霞。出现朝霞，表明阴雨天气就要到来；出现晚霞，表示最近几天里天气晴朗。所以有“朝霞不出门，晚霞行千里”的谚语。")
                    .setFirstLineIndent(2);
            Paragraph p7 = new Paragraph("云能够帮助我们识别阴晴风雨，预知天气变化，这对工农业生产有着重要的意义。我们要学会看云识天气，就要虚心向有经验的人学习，留心观察云的变化，在反复实践中掌握它们的规律。但是，天气变化异常复杂，看云识天气有一定的限度。我们要准确地掌握天气变化的情况 ")
                    .setFirstLineIndent(2);
            ofdDoc.add(p1);
            ofdDoc.add(p2);
            ofdDoc.add(p3);
            ofdDoc.add(p4);
            ofdDoc.add(p5);
            ofdDoc.add(p6);
            ofdDoc.add(p7);
        }
        System.out.println("生成文档位置: " + path.toAbsolutePath());
    }

    @Test
    public void streamTestLineDiffPageSplit() throws IOException {
        Path path = Paths.get("target/VPage9.ofd").toAbsolutePath();
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            Paragraph p = new Paragraph();
            Span sp0 = new Span("我们无论遇到什么困难也不要怕，微笑着面对它，消除恐惧的最好办法就是直面恐惧，")
                    .setFontSize(5d).setItalic(true);
            p.add(sp0);
            ofdDoc.add(p);

            p = new Paragraph();
            Span sp1 = new Span("坚持就是胜利，")
                    .setFontSize(5d)
                    .setUnderline(true);
            Span sp2 = new Span("加油！")
                    .setFontSize(10d)
                    .setUnderline(true);
            Span sp3 = new Span("奥力给！")
                    .setColor(255, 0, 0)
                    .setBold(true)
                    .setFontSize(15d);

            p.add(sp1).add(sp2).add(sp3);
            ofdDoc.add(p);

        }
        System.out.println("生成文档位置: " + path.toAbsolutePath());
    }


    @Test
    public void elementOpacityTest() throws IOException {
        Path path = Paths.get("target/ElementOpacityDoc.ofd");
        try (OFDDoc ofdDoc = new OFDDoc(path)) {

            Div div = new Div(30d, 30d)
                    .setBackgroundColor(255, 0, 0)
                    .setMargin(3d)
                    .setBorder(1d)
                    .setOpacity(0.5)
                    .setFloat(AFloat.center);

            Path imgPath = Paths.get("src/test/resources", "asf-logo.tif");
            Img img = new Img(imgPath);
            img.setOpacity(0.3)
                    .setMargin(3d)
                    .setBorder(1d);

            Paragraph p = new Paragraph().setFontSize(10d)
                    .add("我们无论遇到什么困难也不要怕，微笑着面对它，" +
                            "消除恐惧的最好办法就是直面恐惧，坚持就是胜利，加油！奥力给！");
            p.setPadding(3d)
                    .setOpacity(0.2d)
                    .setMargin(3d)
                    .setBorder(1d, 2d, 3d, 4d);

            ofdDoc.add(div).add(img).add(p);
        }
        System.out.println("生成文档位置: " + path.toAbsolutePath());
    }

    @Test
    public void splitStrToParagraph() throws IOException {
        String plaintext = "只……只要我把那家伙给拖进来……\n交给你的话……\n你……你真的会……饶我一命吗？" +
                "嘻嘻~当然\n我可是说话算话的啦~\n这算是以他的养分为筹码的交易Give&Take啦……快……快点叫吧！\n" +
                "但是我拒绝！\n——JOJO 岸边露伴";

        Path path = Paths.get("target/SplitStrToParagraphDoc.ofd");
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            for (String pTxt : plaintext.split("\\\n")) {
                Paragraph p = new Paragraph(pTxt);
                ofdDoc.add(p);
            }
        }
        System.out.println("生成文档位置: " + path.toAbsolutePath());
    }

    @Test
    public void splitStrToParagraph2() throws IOException {
        String plaintext = "只……只要我把那家伙给拖进来……\n交给你的话……\n你……你真的会……饶我一命吗？\n\n" +
                "嘻嘻~当然\n我可是说话算话的啦~\n这算是以他的养分为筹码的交易Give&Take啦……快……快点叫吧！\n\n\n\n" +
                "但是我拒绝！\n——JOJO 岸边露伴";

        Path path = Paths.get("target/SplitStrToParagraphDoc2.ofd");
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            Paragraph p = new Paragraph(plaintext);
            ofdDoc.add(p);
            ofdDoc.add(new Paragraph("\nOFD R&W"));
        }
        System.out.println("生成文档位置: " + path.toAbsolutePath());
    }

    @Test
    public void fillPageParagraph() throws IOException {
        Path path = Paths.get("target/FillPageParagraphDoc.ofd").toAbsolutePath();
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            VirtualPage virtualPage = new VirtualPage(210.0, 140.0);
            virtualPage.getStyle().setMargin(0d);
            Paragraph p = new Paragraph("helloword");
            p.setPosition(Position.Absolute);
            p.setX(0.0);
            p.setY(0.0);
            p.setWidth(210.0 - 0.5);
            p.setHeight(140.0 - 0.5);
            p.setBorder(0.25);
            p.setPadding(0.0);
            p.setMargin(0.0);
            virtualPage.add(p);
            ofdDoc.addVPage(virtualPage);
        }
        System.out.println("生成文档位置：" + path.toAbsolutePath());
    }

    @Test
    public void canvasInflow() throws IOException {
        Path path = Paths.get("target/CanvasInflow.ofd").toAbsolutePath();
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            Paragraph p = new Paragraph("这是一个圆形哦");
            p.setClear(Clear.none);
            Canvas canvas = new Canvas(20d, 20d);
            canvas.setClear(Clear.none);
            canvas.setDrawer(ctx -> {
                ctx.beginPath();
                ctx.arc(10, 10, 5, 0, 360);
                ctx.stroke();
            });
            Paragraph p2 = new Paragraph("是不是很好看");
            p2.setClear(Clear.none);
            ofdDoc.add(p)
                    .add(canvas)
                    .add(p2);
        }
        System.out.println("生成文档位置：" + path.toAbsolutePath());
    }

    @Test
    public void testDrawZs() throws Exception{
        Path path = Paths.get("target/drawZs.ofd").toAbsolutePath();

        ZsDyxx zsDyxx = new ZsDyxx();
        zsDyxx.setJc("甘");
        zsDyxx.setNf("2020");
        zsDyxx.setX("凉州区");
        zsDyxx.setBh("0008145");
        zsDyxx.setDjsj("2018-02-12");
        zsDyxx.setQlr("张三");
        zsDyxx.setGyqk("单独所有");
        zsDyxx.setBdcqzh("甘（2020）凉州区不动产权证明第0008145号");
        zsDyxx.setZl("XX县XX镇XX村");
        zsDyxx.setBdcdyh("130637008004GB00008F00040090");
        zsDyxx.setQllx("权利类型");
        zsDyxx.setQlxz("权利性质");
        zsDyxx.setYt("用途");
        zsDyxx.setMj("面积");
        zsDyxx.setSyqx("使用期限");
        zsDyxx.setQlqtzk("权利其他状况");
        zsDyxx.setFj("附记");
        zsDyxx.setCxewm("宁夏电子证照演示生成二维码");

        //创建OFDDoc对象
        try (OFDDoc ofdDoc = new OFDDoc(path)) {
            //创建虚拟页面对象
            Font font = FontSet.get(FontName.NotoSerifBold);

            drawFirstPage(ofdDoc,font);

            drawSecondPage(ofdDoc, font, zsDyxx);

            drawThirdPage(ofdDoc,font,zsDyxx);

            drawFourthPage(ofdDoc,font,zsDyxx);

            drawFifthPage(ofdDoc,font,zsDyxx);

        }
        System.out.println("生成文档位置：" + path.toAbsolutePath());
    }

    public void drawFirstPage(OFDDoc ofdDoc,Font font) throws Exception{
        VirtualPage virtualPage = new VirtualPage(167.0d, 240.0d);
        //创建最外层DIV，设置背景色
        Div div = new Div(167.0d, 240.0d)
                .setPosition(Position.Absolute)
                .setX(0d)
                .setY(0d)
                .setBackgroundColor(141, 29, 43)
                .setBorder(0.353)
                .setClear(Clear.none)
                .setFloat(AFloat.center);
        virtualPage.add(div);
        virtualPage.getStyle().setMargin(0d);


        Path imgPath = Paths.get("src/test/resources", "证书国徽.jpg");
        //创建IMG对象，引入图片
        Img img = new Img(imgPath);

        img.setPosition(Position.Absolute)
                .setX(55d).setY(48d);
        img.setHeight(56d);
        img.setWidth(60.5d);
        img.setBorder(0d);
        img.setPadding(0d);
        virtualPage.add(img);

        //创建段落对象，设置文字属性
        Span span = new Span("中华人民共和国")
                .setFont(font)
                .setFontSize(8d)
                .setLetterSpacing(5d);
        Paragraph p = new Paragraph(114.0d,19.5d);
        p.setFloat(AFloat.center);
        p.setPosition(Position.Absolute);
        p.setX(33.0d);
        p.setY(145.0d);
        p.setPadding(0.0);
        p.setMargin(0.0);
        p.add(span);
        virtualPage.add(p);

        span = new Span("不动产权证书")
                .setFont(font)
                .setFontSize(8d)
                .setLetterSpacing(7d);
        p = new Paragraph(95.0d,16.5d);
        p.setFloat(AFloat.center);
        p.setPosition(Position.Absolute);
        p.setX(36.0d);
        p.setY(182.0d);
        p.setPadding(0.0);
        p.setMargin(0.0);
        p.add(span);
        virtualPage.add(p);

        ofdDoc.addVPage(virtualPage);
    }

    public void drawSecondPage(OFDDoc ofdDoc,Font font,ZsDyxx zsDyxx) throws Exception{
        VirtualPage virtualPage2 = new VirtualPage(167.0d, 240.0d);
        Div div2 = new Div(167.0d, 240.0d)
                .setPosition(Position.Absolute)
                .setX(0d)
                .setY(0d)
                .setBackgroundColor(255, 255, 255)
                .setBorder(0.353)
                .setClear(Clear.none)
                .setFloat(AFloat.center);
        virtualPage2.add(div2);

        Div div3 = new Div(146.5d, 220.0d)
                .setPosition(Position.Absolute)
                .setX(10d)
                .setY(10d)
                .setBackgroundColor(255, 255, 255)
                .setBorder(0.353)
                .setClear(Clear.none)
                .setFloat(AFloat.center);
        virtualPage2.add(div3);

        virtualPage2.getStyle().setMargin(0d);

        Paragraph tempP = new Paragraph().setLineSpace(8d);
        tempP.setPosition(Position.Absolute)
                .setWidth(102.5d)
                .setHeight(72.5d)
                .setX(32d)
                .setY(65d);
        tempP.setFloat(AFloat.center);

        Span tempSpan = new Span("  根据《中华人民共和国物权法》等法律 ")
                .setFont(font)
                .setFontSize(3d)
                .setLetterSpacing(2d);
        tempP.add(tempSpan);

        tempSpan = new Span("法规，为保护不动产权利人合法权益，对 ")
                .setFont(font)
                .setFontSize(3d)
                .setLetterSpacing(2d);
        tempP.add(tempSpan);

        tempSpan = new Span("不动产权利人申请登记的本证所列不动产 ")
                .setFont(font)
                .setFontSize(3d)
                .setLetterSpacing(2d);
        tempP.add(tempSpan);

        tempSpan = new Span("权利，经审查核实，准予登记，颁发此证 ")
                .setFont(font)
                .setFontSize(3d)
                .setLetterSpacing(2d);
        tempP.add(tempSpan);

        virtualPage2.add(tempP);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String text = zsDyxx.getCxewm();

        BitMatrix bitMatrix = qrCodeWriter.encode(new String(text.getBytes("UTF-8"), "ISO-8859-1"),
                BarcodeFormat.QR_CODE, 50, 45);

        String qrCodeImgFile = snowFlakeIdWorker.nextId() + ".PNG";

        //前提保证系统存在 D:\tempDir\qrCodeFile 路径
        Path path = FileSystems.getDefault().getPath("D:\\tempDir\\qrCodeFile\\"+qrCodeImgFile);

        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        Path ewmImgPath = Paths.get(path.toString());
        Img ewmImg = new Img(ewmImgPath);

        ewmImg.setPosition(Position.Absolute)
                .setX(20d).setY(152.5d);
        ewmImg.setHeight(50d);
        ewmImg.setWidth(45d);
        ewmImg.setBorder(0d);
        ewmImg.setPadding(0d);
        virtualPage2.add(ewmImg);

        //TODO 目前删除失败
        if(StringUtils.isNotBlank(path.toString())){
            File file = new File(path.toString());
            if(file.exists() && !file.isDirectory() && file.isFile()){
                boolean delete = file.delete();
                System.out.println("文件是否删除成功：" + delete);
            }
        }

        Div tempDiv = new Div();
        tempDiv.setPosition(Position.Absolute)
                .setWidth(42d)
                .setHeight(45d)
                .setX(91.5d)
                .setY(137.5d);
        virtualPage2.add(tempDiv);

        tempP = new Paragraph().setLineSpace(8d);
        tempP.setPosition(Position.Absolute)
                .setWidth(42d)
                .setHeight(45d)
                .setX(91.5d)
                .setY(152.5d);
        tempP.setFloat(AFloat.center);

        tempSpan = new Span("登记机构（章）")
                .setFont(font)
                .setFontSize(3d)
                .setLetterSpacing(1d)
                .setLinebreak(true);
        tempP.add(tempSpan);

        String djsjSpanText = "";
        if(StringUtils.isNotBlank(zsDyxx.getDjsj())){
            String[] djsjArr = zsDyxx.getDjsj().split("-");
            if(djsjArr.length == 3){
                djsjSpanText = djsjArr[0] + "年" + djsjArr[1] + "月" + djsjArr[2] + "日";
            }
        }

        tempSpan = new Span(djsjSpanText)
                .setFont(font)
                .setFontSize(3d)
                .setLetterSpacing(1d)
                .setLinebreak(true);
        tempP.add(tempSpan);
        virtualPage2.add(tempP);

        tempP = new Paragraph("中华人民共和国自然资源部监制").setLineSpace(2d);
        tempP.setPosition(Position.Absolute)
                .setWidth(90d)
                .setHeight(11d)
                .setX(72d)
                .setY(190d);
        tempP.setFloat(AFloat.center);
        virtualPage2.add(tempP);

        tempP = new Paragraph("编号 No                 ").setLineSpace(2d);
        tempP.setPosition(Position.Absolute)
                .setWidth(59d)
                .setHeight(11d)
                .setX(79d)
                .setY(201d);
        tempP.setFloat(AFloat.center);
        virtualPage2.add(tempP);


        ofdDoc.addVPage(virtualPage2);
    }

    public void drawThirdPage(OFDDoc ofdDoc,Font font,ZsDyxx zsDyxx){
        VirtualPage virtualPage3 = new VirtualPage(167.0d, 240.0d);
        Div div4 = new Div(167.0d, 240.0d)
                .setPosition(Position.Absolute)
                .setX(0d)
                .setY(0d)
                .setBackgroundColor(255, 255, 255)
                .setBorder(0.353)
                .setClear(Clear.none)
                .setFloat(AFloat.center);
        virtualPage3.add(div4);

        String bdcqzhText = "  （     ）   不动产权第    号  ";
        if(StringUtils.isNotBlank(zsDyxx.getJc()) && StringUtils.isNotBlank(zsDyxx.getNf())
                && StringUtils.isNotBlank(zsDyxx.getX()) && StringUtils.isNotBlank(zsDyxx.getBh())){
            bdcqzhText = zsDyxx.getJc() + "（" + zsDyxx.getNf() + "）" + zsDyxx.getX() + "不动产权第" + zsDyxx.getBh() + "号";
        }

        Paragraph threeTempPara = new Paragraph(bdcqzhText)
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(105d)
                .setHeight(10.5d)
                .setX(40.5d)
                .setY(15d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph();
        Span tempSpan = new Span("权利人").setFont(font).setFontSize(3d).setLetterSpacing(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(25d)
                .setHeight(13.5d)
                .setX(17.5d)
                .setY(25.5d).setBorder(0.1d);
        threeTempPara.add(tempSpan);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph();
        tempSpan = new Span(zsDyxx.getQlr()).setFont(font).setFontSize(3d).setLetterSpacing(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(108d)
                .setHeight(13.5d)
                .setX(42.5d)
                .setY(25.5d).setBorder(0.1d);
        threeTempPara.add(tempSpan);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph();
        tempSpan = new Span("共有情况").setFont(font).setFontSize(3d).setLetterSpacing(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(25d)
                .setHeight(13.5d)
                .setX(17.5d)
                .setY(39d).setBorder(0.1d);
        threeTempPara.add(tempSpan);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph();
        tempSpan = new Span(zsDyxx.getGyqk()).setFont(font).setFontSize(3d).setLetterSpacing(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(108d)
                .setHeight(13.5d)
                .setX(42.5d)
                .setY(39d).setBorder(0.1d);
        threeTempPara.add(tempSpan);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph("坐落")
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(25d)
                .setHeight(13.5d)
                .setX(17.5d)
                .setY(52.5d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph(zsDyxx.getZl())
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(108d)
                .setHeight(13.5d)
                .setX(42.5d)
                .setY(52.5d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph("不动产单元号")
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(25d)
                .setHeight(13.5d)
                .setX(17.5d)
                .setY(66d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph(zsDyxx.getBdcdyh())
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(108d)
                .setHeight(13.5d)
                .setX(42.5d)
                .setY(66d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph("权利类型")
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(25d)
                .setHeight(13.5d)
                .setX(17.5d)
                .setY(79.5d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph(zsDyxx.getQllx())
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(108d)
                .setHeight(13.5d)
                .setX(42.5d)
                .setY(79.5d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph("权利性质")
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(25d)
                .setHeight(13.5d)
                .setX(17.5d)
                .setY(93d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph(zsDyxx.getQlxz())
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(108d)
                .setHeight(13.5d)
                .setX(42.5d)
                .setY(93d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph("用途")
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(25d)
                .setHeight(13.5d)
                .setX(17.5d)
                .setY(106.5d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph(zsDyxx.getYt())
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(108d)
                .setHeight(13.5d)
                .setX(42.5d)
                .setY(106.5d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph("面积")
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(25d)
                .setHeight(13.5d)
                .setX(17.5d)
                .setY(120d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph(zsDyxx.getMj())
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(108d)
                .setHeight(13.5d)
                .setX(42.5d)
                .setY(120d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph("使用期限")
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(25d)
                .setHeight(13.5d)
                .setX(17.5d)
                .setY(133.5d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph(zsDyxx.getSyqx())
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(108d)
                .setHeight(13.5d)
                .setX(42.5d)
                .setY(133.5d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph("权利其他状况")
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(25d)
                .setHeight(75.5d)
                .setX(17.5d)
                .setY(147d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        threeTempPara = new Paragraph(zsDyxx.getQlqtzk())
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        threeTempPara.setPosition(Position.Absolute)
                .setWidth(108d)
                .setHeight(75.5d)
                .setX(42.5d)
                .setY(147d).setBorder(0.1d);
        threeTempPara.setFloat(AFloat.center);
        virtualPage3.add(threeTempPara);

        ofdDoc.addVPage(virtualPage3);
    }

    public void drawFourthPage(OFDDoc ofdDoc,Font font,ZsDyxx zsDyxx){
        VirtualPage virtualPage4 = new VirtualPage(167.0d, 240.0d);
        Div div5 = new Div(167.0d, 240.0d)
                .setPosition(Position.Absolute)
                .setX(0d)
                .setY(0d)
                .setBackgroundColor(255, 255, 255)
                .setBorder(0.353)
                .setClear(Clear.none)
                .setFloat(AFloat.center);
        virtualPage4.add(div5);

        Paragraph fourTempPara = new Paragraph("附     记")
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        fourTempPara.setPosition(Position.Absolute)
                .setWidth(25d)
                .setHeight(8d)
                .setX(70d)
                .setY(15d);
        fourTempPara.setFloat(AFloat.center);
        virtualPage4.add(fourTempPara);

        Div div6 = new Div(133.0d, 197.0d)
                .setPosition(Position.Absolute)
                .setX(17.5d)
                .setY(25.5d)
                .setBackgroundColor(255, 255, 255)
                .setBorder(0.353)
                .setClear(Clear.none)
                .setFloat(AFloat.center);
        virtualPage4.add(div6);

        ofdDoc.addVPage(virtualPage4);
    }

    public void drawFifthPage(OFDDoc ofdDoc,Font font,ZsDyxx zsDyxx){
        VirtualPage virtualPage5 = new VirtualPage(167.0d, 240.0d);
        Div div6 = new Div(167.0d, 240.0d)
                .setPosition(Position.Absolute)
                .setX(0d)
                .setY(0d)
                .setBackgroundColor(255, 255, 255)
                .setBorder(0.353)
                .setClear(Clear.none)
                .setFloat(AFloat.center);
        virtualPage5.add(div6);

        Paragraph fiveTempPara = new Paragraph("附  图  页")
                .setDefaultFont(font).setFontSize(3d).setLineSpace(2d);
        fiveTempPara.setPosition(Position.Absolute)
                .setWidth(25d)
                .setHeight(8d)
                .setX(72d)
                .setY(15d);
        fiveTempPara.setFloat(AFloat.center);
        virtualPage5.add(fiveTempPara);

        Div div7 = new Div(133.0d, 197.0d)
                .setPosition(Position.Absolute)
                .setX(17.5d)
                .setY(25.5d)
                .setBackgroundColor(255, 255, 255)
                .setBorder(0.353)
                .setClear(Clear.none)
                .setFloat(AFloat.center);
        virtualPage5.add(div7);

        Div div8 = new Div(123.0d, 187.0d)
                .setPosition(Position.Absolute)
                .setX(22.5d)
                .setY(30.5d)
                .setBackgroundColor(255, 255, 255)
                .setBorder(0.353)
                .setClear(Clear.none)
                .setFloat(AFloat.center);
        virtualPage5.add(div8);

        ofdDoc.addVPage(virtualPage5);
    }

}