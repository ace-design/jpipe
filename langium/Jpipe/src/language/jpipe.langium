grammar Jpipe

entry Model:
    (class += Class '{' statements+=Statement* '}')*;

Class: types+=ClassType name=ID;

ClassType: 
    type=('justification' | 'implementation');

DeclarationType: Evidence|Strategy|Conclusion|SubConclusion;


Statement:
    statementType+=(DeclarativeStatement | SupportingStatement)*;

DeclarativeStatement:
    DeclarationType 'is' information=STRING;

SupportingStatement:
    supporter=[DeclarationType:ID] 'supports' supportee=[DeclarationType:ID]; 

    
Evidence:
    'evidence' name=ID;

Strategy:
    'strategy' name=ID;
    
Conclusion:
    'conclusion' name=ID;

SubConclusion:
    'sub-conclusion' name=ID;



hidden terminal WS: /\s+/;

terminal ID: /[_a-zA-Z][\w_]*/;
terminal INT returns number: /[0-9]+/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;

hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;
