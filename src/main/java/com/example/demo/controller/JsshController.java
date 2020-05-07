package com.example.demo.controller;

import ch.ethz.ssh2.Connection;
import com.example.demo.alltools.Jshell;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class JsshController {
    @RequestMapping("/execShell")//
    public String JsshController() {
        return "sshLogin.html";
    }

    @ResponseBody
    @RequestMapping("/sshLogin")//
    public String sshLogin(String hostip, String username, String password, String logdir) {
        String[] iparr = hostip.split(",");//将hostip拆分成数组
        Jshell jshell = new Jshell();
        Connection conn = null;
        if (iparr.length > 1) {//当ip个数大于1时
            String[] result;
            String lastres;
            String res = "";
            for (int i = 0; i < iparr.length; i++) {
                conn = Jshell.login(iparr[i], username, password);
                result = jshell.execute(conn, iparr[i], logdir).split("\n");
                lastres = iparr[i] + " " + result[result.length - 1];
                res += lastres + "\n";
            }
            return res;
        } else {//当ip个数为1时
            conn = Jshell.login(hostip, username, password);
            String res = jshell.execute(conn, hostip, logdir);
            return res;
        }
    }
}
