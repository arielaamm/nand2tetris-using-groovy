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
def outputFile = new File("${folder.path}/${folder.name}.asm").newWriter()
String buffer = ""
int counter = 0
def vmFiles = folder.listFiles().findAll { it.name.endsWith('.vm') }

if (vmFiles.size() >=2)
{
  buffer = "@256\n" +
          "D=A\n" +
          "@SP\n" +
          "M=D\n"
  def line = "call Sys.init 0"
  buffer += callF(line.split(" "),counter)
  counter++
  buffer += "@Sys.init\n" +
          "0;JMP\n"
}
//if (folder.listFiles().name.contains("Sys.vm") && vmFiles.size() == 1)
//        {
//          buffer = "@261\n" +
//                  "D=A\n" +
//                  "@SP\n" +
//                  "M=D\n" +
//                  "@Sys.init\n" +
//                  "0;JMP\n"
//        }
folder.eachFile { file ->
  if (file.name.endsWith(".vm")) {
    file.eachLine { line ->
      if(!line.startsWith("//")) {
        def parts = line.split(" ")
        def operation = parts[0]
        switch(operation) {
          case "push":
            buffer += push(parts,file.name - ".vm")
            break
          case "add":
            buffer += add()
            break
          case "sub":
            buffer += sub()
            break
          case "pop":
            buffer += pop(parts,file.name - ".vm")
            counter++
            break
          case "neg":
            buffer += neg()
            break
          case "eq":
            buffer += eq(counter)
            counter++
            break
          case "gt":
            buffer += gt(counter)
            counter++
            break
          case "lt":
            buffer += lt(counter)
            counter++
            break
          case "and":
            buffer += and()
            break
          case "or":
            buffer += or()
            break
          case "not":
            buffer += not()
            break
          case "label":
            buffer += label(parts)
            break
          case "if-goto":
            buffer += ifGoto(parts)
            break
          case "function":
            buffer += function(parts)
            break
          case "call":
            buffer += callF(parts,counter)
            counter++
            break
          case "return":
            buffer += returnF()
            break
          case "goto":
            buffer += gotoF(parts)
            break
          case "#and":
            buffer += and_new(parts)
            break
        }
        buffer += "\n"
      }
      else
        return
    }
  }
}


outputFile << buffer
outputFile.close()


def String push(String[] strings,String name) {
  def str = ""
  switch (strings[1]) {
  //grope 5
    case "constant":
      return  "@${strings[2]}\n" +
              "D=A\n" +
              "@SP\n" +
              "A=M\n" +
              "M=D\n" +
              "@SP\n" +
              "M=M+1\n"
      //grope 1
    case "local":
      str +=  "@${strings[2]}\n" +
              "D=A\n" +
              "@LCL\n" +
              "A=M+D\n" +
              "D=M\n" +
              "@SP\n" +
              "A=M\n" +
              "M=D\n" +
              "@SP\n" +
              "M=M+1\n"

      break
    case "argument":
      str +=  "@${strings[2]}\n" +
              "D=A\n" +
              "@ARG\n" +
              "A=M+D\n" +
              "D=M\n" +
              "@SP\n" +
              "A=M\n" +
              "M=D\n" +
              "@SP\n" +
              "M=M+1\n"
      break
    case "this":
      str +=  "@${strings[2]}\n" +
              "D=A\n" +
              "@THIS\n" +
              "A=M+D\n" +
              "D=M\n" +
              "@SP\n" +
              "A=M\n" +
              "M=D\n" +
              "@SP\n" +
              "M=M+1\n"
      break
    case "that":
      str +=  "@${strings[2]}\n" +
              "D=A\n" +
              "@THAT\n" +
              "A=M+D\n" +
              "D=M\n" +
              "@SP\n" +
              "A=M\n" +
              "M=D\n" +
              "@SP\n" +
              "M=M+1\n"
      break
    case "temp":
      int temp = 5 + strings[2].toInteger()
      str += "@${temp}\n" +
              "D=M\n" +
              "@SP\n" +
              "A=M\n" +
              "M=D\n" +
              "@SP\n" +
              "M=M+1\n"
      break
    case "pointer":
      if (strings[2] == "0") {
        str +=  "@THIS\n" +
                "D=M\n" +
                "@SP\n" +
                "A=M\n" +
                "M=D\n" +
                "@SP\n" +
                "M=M+1\n"
      }
      else if (strings[2] == "1") {
        str +=  "@THAT\n" +
                "D=M\n" +
                "@SP\n" +
                "A=M\n" +
                "M=D\n" +
                "@SP\n" +
                "M=M+1\n"
      }
      break
    case "static":
      def temp = "${name}.${strings[2].toInteger()}"
      str =   "@${temp}\n" +
              "D=M\n" +
              "@SP\n" +
              "A=M\n" +
              "M=D\n" +
              "@SP\n" +
              "M=M+1\n"
      break
  }
  return str
}

def String add() {
  return "@SP\n" +
          "A=M-1\n" +
          "D=M\n" +
          "A=A-1\n" +
          "M=M+D\n" +
          "@SP\n" +
          "M=M-1\n"
}

def String sub() {
  return "@SP\n" +
          "A=M-1\n" +
          "D=M\n" +
          "A=A-1\n" +
          "M=M-D\n" +
          "@SP\n" +
          "M=M-1\n"
}

def String pop(String[] strings,String name) {
  def str = ""
  switch (strings[1]) {
  //grope 1
    case "local":
      str +=  "@SP\n" +
              "A=M-1\n" +
              "D=M\n" +
              "@LCL\n" +
              "A=M\n"
      for (int i = 0; i < strings[2].toInteger(); i++) {
        str += "A=A+1\n"
      }
      str += "M=D\n" +
              "@SP\n" +
              "M=M-1\n"
      break
    case "argument":
      str +=  "@SP\n" +
              "A=M-1\n" +
              "D=M\n" +
              "@ARG\n" +
              "A=M\n"
      for (int i = 0; i < strings[2].toInteger(); i++) {
        str += "A=A+1\n"
      }
      str += "M=D\n" +
              "@SP\n" +
              "M=M-1\n"
      break
    case "this":
      str +=  "@SP\n" +
              "A=M-1\n" +
              "D=M\n" +
              "@THIS\n" +
              "A=M\n"
      for (int i = 0; i < strings[2].toInteger(); i++) {
        str += "A=A+1\n"
      }
      str += "M=D\n" +
              "@SP\n" +
              "M=M-1\n"
      break
    case "that":
      str +=  "@SP\n" +
              "A=M-1\n" +
              "D=M\n" +
              "@THAT\n" +
              "A=M\n"
      for (int i = 0; i < strings[2].toInteger(); i++) {
        str += "A=A+1\n"
      }
      str += "M=D\n" +
              "@SP\n" +
              "M=M-1\n"
      break
    case "temp":
      str +=  "@SP\n" +
              "A=M-1\n" +
              "D=M\n"
      int temp = 5 + strings[2].toInteger()
      str +=  "@${temp}\n" +
              "M=D\n" +
              "@SP\n" +
              "M=M-1\n"
      break
    case "pointer":
      if (strings[2] == "0") {
        str +=  "@SP\n" +
                "A=M-1\n" +
                "D=M\n" +
                "@THIS\n" +
                "M=D\n"+
                "@SP\n" +
                "M=M-1\n"
      }
      else if (strings[2] == "1") {
        str +=  "@SP\n" +
                "A=M-1\n" +
                "D=M\n" +
                "@THAT\n" +
                "M=D\n"+
                "@SP\n" +
                "M=M-1\n"
      }
      break
    case "static":
      str =   "@SP\n" +
              "M=M-1\n" +
              "A=M\n" +
              "D=M\n"
      def temp = "${name}.${strings[2].toInteger()}"
      str += "@${temp}\n" +
              "M=D\n"
      break
  }
  return str
}

def String or() {
  return "@SP\n" +
          "A=M-1\n" +
          "D=M\n" +
          "A=A-1\n" +
          "M=M|D\n" +
          "@SP\n" +
          "M=M-1\n"
}

def String neg() {
  return "@SP\n" +
          "A=M-1\n" +
          "M=-M\n"
}

def String and() {
  return "@SP\n" +
          "A=M-1\n" +
          "D=M\n" +
          "A=A-1\n" +
          "M=M&D\n" +
          "@SP\n" +
          "M=M-1\n"
}

def String eq(int counter) {
  return "@SP\n" +
          "A=M-1\n" +
          "D=M\n" +
          "A=A-1\n" +
          "D=D-M\n" +
          "@IF_TRUE${counter}\n" +
          "D;JEQ\n" +
          "D=0\n" +
          "@IF_FALSE${counter}\n" +
          "0;JMP\n" +
          "(IF_TRUE${counter})\n" +
          "D=-1\n" +
          "(IF_FALSE${counter})\n" +
          "@SP\n" +
          "A=M-1\n" +
          "A=A-1\n" +
          "M=D\n" +
          "@SP\n" +
          "M=M-1\n"


}

def String lt(int counter) {
  return "@SP\n" +
          "A=M-1\n" +
          "D=M\n" +
          "A=A-1\n" +
          "D=M-D\n" +
          "@IF_TRUE${counter}\n" +
          "D;JLT\n" +
          "D=0\n" +
          "@IF_FALSE${counter}\n" +
          "0;JMP\n" +
          "(IF_TRUE${counter})\n" +
          "D=-1\n" +
          "(IF_FALSE${counter})\n" +
          "@SP\n" +
          "A=M-1\n" +
          "A=A-1\n" +
          "M=D\n" +
          "@SP\n" +
          "M=M-1\n"
}

def String gt(int counter) {
  return "@SP\n" +
          "A=M-1\n" +
          "D=M\n" +
          "A=A-1\n" +
          "D=M-D\n" +
          "@IF_TRUE${counter}\n" +
          "D;JGT\n" +
          "D=0\n" +
          "@IF_FALSE${counter}\n" +
          "0;JMP\n" +
          "(IF_TRUE${counter})\n" +
          "D=-1\n" +
          "(IF_FALSE${counter})\n" +
          "@SP\n" +
          "A=M-1\n" +
          "A=A-1\n" +
          "M=D\n" +
          "@SP\n" +
          "M=M-1\n"
}

def String not() {
  return "@SP\n" +
          "A=M-1\n" +
          "M=!M\n"
}

def String label(String[] strings) {
  return "(${strings[1]})\n"
}

def String ifGoto(String[] strings) {
  return "@SP\n" +
          "M=M-1\n" +
          "A=M\n" +
          "D=M\n" +
          "@${strings[1]}\n" +
          "D;JNE\n"
}

def String gotoF(String[] strings) {
  return "@${strings[1]}\n" +
          "0;JMP\n"
}

def String function(String[] strings) {
  def str = "(${strings[1]})\n" +
          "@${strings[1]}.local\n" +
          "M=0\n" +
          "(${strings[1]}.loc_loop)\n" +
          "@${strings[2]}\n" +
          "D=A\n" +
          "@${strings[1]}.local\n" +
          "D=D-M\n" +
          "@${strings[1]}.end_loc_loop\n" +
          "D;JEQ\n" +
          "@SP\n" +
          "M=M+1\n" +
          "A=M-1\n" +
          "M=0\n" +
          "@${strings[1]}.local\n" +
          "M=M+1\n" +
          "@${strings[1]}.loc_loop\n" +
          "0;JMP\n" +
          "(${strings[1]}.end_loc_loop)\n"
  return str
}

def String callF(String[] strings,int counter) {
  def str = "@${strings[1]}.return${counter}\n" +
          "D=A\n" +
          "@SP\n" +
          "M=M+1\n" +
          "A=M-1\n" +
          "M=D\n" +
          "@LCL\n" +
          "D=M\n" +
          "@SP\n" +
          "M=M+1\n" +
          "A=M-1\n" +
          "M=D\n" +
          "@ARG\n" +
          "D=M\n" +
          "@SP\n" +
          "M=M+1\n" +
          "A=M-1\n" +
          "M=D\n" +
          "@THIS\n" +
          "D=M\n" +
          "@SP\n" +
          "M=M+1\n" +
          "A=M-1\n" +
          "M=D\n" +
          "@THAT\n" +
          "D=M\n" +
          "@SP\n" +
          "M=M+1\n" +
          "A=M-1\n" +
          "M=D\n" +
          "D=A+1\n"
  int temp = 5 + strings[2].toInteger()
  str +=  "@${temp}\n" +
          "D=D-A\n" +
          "@ARG\n" +
          "M=D\n" +
          "@SP\n" +
          "D=M\n" +
          "@LCL\n" +
          "M=D\n" +
          "@${strings[1]}\n" +
          "0;JMP\n"
  def temp1 =  "${strings[1]}.return${counter}"
  str += "("+ temp1 + ")\n"
  return str
}

def String returnF() {
  // save the return address
  def str = "@LCL\n" +
          "D=M\n" +
          "@5\n" +
          "A=D-A\n" +
          "D=M\n" +
          "@13\n" +
          "M=D\n" +
          "@SP\n" +
          "M=M-1\n" +
          "A=M\n"+
          "D=M\n" +
          "@ARG\n" +
          "A=M\n" +
          "M=D\n"
  // pop the return value
  str += "@ARG\n" +
          "D=M\n" +
          "@SP\n" +
          "M=D+1\n" +
          "@LCL\n" +
          "M=M-1\n" +
          "A=M\n" +
          "D=M\n" +
          "@THAT\n" +
          "M=D\n" +
          "@LCL\n" +
          "M=M-1\n" +
          "A=M\n" +
          "D=M\n" +
          "@THIS\n" +
          "M=D\n" +
          "@LCL\n" +
          "M=M-1\n" +
          "A=M\n" +
          "D=M\n" +
          "@ARG\n" +
          "M=D\n" +
          "@LCL\n" +
          "M=M-1\n" +
          "A=M\n" +
          "D=M\n" +
          "@LCL\n" +
          "M=D\n" +
          "@13\n" +
          "A=M\n" +
          "0;JMP\n"
  return str
}

def String and_new(String[] strings) {
  def str = "@SP\n" +
          "A=M-1\n" +
          "D=M\n" +
          "A=A-1\n" +
          "M=D&M\n" +
          "@SP\n" +
          "M=M-1\n" +
          "@SP\n" +
          "A=M-1\n" +
          "M=!M\n"
  return str
}
