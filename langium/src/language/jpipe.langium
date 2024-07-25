grammar Jpipe

entry Model:
    (loads+=(Load))* (classes+=Class)*;

Load: 
    "load" class=[Class] "from" link=STRING;

Class:
    kind=('justification' | 'pattern' | 'composition') (name=ID | name=ID "implements" implemented=[Class]) (JustificationPattern | Composition);

JustificationPattern:
    '{' (variables+=Variable | instructions+=Instruction | supports += Support)* '}';

Composition:
    '{' (variables+=CompositionVariable | instructions+=CompositionInstruction)* '}';

Variable: 
    kind=('evidence' | 'strategy' | 'sub-conclusion' | 'conclusion' | '@support') name=ID;

Instruction:
    Variable 'is' information=STRING;

Support:
    left=[Variable:ID] 'supports' right=[Variable:ID];

CompositionVariable:
    kind='justification' name=ID;

CompositionInstruction:
    CompositionVariable 'is' information=CompositionInformation;

CompositionInformation:
    left=[Class] 'with' right=[Class];


hidden terminal WS: /\s+/;
terminal ID: /[_a-zA-Z][\w_]*/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;
hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;