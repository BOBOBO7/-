package menu;

import bean.Token;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccessTokenUtils {
    public static final String APPID="wx8be7c5522deb5aa8";
    public static final String APPSECRET="4e5b793a7e72f76be759f830d9272f77";
    public static final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    public static String doGet(String urlPath) throws IOException {
//        创建URL对象，建立远程连接
        URL url=new URL(urlPath);
//        返回一个连接，连接后台和微信服务器
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        设置请求方式为GET请求
        urlConnection.setRequestMethod("GET");
//        因为是GET，所以不需要传参数
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(false);
//        打开连接
        urlConnection.connect();

//        将字节流包装成高效流
        BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"utf-8"));
//        读取返回的数据
        StringBuilder sb=new StringBuilder();
        String len;
        while ((len=br.readLine())!=null){
            sb.append(len);
        }

        return sb.toString();
    }

    public static String doPost(String urlPath,String params) throws IOException {
//        创建URL对象，建立远程连接
        URL url=new URL(urlPath);
//        返回一个连接，连接后台和微信服务器
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        设置请求方式为GET请求
        urlConnection.setRequestMethod("GET");
//        因为是GET，所以不需要传参数
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setUseCaches(false);

//        向微信服务端传递参数
        PrintWriter printWriter = new PrintWriter(urlConnection.getOutputStream());
        printWriter.write(params);

        printWriter.flush();
//        读取返回的数据

        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"));
        StringBuilder sb=new StringBuilder();
        String len;
        while ((len=br.readLine())!=null){
            sb.append(len);
        }

        return sb.toString();
    }
    //获取指令
    public static Token getAccessToken(){
        String url=ACCESS_TOKEN_URL.replace("APPID",APPID).replace("APPSECRET",APPSECRET);
        Token token=new Token();
        try {
            String AccessToken = AccessTokenUtils.doGet(url);

//            将字符串转换为json格式
            ObjectMapper mapper=new ObjectMapper();
            token=mapper.readValue(AccessToken,Token.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }
}
