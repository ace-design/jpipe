grammar Jpipe

entry Model:
    (entries += (Justification | Pattern | Composition))*;

type Variable = JustificationVariable | PatternVariable;

type Instruction = JustificationInstruction | PatternInstruction;

type Class = JustificationClass | PatternClass;

Justification:
    (JustificationClass | JustificationImplementsClass) body=JustificationBody;

JustificationClass:
    kind=('justification') name=ID;

JustificationImplementsClass:
    kind=('justification') name=ID 'implements' class=[PatternClass];

JustificationBody:
    '{' (variables+=JustificationVariable | instructions+=JustificationInstruction | supports += JustificationSupport)* '}';

JustificationVariable: 
    kind=('evidence' | 'strategy' | 'sub-conclusion' | 'conclusion') name=ID;

JustificationInstruction:
    JustificationVariable 'is' information=STRING;

JustificationSupport:
    left=[JustificationVariable:ID] 'supports' right=[JustificationVariable:ID];


Pattern:
    PatternClass body=PatternBody;

PatternClass:
    kind=('pattern') name=ID;

PatternBody:
    '{' (variables+=PatternVariable | instructions += PatternInstruction | supports += PatternSupport)* '}';

PatternVariable: 
    kind=('evidence' | 'strategy' | 'sub-conclusion' | 'conclusion' | '@support') name=ID;

PatternInstruction:
    PatternVariable 'is' information=STRING;

PatternSupport:
    left=[PatternVariable:ID] 'supports' right=[PatternVariable:ID];



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
    left=[JustificationClass] 'with' right=[JustificationClass];
hidden terminal WS: /\s+/;
terminal ID: /[_a-zA-Z][\w_]*/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;
hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;
