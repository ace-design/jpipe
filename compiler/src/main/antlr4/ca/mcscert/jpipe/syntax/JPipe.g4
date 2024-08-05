grammar JPipe;

/******************
 ** Parser rules **
 ******************/

// Root rule for parsing (called by the compilationChain)
unit            : (justification)+ EOF;

// Declare a justification
justification   : JUSTIFICATION id=ID OPEN justif_body CLOSE;
justif_body     : (evidence | sub_conclusion | strategy | relation | conclusion)+;

// Body of a justification/pattern content

element:        id=ID IS name=STRING;
evidence        : EVIDENCE      element;
strategy        : STRATEGY      element;
sub_conclusion  : SUBCONCLUSION element;
conclusion      : CONCLUSION    element;

relation        : from=ID SUPPORT_LNK to=ID;

/*****************
 ** Lexer rules **
 *****************/

// Keywords

JUSTIFICATION   : 'justification';
IS              : 'is';
EVIDENCE        : 'evidence';
STRATEGY        : 'strategy';
SUBCONCLUSION   : 'sub-conclusion';
CONCLUSION      : 'conclusion';
SUPPORT_LNK     : 'supports';

// Making whitespaces and newlines irrelevant to the syntax
WHITESPACE  : [ \t]+                -> channel(HIDDEN);
NEWLINE     : ('\r'? '\n' | '\r')+  -> channel(HIDDEN);

// Supporting multi-line and single-line comments
COMMENT     : '/*' .*? '*/'         -> channel(HIDDEN);
LINE_COMMENT: '//'STRING_CHAR*      -> channel(HIDDEN);

// Symbols & strings
INTEGER     : [0-9][1-9]*;
ID          : ( [A-Z] | [a-z] | INTEGER | '_' | '-')+;
STRING      : '"' STRING_CHAR* '"' | '\'' STRING_CHAR* '\'' ;
OPEN        : '{';
CLOSE       : '}';

fragment STRING_CHAR : ~('\r' | '\n');

