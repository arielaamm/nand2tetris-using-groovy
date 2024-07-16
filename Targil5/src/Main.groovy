
import javax.swing.JFileChooser
public class FileUtils {
    def String chooseFile(){
        def initialDirectory = new File("C:\\Users\\arial\\groovy\\nand2tetris\\projects")
        def fileChooser = new JFileChooser(initialDirectory)
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
        int returnValue = fileChooser.showOpenDialog(null)

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            def directory = fileChooser.getSelectedFile()
            return directory.absolutePath
        }
    }
}

FileUtils fileUtils = new FileUtils()
def path = fileUtils.chooseFile()
def inputFile = new File(path)


File[] files = inputFile.isFile() ? new File[] { inputFile } : inputFile.listFiles();

for (File file : files) {
    if (!file.getName().endsWith(".jack")) {
        continue;
    }

    File outputFile = new File(file.getAbsolutePath().replace(".jack", ".vm"));

    CompilationEngine compilationEngine = new CompilationEngine(file, outputFile);

    try {
        compilationEngine.compileClass();
    } catch (CompilationEngineException e) {
        e.printStackTrace();
    }
}



