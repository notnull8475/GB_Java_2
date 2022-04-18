package ru.geekbrains.lesson3.online;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class Collections {

    enum Color {
        WHITE("#FFFFFF"), RED("#FF0000"), GREEN("#00FF00"), BLUE("#0000FF");
        private final String code;

        Color(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    static class Box implements Comparable {
        int width;
        int height;

        Box(int w, int h) {
            width = w;
            height = h;
        }

        @Override
        public String toString() {
            return String.format("Box(%d,%d)", width, height);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Box other)) return false;
            return width == other.width && height == other.height;
        }

        @Override
        public int hashCode() {
            return Objects.hash(width, height);
        }

        private int square() {
            return width * height;
        }

        @Override
        public int compareTo(Object obj) {
//            Box other = (Box) obj;
//            int sq = square();
//            int oSq = other.square();
//
//            return (sq < oSq) ? -1 : (sq == oSq) ? 0 : 1;
            return square() - ((Box) obj).square();
        }
    }

    static class TestStream implements Closeable {

        TestStream(String name) throws FileNotFoundException {
            if (name.equals("error")) throw new FileNotFoundException("file not found");
            System.out.println("File opened");
        }

        void read() throws SQLException {
            throw new SQLException("read failed");
        }

        @Override
        public void close() throws IOException {
            throw new IOException("failed to close!");
//            System.out.println("stream closed, resource released");
        }

    }

    public static void main(String[] args) {
//        try(TestStream testStream = new TestStream("correct")){
//            testStream.read();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(Color.BLUE);
//        System.out.println(Color.BLUE.getCode());
//        System.out.println(Color.BLUE == Color.GREEN);
//        Color[] colors = Color.values();
//        for (Color color : colors) {
//            System.out.println(color);
//        }
        Box b = new Box(1, 2);

        Box[] arrayBox = {new Box(1, 2),
                new Box(3, 4),
                new Box(2, 3),
                new Box(4, 5), b};

        HashSet<Box> set = new HashSet<>(Arrays.asList(arrayBox));
        System.out.println(set);


        TreeSet<Box> treeSet = new TreeSet<>(Arrays.asList(arrayBox));
        System.out.println(treeSet);
        Map<String,String> stringStringMap = new HashMap<>();

    }
}
