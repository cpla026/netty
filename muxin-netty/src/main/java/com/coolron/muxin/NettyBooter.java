package com.coolron.muxin;/**
 * Created by Administrator on 2019/6/23.
 */

import com.coolron.muxin.netty.WSServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @Auther: xf
 * @Date: 2019/6/23 16:14
 * @Description: netty 服务启动
 *
 *  监听springboot 整个容器加载完毕 后启动netty
 *
 *  通过上下文判断启动是否完毕  使用 ContextRefreshedEvent
 */
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent>{
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(null == event.getApplicationContext().getParent()){
            try {
                WSServer.getInstance().start();

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
