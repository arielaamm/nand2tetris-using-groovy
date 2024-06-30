public class Parsing{
    File public_file
    int countLine
    def public_buffer
    def tab
    def parsing(File folder)
    {
        folder.eachFile { file ->
            if (file.name.endsWith('TNew.xml'))
            {
                def outputFile = new File("${folder.path}/${file.name-"TNew.xml"}New.xml").newWriter()
                public_file = file
                public_buffer = ""
                tab = ""
                countLine = 1
                writeToXml(file as File)
                outputFile << public_buffer
                outputFile.close()
                return outputFile
            }
        }
    }

    def getLine() {
        int i = 0
        def returnValue = ""
        public_file.eachLine{ line ->
            if (i == countLine)
            {
                returnValue = tab + line + "\n"
            }
            i = i + 1
        }
        countLine++
        return returnValue
    }

    def checkLine()
    {
        def temp = getLine().split(" ")[1]
        countLine--
        return temp
    }

    def writeToXml(File file) {
        parsedClass()
    }

    def parsedClass() {
        public_buffer += "<class>\n"
        tab += "\t"
        public_buffer += getLine() //<keyword> class </keyword>
        public_buffer += getLine() //<identifier> $name_class </identifier>
        public_buffer += getLine() //<symbol> { </symbol>
        parsedClassVarDec()
        parsedSubroutine()
        public_buffer += getLine() //<symbol> } </symbol>
        public_buffer += "</class>\n"
    }

    def parsedClassVarDec() {
        def check = checkLine()
        while (check == "static" || check == "field") {
            public_buffer += tab + "<classVarDec>\n"
            tab += "\t"
            public_buffer += getLine()
            public_buffer += getLine()
            public_buffer += getLine()
            while (checkLine() == ",") {
                public_buffer += getLine()
                public_buffer += getLine()
            }
            public_buffer += getLine()
            tab -= "\t"
            public_buffer += tab + "</classVarDec>\n"
            check = checkLine()
        }
    }

    def parsedSubroutine() {
        def check = checkLine()
        while (check == "constructor" || check == "function" || check == "method") {
            public_buffer += tab + "<subroutineDec>\n"
            tab += "\t"
            public_buffer += getLine()
            public_buffer += getLine()
            public_buffer += getLine()
            public_buffer += getLine()
            parsedParameterList()
            public_buffer += getLine()
            parsedSubroutineBody()
            tab -= "\t"
            public_buffer += tab + "</subroutineDec>\n"
            check = checkLine()
        }
    }

    def parsedParameterList() {
        public_buffer += tab + "<parameterList>\n"
        if (checkLine() != ")") {
            tab += "\t"
            public_buffer += getLine()
            public_buffer += getLine()
            while (checkLine() == ",") {
                public_buffer += getLine()
                public_buffer += getLine()
                public_buffer += getLine()
            }
            tab -= "\t"
        }
        public_buffer += tab + "</parameterList>\n"

    }

    def parsedSubroutineBody() {
        public_buffer += tab + "<subroutineBody>\n"
        tab += "\t"
        public_buffer += getLine()
        varDec()
        statements()
        public_buffer += getLine()
        tab -= "\t"
        public_buffer += tab + "</subroutineBody>\n"
    }

    def varDec() {
        def check = checkLine()
        while (check == "var") {
            public_buffer += tab + "<varDec>\n"
            tab += "\t"
            public_buffer += getLine()
            public_buffer += getLine()
            public_buffer += getLine()
            while (checkLine() == ",") {
                public_buffer += getLine()
                public_buffer += getLine()
            }
            public_buffer += getLine()
            tab -= "\t"
            public_buffer += tab + "</varDec>\n"
            check = checkLine()
        }
    }

    def statements() {
        public_buffer += tab + "<statements>\n"
        statement()
        public_buffer += tab + "</statements>\n"
    }

    def statement() {
        def check = checkLine()
        while (check == "let" || check == "if" || check == "while" || check == "do" || check == "return") {
            if (check == "let") {
                tab += "\t"
                parsedLetStatement()
                tab -= "\t"
            }
            if (check == "if") {
                tab += "\t"
                parsedIfStatement()
                tab -= "\t"
            }
            if (check == "while") {
                tab += "\t"
                parsedWhileStatement()
                tab -= "\t"
            }
            if (check == "do") {
                tab += "\t"
                parsedDoStatement()
                tab -= "\t"
            }
            if (check == "return") {
                tab += "\t"
                parsedReturnStatement()
                tab -= "\t"
            }
            check = checkLine()
        }
    }

    def parsedLetStatement() {
        public_buffer += tab + "<letStatement>\n"
        tab += "\t"
        public_buffer += getLine()
        public_buffer += getLine()
        if (checkLine() == "[") {
            public_buffer += getLine()
            expression()
            public_buffer += getLine()
        }
        public_buffer += getLine()
        expression()
        public_buffer += getLine()
        tab -= "\t"
        public_buffer += tab + "</letStatement>\n"
    }

    def parsedIfStatement() {
        public_buffer += tab + "<ifStatement>\n"
        tab += "\t"
        public_buffer += getLine()
        public_buffer += getLine()
        expression()
        public_buffer += getLine()
        public_buffer += getLine()
        statements()
        public_buffer += getLine()
        if (checkLine() == "else") {
            public_buffer += getLine()
            public_buffer += getLine()
            statements()
            public_buffer += getLine()
        }
        tab -= "\t"
        public_buffer += tab + "</ifStatement>\n"
    }


    def parsedWhileStatement() {
        public_buffer += tab + "<whileStatement>\n"
        tab += "\t"
        public_buffer += getLine()
        public_buffer += getLine()
        expression()
        public_buffer += getLine()
        public_buffer += getLine()
        statements()
        public_buffer += getLine()
        tab -= "\t"
        public_buffer += tab + "</whileStatement>\n"
    }

    def parsedDoStatement() {
        public_buffer += tab + "<doStatement>\n"
        tab += "\t"
        public_buffer += getLine()
        subroutineCall()
        public_buffer += getLine()
        tab -= "\t"
        public_buffer += tab + "</doStatement>\n"
    }

    def parsedReturnStatement() {
        public_buffer += tab + "<returnStatement>\n"
        tab += "\t"
        public_buffer += getLine()
        if (checkLine() != ";") {
            expression()
        }
        public_buffer += getLine()
        tab -= "\t"
        public_buffer += tab + "</returnStatement>\n"
    }

    def expression() {
        public_buffer += tab + "<expression>\n"
        tab += "\t"
        term()
        def charOp = checkLine()
        while (charOp == "+" || charOp == "-" || charOp == "*" || charOp == "/"
                || charOp == "&amp;" || charOp == "|" || charOp == "&lt;" || charOp == "&gt;" || charOp == "=") {
            public_buffer += getLine()
            term()
            charOp = checkLine()
        }
        tab -= "\t"
        public_buffer += tab + "</expression>\n"
    }

    def term() {
        public_buffer += tab + "<term>\n"
        tab += "\t"
        def check1 = checkLine()
        countLine++
        def check2 = checkLine()
        countLine--
        if (check2 == "")
        {
            public_buffer += getLine()
        }
        else if (check1 == "-" || check1 == "~") {
            public_buffer += getLine()
            term()
        }
        else if (check2 == "[")
        {
            public_buffer += getLine()
            public_buffer += getLine()
            expression()
            public_buffer += getLine()
        }
        else if (check1 == "(") {
            public_buffer += getLine()
            expression()
            public_buffer += getLine()
        }
        else if (check2 == "(" || check2 == ".")
        {
            subroutineCall()
        }

        else {
            public_buffer += getLine()
        }
        tab -= "\t"
        public_buffer += tab + "</term>\n"
    }

    def subroutineCall() {
        public_buffer += getLine()
        if (checkLine() == "(") {
            public_buffer += getLine()
            expressionList()
            public_buffer += getLine()
        }
        else if (checkLine() == ".") {
            public_buffer += getLine()
            public_buffer += getLine()
            public_buffer += getLine()
            expressionList()
            public_buffer += getLine()
        }
    }

    void expressionList() {
        public_buffer += tab + "<expressionList>\n"
        if (checkLine() != ")") {
            tab += "\t"
            expression()
            while (checkLine() == ",") {
                public_buffer += getLine()
                expression()
            }
            tab -= "\t"
        }
        public_buffer += tab + "</expressionList>\n"
    }
}
