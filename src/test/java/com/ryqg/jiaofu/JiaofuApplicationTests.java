package com.ryqg.jiaofu;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import com.ryqg.jiaofu.business.common.Pdf2HtmlExConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.UUID;

@SpringBootTest
class JiaofuApplicationTests {

    @Test
    void contextLoads() {
    }



    public static void main(String[] args) {
        try {
            // 示例 1: 从文件转换
            Pdf2HtmlExConverter converter = new Pdf2HtmlExConverter("D:\\Users\\caozh\\Downloads\\pdf2htmlEX-win32-0.14.6-with-poppler-data\\pdf2htmlEX.exe");
            String fileName = UUID.randomUUID() + ".html";
            converter.setFileName(fileName);
            String convertFileName = converter.convert("D:\\Users\\caozh\\Downloads\\pdftest\\※【期末复习易错系列】【期末易错题专项提升训练】五下数学.pdf");
            Document parse = Jsoup.parse(new File(convertFileName), "utf-8","", Parser.htmlParser());
            parse.outputSettings().prettyPrint(false);
            // 创建一个 NodeVisitor 来遍历节点

            NodeVisitor visitor = new NodeVisitor() {
                @Override
                public void head(Node node, int depth) {
                    if (node instanceof Comment) {
                        node.remove();
                    }
                }

            };
// 遍历文档
            NodeTraversor.traverse(visitor, parse);
            parse.select("[id=sidebar]").remove();
            parse.select("[id=page-container]").removeAttr("id");
            parse.select("script").remove();
            FileUtil.writeString(parse.html(),"D:\\Users\\caozh\\Downloads\\1111.html", CharsetUtil.UTF_8);
            System.out.println(parse.html());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
