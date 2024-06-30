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
def folder = new File(path)
Tokenizing t = new Tokenizing()
def outputFile = t.tokenizing(folder)
Parsing p = new Parsing()
outputFile = p.parsing(folder)