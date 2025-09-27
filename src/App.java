import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class App {
    private static List<String> directories = new ArrayList<>();

    private static class LineCountInfo {
        private final String filePath;
        private final int lineCount;

        public LineCountInfo(String filePath, int lineCount) {
            this.filePath = filePath;
            this.lineCount = lineCount;
        }

        public LineCountInfo(int lineCount, String filePath) {
            this.filePath = filePath;
            this.lineCount = lineCount;
        }

        public String getFilePath() {
            return filePath;
        }

        public int getLineCount() {
            return lineCount;
        }
    }

    private static List<LineCountInfo>  lineCountInfos = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        String directorieslist = null;
        String linecountslist = null;
        for ( String arg : args ) {
            if ( arg.equals("--help") || arg.equals("-h") ) {
                System.out.println("Usage: java -jar App.jar");
                System.out.println("The program reads 'directories.list' and 'linecounts.list' files from the current directory.");
                System.out.println("It then computes and prints the total line counts for each directory listed in 'directories.list'.");
                return;
            } else if ( directorieslist == null ) {
                directorieslist = arg;
            } else if ( linecountslist == null ) {
                linecountslist = arg;
            } else {
                System.err.println("Unknown argument: " + arg); 
                return;
            }
        }
        App main = new App();
        if ( directorieslist == null ) directorieslist = "directories.list";
        if ( linecountslist == null ) linecountslist = "linecounts.list";
        main.readDirectoriesList(directorieslist);
        main.readLineCountsList(linecountslist);
        main.run();
    }

    private void readDirectoriesList(String filename) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String inputLine;
        while ( (inputLine = reader.readLine()) != null ) {
            directories.add(inputLine);
        }
    }

    private void readLineCountsList(String filename) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String inputLine;
        boolean zeroIsNumber = false;
        int lineNo = 0;
        while ( (inputLine = reader.readLine()) != null ) {
            String[] parts = inputLine.split("\\s+");
            if ( parts.length != 2 ) {
                System.err.printf("File %s line %d is not in proper fomat.\n",filename,lineNo++);
                continue;
            }
            int lineCount;
            try {
                lineCount = Integer.parseInt(parts[0]);
                zeroIsNumber = true;
            } catch ( NumberFormatException e ) {
                lineCount = Integer.parseInt(parts[1]);
                zeroIsNumber = false;
            }
            if ( zeroIsNumber ) {
                lineCountInfos.add(new LineCountInfo(lineCount,parts[1]));
            } else {
                lineCountInfos.add(new LineCountInfo(parts[0],lineCount));
            }
            lineNo++;
        }
    }

    private void run() {
        for (String dir : directories) {
            int totalLines = 0;
            for (LineCountInfo info : lineCountInfos) {
                if (matches(info.getFilePath(), dir) && !alsoMatches(info.getFilePath(), dir, directories)) {
                    totalLines += info.getLineCount();
                }
            }
            System.out.println(dir + ": " + totalLines + " lines");
        }
    }

    private boolean matches(String filePath, String dir) {
        return filePath.startsWith(dir + "/");
    }

    private boolean alsoMatches(String filePath, String dir, List<String> directories) {
        for (String d : directories) {
            if (!d.equals(dir) && !matches(dir,d) && matches(filePath, d)) {
                return true;
            }
        }
        return false;
    }
}
