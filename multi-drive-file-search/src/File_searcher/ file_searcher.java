package File_searcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.*;

public class file_searcher{

    private static final Queue<Path> foundPaths = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            // detectin g the files
            System.out.println("Detecting drives...");
            File[] drives = File.listRoots();
            if (drives == null || drives.length == 0) {
                System.out.println("No drives found.");
                return;
            }
