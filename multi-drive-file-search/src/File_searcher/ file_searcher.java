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
      
          try (ForkJoinPool pool = new ForkJoinPool(Math.max(16, Runtime.getRuntime().availableProcessors() * 4)))
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
          
              long end = System.currentTimeMillis();
          
              if (foundPaths.isEmpty())
                  System.out.println("\nNo files or folders found named: " + searchName);
              else 
              {
                  System.out.println("\nFiles/Folders found:");
                  foundPaths.forEach(p -> System.out.println(p.toAbsolutePath()));
              }
              System.out.printf("\nSearch completed in %.2f seconds using %d threads.%n",(end - start) / 1000.0, pool.getParallelism());
          }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
  }

  static class SearchTask extends RecursiveAction 
  {
    private final Path dir;
    private final String base;
    private final String ext;

    SearchTask(Path dir, String base, String ext)
    {
        this.dir = dir;
        this.base = base;
        this.ext = ext;
    }
    @Override
    protected void compute()
    {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) 
        {
          List<SearchTask> subtasks = new CopyOnWriteArrayList<>();
          for (Path path : stream)
          {
            try 
            {
              if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS))
              {
                String name = path.getFileName().toString();
                if (name.equalsIgnoreCase(base))
                foundPaths.add(path);
                subtasks.add(new SearchTask(path, base, ext));
              } 
              else
              {
                String name = path.getFileName().toString();
                int dot = name.lastIndexOf('.');
                String b = dot >= 0 ? name.substring(0, dot) : name;
                String e = dot >= 0 ? name.substring(dot + 1) : "";
                if (b.equalsIgnoreCase(base) &&(ext.isEmpty() || e.equalsIgnoreCase(ext)))
                  foundPaths.add(path);
                }

