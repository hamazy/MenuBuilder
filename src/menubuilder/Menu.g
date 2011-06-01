grammar Menu;

options {
  language = Java;
  output=AST;
  ASTLabelType = MfTree;
}

tokens {
  ROOT; MENU; MENU_ITEM; SUBMENU; MARK;
}

@header {
  package menubuilder;
}

@lexer::header {
  package menubuilder;
}

@members {
  MenuBuilderLoader helper;
}

rule: menu+ submenu* -> ^(ROOT menu+ submenu*);

menu
	: 'menu' STRING menuItem+ 'end' -> ^(MENU STRING menuItem+);
	
menuItem 
	: plainMenuItem | separator;

plainMenuItem
	: mark? STRING -> ^(MENU_ITEM STRING mark?) ;

mark
  : ('o' -> ^('o')
  | 'x' -> ^('x')
  | 'v' -> ^('v')
  | '_' -> ^('_')
  | '>'-> ^('>')
  )  ;

separator
	: '---' -> ^(MENU_ITEM) ;

submenu 
	: 'submenu' name=STRING menuItem+ 'end' -> ^(SUBMENU STRING menuItem+);

fragment LETTER : ('a'..'z' | 'A'..'Z' | '_');

fragment DIGIT : ('0'..'9');

fragment HEX_DIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

fragment ESC_SEQ
    :   '\\' ('b'|'t'|'n'|'f'|'r'|'\"'|'\''|'\\')
    |   UNICODE_ESC
    |   OCTAL_ESC
    ;

fragment OCTAL_ESC
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment UNICODE_ESC
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;
    

ID : LETTER (LETTER | DIGIT)* ;

STRING
    :  '"' ( ESC_SEQ | ~('\\'|'"') )* '"'
    ;

WHITE_SPACE : (' ' | '\t' | '\r' | '\n')+ {skip();} ;

COMMENT : '#' ~'\n'* '\n' {skip();} ;

ILLEGAL : .;
