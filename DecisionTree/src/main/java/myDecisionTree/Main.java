package myDecisionTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/8/8.
 */
public class Main {
    private static final List<ArrayList<String>> dataList = new ArrayList<ArrayList<String>>();
    private static final List<String> attributeList = new ArrayList<String>();

    public static void main(String[] args) throws IOException {

        String fileName="";//输入数据路径
        readData(dataList,attributeList,fileName);//读取数据
        int Species=1;//创建那种树，ID3，C45，CRAT

        DecisionTree dt=new DecisionTree();
        TreeNode tn=dt.createDT(dataList,attributeList,Species);

        System.out.println();
    }
    static void readData(List<ArrayList<String>> dataList,List<String> attributeList,String fileName) throws IOException {
        File file = new File(fileName);
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            ArrayList<String> line;
            String tempString = null;

            //第一行为属性，加入attributeList中
            tempString = reader.readLine();
            String features[]=tempString.split(" ");
            for(int i=0;i<features.length;i++){
                attributeList.add(features[i]);
            }

            while ((tempString = reader.readLine()) != null) {
                String[] splits=tempString.split(" ");
                line=new ArrayList<String>();
                for(int i=0;i<splits.length;i++){
                    line.add(splits[i]);
                }
                dataList.add(line);//添加特征值
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
