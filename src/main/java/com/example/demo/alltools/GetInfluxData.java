package com.example.demo.alltools;


import com.example.demo.controller.influxchart.InfluxController;
import com.example.demo.model.mntrdtl.Hostrst1;
import com.example.demo.model.mntrdtl.Hostrst6;
import com.example.demo.model.mntrdtl.Hostrst7;
import com.example.demo.model.mntrdtl.Hostrst8;
import org.apache.commons.beanutils.BeanUtils;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Component
public class GetInfluxData {


    public List<QueryResult.Result> getInflux(String sql, InfluxDB influxDB) {
        Query query = new Query(sql, "telegraf");
        influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
        //毫秒输出
        QueryResult queryResult = influxDB.query(query, TimeUnit.MILLISECONDS);
        List<QueryResult.Result> resultList = queryResult.getResults();
        return resultList;
    }
      /*  String sss = queryResult.toString();
        System.out.println(sss);
        //两种方式都可以
        InfluxDBConfig config = new InfluxDBConfig(url, "telegraf");
        InfluxDB dbConfig = config.getInfluxDB();
        queryResult = dbConfig.query(query);
        System.out.println(queryResult);*/
        //把查询出的结果集转换成对应的实体对象，聚合成list
    public List changeRst(List<QueryResult.Result> resultList,Object object){
            String tscls = object.getClass().getName();
            List hostcpus = new ArrayList<>();
        for (QueryResult.Result result : resultList) {
            List<QueryResult.Series> seriesList = result.getSeries();
            for (QueryResult.Series series : seriesList) {
                //String name = series.getName();
                Map<String, String> tags = series.getTags();
                List<String> columns = series.getColumns();
                String[] keys = columns.toArray(new String[columns.size()]);
                List<List<Object>> values = series.getValues();
                for (List<Object> value : values) {
                    Map beanMap = new HashMap();
                    //Hostrst1 point = new Hostrst1();
                    //point.setHost(tags.get("host"));
                    for (int i = 0; i < keys.length; i++) {
                        beanMap.put(keys[i], value.get(i));
                    }
                    try {
                        if(object.getClass().equals(Hostrst7.class)) {
                            Hostrst7 newobj = new Hostrst7();
                            newobj.setPath(series.getTags().get("path"));
                            BeanUtils.populate(newobj, beanMap);
                            hostcpus.add(newobj);
                        }else if(object.getClass().equals(Hostrst6.class)) {
                            Hostrst6 newobj6 = new Hostrst6();
                            newobj6.setIntfc(series.getTags().get("interface"));
                            BeanUtils.populate(newobj6, beanMap);
                            hostcpus.add(newobj6);
                        }else if(object.getClass().equals(Hostrst8.class)) {
                            Hostrst8 newobj8 = new Hostrst8();
                            newobj8.setName(series.getTags().get("name"));
                            BeanUtils.populate(newobj8, beanMap);
                            hostcpus.add(newobj8);
                        }else {
                            Object newobj =null;
                            Class c1 = Class.forName(tscls);
                            newobj = c1.newInstance();
                            BeanUtils.populate(newobj, beanMap);
                            hostcpus.add(newobj);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

        return hostcpus;
    }
}