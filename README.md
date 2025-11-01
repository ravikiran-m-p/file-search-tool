# File Searcher

**File Searcher** is a fast, concurrent Java utility that efficiently searches your entire computer for files or folders by name.  
It leverages **Java’s ForkJoinPool** to scan all available drives in parallel, providing lightning-fast results even on large file systems.

---


## Features

-  **Multi-threaded search** using Java’s `ForkJoinPool`
-  **Automatic drive detection** (e.g., `C:\`, `D:\`, `E:\`)
-  **Search by file or folder name** (case-insensitive)
-  **Safe concurrent result collection** via `ConcurrentLinkedQueue`
-  Displays **total search time** and **number of threads used**
-  Silently **skips restricted or inaccessible directories**
-  Optimized for both **speed** and **accuracy**

---
