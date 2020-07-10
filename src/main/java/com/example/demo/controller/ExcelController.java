package com.example.demo.controller;

import com.example.demo.model.jysys.Jydata;
import com.example.demo.service.JydataService;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static sun.security.util.Pem.decode;

@RestController
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    private JydataService jydataService;

    private void createTitle(XSSFWorkbook workbook, XSSFSheet sheet) {
        XSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0, 15 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        XSSFCell cell;
        //设置为居中加粗
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置表头
        cell = row.createCell(0);
        cell.setCellValue("交易系统");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("交易时间");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("总交易量");
        cell.setCellStyle(style);


    }

    @RequestMapping(value = "/getUser", method = RequestMethod.POST)
    public String getUser(HttpServletResponse response, HttpServletRequest request) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("统计表");
        createTitle(workbook, sheet);
        //获取导出数据
        List<Jydata> rows = (List<Jydata>) request.getSession().getAttribute("list");

        //设置日期格式
        XSSFCellStyle style = workbook.createCellStyle();
        //style.setDataFormat(XSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        List<byte[]> images = new ArrayList<>();

        try {
            images.add(decode(request.getParameter("imagesBase64").substring(22)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置宽高
        sheet.setDefaultRowHeight((short) (350 * 30 / 25));
        sheet.setColumnWidth(10, (int) (400 * 1990 / 140));


        //将获取到的base64 编码转换成图片，画到excel中
        XSSFDrawing patriarch = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = null;
        int index = 1;
        for (byte[] image : images) {
            anchor = new XSSFClientAnchor(0, 0, 0, 0, 4, 1, 13, 20);
            Picture pict = patriarch.createPicture(anchor, workbook.addPicture(image, XSSFWorkbook.PICTURE_TYPE_PNG));
            pict.resize(0.9);
            index++;
        }
        // 工程路径（根据个人需要，路径可以写简单的磁盘路径）
        String parentPath = (new File(getClass().getResource("/").getFile()
                .toString())).getParentFile().getParent();
        String path = request.getContextPath();
        String fileName = parentPath + File.separator + "csvfile"
                + File.separator + "交易量统计图.xlsx";
        File file = new File(fileName);


        if (file.isFile()) {
            file.delete();
        }
        // 规定的路径下，生成excel表格
        String filePath = path + File.separator + "csvfile"
                + File.separator + "交易量统计图.xlsx";
        //新增数据行，并且设置单元格数据
        int rowNum = 1;
        for (Jydata jydata : rows) {
            XSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(jydata.getJysystm());//设置交易系统
            row.createCell(1).setCellValue(jydata.getJytime());//设置交易日期
            row.createCell(2).setCellValue(jydata.getJycount());//设置总交易量
            XSSFCell cell = row.createCell(3);
            cell.setCellStyle(style);
            rowNum++;
        }

        //String fileName = "交易信息表.xls";
        //生成excel文件
        buildExcelFile(fileName, workbook);
        //浏览器下载excel
        buildExcelDocument(fileName, workbook, response);
        return filePath;
    }


    //生成excel文件
    protected void buildExcelFile(String filename, XSSFWorkbook workbook) throws Exception {
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    //浏览器下载excel
    protected void buildExcelDocument(String filename, XSSFWorkbook workbook, HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
