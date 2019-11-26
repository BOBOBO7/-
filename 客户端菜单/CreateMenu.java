package menu;

import bean.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class CreateMenu {
    public static void menu(Token token) throws IOException {
        String url=changliang.MESSAGE_BUTTON_URL.replace("ACCESS_TOKEN",token.getAccess_token());

        ViewButton view=new ViewButton();
        view.setType(changliang.MESSAGE_BUTTON_);
        view.setName("预约看房");
        view.setUrl("https://www.baidu.com/");

        ViewButton view1=new ViewButton();
        view1.setType(changliang.MESSAGE_BUTTON_);
        view1.setName("预约记录");
        view1.setUrl("https://www.baidu.com/");

        Menu menu=new Menu();
        menu.setButton(new Button[]{view,view1});

        ObjectMapper mapper=new ObjectMapper();
//        把对象转为json
        String params = mapper.writeValueAsString(menu);
//        System.out.println(params);

        String response = AccessTokenUtils.doPost(url, params);

        Map<String,Object> map = mapper.readValue (response,new TypeReference<Map<String,Object>>(){});
//        System.out.println(map);

        int errcode=-1;
        errcode = (int) map.get("errcode");
        System.out.println(errcode);
        if(errcode==0){
            System.out.println("菜单创建成功");
        }else {
            System.out.println("菜单创建失败");
        }
    }

}
