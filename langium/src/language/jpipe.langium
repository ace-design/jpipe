grammar Jpipe

entry Model:
    (loads+=(Load))* (class_kinds+=Class)*;

Load: 
    "load" class=[Class:ID] "from" link=STRING;

interface Class{
    kind: ('justification' | 'pattern' | 'composition')
    name: string
    implemented?: @Class
}

Class returns Class:
    kind=('justification' | 'pattern' | 'composition') (name=ID | name=ID "implements" implemented=[Class:ID]) (JustificationPattern | Composition);

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
    left=[Class:ID] 'with' right=[Class:ID];


hidden terminal WS: /\s+/;
terminal ID: /[_a-zA-Z][\w_]*/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;
hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;