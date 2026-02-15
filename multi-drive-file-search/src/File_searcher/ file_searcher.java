import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;


public class file_searcher
{

    // thread-safe collection for results
  private static final Collection<Path> results = new ConcurrentLinkedQueue<>();
    //high-performance counter for real-time progress
  private static final LongAdder items_scanned = new LongAdder();

  public static void main(String[] args)
  {
        try (Scanner sc = new Scanner(System.in))
        {
            //detect all drives
            System.out.println("Detecting system drives...");
            File[] drives = File.listRoots();
          
            if (drives == null || drives.length == 0)
            {
                System.out.println("No drives detected.");
                return;
            }

            for (File d : drives) System.out.println("Found Drive: " + d.getAbsolutePath());

            // user Input / target search file 
            System.out.print("\nEnter file or folder name to find: ");
            String target = sc.nextLine().trim().toLowerCase();

            if (target.isEmpty())
            {
                System.out.println("Search name cannot be empty.");
                return;
            }

            System.out.println("\n--- Starting Parallel Search ---");
            long start = System.currentTimeMillis();

            //setup ForkJoinPool (Uses all CPU cores)
            int cores = Runtime.getRuntime().availableProcessors();
            try (ForkJoinPool pool = new ForkJoinPool(cores))
            {
                
                for (File drive : drives)
                {
                    if (drive.canRead())                
                        pool.execute(new search_task(drive.toPath(), target));
                }

                // monitoring loop -  prints progress while threads work
                while (!pool.isQuiescent())
                {
                    System.out.print("\rTotal items scanned: " + items_scanned.sum());
                    Thread.sleep(150); 
                }

                pool.shutdown();
                pool.awaitTermination(1, TimeUnit.HOURS);
            }

            long end = System.currentTimeMillis();
            
            //final reports
            System.out.println("\n\n--- RESULTS ---");
            if (results.isEmpty())
                System.out.println("No matches found for: " + target);
            else
            {
                results.forEach(path -> System.out.println("[FOUND] " + path.toAbsolutePath()));
                System.out.println("\nTotal Matches: " + results.size());
            }
            
            double duration = (end - start) / 1000.0;
          
            System.out.printf("Search finished in %.2f seconds. Scanned %d total files/folders.\n", duration, items_scanned.sum());

        }
        catch (Exception e)
        {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     internal task class that handles the recursive searching logic.
     **/
    static class search_task extends RecursiveAction
    {
        private final Path path;
        private final String target;

        search_task(Path path, String target)
      {
            this.path = path;
            this.target = target;
        }

        @Override
        protected void compute()
      {
            List<search_task> tasks = new ArrayList<>();

          
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path))
            {
                for (Path entry : stream)
                {
                    items_scanned.increment();
                    
                    String name = entry.getFileName().toString().toLowerCase();

                    //check if the current file/folder name contains the target string
                    if (name.contains(target))
                        results.add(entry);
                    

                    //if it's a folder, fork a new task into the pool
                    if (Files.isDirectory(entry, LinkOption.NOFOLLOW_LINKS))
                    {
                        search_task t = new search_task(entry, target);
                        // Puting task in work-stealing queue
                        t.fork(); 
                        tasks.add(t);
                    }
                }
            }
            catch (IOException | SecurityException ignored)
            {
                //skips system folders(secured privatefolders) automaticall
            }

            //clean up &  ensure all sub-tasks finish
            for (search_task t : tasks)
                t.join();
            
        }
    }
}
