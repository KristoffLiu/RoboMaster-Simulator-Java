package com.kristoff.robomaster_simulator.robomasters.decisiontree;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class UtilID3 {
    DecisionNode root;
    private boolean[] flag;
    //训练集
    private Object[] trainArrays;
    //节点索引
    private int nodeIndex;
    public static void main(String[] args)
    {
        //初始化训练集数组
        Object[] arrays = new Object[]{
                new String[]{"是","是","正常","否"},
                new String[]{"是","是","高","是"},
                new String[]{"是","是","很高","是"},
                new String[]{"否","是","正常","否"},
                new String[]{"否","否","高","否"},
                new String[]{"否","是","很高","是"},
                new String[]{"是","否","高","是"}};
        UtilID3 ID3Tree = new UtilID3();
        ID3Tree.create(arrays, 3);
    }

    //创建
    public void create(Object[] arrays, int index)
    {
        this.trainArrays = arrays;
        initial(arrays, index);
        createDTree(arrays);
        printDTree(root);
    }

    //初始化
    public void initial(Object[] dataArray, int index)
    {
        this.nodeIndex = index;

        //数据初始化
        this.flag = new boolean[((String[])dataArray[0]).length];
        for (int i = 0; i<this.flag.length; i++)
        {
            if (i == index)
            {
                this.flag[i] = true;
            }
            else
            {
                this.flag[i] = false;
            }
        }
    }

    //创建决策树
    public void createDTree(Object[] arrays)
    {
        Object[] ob = getMaxGain(arrays);
        if (this.root == null)
        {
            this.root = new DecisionNode();
            root.parent = null;
            root.parentAttribute = null;
            root.attributes = getAttributes(((Integer)ob[1]).intValue());
            root.nodeName = getNodeName(((Integer)ob[1]).intValue());
            root.childNodes = new DecisionNode[root.attributes.length];
            insert(arrays, root);
        }
    }

    //插入决策树
    public void insert(Object[] arrays, DecisionNode parentNode)
    {
        String[] attributes = parentNode.attributes;
        for (int i = 0; i < attributes.length; i++)
        {
            Object[] Arrays = pickUpAndCreateArray(arrays, attributes[i],getNodeIndex(parentNode.nodeName));
            Object[] info = getMaxGain(Arrays);
            double gain = ((Double)info[0]).doubleValue();
            if (gain != 0)
            {
                int index = ((Integer)info[1]).intValue();
                DecisionNode currentNode = new DecisionNode();
                currentNode.parent = parentNode;
                currentNode.parentAttribute = attributes[i];
                currentNode.attributes = getAttributes(index);
                currentNode.nodeName = getNodeName(index);
                currentNode.childNodes = new DecisionNode[currentNode.attributes.length];
                parentNode.childNodes[i] = currentNode;
                insert(Arrays, currentNode);
            }
            else
            {
                DecisionNode leafNode = new DecisionNode();
                leafNode.parent = parentNode;
                leafNode.parentAttribute = attributes[i];
                leafNode.attributes = new String[0];
                leafNode.nodeName = getLeafNodeName(Arrays);
                leafNode.childNodes = new DecisionNode[0];
                parentNode.childNodes[i] = leafNode;
            }
        }
    }

    //输出
    public void printDTree(DecisionNode node)
    {
        System.out.println(node.nodeName);
        DecisionNode[] childs = node.childNodes;
        for (int i = 0; i < childs.length; i++)
        {
            if (childs[i] != null)
            {
                System.out.println("如果："+childs[i].parentAttribute);
                printDTree(childs[i]);
            }
        }
    }

    //剪取数组
    public Object[] pickUpAndCreateArray(Object[] arrays, String attribute, int index)
    {
        List<String[]> list = new ArrayList<String[]>();
        for (int i = 0; i < arrays.length; i++)
        {
            String[] strs = (String[])arrays[i];
            if (strs[index].equals(attribute))
            {
                list.add(strs);
            }
        }
        return list.toArray();
    }

    //取得节点名
    public String getNodeName(int index)
    {
        String[] strs = new String[]{"头痛","肌肉痛","体温","患流感"};
        for (int i = 0; i < strs.length; i++)
        {
            if (i == index)
            {
                return strs[i];
            }
        }
        return null;
    }

    //取得叶子节点名
    public String getLeafNodeName(Object[] arrays)
    {
        if (arrays != null && arrays.length > 0)
        {
            String[] strs = (String[])arrays[0];
            return strs[nodeIndex];
        }
        return null;
    }

    //取得节点索引
    public int getNodeIndex(String name)
    {
        String[] strs = new String[]{"头痛","肌肉痛","体温","患流感"};
        for (int i = 0; i < strs.length; i++)
        {
            if (name.equals(strs[i]))
            {
                return i;
            }
        }
        return -1;
    }



    //得到最大信息增益
    public Object[] getMaxGain(Object[] arrays)
    {
        Object[] result = new Object[2];
        double gain = 0;
        int index = -1;
        for (int i = 0; i<this.flag.length; i++)
        {
            if (!this.flag[i])
            {
                double value = gain(arrays, i);
                if (gain < value)
                {
                    gain = value;
                    index = i;
                }
            }
        }
        result[0] = gain;
        result[1] = index;
        if (index != -1)
        {
            this.flag[index] = true;
        }
        return result;
    }

    //取得属性数组
    public String[] getAttributes(int index)
    {
        @SuppressWarnings("unchecked")
        TreeSet<String> set = new TreeSet<String>(new Comparisons());
        for (int i = 0; i<this.trainArrays.length; i++)
        {
            String[] strs = (String[])this.trainArrays[i];
            set.add(strs[index]);
        }
        String[] result = new String[set.size()];
        return set.toArray(result);

    }

    //计算信息增益
    public double gain(Object[] arrays, int index)
    {
        String[] playBalls = getAttributes(this.nodeIndex);
        int[] counts = new int[playBalls.length];
        for (int i = 0; i<counts.length; i++)
        {
            counts[i] = 0;
        }

        for (int i = 0; i<arrays.length; i++)
        {
            String[] strs = (String[])arrays[i];
            for (int j = 0; j<playBalls.length; j++)
            {
                if (strs[this.nodeIndex].equals(playBalls[j]))
                {
                    counts[j]++;
                }
            }
        }

        double entropyS = 0;
        for (int i = 0;i <counts.length; i++)
        {
            entropyS = entropyS + Entropy.getEntropy(counts[i], arrays.length);
        }

        String[] attributes = getAttributes(index);
        double total = 0;
        for (int i = 0; i<attributes.length; i++)
        {
            total = total + entropy(arrays, index, attributes[i], arrays.length);
        }
        return entropyS - total;
    }


    public double entropy(Object[] arrays, int index, String attribute, int totals)
    {
        String[] playBalls = getAttributes(this.nodeIndex);
        int[] counts = new int[playBalls.length];
        for (int i = 0; i < counts.length; i++)
        {
            counts[i] = 0;
        }

        for (int i = 0; i < arrays.length; i++)
        {
            String[] strs = (String[])arrays[i];
            if (strs[index].equals(attribute))
            {
                for (int k = 0; k<playBalls.length; k++)
                {
                    if (strs[this.nodeIndex].equals(playBalls[k]))
                    {
                        counts[k]++;
                    }
                }
            }
        }

        int total = 0;
        double entropy = 0;
        for (int i = 0; i < counts.length; i++)
        {
            total = total +counts[i];
        }

        for (int i = 0; i < counts.length; i++)
        {
            entropy = entropy + Entropy.getEntropy(counts[i], total);
        }
        return Entropy.getShang(total, totals)*entropy;
    }
}
