grammar Jpipe

entry Model:
    (entries+=Body)*;

Body:
    Justification;

Justification:
    kind=('justification') name=ID '{' (variables+=Variable | instructions+=Instruction | supports += Support )* '}';

Variable: 
    kind=('evidence' | 'strategy' | 'sub-conclusion' | 'conclusion') name=ID;

Instruction:
    Variable 'is' information=STRING;

Support:
    supporter=[Variable:ID] 'supports' supportee=[Variable:ID];

hidden terminal WS: /\s+/;
terminal ID: /[_a-zA-Z][\w_]*/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;
hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;
