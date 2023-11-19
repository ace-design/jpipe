grammar JPipe;

/******************
 ** Parser rules **
 ******************/

// Root rule for parsing (called by the compiler)
unit            : (justification | pattern | load | composition)+ EOF;

// Load contents from another file
load            : LOAD file=STRING;

// Declare a justification
justification   : JUSTIFICATION id=ID (impl=IMPLEMENTS parent=ID)? OPEN justif_body CLOSE;
justif_body     : (evidence | sub_conclusion | strategy | relation | conclusion)+;

// Definition of a pattern
pattern         : PATTERN id=ID OPEN pattern_body CLOSE;
pattern_body    : (abs_support | evidence | sub_conclusion | strategy | relation | conclusion)+;

// Body of a justification/pattern content

identified_element: id=ID IS name=STRING;
evidence        : EVIDENCE      identified_element;
strategy        : STRATEGY      identified_element;
sub_conclusion  : SUBCONCLUSION identified_element;
conclusion      : CONCLUSION    identified_element;
abs_support     : ABS_SUPPORT   identified_element;

relation        : from=ID SUPPORT_LNK to=ID;

// Definition of a composition
composition     : COMPOSITION name=ID OPEN directive+ CLOSE;
directive       : merge_directive;  // Will eventually support weaving
merge_directive : JUSTIFICATION id=ID IS merge_equation;
merge_equation  : left=ID COMPO_OPERATOR (right=ID | merge_equation);


/*****************
 ** Lexer rules **
 *****************/

// Keywords
PATTERN         : 'pattern';
ABS_SUPPORT     : '@support';
JUSTIFICATION   : 'justification';
IMPLEMENTS      : 'implements';
IS              : 'is';
EVIDENCE        : 'evidence';
STRATEGY        : 'strategy';
SUBCONCLUSION   : 'sub-conclusion';
CONCLUSION      : 'conclusion';
SUPPORT_LNK     : 'supports';
LOAD            : 'load';
COMPOSITION     : 'composition';
COMPO_OPERATOR  : 'with';

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


/*************************************************************
 ** Deads code related to justification implementation  TBD **
 *************************************************************/

// PARSING

//implementation  : IMPLEMENTATION id=ID OF justifiation_id=ID OPEN impl_body CLOSE;
//impl_body       : (IMPLEMENTS id=ID OPEN (probe | operation) expectation? CLOSE)+;
//probe           : PROBE IS command;
//operation       : OPERATION IS command;
//expectation     : EXPECTATION IS expression;
//command         : id=ID command_args?;
//command_args    : OP_CALL STRING (SEP_CALL STRING)* CL_CALL;
//expression      : (boolean_expr | command) (op=BOOL_OP expression)*;
//boolean_expr    : (NOT)? symbol=ID (op=ARITH_OP INTEGER)?;


// LEXING

//IMPLEMENTATION  : 'implementation';
//OF              : 'of';

//PROBE           : 'probe';
//EXPECTATION     : 'expectation';
//OPERATION       : 'operation';
//BOOL_OP         : 'or' | 'and';
//NOT             : 'not';
//ARITH_OP        : '==' | '>' | '<' | '<=' | '>=';
