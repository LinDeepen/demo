package com.example.demo;

import cn.hutool.core.lang.ObjectId;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.example.demo.util.RedisUtil;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class DemoApplicationTests {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void contextLoads() {
        System.out.println("测试成功");
    }

    /**
     * 唯一id
     */
    @Test
    public void uniqueIdTest() {
        //生成的UUID是带-的36位字符串，类似于：a5c8a5e8-df2b-4706-bea4-08d0939410e3
        String uuid = IdUtil.randomUUID();
        //生成的是不带-的32位字符串，类似于：b17f24ff026d40949c85a24f4f375d42
        String simpleUUID = IdUtil.simpleUUID();
        System.out.println(uuid);
        System.out.println(simpleUUID);


        //生成24位类似：5b9e306a4df4f8c54a39fb0c
        String objectId1 = ObjectId.next();
        //方法2：从Hutool-4.1.14开始提供
        String objectId2 = IdUtil.objectId();
        System.out.println(objectId1);
        System.out.println(objectId2);


        //参数1为终端ID
        //参数2为数据中心ID
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        // 有两种返回值类型
        long nextId = snowflake.nextId();
        String nextIdStr = snowflake.nextIdStr();
        System.out.println(nextId);
        System.out.println(nextIdStr);
    }

}
