grammar Jpipe

entry Model:
    (loads+=(Load) | links += Link)* (declaration_kinds+=Declaration )*;

Load: 
    "load" link=[Link];

interface Declaration{
    kind: ('justification' | 'pattern' | 'composition')
    name: string
    implementation?: Implementation
}

Link:
    name=STRING;

Declaration returns Declaration:
    kind=('justification' | 'pattern' | 'composition') (name=ID | name=ID implementation=Implementation) (JustificationPattern | Composition);

Implementation:
    "implements" implemented=[Declaration:ID];

JustificationPattern:
    '{' (variables+=Variable | instructions+=Instruction | supports += Support)* '}';

Composition:
    '{' compositionKind=CompositionKind "is" compositionFunction=CompositionFunction compositionBody=CompositionBody '}';

CompositionKind: 
    "justification" name=ID;

CompositionFunction:
    'merge' '(' j1=[Declaration:ID] "," j2=[Declaration:ID] ')';

CompositionBody:
    '{' (settings+=Setting)* '}';

Setting:
    kind=STRING':' setting=STRING;


Variable: 
    kind=('evidence' | 'strategy' | 'sub-conclusion' | 'conclusion' | '@support') name=ID Instruction;

Instruction:
    'is' information=STRING;

Support:
    left=[Variable:ID] 'supports' right=[Variable:ID];


hidden terminal WS: /\s+/;
terminal ID: /[_a-zA-Z][\w_]*/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;
hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;