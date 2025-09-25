public class App {
    private final String[] directories = {
        "submod",
        "submod/lib",
        "submod/utl"
    };

    private class LineCountInfo {
        private final String filePath;
        private final int lineCount;

        public LineCountInfo(String filePath, int lineCount) {
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

    private final LineCountInfo[] lineCountInfos = {
        new LineCountInfo("submod/SubmodMain.java", 120),
        new LineCountInfo("submod/SubmodHelper0.java", 120),
        new LineCountInfo("submod/lib/LibMain.java", 39),
        new LineCountInfo("submod/ext/HelloWorld.java",50),
        new LineCountInfo("submod/utl/UtlMain.java", 220),
        new LineCountInfo("submod/ext/CreateHell.java", 34),
    };

    public static void main(String[] args) throws Exception {
        App main = new App();
        main.run();
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

    private boolean alsoMatches(String filePath, String dir, String[] directories) {
        for (String d : directories) {
            if (!d.equals(dir) && !matches(dir,d) && matches(filePath, d)) {
                return true;
            }
        }
        return false;
    }
}
