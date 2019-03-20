package cn.xpleaf.spider;

import java.util.List;
import java.util.ArrayList;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;



public class test13 implements Watcher{
    CuratorFramework client;
    List<String> childrenList = new ArrayList<String>();
    List<String> newChildrenList = new ArrayList<String>();

    public  test13(){
        //1000：表示curator链接zk的时候超时时间是多少 3：表示链接zk的最大重试次数
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        String connectString = "192.168.20.10:2181";
        int sessionTimeoutMs = 5000;//这个值只能在4000-40000ms之间表示链接断开后多长时间临时节点会消失
        int connectionTimeoutMs = 3000;//获取链接的超时时间
        //创建一个zk连接
        client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs
                ,connectionTimeoutMs,retryPolicy);
        client.start();

        //监视monitor节点,获取下面的所有子节点的变化情况
        try {
            childrenList = client.getChildren().usingWatcher(this).forPath("/monitor");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    /**
     * 实现一个zk监视器，监视某个节点的变化情况
     *
     * 这个监视程序需要一直运行
     * @CPH
     */

    public void process(WatchedEvent event) {
        System.out.println("我被调用了");
        try {
            newChildrenList = client.getChildren().usingWatcher(this).forPath("/monitor");
            for(String ip : childrenList)
            {
                if(!newChildrenList.contains(ip)){
                    System.out.println("节点消失了"+ip);
                    //TODO 给管理员发送短信什么的

                }
            }

            for(String ip : newChildrenList){
                if(!childrenList.contains(ip)){
                    System.out.println("节点新增"+ip);
                }
            }
            //重要
            childrenList = newChildrenList;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

    public void start(){
        while (true){;}
    }

    public static void main(String[] args) {
        test13 watcher = new test13();
        watcher.start();
    }
}