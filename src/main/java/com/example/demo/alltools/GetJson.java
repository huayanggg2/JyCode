package com.example.demo.alltools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

public class GetJson {
    public void readJsonFile() {{
        String today1 = "2020-04-08";
        String today2 = "2020-04-07";
        try {
            String path =GetJson.class.getClassLoader().getResource("allApi.json").getPath();
            File jsonFile = new File(path);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader;
            reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            JSONArray jay = new JSONArray(sb.toString());
            for (int i = 0;i<jay.length();i++){
                JSONObject jobt = jay.getJSONObject(i);
                System.out.println(jobt.getString("sysName"));
                System.out.println(jobt.getString("apiUrl").replaceFirst("today1",today1).replaceFirst("today2",today2));
                System.out.println(jobt.getString("serUrl").replaceFirst("today1",today1).replaceFirst("today2",today2));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    }
}
