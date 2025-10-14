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
            ExecutorService thread_pool = Executors.newFixedThreadPool(drives.length);

            for (File drive : drives)
            {
                thread_pool.submit(() -> search_in_drive(drive, base_name, extension_name));
            }

            thread_pool.shutdown();
            try
            {
                thread_pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } 
            catch (InterruptedException e)
            {
                System.out.println("Search was interrupted: " + e.getMessage());
            }
            
            if (found_files.isEmpty())
            {
                System.out.println("\nNo files or folders found with the name: " + search_name);
            }
            else
            {
                if (extension_name.isEmpty() && found_files.size() > 1)
                    System.out.println("\nMultiple results found. Specify extension if you want. All found items:");

                else
                    System.out.println("\nFiles/Folders found:");
            for (File f : found_files)
                    System.out.println(f.getAbsolutePath());
            }
        }
    }
