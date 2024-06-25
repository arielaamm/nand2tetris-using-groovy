public class Tokenizing {
    def tokenizing(File folder)
    {
        folder.eachFile { file ->
            if (file.name.endsWith('.jack'))
            {
                def outputFile = new File("${folder.path}/${file.name-".jack"}DEMO.xml").newWriter()
                String buffer = ""
                buffer += "<tokens>\n"
                buffer += writeToXml(file as File)
                buffer += "</tokens>\n"
                outputFile << buffer
                outputFile.close()
                return outputFile
            }
        }
    }

    def String writeToXml(File file) {
        String buffer = ""
        def regex = /\b[A-Za-z_]\w*\b|\b(class|constructor|function|method|field|static|var|int|char|boolean|void|true|false|null|this|let|do|if|else|while|return)\b|[{}()\[\].,;+\-*&|<>=~\/]|\b\d+\b|"(?:[^"\n]|\\")*"/
        file.eachLine { line ->
            if (!line.trim().startsWith("//") && !line.trim().startsWith("/*")) {
                line.findAll(regex) { matches ->
                    def match = matches[0]
                    if (match == "class" || match == "constructor" || match == "function" || match == "method" || match == "field" ||
                            match == "static" || match == "var" || match == "int" || match == "char" || match == "boolean" || match == "void" ||
                            match == "true" || match == "false" || match == "null" || match == "this" || match == "let" || match == "do" ||
                            match == "if" || match == "else" || match == "while" || match == "return")
                    {
                        buffer += "<keyword> ${match} </keyword>\n"
                    }
                    else if (match == "}" || match == "{" || match == "[" || match == "]" || match == "(" || match == ")" || match == "." ||
                            match == "," || match == ";" || match == "+" || match == "-" || match == "*" || match == "/" || match == "&" ||
                            match == "|" || match == "<" || match == ">" || match == "="||match=="~")
                    {
                        if (match == "<")
                            buffer += "<symbol> &lt; </symbol>\n"
                        else if (match == ">")
                            buffer += "<symbol> &gt; </symbol>\n"
                        else if (match == "&")
                            buffer += "<symbol> &amp; </symbol>\n"
                        else if (match == "\"")
                            buffer += "<symbol> &quot; </symbol>\n"
                        else
                            buffer += "<symbol> ${match} </symbol>\n"

                    }
                    else if (match.isNumber())
                    {
                        buffer += "<integerConstant> ${match} </integerConstant>\n"

                    }
                    else if (match.startsWith('"') && match.endsWith('"'))
                    {
                        buffer += "<stringConstant> ${match.replace("\"","")} </stringConstant>\n"
                    }
                    else{
                        buffer += "<identifier> ${match} </identifier>\n"
                    }
                }
            }
        }
        return buffer
    }
}