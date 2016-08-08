package myDecisionTree;

import java.util.*;

/**
 * Created by lenovo on 2016/8/8.
 */
public class InfoGain {
    private List<ArrayList<String>> data;
    private List<String> attribute;

    public InfoGain(List<ArrayList<String>> data,List<String> attribute){

        this.data = new ArrayList<ArrayList<String>>();
        for(int i=0;i<data.size();i++){
            List<String> temp = data.get(i);
            ArrayList<String> t = new ArrayList<String>();
            for(int j=0;j<temp.size();j++){
                t.add(temp.get(j));
            }
            this.data.add(t);
        }

        this.attribute = new ArrayList<String>();
        for(int k=0;k<attribute.size();k++){
            this.attribute.add(attribute.get(k));
        }
        /*this.data = data;
        this.attribute = attribute;*/
    }

    //获得熵
    public double getEntropy(){

        Map<String,Long> targetValueMap = getTargetValue();
        Set<String> targetkey = targetValueMap.keySet();
        double entropy = 0.0;
        for(String key : targetkey){
            double p = MathUtils.div((double)targetValueMap.get(key), (double)data.size());
            entropy += (-1) * p * Math.log(p);
        }
        return entropy;
    }

    //获得InfoA
    public double getInfoAttribute(int attributeIndex){

        Map<String,Long> attributeValueMap = getAttributeValue(attributeIndex);
        double infoA = 0.0;
        for(Map.Entry<String, Long> entry : attributeValueMap.entrySet()){
            int size = data.size();
            double attributeP = MathUtils.div((double)entry.getValue() , (double) size);
            Map<String,Long> targetValueMap = getAttributeValueTargetValue(entry.getKey(),attributeIndex);
            long totalCount = 0L;
            for(Map.Entry<String, Long> entryValue :targetValueMap.entrySet()){
                totalCount += entryValue.getValue();
            }
            double valueSum = 0.0;
            for(Map.Entry<String, Long> entryTargetValue : targetValueMap.entrySet()){
                double p = MathUtils.div((double)entryTargetValue.getValue(), (double)totalCount);
                valueSum += Math.log(p) * p;
            }
            infoA += (-1) * attributeP * valueSum;
        }
        return infoA;

    }

    //得到属性值在决策空间的比例
    public Map<String,Long> getAttributeValueTargetValue(String attributeName,int attributeIndex){

        Map<String,Long> targetValueMap = new HashMap<String,Long>();
        Iterator<ArrayList<String>> iterator = data.iterator();
        while(iterator.hasNext()){
            List<String> tempList = iterator.next();
            if(attributeName.equalsIgnoreCase(tempList.get(attributeIndex))){
                int size = tempList.size();
                String key = tempList.get(size - 1);
                Long value = targetValueMap.get(key);
                targetValueMap.put(key, value != null ? ++value :1L);
            }
        }
        return targetValueMap;
    }

    //得到属性在决策空间上的数量
    public Map<String,Long> getAttributeValue(int attributeIndex){

        Map<String,Long> attributeValueMap = new HashMap<String,Long>();
        for(ArrayList<String> note : data){
            String key = note.get(attributeIndex);
            Long value = attributeValueMap.get(key);
            attributeValueMap.put(key, value != null ? ++value :1L);
        }
        return attributeValueMap;

    }

    public List<ArrayList<String>> getData4Value(String attrValue,int attrIndex){

        List<ArrayList<String>> resultData = new ArrayList<ArrayList<String>>();
        Iterator<ArrayList<String>> iterator = data.iterator();
        for(;iterator.hasNext();){
            ArrayList<String> templist = iterator.next();
            if(templist.get(attrIndex).equalsIgnoreCase(attrValue)){
                ArrayList<String> temp = (ArrayList<String>) templist.clone();
                resultData.add(temp);
            }
        }
        return resultData;
    }

    //获得增益率
    public double getGainRatio(int attributeIndex){
        return MathUtils.div(getGain(attributeIndex), getSplitInfo(attributeIndex));
    }

    //获得增益量
    public double getGain(int attributeIndex){
        return getEntropy() - getInfoAttribute(attributeIndex);
    }

    //得到惩罚因子
    public double getSplitInfo(int attributeIndex){
        Map<String,Long> attributeValueMap = getAttributeValue(attributeIndex);
        double splitA = 0.0;
        for(Map.Entry<String, Long> entry : attributeValueMap.entrySet()){
            int size = data.size();
            double attributeP = MathUtils.div((double)entry.getValue() , (double) size);
            splitA += attributeP * Math.log(attributeP) * (-1);
        }
        return splitA;
    }

    //得到目标函数在当前集合范围内的离散的值
    public Map<String,Long> getTargetValue(){

        Map<String,Long> targetValueMap = new HashMap<String,Long>();
        Iterator<ArrayList<String>> iterator = data.iterator();
        while(iterator.hasNext()){
            List<String> tempList = iterator.next();
            String key = tempList.get(tempList.size() - 1);
            Long value = targetValueMap.get(key);
            targetValueMap.put(key, value != null ? ++value : 1L);
        }
        return targetValueMap;
    }

    //获得TARGET值
    public static List<String> getTarget(List<ArrayList<String>> data){

        List<String> list = new ArrayList<String>();
        for(ArrayList<String> temp : data){
            int index = temp.size() -1;
            String value = temp.get(index);
            list.add(value);
        }
        return list;
    }

    //判断当前纯度是否100%
    public static String IsPure(List<String> list){
        Set<String> set = new HashSet<String>();
        for(String name :list){
            set.add(name);
        }
        if(set.size() > 1) return null;
        Iterator<String> iterator = set.iterator();
        return iterator.next();
    }
}
