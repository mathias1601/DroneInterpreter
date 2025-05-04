grammar AeroScript;

@header {
package no.uio.aeroscript.antlr;
}

// Whitespace and comments added
WS           : [ \t\r\n\u000C]+ -> channel(HIDDEN);
COMMENT      : '/*' .*? '*/' -> channel(HIDDEN) ;
LINE_COMMENT : '//' ~[\r\n]* -> channel(HIDDEN) ;

LCURL   : '{';
RCURL   : '}';
LSQUARE : '[';
RSQUARE : ']';
LPAREN  : '(';
RPAREN  : ')';

NEG     : '--';
SEMI    : ';';
COMMA   : ',';
GREATER : '>';

PLUS    : '+';
MINUS   : '-';
TIMES   : '*';

ARROW   : '->';

ON      : 'on';
EVENT   : 'event';

ASCEND  : 'ascend';
BY      : 'by';
MOVE    : 'move';
TO      : 'to';
POINT   : 'point';
TURN    : 'turn';
RIGHT   : 'right';
LEFT    : 'left';
AT      : 'at';
SPEED   : 'speed';
RETURN  : 'return';
BASE    : 'base';
DESCEND : 'descend';
GROUND  : 'ground';
RANDOM  : 'random';
OBSTACLE: 'obstacle';
BATTERY : 'low battery';
MESSAGE : 'message';
FOR     : 'for';

// Keywords
ID: [a-zA-Z_]+; // Allow underscore in ID
NUMBER: [0-9]+('.'[0-9]+)?;

// Entry point
program : execution (execution)* EOF;
execution : (exec=ARROW)? define=ID LCURL (statement)* RCURL (ARROW declare=ID)?;

// Statements
statement   : action | reaction | execution;
reaction    : ON event ARROW func=ID;
event       : OBSTACLE | BATTERY | MESSAGE LSQUARE msg=ID RSQUARE ;
action      : (acAscend | acMove | acTurn | acDock | acDescend) (FOR expression 's' | AT SPEED expression)?;
acAscend    : ASCEND BY expression; 
acMove      : MOVE ('to' POINT point | 'by' expression);
acTurn      : TURN (RIGHT | LEFT)? BY expression;
acDock      : RETURN TO BASE;
acDescend   : ground ='DESCEND TO GROUND';

expression : NEG expression |
             expression TIMES expression |
             expression (PLUS | MINUS) expression |
             RANDOM rrange=expression? |
             POINT ppoint=expression |
             point |
             range |
             NUMBER |
             LPAREN expression RPAREN;

point : LPAREN expression COMMA expression RPAREN;
range : LSQUARE expression COMMA expression RSQUARE;
