package com.example.demo.util.wxwork;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 生成CSV文件
 */
@Slf4j
public class CreateWXworkCSV {

    /**
     *
     *  导出生成csv格式的文件
     * @param   path 保存CSV文件路径
     * @param   titles csv格式头文
     * @param   propertys 需要导出的数据实体的属性，注意与title一一对应
     * @param   list 需要导出的对象集合
     * @return
     * @throws   IOException
     * @throws   IllegalAccessException
     * @throws   IllegalArgumentException
     */
    public static boolean exportCsv(String path,String[] titles, String[] propertys, List list) {

        try {

            File file = new File(path);

            //构建输出流，同时指定编码
            OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(file), "gbk");

            //csv文件是逗号分隔，除第一个外，每次写入一个单元格数据后需要输入逗号
            for(String title : titles){
                ow.write(title);
                ow.write(",");
            }
            //写完文件头后换行
            ow.write("\r\n");
            //写内容
            for(Object obj : list){
                //利用反射获取所有字段
                Field[] fields = obj.getClass().getDeclaredFields();
                for(String property : propertys){
                    for(Field field : fields){
                        //设置字段可见性
                        field.setAccessible(true);
                        if(property.equals(field.getName())){

                            if (StringUtils.isNotBlank(field.get(obj).toString())){
                                ow.write(field.get(obj).toString());
                            }
                            ow.write(",");
                            continue;
                        }
                    }
                }
                //写完一行换行
                ow.write("\r\n");
            }
            ow.flush();
            ow.close();

            log.info("生成CSV成功--文件路径：" + path);

        }catch (Exception e){
            log.info("生成CSV失败--文件路径：" + path);
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
