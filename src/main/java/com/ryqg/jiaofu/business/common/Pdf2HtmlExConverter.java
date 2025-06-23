package com.ryqg.jiaofu.business.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Pdf2HtmlExConverter {
    private static final int DEFAULT_TIMEOUT_SECONDS = 5; // 默认超时时间 5 分钟
    private static final int BUFFER_SIZE = 8192; // 缓冲区大小

    private final String pdf2HtmlExPath; // 可执行文件路径
    private int timeoutSeconds = DEFAULT_TIMEOUT_SECONDS;
    private boolean embedCss = true;
    private boolean embedOutline = false;
    private boolean embedJavaScript = false;
    private boolean noDrm = true;
    private boolean processOutline = true;
    private String fileName;

    private String folder = System.getProperty("java.io.tmpdir") + File.separator + "pdf";


    /**
     * 构造函数，使用系统默认路径的 pdf2htmlEX
     */
    public Pdf2HtmlExConverter() {
        this("pdf2htmlEX.exe");
    }

    /**
     * 构造函数，指定 pdf2htmlEX 可执行文件路径
     * @param pdf2HtmlExPath 可执行文件路径
     */
    public Pdf2HtmlExConverter(String pdf2HtmlExPath) {
        this.pdf2HtmlExPath = pdf2HtmlExPath;
    }

    // 配置方法
    public Pdf2HtmlExConverter setTimeout(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
        return this;
    }

    public Pdf2HtmlExConverter setEmbedCss(boolean embedCss) {
        this.embedCss = embedCss;
        return this;
    }

    public Pdf2HtmlExConverter setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public Pdf2HtmlExConverter setProcessOutline(boolean processOutline) {
        this.processOutline = processOutline;
        return this;
    }

    public Pdf2HtmlExConverter setEmbedOutline(boolean embedOutline) {
        this.embedOutline = embedOutline;
        return this;
    }

    public Pdf2HtmlExConverter setNoDrm(boolean noDrm) {
        this.noDrm = noDrm;
        return this;
    }

    public Pdf2HtmlExConverter setEmbedJavaScript(boolean embedJavaScript) {
        this.embedJavaScript = embedJavaScript;
        return this;
    }

    public Pdf2HtmlExConverter setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    /**
     * 将 PDF 文件转换为 HTML 字节数组
     * @param pdfFile 输入的 PDF 文件
     * @return HTML 内容的字节数组
     * @throws Exception 转换过程中发生的异常
     */
    public String convert(String pdfFile) throws Exception {
        List<String> command = buildCommand();
        command.add(command.size() - 1,pdfFile);
        System.out.println(String.join(" ", command));
        Process pro = Runtime.getRuntime().exec(String.join(" ", command));
        pro.waitFor();
        return folder + File.separator + fileName;
    }


    /**
     * 构建 pdf2htmlEX 命令参数
     */
    private List<String> buildCommand() {
        //  --embed-css 1 --fit-width 900 --embed-javascript 0 --embed-outline 0 --process-outline 0 --tounicode -1 -f 1 -l 5 --no-drm 1  --process-outline 1
        List<String> command = new ArrayList<>();
        command.add(pdf2HtmlExPath);

        command.add("--embed-css");
        command.add(String.valueOf(embedCss ? 1 : 0));

        command.add("--fit-width");
        command.add("970");

        command.add("--embed-javascript");
        command.add(String.valueOf(embedJavaScript ? 1 : 0));

        command.add("--embed-outline");
        command.add(String.valueOf(embedOutline ? 1 : 0));

        command.add("--tounicode");
        command.add("-1");

        command.add("--embed-font");
        command.add("1");

        command.add("--optimize-text");
        command.add("1");

        command.add("--no-drm");
        command.add(String.valueOf(noDrm ? 1 : 0));

        command.add("--process-outline");
        command.add(String.valueOf(processOutline ? 1 : 0));

        command.add("-f");
        command.add("1");

        command.add("-l");
        command.add("5");

        command.add("--dest-dir");
        command.add(folder); // 输出到标准输出

        command.add("--process-outline");
        command.add("0"); // 禁用大纲处理以提高性能

        command.add("--process-annotation");
        command.add("0"); // 禁用大纲处理以提高性能

        command.add("--optimize-text");
        command.add("1"); // 禁用大纲处理以提高性能


        command.add( fileName);
        return command;
    }

    /**
     * 执行命令并返回输出结果
     */
    private void executeCommand(List<String> command) throws Exception {
        executeCommand(command, null);
    }

    /**
     * 执行命令并返回输出结果，支持向标准输入写入数据
     */
    public void executeCommand(List<String> command, byte[] inputData) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(false); // 不合并流，单独处理

        processBuilder.start();
    }
}
