package com.taotao;

import com.taotao.pojo.TbItem;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wwt
 * @Date: 2019/10/22 22:28
 */
public class test {

    @Test
    public void test1(){
        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        for (Integer integer : list) {
            if (integer == 3){
                integer = 30;
            }
        }

        System.out.println(list);

        List<TbItem> list1 = new ArrayList<>();

        TbItem item = new TbItem();
        item.setNum(3);
        item.setCid(2l);

        list1.add(item);

        for (int i = 0; i < list1.size(); i++) {
            if (list1.get(i).getNum() == 3){
                list1.remove(list1.get(i));
            }
        }

        System.out.println(list1.size());
    }
}
