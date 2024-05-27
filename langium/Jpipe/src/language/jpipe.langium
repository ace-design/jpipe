grammar Jpipe

entry Model:
    (entries+=Body)*;

Body:
    class=Class '{' ( declarations+=DeclarativeStatement | supports += SupportingStatement )* '}';

Class: 
    type=('justification') name=ID;

DeclarativeStatement:
    type=(EV | 'strategy' | 'sub-conclusion' | 'conclusion') name=ID 'is' information=STRING;

SupportingStatement:
    supporter=[DeclarativeStatement] operator='supports' supportee=[DeclarativeStatement]; 

hidden terminal WS: /\s+/;
terminal EV returns string: /evidence/;
terminal ID: /[_a-zA-Z][\w_]*/;
terminal INT returns number: /[0-9]+/;
terminal STRING: /"(\\.|[^"\\])*"|'(\\.|[^'\\])*'/;
hidden terminal ML_COMMENT: /\/\*[\s\S]*?\*\//;
hidden terminal SL_COMMENT: /\/\/[^\n\r]*/;
