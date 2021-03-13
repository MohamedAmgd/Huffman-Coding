import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HuffmanCoding {
    private static final String SPILT_ELEMENT = "`,";
    public static final String TABLE_END_ELEMENT = SPILT_ELEMENT.repeat(2);
    private String lastOperationHeader;

    public String compress(String input) {
        HashMap<Character, Integer> asciiMap = new HashMap<>();
        for (Character c : input.toCharArray()) {
            if (asciiMap.containsKey(c)) {
                int frequency = asciiMap.get(c);
                asciiMap.replace(c, frequency + 1);
            } else {
                asciiMap.put(c, 1);
            }
        }
        ArrayList<HuffmanTreeNode> initalHuffmanTree = new ArrayList<>();
        for (Map.Entry<Character, Integer> e : asciiMap.entrySet()) {
            AsciiElement element = new AsciiElement(e.getKey().toString(), e.getValue());
            initalHuffmanTree.add(new HuffmanTreeNode(element));
        }
        HuffmanTreeNode huffmanTreeRoot = buildHuffmanTree(initalHuffmanTree);
        HashMap<Character, String> newCodesMap = generateNewCodesMap(asciiMap.keySet(), huffmanTreeRoot);
        String header = headerFromCodesMap(newCodesMap);
        setLastOperationHeader(header);
        StringBuilder output = new StringBuilder();
        for (Character c : input.toCharArray()) {
            output.append(newCodesMap.get(c));
        }
        return output.toString();
    }

    private String headerFromCodesMap(HashMap<Character, String> newCodesMap) {
        StringBuilder header = new StringBuilder();
        for (Map.Entry<Character, String> e : newCodesMap.entrySet()) {
            header.append(e.getKey()).append(SPILT_ELEMENT).append(e.getValue()).append(SPILT_ELEMENT);
        }
        header.append(SPILT_ELEMENT);
        return header.toString();
    }

    private HashMap<Character, String> codesMapFromHeader(String header) {
        String[] splitValues = header.split(SPILT_ELEMENT);
        HashMap<Character, String> codesMap = new HashMap<>();
        for (int i = 0; i < splitValues.length; i += 2) {
            codesMap.put(splitValues[i].charAt(0), splitValues[i + 1]);
        }
        return codesMap;
    }

    private HashMap<Character, String> generateNewCodesMap(Set<Character> keySet, HuffmanTreeNode huffmanTreeRoot) {
        HashMap<Character, String> newCodesMap = new HashMap<>();
        for (Character c : keySet) {
            if (keySet.size() == 1) {
                newCodesMap.put(c, "1");
                break;
            }
            StringBuilder newCode = new StringBuilder();
            HuffmanTreeNode node = huffmanTreeRoot;
            while (!node.isLeaf()) {
                if (node.getLeft().getData().getName().contains(c.toString())) {
                    newCode.append("0");
                    node = node.getLeft();
                } else {
                    newCode.append("1");
                    node = node.getRight();
                }
            }
            newCodesMap.put(c, newCode.toString());
        }
        return newCodesMap;
    }

    private HuffmanTreeNode buildHuffmanTree(ArrayList<HuffmanTreeNode> oldHuffmanTree) {
        sortHuffmanTree(oldHuffmanTree);
        ArrayList<HuffmanTreeNode> newHuffmanTree = new ArrayList<>();
        if (oldHuffmanTree.size() >= 2) {
            HuffmanTreeNode nodeSum = new HuffmanTreeNode();
            nodeSum.createByTwoChilds(oldHuffmanTree.get(0), oldHuffmanTree.get(1));
            newHuffmanTree.add(nodeSum);
            oldHuffmanTree.remove(0);
            oldHuffmanTree.remove(0);
        }
        while (!oldHuffmanTree.isEmpty()) {
            if (oldHuffmanTree.size() > 1) {
                int frequency1 = oldHuffmanTree.get(0).getData().getFrequency();
                int frequency2 = oldHuffmanTree.get(1).getData().getFrequency();
                int bigestFrequencyInNewTree = newHuffmanTree.get(newHuffmanTree.size() - 1).getData().getFrequency();
                if (frequency1 < bigestFrequencyInNewTree && frequency2 >= bigestFrequencyInNewTree) {
                    HuffmanTreeNode nodeSum = new HuffmanTreeNode();
                    HuffmanTreeNode left = newHuffmanTree.get(newHuffmanTree.size() - 1);
                    HuffmanTreeNode right = oldHuffmanTree.get(0);
                    nodeSum.createByTwoChilds(left, right);
                    newHuffmanTree.remove(newHuffmanTree.size() - 1);
                    newHuffmanTree.add(nodeSum);
                    oldHuffmanTree.remove(0);
                } else {
                    HuffmanTreeNode nodeSum = new HuffmanTreeNode();
                    nodeSum.createByTwoChilds(oldHuffmanTree.get(0), oldHuffmanTree.get(1));
                    newHuffmanTree.add(nodeSum);
                    oldHuffmanTree.remove(0);
                    oldHuffmanTree.remove(0);
                }
            } else {
                newHuffmanTree.add(oldHuffmanTree.get(0));
                oldHuffmanTree.remove(0);
            }
        }
        if (newHuffmanTree.size() != 1) {
            return buildHuffmanTree(newHuffmanTree);
        }
        return newHuffmanTree.get(0);
    }

    private void sortHuffmanTree(ArrayList<HuffmanTreeNode> huffmanTree) {
        Collections.sort(huffmanTree, new Comparator<HuffmanTreeNode>() {
            @Override
            public int compare(HuffmanTreeNode o1, HuffmanTreeNode o2) {
                AsciiElement e1 = o1.getData();
                AsciiElement e2 = o2.getData();
                return Integer.compare(e1.getFrequency(), e2.getFrequency());
            }
        });
    }

    public String decompress(String header, String data) {
        StringBuilder output = new StringBuilder();
        HashMap<Character, String> codesMap = codesMapFromHeader(header);
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < data.length(); i++) {
            codeBuilder.append(data.charAt(i));
            for (Map.Entry<Character, String> e : codesMap.entrySet()) {
                if (e.getValue().equals(codeBuilder.toString())) {
                    output.append(e.getKey());
                    codeBuilder.delete(0, codeBuilder.length());
                }
            }
        }
        return output.toString();
    }

    public void setLastOperationHeader(String lastOperationHeader) {
        this.lastOperationHeader = lastOperationHeader;
    }

    public String getLastOperationHeader() {
        return lastOperationHeader;
    }
}
