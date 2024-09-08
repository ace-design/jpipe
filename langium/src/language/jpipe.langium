grammar Jpipe

entry Model:
    (loads+=(Load))* (declaration_kinds+=Declaration )*;

Load: 
    "load" name=STRING;

interface Declaration{
    kind: ('justification' | 'pattern' | 'composition')
    name: string
    implementation?: Implementation
}

Declaration returns Declaration:
    kind=('justification' | 'pattern' | 'composition') (name=ID | name=ID implementation=Implementation) (JustificationPattern | Composition);

Implementation:
    "implements" implemented=[Declaration:ID];

JustificationPattern:
    '{' (variables+=Variable | instructions+=Instruction | supports += Support)* '}';

Composition:
    '{' (variables+=CompositionVariable | instructions+=CompositionInstruction)* '}';

Variable: 
    kind=('evidence' | 'strategy' | 'sub-conclusion' | 'conclusion' | '@support') name=ID Instruction;

Instruction:
    'is' information=STRING;

Support:
    left=[Variable:ID] 'supports' right=[Variable:ID];

CompositionVariable:
    kind='justification' name=ID;

CompositionInstruction:
    CompositionVariable 'is' information=CompositionInformation;

CompositionInformation:
    left=[Declaration:ID] 'with' right=[Declaration:ID];


hidden terminal WS: /\s+/;
terminal ID: /[_a-zA-Z][\w_]*/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;
hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;