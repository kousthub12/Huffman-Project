import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Comparator;

class MyComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y)
    {
        return x.data - y.data;
    }
}
public class Huffman {
    static final String INPUT_FILE = "/Users/pigeon/Downloads/Huffman/MobyDick.txt";
    static final String COMPRESSED_FILE = "/Users/pigeon/Downloads/Huffman/MobyDickCompressed.txt";
    static final String DECOMPRESSED_FILE = "/Users/pigeon/Downloads/Huffman/MobyDickDeCompressed.txt";
    static final int EOF = -1;
    public static void printTreeCode(HuffmanNode root, String s)
    {
        if (root != null)
        {
            if (root.left == null && root.right == null) {
               System.out.println(root.ch + ":" + s);
               return;
            }
        }
        else
        {
            return;
        }
        printTreeCode(root.left, s + "0");
        printTreeCode(root.right, s + "1");
    }
    
    public static void saveTreeCode(HuffmanNode root, String s, HashMap<Integer, String> charBitMap)
    {
        if (root != null)
        {
            if (root.left == null && root.right == null) {
                charBitMap.put(root.ch, s);
                return;
            }
        }
        else
        {
            return;
        }
        saveTreeCode(root.left, s + "0", charBitMap);
        saveTreeCode(root.right, s + "1", charBitMap);
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        HashMap<Integer, Integer> map = charFreqMap();
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<HuffmanNode>(map.size(), new MyComparator());
        HashMap<Integer, String> charBitMap = new HashMap<Integer, String>();
        
        for (int i = 0; i < map.size(); i++) {
            HuffmanNode hn = new HuffmanNode();
            hn.ch =  (Integer) map.keySet().toArray()[i];
            hn.data = map.get(hn.ch);
            hn.left = null;
            hn.right = null;
            pq.add(hn);
        }
        
        HuffmanNode root = null;
        while (pq.size() > 1) {
            HuffmanNode x = pq.peek();
            pq.poll();
            HuffmanNode y = pq.peek();
            pq.poll();
            HuffmanNode total = new HuffmanNode();
            total.data = x.data + y.data;
            total.ch = (char) 0;
            total.left = x;
            total.right = y;
            root = total;
            pq.add(total);
        }
        
        saveTreeCode(root, "",charBitMap);
        compress(charBitMap);
        decompress(root);
    }
    public static void compress(HashMap<Integer, String> charBitMap) throws FileNotFoundException, IOException
    {
        BitOutputStream bos = new BitOutputStream(COMPRESSED_FILE);
        BitInputStream bis = new BitInputStream(INPUT_FILE);
        while (true)
        {
            int intChar = bis.read();
            if (intChar == -1)
                break;
                
            String bitArr = charBitMap.get(intChar);
            
            for (int i=0; i<bitArr.length(); i++)
            {
                int bit = (int) bitArr.charAt(i);
                bos.writeBits(1, bit);
            }    
        }
        bos.write(EOF);
        bos.close();
        bis.close();
    }
    
    public static void decompress(HuffmanNode root) throws FileNotFoundException,IOException
    {
        BitInputStream bis = new BitInputStream(COMPRESSED_FILE);
        BitOutputStream bos = new BitOutputStream(DECOMPRESSED_FILE);
        int bit = bis.readBits(1);
        while (bit >= 0)
        {
            HuffmanNode node = root;
            while (!node.isLeaf() && bit >= 0){        
                if(bit == 0){
                    node = node.left;
                }
                else{
                    node = node.right;
                }
                bit = bis.readBits(1);
            }
            int ch =  node.ch;
            bos.write(ch);
        }   
        bos.close();
        bis.close();
    }
    public static HashMap charFreqMap() throws FileNotFoundException, IOException
    {
        HashMap<Integer, Integer> map = new HashMap<>();
        BitInputStream bis = new BitInputStream(INPUT_FILE);
        while (true)
        {
            int intChar = bis.read();
            if (intChar == -1)
                break;
            
            if (map.containsKey(intChar))
            {
                map.put(intChar, map.get(intChar) + 1);
            }
            else
            {
                map.put(intChar, 1);
            } 
        }
        bis.close();
        return map;
    }
}