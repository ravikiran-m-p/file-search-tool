package File_searcher;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class file_searcher
{
  private static final List<File> found_files = Collections.synchronizedList(new ArrayList<>());

  public static void main(String[] args)
  {
     
