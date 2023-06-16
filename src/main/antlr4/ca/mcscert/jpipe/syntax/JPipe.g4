grammar JPipe;

/** Parser rules **/
unit:
    element+
    EOF;

element:
    justification | justif_fragment | composition;

justification:
    JUSTIF id=ID '{'
        (evidence | sub_conclusion | relation)*
    '}';

justif_fragment:
    FRAGMENT id=ID '{' '}';

composition:
    COMPOSITION '{' '}';

relation:
    from=ID '->' to=ID;

evidence:
    EVIDENCE id=ID IS name=STRING '{' '}';

sub_conclusion:
    SUBCONCLUSION id=ID IS name=STRING;

/** Lexer rules **/

WHITESPACE  : [ \t]+               -> channel(HIDDEN);
NEWLINE     : ('\r'? '\n' | '\r')+ -> channel(HIDDEN) ;

COMMENT     : '/*' .*? '*/'    -> skip;
LINE_COMMENT: '//'STRING_CHAR* -> skip;

// Keywords
IS           : 'is';
JUSTIF       : 'justification';
FRAGMENT     : 'fragment';
COMPOSITION  : 'composition';
EVIDENCE     : 'evidence';
SUBCONCLUSION: 'sub-conclusion';

// Symbols
ID          : ('A'..'Z' | 'a'..'z' | '0'..'9')+;
STRING      : '"' STRING_CHAR* '"';

fragment STRING_CHAR : ~('\r' | '\n');