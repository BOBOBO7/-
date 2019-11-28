
package servlet;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.zhenzi.sms.ZhenziSmsClient;

/**
 * 获取验证码
 */
@WebServlet("/SendSmsServlet")
public class SendSmsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//短信平台相关参数
	private String apiUrl = "https://sms_developer.zhenzikj.com";
	private String appId = "103484";
	private String appSecret = "31673881-efb2-471e-b81e-2a30778cc45c";
       
    public SendSmsServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
	}

	/**
	 * 短信平台使用的是榛子云短信(smsow.zhenzikj.com)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String mobile = request.getParameter("mobile");
            System.out.println(mobile);
 			JSONObject json = null;
			//生成6位验证码
			String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
			//发送短信
			ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);
			String result = client.send(mobile, "您的验证码为:" + verifyCode + "，该码有效期为5分钟，该码只能使用一次!");
			json = JSONObject.parseObject(result);
			if(json.getIntValue("code") != 0){//发送短信失败
				renderData(response, "fail");
				return; 
			}
			//将验证码存到session中,同时存入创建时间
			//以json存放，这里使用的是阿里的fastjson
			HttpSession session = request.getSession();
			json = new JSONObject();
			json.put("mobile", mobile);
			json.put("verifyCode", verifyCode);
			json.put("createTime", System.currentTimeMillis());
			// 将认证码存入SESSION
			request.getSession().setAttribute("verifyCode", json);
			renderData(response, "success");
			return ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		renderData(response, "fail");
	}
	
	protected void renderData(HttpServletResponse response, String data){
		try {
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
