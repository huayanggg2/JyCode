package com.example.demo.controller.ezctlagent;

import ch.ethz.ssh2.Connection;
import com.example.demo.alltools.Jshell;
import com.example.demo.model.agent.Sshhost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class UpfileController {
    @Value("${logup.logfile}")
    private String logfile;

    @ResponseBody
    @RequestMapping(value = "/upfile/flist", method = RequestMethod.POST)
    public Map<String,Object> upfileController(HttpServletRequest request) throws FileNotFoundException {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Sshhost sshhost = new Sshhost();//创建主机对象
        Jshell jshell = new Jshell();//创建shell会话
        Connection conn = null;
        sshhost.setHostip("158.222.188.158");
        sshhost.setUsername("log");
        sshhost.setPassword("log");
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("fileList");//获取前端多文件集合
        //File fileDir = new File("src/main/resources/static/filedir/");//创建文件上传位置
        File fileDir = new File(logfile);//创建文件上传位置
        String filePath = fileDir.getAbsolutePath();//获取文件绝对路径
        for (int i = 0,fs = files.size(); i < fs; i++) {
            MultipartFile file = files.get(i);//循环得到文件
            if (file.isEmpty()) {
                resultMap.put("status", "0000");
                resultMap.put("message","成功");
                resultMap.put("syslst", "上传第" + (i++) + "个文件失败");
                return resultMap;
            }
            String fileName = "uplog" + i + ".txt";//文件名称
            String cmd = "cat /home/log/" + fileName + " > /home/log/tmp/aa.log";
            File dest = new File(filePath, fileName);
            try {
                file.transferTo(dest);
               /* String frw = FileRandW.readFile(dest);
                FileRandW.outFile(frw,filePath+"/"+fileName);*/
                conn = jshell.login(sshhost);
                jshell.scpagt(filePath + "/" + fileName, "/home/log/", conn);
                jshell.execute(conn, cmd);
            } catch (IOException e) {
                e.printStackTrace();
                resultMap.put("status", "0000");
                resultMap.put("message","成功");
                resultMap.put("syslst", "上传第" + (i++) + "个文件失败");
                return resultMap;
            }
        }
        resultMap.put("status", "0000");
        resultMap.put("message","成功");
        resultMap.put("syslst", "上传成功！请在平台输入：'SourceModuleName:客户合约_tmp.log' 查看日志");
        return resultMap;
    }

    @ResponseBody
    @RequestMapping(value = "/upfile/subwords", method = RequestMethod.POST)//
    public Map<String,Object> worldsUp(@RequestBody String words) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Sshhost sshhost = new Sshhost();//创建主机对象
        Jshell jshell = new Jshell();//创建shell会话
        Connection conn = null;
        sshhost.setHostip("158.222.188.158");
        sshhost.setUsername("log");
        sshhost.setPassword("log");
        FileWriter fwriter = null;
        //String filePath = "src/main/resources/static/filedir/";
        String fileName = "uplogmm.txt";//文件名称
        try {
            fwriter = new FileWriter(logfile + fileName);
            //fwriter.write(words + "\n");
            fwriter.write(words);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        String cmd = "chmod 755 /home/log/" + fileName + " && cat /home/log/" + fileName + " > /home/log/tmp/ab.log";//远程执行命令
        conn = jshell.login(sshhost);
        jshell.scpagt(logfile + "/" + fileName, "/home/log", conn);
        jshell.execute(conn, cmd);
        resultMap.put("status", "0000");
        resultMap.put("message","成功");
        //resultMap.put("sucescmd", "上传成功！请在平台输入：'SourceModuleName:客户合约_tmp.log' 查看日志");
        return resultMap;
    }


}
