grammar Jpipe

entry Model:
    (entries+=Justification)*;

Justification:
    JustificationClass body=JustificationBody;

JustificationClass infers Class:
    kind=('justification') name=ID;

JustificationBody:
    '{' (variables+=JustificationVariable | instructions+=JustificationInstruction | supports += JustificationSupport )* '}';

JustificationVariable infers Variable: 
    kind=('evidence' | 'strategy' | 'sub-conclusion' | 'conclusion') name=ID;

JustificationInstruction:
    JustificationVariable 'is' information=STRING;

JustificationSupport:
    left=[Variable:ID] 'supports' right=[Variable:ID];


hidden terminal WS: /\s+/;
terminal ID: /[_a-zA-Z][\w_]*/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;
hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;
