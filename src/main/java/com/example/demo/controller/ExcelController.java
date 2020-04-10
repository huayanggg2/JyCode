package com.example.demo.controller;

import com.example.demo.model.Jydata;
import com.example.demo.model.Jydetail;
import com.example.demo.service.JydataService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;


@RestController
@RequestMapping("/excel")
public class ExcelController {
    @Autowired
    private JydataService jydataService;

    private void createTitle(HSSFWorkbook workbook, HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,18*256);
        sheet.setColumnWidth(2,10*256);
        HSSFCell cell;
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
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
    @GetMapping(value = "/getUser")
    public String getUser(HttpServletResponse response,HttpServletRequest request) throws Exception{

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("统计表");
        createTitle(workbook,sheet);
        //获取导出数据
        List<Jydata> rows = (List<Jydata>) request.getSession().getAttribute("list");

        //设置日期格式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

        //新增数据行，并且设置单元格数据
        int rowNum=1;
        for(Jydata jydata:rows){
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(jydata.getJysystm());//设置交易系统
            row.createCell(1).setCellValue(jydata.getJytime());//设置交易日期
            row.createCell(2).setCellValue(jydata.getJycount());//设置总交易量
            HSSFCell cell = row.createCell(3);
            cell.setCellStyle(style);
            rowNum++;
        }

        String fileName = "交易信息表.xls";
        //生成excel文件
        buildExcelFile(fileName, workbook);
        //浏览器下载excel
        buildExcelDocument(fileName,workbook,response);
        return "download excel";
    }
    @GetMapping(value = "/getDetail")
    public String getDetail(HttpServletResponse response,HttpServletRequest request) throws Exception{
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("统计表");
        createTitleDet(workbook,sheet);
        //获取导出数据
        List<Jydetail> rows = (List<Jydetail>) request.getSession().getAttribute("delist");

        //设置日期格式
        HSSFCellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));

        //新增数据行，并且设置单元格数据
        int rowNum=1;
        for(Jydetail jydetail:rows){
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(jydetail.getSerapi());//设置交易系统
            row.createCell(1).setCellValue(jydetail.getSuces());//设置交易日期
            row.createCell(2).setCellValue(jydetail.getFails());//设置总交易量
            row.createCell(3).setCellValue(jydetail.getPerct());//设置总交易量
            HSSFCell cell = row.createCell(4);
            cell.setCellStyle(style);
            rowNum++;
        }

        String fileName = "交易信息表.xls";
        //生成excel文件
        buildExcelFile(fileName, workbook);
        //浏览器下载excel
        buildExcelDocument(fileName,workbook,response);
        return "download excel";
    }
    private void createTitleDet(HSSFWorkbook workbook, HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,45*256);
        sheet.setColumnWidth(1,12*256);
        sheet.setColumnWidth(2,12*256);
        sheet.setColumnWidth(3,12*256);
        HSSFCell cell;
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置表头
        cell = row.createCell(0);
        cell.setCellValue("serviceApi");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("成功笔数");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("失败笔数");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("成功率(%)");
        cell.setCellStyle(style);


    }

    //生成excel文件
    protected void buildExcelFile(String filename,HSSFWorkbook workbook) throws Exception{
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    //浏览器下载excel
    protected void buildExcelDocument(String filename,HSSFWorkbook workbook,HttpServletResponse response) throws Exception{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
