package com.ryqg.jiaofu.web.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.ryqg.jiaofu.business.common.Pdf2HtmlExConverter;
import com.ryqg.jiaofu.common.converter.PDFInfoConverter;
import com.ryqg.jiaofu.domain.pojo.PDFInfo;
import com.ryqg.jiaofu.domain.vo.PDFInfoVO;
import com.ryqg.jiaofu.web.mapper.PDFMapper;
import com.ryqg.jiaofu.web.service.PDFService;
import lombok.AllArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PDFServiceImpl implements PDFService {
    private static final String TMP_FILE_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "pdf" ;
    private static final String FILE_NAME = "index.html";
    private final PDFMapper pDFMapper;

    private final PDFInfoConverter pdfInfoConverter;


    @Override
    public String getPDFView(String docId) {
        HttpResponse httpResponse = HttpRequest.get("http://117.72.56.61/pdf/index.html").execute();
        if (HttpStatus.HTTP_OK != httpResponse.getStatus()) {
            try {
                Pdf2HtmlExConverter converter = new Pdf2HtmlExConverter("D:\\Users\\caozh\\Downloads\\pdf2htmlEX-win32-0.14.6-with-poppler-data\\pdf2htmlEX.exe");
                String folder = TMP_FILE_FOLDER + File.separator + docId;
                converter.setFolder(folder);
                converter.setFileName(FILE_NAME);
                String filePath = folder + File.separator + FILE_NAME;
                converter.convert("D:\\Users\\caozh\\Downloads\\pdftest\\※【期末复习易错系列】【期末易错题专项提升训练】五下数学.pdf");

                Document parse = Jsoup.parse(new File(filePath), "utf-8", "", Parser.htmlParser());
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
                NodeTraversor.traverse(visitor, parse);
                parse.select("[id=sidebar]").remove();
                parse.select("[id=page-container]").removeAttr("id");
                parse.select("script").remove();
                return parse.html();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return httpResponse.body();

    }

    @Override
    public List<PDFInfoVO> getPDFList() {
        List<PDFInfo> pdfInfos =  pDFMapper.selectPDFList();
        List<PDFInfoVO> pdfInfoVOS = new ArrayList<>();
        pdfInfos.forEach(pdfInfo -> {
            PDFInfoVO pdfInfoVO = pdfInfoConverter.toVO(pdfInfo);
            pdfInfoVOS.add(pdfInfoVO);
        });
        return pdfInfoVOS;
    }
}
