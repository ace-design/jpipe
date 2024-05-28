grammar Jpipe

entry Model:
    (entries+=Body)*;

Body:
    Justification;

Justification:
    kind=('justification') name=ID '{' ( instructions+=Instruction | supports += Support )* '}';


Instruction:
    kind=('evidence' | 'strategy' | 'sub-conclusion' | 'conclusion') name=ID 'is' information=STRING;

Support:
    supporter=[Instruction] 'supports' supportee=[Instruction]; 

hidden terminal WS: /\s+/;
terminal EV returns string: /evidence/;
terminal ID: /[_a-zA-Z][\w_]*/;
terminal INT returns number: /[0-9]+/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;
hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;
