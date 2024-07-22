grammar Jpipe

entry Model:
    (entries+=Justification)*;



Justification:
    JustificationClass body=JustificationBody;

JustificationClass:
    kind=('justification') name=ID;

JustificationBody:
    '{' (variables+=JustificationVariable | instructions+=JustificationInstruction | supports += JustificationSupport )* '}';

JustificationVariable: 
    kind=('evidence' | 'strategy' | 'sub-conclusion' | 'conclusion') name=ID;

JustificationInstruction:
    JustificationVariable 'is' information=STRING;

JustificationSupport:
    left=[JustificationVariable:ID] 'supports' right=[JustificationVariable:ID];


hidden terminal WS: /\s+/;
terminal ID: /[_a-zA-Z][\w_]*/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;
hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;
