
@0
D=A
@SP
A=M
M=D
@SP
M=M+1

@SP
A=M-1
D=M
@LCL
A=M
M=D
@SP
M=M-1

(LOOP_START)

@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

@0
D=A
@LCL
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

@SP
A=M-1
D=M
A=A-1
M=M+D
@SP
M=M-1

@SP
A=M-1
D=M
@LCL
A=M
M=D
@SP
M=M-1

@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

@1
D=A
@SP
A=M
M=D
@SP
M=M+1

@SP
A=M-1
D=M
A=A-1
M=M-D
@SP
M=M-1

@SP
A=M-1
D=M
@ARG
A=M
M=D
@SP
M=M-1

@0
D=A
@ARG
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

@SP
M=M-1
A=M
D=M
@LOOP_START
D;JNE

@0
D=A
@LCL
A=M+D
D=M
@SP
A=M
M=D
@SP
M=M+1

