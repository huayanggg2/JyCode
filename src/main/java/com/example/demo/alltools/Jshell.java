package com.example.demo.alltools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import ch.ethz.ssh2.*;
import com.example.demo.model.agent.Sshhost;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jshell {
    private static final Logger log = LoggerFactory.getLogger(Jshell.class);
    private static String  DEFAULTCHART="UTF-8";
    private static final int TIME_OUT = 10 * 5 * 60;
    /**
     * 登录主机
     * @return
     *      登录成功返回true，否则返回false
     */
    public static Connection login(Sshhost sshhost){
        boolean flg=false;
        Connection conn = null;
        try {
            conn = new Connection(sshhost.getHostip());
            conn.connect();//连接
            flg=conn.authenticateWithPassword(sshhost.getUsername(), sshhost.getPassword());//认证
            if(flg){
                log.info("=========登录成功========="+conn);
                return conn;
            }
        } catch (IOException e) {
            log.error("=========登录失败========="+e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    //,String hostid,String username,String password,String logDir
    public void scpagt(String filepath, String logdir, Connection conn) {
        try {
            SCPClient client = new SCPClient(conn);
            client.put(filepath, logdir); //本地文件scp到远程目录
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String execute(Connection conn,String cmd){
        String result=null;
        try {
            if(conn !=null){

                Session session= conn.openSession();//打开一个会话
                session.execCommand(cmd);//执行命令
                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
                result=processStdout(session.getStdout(),DEFAULTCHART);
                //如果为得到标准输出为空，说明脚本执行出错了
                if(StringUtils.isBlank(result)){
                    log.info("得到标准输出为空,链接conn:"+conn+",执行的命令："+cmd);
                    result=processStdout(session.getStderr(),DEFAULTCHART);
                }else{
                    log.info("执行命令成功,链接conn:"+conn+",执行的命令："+cmd);
                }
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            log.info("执行命令失败,链接conn:"+conn+",执行的命令："+cmd+"  "+e.getMessage());
            e.printStackTrace();
        }catch (IllegalStateException ie){
            ie.printStackTrace();
            return "远程连接失败";
        }
        return result;
    }
    /**
     * 解析脚本执行返回的结果集
     * @param in 输入流对象
     * @param charset 编码
     * @return
     *       以纯文本的格式返回
     */
    private static String processStdout(InputStream in, String charset){
        InputStream  stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout,charset));
            String line=null;
            while((line=br.readLine()) != null){
                buffer.append(line+"\n");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("解析脚本出错："+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("解析脚本出错："+e.getMessage());
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
