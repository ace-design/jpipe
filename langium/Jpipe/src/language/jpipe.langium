grammar Jpipe

entry Model:
    (class=Class '{' (  declarations+=DeclarativeStatement | supports += SupportingStatement )* '}')*;

Class: 
    type=('justification' | 'implementation') name=ID;

DeclarativeStatement:
    type=('evidence' | 'strategy' | 'sub-conclusion' | 'conclusion') name=ID 'is' information=STRING;

SupportingStatement:
    supporter=[DeclarativeStatement] 'supports' supportee=[DeclarativeStatement]; 


hidden terminal WS: /\s+/;

terminal ID: /[_a-zA-Z][\w_]*/;
terminal INT returns number: /[0-9]+/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;

hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;
