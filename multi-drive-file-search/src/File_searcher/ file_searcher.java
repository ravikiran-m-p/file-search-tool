package File_searcher;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class file_searcher
{
    private static final List<File> found_files = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args)
    {
        try (Scanner input = new Scanner(System.in))
        {
            System.out.println("Detecting drives in your system...");
            File[] drives = File.listRoots();
            System.out.println("Drives detected in your system:");
            if (drives == null || drives.length == 0)
            {
                System.out.println("No drives found.");
                return;
            }

            for (File drive : drives)
                System.out.println(drive.getAbsolutePath());

            System.out.print("\nEnter the file/folder name you want to search (with or without extension): ");
            String search_name = input.nextLine();

            String base_name;
            String extension_name;

            if (search_name.contains("."))
            {
                int last_dot = search_name.lastIndexOf('.');
                base_name = search_name.substring(0, last_dot);
                extension_name = search_name.substring(last_dot + 1);
            }
            else
            {
                base_name = search_name;
                extension_name = "";
            }
