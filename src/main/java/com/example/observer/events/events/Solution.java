package com.example.observer.events.events;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @描述:
 */
public class Solution
{


    public static void main(String[] args)
    {
        //1 字符串数组，重复字符串，相同次数按照首字符排序
        String[] needToDeal = {"abcd","abcd","abdd","abdd"};
        solutionTest(needToDeal);
    }

    private static void solutionTest(String[] needToDeal)
    {

        Map<String, Info> midMap = new HashMap<>();
        //分组计数
        List<Info> infos = new ArrayList<Info>();
        Arrays.stream(needToDeal).forEach(info->{
            if (midMap.containsKey(info)){
                Info info1 = midMap.get(info);
                info1.setValue(info1.getValue() +1);
            }
            else{
                Info info1 = new Info();
                info1.setValue(1);
                info1.setKey(info);
                infos.add(info1);
                midMap.put(info,info1);
            }
        });

        Collections.sort(infos);
        infos.stream().forEach(info -> {
            System.out.print(info.getKey());
            System.out.print(",");
        });
    }

    @Getter
    @Setter
    private static class Info implements Comparable<Info>{
        private String key;
        private Integer value;
        @Override
        public int compareTo(Info o)
        {
            if (this.getValue() > o.getValue()){
                return -1;
            }
            else if (this.getValue() == o.getValue()){
                if (this.getKey().compareTo(o.getKey()) < 0){
                    return -1;
                }
                return 1;
            }
            return 1;
        }
    }

}
