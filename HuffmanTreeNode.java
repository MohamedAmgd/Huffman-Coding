
public class HuffmanTreeNode {
    private AsciiElement data;
    private HuffmanTreeNode left;
    private HuffmanTreeNode right;

    public HuffmanTreeNode(AsciiElement data) {
        this.data = data;
    }

    public HuffmanTreeNode() {
    }

    public void createByTwoChilds(HuffmanTreeNode left, HuffmanTreeNode right) {
        this.left = left;
        this.right = right;
        AsciiElement sumNode = new AsciiElement();
        sumNode.setName(left.getData().getName().concat(right.getData().getName()));
        sumNode.setFrequency(left.getData().getFrequency() + right.getData().getFrequency());
        this.data = sumNode;
    }

    public boolean isLeaf() {
        return (this.left == null && this.right == null);
    }

    public void setData(AsciiElement data) {
        this.data = data;
    }

    public AsciiElement getData() {
        return data;
    }

    public void setLeft(HuffmanTreeNode left) {
        this.left = left;
    }

    public HuffmanTreeNode getLeft() {
        return left;
    }

    public void setRight(HuffmanTreeNode right) {
        this.right = right;
    }

    public HuffmanTreeNode getRight() {
        return right;
    }

}
