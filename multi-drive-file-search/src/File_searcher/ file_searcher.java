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
          System.out.println("detecting drives in your system...");
          File[] drives = File.listRoots();
          System.out.println("drives detected in your system:");
          if (drives == null || drives.length == 0)
          {
              System.out.println("no drives found.");
              return;
          }

        for (File drive : drives)
            System.out.println(drive.getAbsolutePath());

            System.out.print("\nenter the file name you want to search (with / without extension): ");
            String search_name = input.nextLine();

            String base_name;
            String extension_name;

