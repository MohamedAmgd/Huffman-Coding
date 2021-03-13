import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean restart = true;
        while (restart) {
            System.out.print("Enter c for compress or d for decompress or q for quit : ");
            String input = scanner.nextLine();
            String filePath;
            switch (input) {
            case "c":
                System.out.print("Enter file path:");
                filePath = scanner.nextLine();
                compress(filePath);
                break;
            case "d":
                System.out.print("Enter file path:");
                filePath = scanner.nextLine();
                decompress(filePath);
                break;

            case "q":
                restart = false;
                scanner.close();
                break;
            default:
                compress("input.txt");
                decompress("input.compressed");
                break;
            }
        }
    }

    public static void compress(String filePath) throws IOException {
        long start = System.currentTimeMillis();
        String input = Files.readString(Paths.get(filePath));
        HuffmanCoding huffmanCoding = new HuffmanCoding();
        String data = huffmanCoding.compress(input);
        String header = huffmanCoding.getLastOperationHeader();
        writeBinaryFile(filePath.substring(0, filePath.indexOf(".")).concat(".compressed"), header, data);
        long end = System.currentTimeMillis();
        System.out.println("Compressed in ".concat(end - start + "ms"));
        System.out.println("Last operation table :");
        System.out.println(header.substring(0, header.length() - HuffmanCoding.TABLE_END_ELEMENT.length()));
    }

    public static void decompress(String filePath) throws IOException {
        long start = System.currentTimeMillis();
        HuffmanCoding huffmanCoding = new HuffmanCoding();
        String[] headerData = readBinaryFile(filePath);
        String decompressed = huffmanCoding.decompress(headerData[0], headerData[1]);
        writeBinaryFile(filePath.substring(0, filePath.indexOf(".")).concat("_decompressed.txt"), decompressed, "");
        long end = System.currentTimeMillis();
        System.out.println("Decompressed in ".concat(end - start + "ms"));
        System.out.println("Last operation table :");
        System.out
                .println(headerData[0].substring(0, headerData[0].length() - HuffmanCoding.TABLE_END_ELEMENT.length()));
    }

    public static void writeBinaryFile(String filePath, String header, String data) {
        byte[] headerBytes = header.getBytes();
        byte[] dataBytes = stringToBitset(data).toByteArray();
        byte[] outputBytes = new byte[headerBytes.length + dataBytes.length];
        System.arraycopy(headerBytes, 0, outputBytes, 0, headerBytes.length);
        System.arraycopy(dataBytes, 0, outputBytes, headerBytes.length, dataBytes.length);
        Path path = Paths.get(filePath);
        try {
            Files.write(path, outputBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BitSet stringToBitset(String binary) {
        BitSet bitset = new BitSet(binary.length());
        int len = binary.length();
        for (int i = len - 1; i >= 0; i--) {
            if (binary.charAt(i) == '1') {
                bitset.set(len - i - 1);
            }
        }
        return bitset;
    }

    public static String bitsetToString(BitSet input) {
        StringBuilder output = new StringBuilder();
        int len = input.length();
        for (int i = len - 1; i >= 0; i--) {
            if (input.get(i)) {
                output.append("1");
            } else {
                output.append("0");
            }
        }
        return output.toString();
    }

    public static String[] readBinaryFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        String[] headerDataStrings = new String[2];
        byte[] allBytes = Files.readAllBytes(path);
        String allBytesString = new String(allBytes);
        int endHeaderIndex = allBytesString.indexOf(HuffmanCoding.TABLE_END_ELEMENT)
                + HuffmanCoding.TABLE_END_ELEMENT.length();
        headerDataStrings[0] = allBytesString.substring(0, endHeaderIndex);
        byte[] dataBytes = new byte[allBytes.length - endHeaderIndex];
        System.arraycopy(allBytes, endHeaderIndex, dataBytes, 0, dataBytes.length);
        BitSet dataBitSet = BitSet.valueOf(dataBytes);
        headerDataStrings[1] = bitsetToString(dataBitSet);
        return headerDataStrings;
    }
}