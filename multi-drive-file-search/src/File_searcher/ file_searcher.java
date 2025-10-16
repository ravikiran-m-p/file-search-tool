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

  public static void main(String[] args)
  {
      try (Scanner input = new Scanner(System.in))
      {
        
      // detectin g the files
        
      System.out.println("Detecting drives...");
      File[] drives = File.listRoots();
      if (drives == null || drives.length == 0)
      {
        System.out.println("No drives found.");
        return;
      }
      
      for (File d : drives)
          System.out.println(d.getAbsolutePath());
                  
      System.out.print("\nEnter the file/folder name to search: ");
      String searchName = input.nextLine().trim();
      
      String baseName;
      String extName;
        
      if (searchName.contains("."))
      {
        int i = searchName.lastIndexOf('.');
        baseName = searchName.substring(0, i);
        extName = searchName.substring(i + 1);
      } 
      else
      {
        baseName = searchName;
        extName = "";
      }
      
      long start = System.currentTimeMillis();

      try (ForkJoinPool pool = new ForkJoinPool(
            Math.max(16, Runtime.getRuntime().availableProcessors() * 4)))
      {
          for (File drive : drives)
          {
              if (drive.exists() && drive.canRead())
              {
                  Path root = drive.toPath();
                  pool.execute(new SearchTask(root, baseName, extName));
              }
            }

      pool.shutdown();
      pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
