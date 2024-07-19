grammar JPipe;

/******************
 ** Parser rules **
 ******************/

// Root rule for parsing (called by the compiler)
unit            : (justification | pattern | load | implementation)+ EOF;

justification   : JUSTIFICATION id=ID (impl=IMPLEMENTS parent=ID)? OPEN justif_body CLOSE;
justif_body     : (evidence | sub_conclusion | strategy | relation | conclusion)+;

pattern         : PATTERN id=ID OPEN pattern_body CLOSE;
pattern_body    : (abs_support | evidence | sub_conclusion | strategy | relation | conclusion)+;

load            : LOAD file=STRING;

implementation  : IMPLEMENTATION id=ID OF justifiation_id=ID OPEN impl_body CLOSE;
impl_body       : (IMPLEMENTS id=ID OPEN (probe | operation) expectation? CLOSE)+;

identified_element: id=ID IS name=STRING;
evidence        : EVIDENCE      identified_element;
strategy        : STRATEGY      identified_element;
sub_conclusion  : SUBCONCLUSION identified_element;
conclusion      : CONCLUSION    identified_element;
abs_support     : ABS_SUPPORT   identified_element;

probe           : PROBE IS command;
operation       : OPERATION IS command;
expectation     : EXPECTATION IS expression;

relation        : from=ID SUPPORT_LNK to=ID;

command         : id=ID command_args?;
command_args    : OP_CALL STRING (SEP_CALL STRING)* CL_CALL;

expression      : (boolean_expr | command) (op=BOOL_OP expression)*;
boolean_expr    : (NOT)? symbol=ID (op=ARITH_OP INTEGER)?;

/*****************
 ** Lexer rules **
 *****************/

// Keywords
PATTERN         : 'pattern';
ABS_SUPPORT     : '@support';
JUSTIFICATION   : 'justification';
IS              : 'is';
EVIDENCE        : 'evidence';
STRATEGY        : 'strategy';
SUBCONCLUSION   : 'sub-conclusion';
CONCLUSION      : 'conclusion';
SUPPORT_LNK     : 'supports';
LOAD            : 'load';
IMPLEMENTATION  : 'implementation';
OF              : 'of';
IMPLEMENTS      : 'implements';
PROBE           : 'probe';
EXPECTATION     : 'expectation';
OPERATION       : 'operation';
BOOL_OP         : 'or' | 'and';
NOT             : 'not';
ARITH_OP        : '==' | '>' | '<' | '<=' | '>=';

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
OP_CALL     : '(';
CL_CALL     : ')';
SEP_CALL    : ',';

fragment STRING_CHAR : ~('\r' | '\n');
