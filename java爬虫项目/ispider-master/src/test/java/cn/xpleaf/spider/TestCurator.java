package zkdemo;

import java.net.InetAddress;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;


public class TestCurator {

    @Test
    public void test1() throws Exception{
        //1000：表示curator链接zk的时候超时时间是多少 3：表示链接zk的最大重试次数
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        String connectString = "djt1:2181,djt2:2181,djt3:2181,djt4:2181,djt5:2181";
        int sessionTimeoutMs = 5000;//这个值只能在4000-40000ms之间表示链接断开后多长时间临时节点会小时
        int connectionTimeoutMs = 3000;//获取链接的超时时间
        //创建一个zk连接
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs
                ,connectionTimeoutMs,retryPolicy);

        client.start();

        InetAddress localHost = InetAddress.getLocalHost();
        String ip = localHost.getHostAddress();

        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)//指定节点类型
                .withACL(Ids.OPEN_ACL_UNSAFE)//指定设置节点权限信息
                .forPath("/monitor/"+ip);//指定节点名称

        while(true)
        {
            ;
        }
    }
}