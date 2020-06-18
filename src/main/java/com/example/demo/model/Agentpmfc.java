package com.example.demo.model;

public class Agentpmfc {
        private String acsdeip;//通信ip
        private String asvn;//探针版本
        private String ahostip;//管理Ip
        private String vxstatus;//探针进程状态
        private String cpu;//探针cpu
        private String syscpu;//系统cpu
        private String sysmem;//系统内存
        private String status;//自动控制开关

        public String getStatus() {
                return status;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public String getSyscpu() {
                return syscpu;
        }

        public void setSyscpu(String syscpu) {
                this.syscpu = syscpu;
        }

        public String getSysmem() {
                return sysmem;
        }

        public void setSysmem(String sysmem) {
                this.sysmem = sysmem;
        }

        public String getAcsdeip() {
                return acsdeip;
        }

        public void setAcsdeip(String acsdeip) {
                this.acsdeip = acsdeip;
        }

        public String getAsvn() {
                return asvn;
        }

        public void setAsvn(String asvn) {
                this.asvn = asvn;
        }

        public String getAhostip() {
                return ahostip;
        }

        public void setAhostip(String ahostip) {
                this.ahostip = ahostip;
        }

        public String getVxstatus() {
                return vxstatus;
        }

        public void setVxstatus(String vxstatus) {
                this.vxstatus = vxstatus;
        }

        public String getCpu() {
                return cpu;
        }

        public void setCpu(String cpu) {
                this.cpu = cpu;
        }

        public String getMem() {
                return mem;
        }

        public void setMem(String mem) {
                this.mem = mem;
        }

        private String mem;

}
