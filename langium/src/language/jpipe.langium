grammar Jpipe

entry Model:
    (entries+=Justification)*;

interface Class{
    kind: 'justification' | 'pattern' | 'composition'
    name: string
}

interface Variable{
    kind: 'evidence' | 'strategy' | 'sub-conclusion' | 'conclusion'
    name: string
}

interface Instruction{
    information: string
}

interface Support{
    left: @Variable
    right: @Variable
}

Justification:
    JustificationClass body=JustificationBody;

JustificationClass returns Class:
    kind=('justification') name=ID;

JustificationBody:
    '{' (instructions+=JustificationInstruction | supports += JustificationSupport)* '}';

JustificationVariable returns Variable: 
    kind=('evidence' | 'strategy' | 'sub-conclusion' | 'conclusion') name=ID;

JustificationInstruction returns Instruction:
    JustificationVariable 'is' information=STRING;

JustificationSupport returns Support:
    left=[Variable:ID] 'supports' right=[Variable:ID];


hidden terminal WS: /\s+/;
terminal ID: /[_a-zA-Z][\w_]*/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;
hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;
