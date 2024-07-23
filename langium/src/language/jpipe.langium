grammar Jpipe

entry Model:
    (entries += (JustificationPattern | Composition))*;

JustificationPattern:
    Class body=Body;

Class:
    kind=('justification' | 'pattern') (name=ID | name=ID 'implements' implemented=[Class]);

Body:
    '{' (variables+=Variable | instructions+=Instruction | supports += Support)* '}';

Variable: 
    kind=('evidence' | 'strategy' | 'sub-conclusion' | 'conclusion' | '@support') name=ID;

Instruction:
    Variable 'is' information=STRING;

Support:
    left=[Variable:ID] 'supports' right=[Variable:ID];


Composition:
    CompositionClass body=CompositionBody;

CompositionClass:
    kind=('composition') name=ID;

CompositionBody:
    '{' (variables+=CompositionVariable | instructions += CompositionInstruction)* '}';

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
