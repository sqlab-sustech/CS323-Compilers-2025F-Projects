grammar Calc1;

expr  	: expr '*' expr
	 	| expr '/' expr
	 	| expr '+' expr
	 	| expr '-' expr
	 	| factor
        ;

factor  : INT
        | ID
        ;

INT : [0-9]+ ;
ID : [a-zA-Z]+ ;
WS  : [ \t\r\n]+ -> skip ;
