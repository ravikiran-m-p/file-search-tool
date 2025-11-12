# File Searcher

**File Searcher** is a fast, concurrent Java utility that efficiently searches your entire computer for files or folders by name.  
It leverages **Java’s ForkJoinPool** to scan all available drives in parallel, providing lightning-fast results even on large file systems.

---

## Features

-  **Multi-threaded search** using Java’s `ForkJoinPool`
-  **Automatic drive detection** (e.g., `C:\`, `D:\`, `E:\`, `P:\`)
-  **Search by file or folder name** (case-insensitive)
-  **Safe concurrent result collection** via `ConcurrentLinkedQueue`
-  Displays **total search time** and **number of threads used**
-  Silently **skips restricted or inaccessible directories**
-  Optimized for both **speed** and **accuracy**

---


## Technologies Used

| Component | Purpose |
|------------|----------|
| **Java 17+** | Core programming language |
| **ForkJoinPool** | Multi-threaded parallel execution |
| **java.nio.file.\*** | Efficient filesystem traversal |
| **java.util.concurrent.\*** | Thread-safe concurrent data structures |

---

## How It Works

1. **Detects all available system drives** automatically.  
2. **Prompts the user** to enter a file or folder name.  
3. **Creates parallel search tasks** using a `ForkJoinPool`:  
   - Each task recursively scans directories in its assigned drive.  
   - Matching file/folder names are stored in a **thread-safe queue**.  
4. Once all tasks complete, the program prints:  
   -  All matching paths  
   -  Total time taken  
   -  Number of threads used

---

##  Example Output

Detecting drives... C: D: E: P:\

Enter the file/folder name to search: sample.txt

Files/Folders found:
C:\Users\ravikiran\Documents\sample.txt
D:\Backup\sample.txt

Search completed in 3.27 seconds using 32 threads.

---

## How to Run

1. **Clone the repository:**
   ```bash
   https://github.com/ravikiran-m-p/file-search-tool.git

2. Open the project in your preferred Java IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

3. Compile the code: ```javac -d out src/File_searcher/file_searcher.java```

4. Run the program: ``` java -cp out File_searcher.file_searcher ```

---

## Notes

- Requires read permissions for all drives to access subdirectories.

- Silently skips restricted or inaccessible folders.

- Handles both:

  1. File names (e.g., notes.txt),
  
  2. Folder names (e.g., Documents)

- Performance depends on:

  1. File system size,
      
  2. Thread count,
      
  3. System I/O speed

---

 ## Future Enhancements

- Add regex-based search for advanced matching.
- Include file content search (like grep).
- Provide a GUI version with progress visualization.
- Add export to file (e.g., .txt or .csv).
- Integrate search history or favorites.
