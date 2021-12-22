public class HuffmanNode {
    int data;
    int ch;
    HuffmanNode left;
    HuffmanNode right;
    public boolean isLeaf(){
        if(this.left == null && this.right == null){
            return true;
        }
        return false;
    }
    
}