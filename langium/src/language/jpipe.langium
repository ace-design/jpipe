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
    '{' declaration=CompositionDeclaration '}';

CompositionDeclaration returns Declaration:
    kind=('justification' | 'pattern') name=ID CompositionBody;

CompositionBody:
    "is" "merge" '(' declaration1=[Declaration:ID] ',' declaration2=[Declaration:ID] ')' '{' (settings+=Setting)*'}';
    
Setting:
    settingName=ID ':' settingValue=STRING;

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