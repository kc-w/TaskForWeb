package GeTui;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.StartActivityTemplate;
import com.gexin.rp.sdk.template.style.Style0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AppPush {

    // STEP1：获取应用基本信息
    private static String appId = "K5j7ubhMgZ9J3cYPSzK9dA";
    private static String appKey = "feGWlAp3vl8FbFeaf0viW6";
    private static String masterSecret = "0i5iYOfeu49jNCuNP8smA";
    private static String url = "http://api.getui.com/apiex.htm";

    public void push(List<String> cids,String title,String content){

        IGtPush push = new IGtPush(url, appKey, masterSecret);

        Style0 style = new Style0();

        //设置推送标题、推送内容
        style.setTitle(title);
        style.setText(content);

//        // 设置推送图标
//        style.setLogo("ico.jpg");

        // 角标, 必须大于0, 个推通道下发有效; 此属性目前仅针对华为 EMUI 4.1 及以上设备有效
//        style.setBadgeAddNum(1);
        // 设置响铃
        style.setRing(true);
        // 设置震动
        style.setVibrate(true);
        style.setChannelLevel(4);


        //选择通知模板
        StartActivityTemplate template = new StartActivityTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setIntent("intent:#Intent;component=com.example.taskforandroid/.Activity.LoginActivity;end");
        template.setStyle(style);


        if (cids.size()==0){
            //定义消息对象,设置推送消息有效期等推送参数
            List<String> appIds = new ArrayList<String>();
            appIds.add(appId);

            //AppMessage对应pushMessageToApp:推送给所有用户;
            AppMessage message = new AppMessage();
            message.setData(template);
            message.setAppIdList(appIds);
            message.setOffline(true);
            message.setOfflineExpireTime(24 * 3600 * 1000);

            //指定应用名称全客户端推送 pushMessageToApp(AppMessage message)
            IPushResult ret = push.pushMessageToApp(message);
            System.out.println(ret.getResponse().toString());

        }else {

            //SingleMessage对应pushMessageToSingle:推送给单个用户
            SingleMessage message1 = new SingleMessage();
            message1.setData(template);
            message1.setOffline(true);
            message1.setOfflineExpireTime(24 * 3600 * 1000);

            System.out.println(cids.size());

            Target target;
            for(String cid:cids){
                target = new Target();
                target.setAppId(appId);
                target.setClientId(cid);

                //单推——推送指定客户端推送 pushMessageToSingle(SingleMessage message, Target target)
                IPushResult ret1 = push.pushMessageToSingle(message1,target);
            }

        }



    }


}

