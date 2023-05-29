package com.example.demo.util;

import java.util.*;

/**
 * List集合工具类
 */
public class CollectionUtil {

    /**
     * 找出两个集合中不同的元素
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @return 不同元素集合
     */
    public static <T> Set<T> getDifferentElements(Collection<? extends T> collection1, Collection<? extends T> collection2) {
        // 创建HashSet以存储不同的元素
        HashSet<T> result = new HashSet<>(collection1);
        result.addAll(collection2);
        HashSet<T> tmp = new HashSet<>(collection1);
        tmp.retainAll(collection2); // 保留交集
        result.removeAll(tmp); // 去除交集
        return result;
    }

    /**
     * 找出两个集合中相同的元素
     *
     * @param collection1 集合1
     * @param collection2 集合2
     * @return 相同元素集合
     */
    public static <T> Set<T> getSameElements(Collection<? extends T> collection1, Collection<? extends T> collection2) {
        // 创建HashSet以存储相同的元素
        HashSet<T> result = new HashSet<>(collection1);
        result.retainAll(collection2);
        return result;
    }

    //功能测试
    public void main(String[] args) {
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);

        List<Integer> list2 = new ArrayList<>();
        list2.add(3);
        list2.add(4);
        list2.add(5);
        list2.add(6);

        Set<Integer> diffSet = CollectionUtil.getDifferentElements(list1, list2);
        List<Integer> diffList = new ArrayList<>(diffSet);
        System.out.println("不同的集合" + diffList);

        Set<Integer> sameSet = CollectionUtil.getSameElements(list1, list2);
        List<Integer> sameList = new ArrayList<>(sameSet);
        System.out.println("相同的集合" + sameList);


    }
}