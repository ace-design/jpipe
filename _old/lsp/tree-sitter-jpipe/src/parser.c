#include "tree_sitter/parser.h"

#if defined(__GNUC__) || defined(__clang__)
#pragma GCC diagnostic ignored "-Wmissing-field-initializers"
#endif

#define LANGUAGE_VERSION 14
#define STATE_COUNT 94
#define LARGE_STATE_COUNT 2
#define SYMBOL_COUNT 63
#define ALIAS_COUNT 0
#define TOKEN_COUNT 33
#define EXTERNAL_TOKEN_COUNT 0
#define FIELD_COUNT 8
#define MAX_ALIAS_SEQUENCE_LENGTH 6
#define PRODUCTION_ID_COUNT 15

enum ts_symbol_identifiers {
  anon_sym_justification = 1,
  anon_sym_implements = 2,
  anon_sym_LBRACE = 3,
  anon_sym_RBRACE = 4,
  anon_sym_pattern = 5,
  anon_sym_load = 6,
  anon_sym_implementation = 7,
  anon_sym_of = 8,
  anon_sym_is = 9,
  anon_sym_evidence = 10,
  anon_sym_strategy = 11,
  anon_sym_sub_DASHconclusion = 12,
  anon_sym_conclusion = 13,
  anon_sym_ATsupport = 14,
  anon_sym_probe = 15,
  anon_sym_operation = 16,
  anon_sym_expectation = 17,
  anon_sym_supports = 18,
  anon_sym_LPAREN = 19,
  anon_sym_COMMA = 20,
  anon_sym_RPAREN = 21,
  anon_sym_not = 22,
  sym_ID = 23,
  sym_STRING = 24,
  sym_INTEGER = 25,
  anon_sym_or = 26,
  anon_sym_and = 27,
  anon_sym_EQ_EQ = 28,
  anon_sym_GT = 29,
  anon_sym_LT = 30,
  anon_sym_LT_EQ = 31,
  anon_sym_GT_EQ = 32,
  sym_source_file = 33,
  sym_justification = 34,
  sym_justif_body = 35,
  sym_pattern = 36,
  sym_pattern_body = 37,
  sym_load = 38,
  sym_implementation = 39,
  sym_impl_body = 40,
  sym_identified_element = 41,
  sym_evidence = 42,
  sym_strategy = 43,
  sym_sub_conclusion = 44,
  sym_conclusion = 45,
  sym_abs_support = 46,
  sym_probe = 47,
  sym_operation = 48,
  sym_expectation = 49,
  sym_relation = 50,
  sym_command = 51,
  sym_command_args = 52,
  sym_expression = 53,
  sym_boolean_expr = 54,
  sym_BOOL_OP = 55,
  sym_ARITH_OP = 56,
  aux_sym_source_file_repeat1 = 57,
  aux_sym_justif_body_repeat1 = 58,
  aux_sym_pattern_body_repeat1 = 59,
  aux_sym_impl_body_repeat1 = 60,
  aux_sym_command_args_repeat1 = 61,
  aux_sym_expression_repeat1 = 62,
};

static const char * const ts_symbol_names[] = {
  [ts_builtin_sym_end] = "end",
  [anon_sym_justification] = "justification",
  [anon_sym_implements] = "implements",
  [anon_sym_LBRACE] = "{",
  [anon_sym_RBRACE] = "}",
  [anon_sym_pattern] = "pattern",
  [anon_sym_load] = "load",
  [anon_sym_implementation] = "implementation",
  [anon_sym_of] = "of",
  [anon_sym_is] = "is",
  [anon_sym_evidence] = "evidence",
  [anon_sym_strategy] = "strategy",
  [anon_sym_sub_DASHconclusion] = "sub-conclusion",
  [anon_sym_conclusion] = "conclusion",
  [anon_sym_ATsupport] = "@support",
  [anon_sym_probe] = "probe",
  [anon_sym_operation] = "operation",
  [anon_sym_expectation] = "expectation",
  [anon_sym_supports] = "supports",
  [anon_sym_LPAREN] = "(",
  [anon_sym_COMMA] = ",",
  [anon_sym_RPAREN] = ")",
  [anon_sym_not] = "not",
  [sym_ID] = "ID",
  [sym_STRING] = "STRING",
  [sym_INTEGER] = "INTEGER",
  [anon_sym_or] = "or",
  [anon_sym_and] = "and",
  [anon_sym_EQ_EQ] = "==",
  [anon_sym_GT] = ">",
  [anon_sym_LT] = "<",
  [anon_sym_LT_EQ] = "<=",
  [anon_sym_GT_EQ] = ">=",
  [sym_source_file] = "source_file",
  [sym_justification] = "justification",
  [sym_justif_body] = "justif_body",
  [sym_pattern] = "pattern",
  [sym_pattern_body] = "pattern_body",
  [sym_load] = "load",
  [sym_implementation] = "implementation",
  [sym_impl_body] = "impl_body",
  [sym_identified_element] = "identified_element",
  [sym_evidence] = "evidence",
  [sym_strategy] = "strategy",
  [sym_sub_conclusion] = "sub_conclusion",
  [sym_conclusion] = "conclusion",
  [sym_abs_support] = "abs_support",
  [sym_probe] = "probe",
  [sym_operation] = "operation",
  [sym_expectation] = "expectation",
  [sym_relation] = "relation",
  [sym_command] = "command",
  [sym_command_args] = "command_args",
  [sym_expression] = "expression",
  [sym_boolean_expr] = "boolean_expr",
  [sym_BOOL_OP] = "BOOL_OP",
  [sym_ARITH_OP] = "ARITH_OP",
  [aux_sym_source_file_repeat1] = "source_file_repeat1",
  [aux_sym_justif_body_repeat1] = "justif_body_repeat1",
  [aux_sym_pattern_body_repeat1] = "pattern_body_repeat1",
  [aux_sym_impl_body_repeat1] = "impl_body_repeat1",
  [aux_sym_command_args_repeat1] = "command_args_repeat1",
  [aux_sym_expression_repeat1] = "expression_repeat1",
};

static const TSSymbol ts_symbol_map[] = {
  [ts_builtin_sym_end] = ts_builtin_sym_end,
  [anon_sym_justification] = anon_sym_justification,
  [anon_sym_implements] = anon_sym_implements,
  [anon_sym_LBRACE] = anon_sym_LBRACE,
  [anon_sym_RBRACE] = anon_sym_RBRACE,
  [anon_sym_pattern] = anon_sym_pattern,
  [anon_sym_load] = anon_sym_load,
  [anon_sym_implementation] = anon_sym_implementation,
  [anon_sym_of] = anon_sym_of,
  [anon_sym_is] = anon_sym_is,
  [anon_sym_evidence] = anon_sym_evidence,
  [anon_sym_strategy] = anon_sym_strategy,
  [anon_sym_sub_DASHconclusion] = anon_sym_sub_DASHconclusion,
  [anon_sym_conclusion] = anon_sym_conclusion,
  [anon_sym_ATsupport] = anon_sym_ATsupport,
  [anon_sym_probe] = anon_sym_probe,
  [anon_sym_operation] = anon_sym_operation,
  [anon_sym_expectation] = anon_sym_expectation,
  [anon_sym_supports] = anon_sym_supports,
  [anon_sym_LPAREN] = anon_sym_LPAREN,
  [anon_sym_COMMA] = anon_sym_COMMA,
  [anon_sym_RPAREN] = anon_sym_RPAREN,
  [anon_sym_not] = anon_sym_not,
  [sym_ID] = sym_ID,
  [sym_STRING] = sym_STRING,
  [sym_INTEGER] = sym_INTEGER,
  [anon_sym_or] = anon_sym_or,
  [anon_sym_and] = anon_sym_and,
  [anon_sym_EQ_EQ] = anon_sym_EQ_EQ,
  [anon_sym_GT] = anon_sym_GT,
  [anon_sym_LT] = anon_sym_LT,
  [anon_sym_LT_EQ] = anon_sym_LT_EQ,
  [anon_sym_GT_EQ] = anon_sym_GT_EQ,
  [sym_source_file] = sym_source_file,
  [sym_justification] = sym_justification,
  [sym_justif_body] = sym_justif_body,
  [sym_pattern] = sym_pattern,
  [sym_pattern_body] = sym_pattern_body,
  [sym_load] = sym_load,
  [sym_implementation] = sym_implementation,
  [sym_impl_body] = sym_impl_body,
  [sym_identified_element] = sym_identified_element,
  [sym_evidence] = sym_evidence,
  [sym_strategy] = sym_strategy,
  [sym_sub_conclusion] = sym_sub_conclusion,
  [sym_conclusion] = sym_conclusion,
  [sym_abs_support] = sym_abs_support,
  [sym_probe] = sym_probe,
  [sym_operation] = sym_operation,
  [sym_expectation] = sym_expectation,
  [sym_relation] = sym_relation,
  [sym_command] = sym_command,
  [sym_command_args] = sym_command_args,
  [sym_expression] = sym_expression,
  [sym_boolean_expr] = sym_boolean_expr,
  [sym_BOOL_OP] = sym_BOOL_OP,
  [sym_ARITH_OP] = sym_ARITH_OP,
  [aux_sym_source_file_repeat1] = aux_sym_source_file_repeat1,
  [aux_sym_justif_body_repeat1] = aux_sym_justif_body_repeat1,
  [aux_sym_pattern_body_repeat1] = aux_sym_pattern_body_repeat1,
  [aux_sym_impl_body_repeat1] = aux_sym_impl_body_repeat1,
  [aux_sym_command_args_repeat1] = aux_sym_command_args_repeat1,
  [aux_sym_expression_repeat1] = aux_sym_expression_repeat1,
};

static const TSSymbolMetadata ts_symbol_metadata[] = {
  [ts_builtin_sym_end] = {
    .visible = false,
    .named = true,
  },
  [anon_sym_justification] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_implements] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_LBRACE] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_RBRACE] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_pattern] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_load] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_implementation] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_of] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_is] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_evidence] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_strategy] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_sub_DASHconclusion] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_conclusion] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_ATsupport] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_probe] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_operation] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_expectation] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_supports] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_LPAREN] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_COMMA] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_RPAREN] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_not] = {
    .visible = true,
    .named = false,
  },
  [sym_ID] = {
    .visible = true,
    .named = true,
  },
  [sym_STRING] = {
    .visible = true,
    .named = true,
  },
  [sym_INTEGER] = {
    .visible = true,
    .named = true,
  },
  [anon_sym_or] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_and] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_EQ_EQ] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_GT] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_LT] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_LT_EQ] = {
    .visible = true,
    .named = false,
  },
  [anon_sym_GT_EQ] = {
    .visible = true,
    .named = false,
  },
  [sym_source_file] = {
    .visible = true,
    .named = true,
  },
  [sym_justification] = {
    .visible = true,
    .named = true,
  },
  [sym_justif_body] = {
    .visible = true,
    .named = true,
  },
  [sym_pattern] = {
    .visible = true,
    .named = true,
  },
  [sym_pattern_body] = {
    .visible = true,
    .named = true,
  },
  [sym_load] = {
    .visible = true,
    .named = true,
  },
  [sym_implementation] = {
    .visible = true,
    .named = true,
  },
  [sym_impl_body] = {
    .visible = true,
    .named = true,
  },
  [sym_identified_element] = {
    .visible = true,
    .named = true,
  },
  [sym_evidence] = {
    .visible = true,
    .named = true,
  },
  [sym_strategy] = {
    .visible = true,
    .named = true,
  },
  [sym_sub_conclusion] = {
    .visible = true,
    .named = true,
  },
  [sym_conclusion] = {
    .visible = true,
    .named = true,
  },
  [sym_abs_support] = {
    .visible = true,
    .named = true,
  },
  [sym_probe] = {
    .visible = true,
    .named = true,
  },
  [sym_operation] = {
    .visible = true,
    .named = true,
  },
  [sym_expectation] = {
    .visible = true,
    .named = true,
  },
  [sym_relation] = {
    .visible = true,
    .named = true,
  },
  [sym_command] = {
    .visible = true,
    .named = true,
  },
  [sym_command_args] = {
    .visible = true,
    .named = true,
  },
  [sym_expression] = {
    .visible = true,
    .named = true,
  },
  [sym_boolean_expr] = {
    .visible = true,
    .named = true,
  },
  [sym_BOOL_OP] = {
    .visible = true,
    .named = true,
  },
  [sym_ARITH_OP] = {
    .visible = true,
    .named = true,
  },
  [aux_sym_source_file_repeat1] = {
    .visible = false,
    .named = false,
  },
  [aux_sym_justif_body_repeat1] = {
    .visible = false,
    .named = false,
  },
  [aux_sym_pattern_body_repeat1] = {
    .visible = false,
    .named = false,
  },
  [aux_sym_impl_body_repeat1] = {
    .visible = false,
    .named = false,
  },
  [aux_sym_command_args_repeat1] = {
    .visible = false,
    .named = false,
  },
  [aux_sym_expression_repeat1] = {
    .visible = false,
    .named = false,
  },
};

enum ts_field_identifiers {
  field_file = 1,
  field_from = 2,
  field_id = 3,
  field_justification_id = 4,
  field_name = 5,
  field_op = 6,
  field_parent = 7,
  field_to = 8,
};

static const char * const ts_field_names[] = {
  [0] = NULL,
  [field_file] = "file",
  [field_from] = "from",
  [field_id] = "id",
  [field_justification_id] = "justification_id",
  [field_name] = "name",
  [field_op] = "op",
  [field_parent] = "parent",
  [field_to] = "to",
};

static const TSFieldMapSlice ts_field_map_slices[PRODUCTION_ID_COUNT] = {
  [1] = {.index = 0, .length = 1},
  [2] = {.index = 1, .length = 1},
  [3] = {.index = 2, .length = 2},
  [4] = {.index = 4, .length = 2},
  [5] = {.index = 6, .length = 2},
  [6] = {.index = 8, .length = 2},
  [7] = {.index = 10, .length = 1},
  [8] = {.index = 11, .length = 2},
  [9] = {.index = 13, .length = 1},
  [10] = {.index = 14, .length = 1},
  [11] = {.index = 15, .length = 2},
  [12] = {.index = 17, .length = 1},
  [13] = {.index = 18, .length = 2},
  [14] = {.index = 20, .length = 2},
};

static const TSFieldMapEntry ts_field_map_entries[] = {
  [0] =
    {field_file, 1},
  [1] =
    {field_id, 1},
  [2] =
    {field_id, 1},
    {field_parent, 3},
  [4] =
    {field_id, 1},
    {field_justification_id, 3},
  [6] =
    {field_from, 0},
    {field_to, 2},
  [8] =
    {field_id, 0},
    {field_name, 2},
  [10] =
    {field_id, 1, .inherited = true},
  [11] =
    {field_id, 0, .inherited = true},
    {field_id, 1, .inherited = true},
  [13] =
    {field_id, 0},
  [14] =
    {field_op, 1, .inherited = true},
  [15] =
    {field_id, 0},
    {field_op, 1},
  [17] =
    {field_op, 0},
  [18] =
    {field_op, 0, .inherited = true},
    {field_op, 1, .inherited = true},
  [20] =
    {field_id, 1},
    {field_op, 2},
};

static const TSSymbol ts_alias_sequences[PRODUCTION_ID_COUNT][MAX_ALIAS_SEQUENCE_LENGTH] = {
  [0] = {0},
};

static const uint16_t ts_non_terminal_alias_map[] = {
  0,
};

static const TSStateId ts_primary_state_ids[STATE_COUNT] = {
  [0] = 0,
  [1] = 1,
  [2] = 2,
  [3] = 3,
  [4] = 4,
  [5] = 5,
  [6] = 6,
  [7] = 7,
  [8] = 8,
  [9] = 9,
  [10] = 10,
  [11] = 11,
  [12] = 12,
  [13] = 13,
  [14] = 14,
  [15] = 15,
  [16] = 16,
  [17] = 17,
  [18] = 18,
  [19] = 19,
  [20] = 20,
  [21] = 21,
  [22] = 22,
  [23] = 23,
  [24] = 24,
  [25] = 25,
  [26] = 23,
  [27] = 27,
  [28] = 28,
  [29] = 29,
  [30] = 30,
  [31] = 31,
  [32] = 32,
  [33] = 33,
  [34] = 34,
  [35] = 27,
  [36] = 36,
  [37] = 37,
  [38] = 38,
  [39] = 39,
  [40] = 40,
  [41] = 41,
  [42] = 42,
  [43] = 43,
  [44] = 44,
  [45] = 45,
  [46] = 46,
  [47] = 47,
  [48] = 48,
  [49] = 49,
  [50] = 50,
  [51] = 51,
  [52] = 52,
  [53] = 53,
  [54] = 54,
  [55] = 55,
  [56] = 56,
  [57] = 57,
  [58] = 58,
  [59] = 59,
  [60] = 60,
  [61] = 61,
  [62] = 62,
  [63] = 63,
  [64] = 64,
  [65] = 65,
  [66] = 66,
  [67] = 67,
  [68] = 68,
  [69] = 69,
  [70] = 70,
  [71] = 71,
  [72] = 72,
  [73] = 73,
  [74] = 74,
  [75] = 75,
  [76] = 76,
  [77] = 77,
  [78] = 78,
  [79] = 79,
  [80] = 80,
  [81] = 81,
  [82] = 82,
  [83] = 83,
  [84] = 84,
  [85] = 85,
  [86] = 86,
  [87] = 87,
  [88] = 88,
  [89] = 89,
  [90] = 90,
  [91] = 91,
  [92] = 92,
  [93] = 93,
};

static bool ts_lex(TSLexer *lexer, TSStateId state) {
  START_LEXER();
  eof = lexer->eof(lexer);
  switch (state) {
    case 0:
      if (eof) ADVANCE(112);
      if (lookahead == '(') ADVANCE(135);
      if (lookahead == ')') ADVANCE(137);
      if (lookahead == ',') ADVANCE(136);
      if (lookahead == '<') ADVANCE(184);
      if (lookahead == '=') ADVANCE(2);
      if (lookahead == '>') ADVANCE(183);
      if (lookahead == '@') ADVANCE(85);
      if (lookahead == 'a') ADVANCE(48);
      if (lookahead == 'c') ADVANCE(64);
      if (lookahead == 'e') ADVANCE(107);
      if (lookahead == 'i') ADVANCE(46);
      if (lookahead == 'j') ADVANCE(103);
      if (lookahead == 'l') ADVANCE(61);
      if (lookahead == 'n') ADVANCE(62);
      if (lookahead == 'o') ADVANCE(31);
      if (lookahead == 'p') ADVANCE(4);
      if (lookahead == 's') ADVANCE(89);
      if (lookahead == '{') ADVANCE(115);
      if (lookahead == '}') ADVANCE(116);
      if (lookahead == '"' ||
          lookahead == '\'') ADVANCE(111);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') SKIP(0)
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(179);
      END_STATE();
    case 1:
      if (lookahead == '-') ADVANCE(18);
      END_STATE();
    case 2:
      if (lookahead == '=') ADVANCE(182);
      END_STATE();
    case 3:
      if (lookahead == '@') ADVANCE(85);
      if (lookahead == 'c') ADVANCE(162);
      if (lookahead == 'e') ADVANCE(175);
      if (lookahead == 's') ADVANCE(170);
      if (lookahead == '}') ADVANCE(116);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') SKIP(3)
      if (lookahead == '-' ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 4:
      if (lookahead == 'a') ADVANCE(96);
      if (lookahead == 'r') ADVANCE(63);
      END_STATE();
    case 5:
      if (lookahead == 'a') ADVANCE(20);
      END_STATE();
    case 6:
      if (lookahead == 'a') ADVANCE(97);
      END_STATE();
    case 7:
      if (lookahead == 'a') ADVANCE(95);
      END_STATE();
    case 8:
      if (lookahead == 'a') ADVANCE(100);
      END_STATE();
    case 9:
      if (lookahead == 'a') ADVANCE(101);
      END_STATE();
    case 10:
      if (lookahead == 'a') ADVANCE(102);
      if (lookahead == 's') ADVANCE(114);
      END_STATE();
    case 11:
      if (lookahead == 'b') ADVANCE(1);
      if (lookahead == 'p') ADVANCE(76);
      END_STATE();
    case 12:
      if (lookahead == 'b') ADVANCE(23);
      END_STATE();
    case 13:
      if (lookahead == 'c') ADVANCE(44);
      END_STATE();
    case 14:
      if (lookahead == 'c') ADVANCE(99);
      END_STATE();
    case 15:
      if (lookahead == 'c') ADVANCE(25);
      END_STATE();
    case 16:
      if (lookahead == 'c') ADVANCE(9);
      END_STATE();
    case 17:
      if (lookahead == 'c') ADVANCE(45);
      END_STATE();
    case 18:
      if (lookahead == 'c') ADVANCE(73);
      END_STATE();
    case 19:
      if (lookahead == 'd') ADVANCE(181);
      END_STATE();
    case 20:
      if (lookahead == 'd') ADVANCE(118);
      END_STATE();
    case 21:
      if (lookahead == 'd') ADVANCE(28);
      END_STATE();
    case 22:
      if (lookahead == 'e') ADVANCE(47);
      END_STATE();
    case 23:
      if (lookahead == 'e') ADVANCE(131);
      END_STATE();
    case 24:
      if (lookahead == 'e') ADVANCE(33);
      END_STATE();
    case 25:
      if (lookahead == 'e') ADVANCE(122);
      END_STATE();
    case 26:
      if (lookahead == 'e') ADVANCE(80);
      END_STATE();
    case 27:
      if (lookahead == 'e') ADVANCE(14);
      END_STATE();
    case 28:
      if (lookahead == 'e') ADVANCE(58);
      END_STATE();
    case 29:
      if (lookahead == 'e') ADVANCE(59);
      END_STATE();
    case 30:
      if (lookahead == 'e') ADVANCE(83);
      END_STATE();
    case 31:
      if (lookahead == 'f') ADVANCE(120);
      if (lookahead == 'p') ADVANCE(30);
      if (lookahead == 'r') ADVANCE(180);
      END_STATE();
    case 32:
      if (lookahead == 'f') ADVANCE(36);
      END_STATE();
    case 33:
      if (lookahead == 'g') ADVANCE(108);
      END_STATE();
    case 34:
      if (lookahead == 'i') ADVANCE(32);
      END_STATE();
    case 35:
      if (lookahead == 'i') ADVANCE(21);
      END_STATE();
    case 36:
      if (lookahead == 'i') ADVANCE(16);
      END_STATE();
    case 37:
      if (lookahead == 'i') ADVANCE(66);
      END_STATE();
    case 38:
      if (lookahead == 'i') ADVANCE(67);
      END_STATE();
    case 39:
      if (lookahead == 'i') ADVANCE(68);
      END_STATE();
    case 40:
      if (lookahead == 'i') ADVANCE(69);
      END_STATE();
    case 41:
      if (lookahead == 'i') ADVANCE(70);
      END_STATE();
    case 42:
      if (lookahead == 'i') ADVANCE(71);
      END_STATE();
    case 43:
      if (lookahead == 'l') ADVANCE(22);
      END_STATE();
    case 44:
      if (lookahead == 'l') ADVANCE(104);
      END_STATE();
    case 45:
      if (lookahead == 'l') ADVANCE(106);
      END_STATE();
    case 46:
      if (lookahead == 'm') ADVANCE(74);
      if (lookahead == 's') ADVANCE(121);
      END_STATE();
    case 47:
      if (lookahead == 'm') ADVANCE(29);
      END_STATE();
    case 48:
      if (lookahead == 'n') ADVANCE(19);
      END_STATE();
    case 49:
      if (lookahead == 'n') ADVANCE(13);
      END_STATE();
    case 50:
      if (lookahead == 'n') ADVANCE(117);
      END_STATE();
    case 51:
      if (lookahead == 'n') ADVANCE(132);
      END_STATE();
    case 52:
      if (lookahead == 'n') ADVANCE(128);
      END_STATE();
    case 53:
      if (lookahead == 'n') ADVANCE(133);
      END_STATE();
    case 54:
      if (lookahead == 'n') ADVANCE(113);
      END_STATE();
    case 55:
      if (lookahead == 'n') ADVANCE(119);
      END_STATE();
    case 56:
      if (lookahead == 'n') ADVANCE(126);
      END_STATE();
    case 57:
      if (lookahead == 'n') ADVANCE(164);
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') SKIP(57)
      if (lookahead == '-' ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 58:
      if (lookahead == 'n') ADVANCE(15);
      END_STATE();
    case 59:
      if (lookahead == 'n') ADVANCE(92);
      END_STATE();
    case 60:
      if (lookahead == 'n') ADVANCE(17);
      END_STATE();
    case 61:
      if (lookahead == 'o') ADVANCE(5);
      END_STATE();
    case 62:
      if (lookahead == 'o') ADVANCE(90);
      END_STATE();
    case 63:
      if (lookahead == 'o') ADVANCE(12);
      END_STATE();
    case 64:
      if (lookahead == 'o') ADVANCE(49);
      END_STATE();
    case 65:
      if (lookahead == 'o') ADVANCE(81);
      END_STATE();
    case 66:
      if (lookahead == 'o') ADVANCE(51);
      END_STATE();
    case 67:
      if (lookahead == 'o') ADVANCE(52);
      END_STATE();
    case 68:
      if (lookahead == 'o') ADVANCE(53);
      END_STATE();
    case 69:
      if (lookahead == 'o') ADVANCE(54);
      END_STATE();
    case 70:
      if (lookahead == 'o') ADVANCE(55);
      END_STATE();
    case 71:
      if (lookahead == 'o') ADVANCE(56);
      END_STATE();
    case 72:
      if (lookahead == 'o') ADVANCE(82);
      END_STATE();
    case 73:
      if (lookahead == 'o') ADVANCE(60);
      END_STATE();
    case 74:
      if (lookahead == 'p') ADVANCE(43);
      END_STATE();
    case 75:
      if (lookahead == 'p') ADVANCE(27);
      END_STATE();
    case 76:
      if (lookahead == 'p') ADVANCE(65);
      END_STATE();
    case 77:
      if (lookahead == 'p') ADVANCE(72);
      END_STATE();
    case 78:
      if (lookahead == 'p') ADVANCE(77);
      END_STATE();
    case 79:
      if (lookahead == 'r') ADVANCE(6);
      END_STATE();
    case 80:
      if (lookahead == 'r') ADVANCE(50);
      END_STATE();
    case 81:
      if (lookahead == 'r') ADVANCE(94);
      END_STATE();
    case 82:
      if (lookahead == 'r') ADVANCE(91);
      END_STATE();
    case 83:
      if (lookahead == 'r') ADVANCE(7);
      END_STATE();
    case 84:
      if (lookahead == 's') ADVANCE(134);
      END_STATE();
    case 85:
      if (lookahead == 's') ADVANCE(105);
      END_STATE();
    case 86:
      if (lookahead == 's') ADVANCE(93);
      END_STATE();
    case 87:
      if (lookahead == 's') ADVANCE(38);
      END_STATE();
    case 88:
      if (lookahead == 's') ADVANCE(42);
      END_STATE();
    case 89:
      if (lookahead == 't') ADVANCE(79);
      if (lookahead == 'u') ADVANCE(11);
      END_STATE();
    case 90:
      if (lookahead == 't') ADVANCE(138);
      END_STATE();
    case 91:
      if (lookahead == 't') ADVANCE(130);
      END_STATE();
    case 92:
      if (lookahead == 't') ADVANCE(10);
      END_STATE();
    case 93:
      if (lookahead == 't') ADVANCE(34);
      END_STATE();
    case 94:
      if (lookahead == 't') ADVANCE(84);
      END_STATE();
    case 95:
      if (lookahead == 't') ADVANCE(37);
      END_STATE();
    case 96:
      if (lookahead == 't') ADVANCE(98);
      END_STATE();
    case 97:
      if (lookahead == 't') ADVANCE(24);
      END_STATE();
    case 98:
      if (lookahead == 't') ADVANCE(26);
      END_STATE();
    case 99:
      if (lookahead == 't') ADVANCE(8);
      END_STATE();
    case 100:
      if (lookahead == 't') ADVANCE(39);
      END_STATE();
    case 101:
      if (lookahead == 't') ADVANCE(40);
      END_STATE();
    case 102:
      if (lookahead == 't') ADVANCE(41);
      END_STATE();
    case 103:
      if (lookahead == 'u') ADVANCE(86);
      END_STATE();
    case 104:
      if (lookahead == 'u') ADVANCE(87);
      END_STATE();
    case 105:
      if (lookahead == 'u') ADVANCE(78);
      END_STATE();
    case 106:
      if (lookahead == 'u') ADVANCE(88);
      END_STATE();
    case 107:
      if (lookahead == 'v') ADVANCE(35);
      if (lookahead == 'x') ADVANCE(75);
      END_STATE();
    case 108:
      if (lookahead == 'y') ADVANCE(124);
      END_STATE();
    case 109:
      if (lookahead == '"' ||
          lookahead == '\'') ADVANCE(178);
      if (lookahead != 0) ADVANCE(109);
      END_STATE();
    case 110:
      if (('\t' <= lookahead && lookahead <= '\r') ||
          lookahead == ' ') SKIP(110)
      if (lookahead == '-' ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 111:
      if (lookahead != 0 &&
          lookahead != '"' &&
          lookahead != '\'') ADVANCE(109);
      END_STATE();
    case 112:
      ACCEPT_TOKEN(ts_builtin_sym_end);
      END_STATE();
    case 113:
      ACCEPT_TOKEN(anon_sym_justification);
      END_STATE();
    case 114:
      ACCEPT_TOKEN(anon_sym_implements);
      END_STATE();
    case 115:
      ACCEPT_TOKEN(anon_sym_LBRACE);
      END_STATE();
    case 116:
      ACCEPT_TOKEN(anon_sym_RBRACE);
      END_STATE();
    case 117:
      ACCEPT_TOKEN(anon_sym_pattern);
      END_STATE();
    case 118:
      ACCEPT_TOKEN(anon_sym_load);
      END_STATE();
    case 119:
      ACCEPT_TOKEN(anon_sym_implementation);
      END_STATE();
    case 120:
      ACCEPT_TOKEN(anon_sym_of);
      END_STATE();
    case 121:
      ACCEPT_TOKEN(anon_sym_is);
      END_STATE();
    case 122:
      ACCEPT_TOKEN(anon_sym_evidence);
      END_STATE();
    case 123:
      ACCEPT_TOKEN(anon_sym_evidence);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 124:
      ACCEPT_TOKEN(anon_sym_strategy);
      END_STATE();
    case 125:
      ACCEPT_TOKEN(anon_sym_strategy);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 126:
      ACCEPT_TOKEN(anon_sym_sub_DASHconclusion);
      END_STATE();
    case 127:
      ACCEPT_TOKEN(anon_sym_sub_DASHconclusion);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 128:
      ACCEPT_TOKEN(anon_sym_conclusion);
      END_STATE();
    case 129:
      ACCEPT_TOKEN(anon_sym_conclusion);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 130:
      ACCEPT_TOKEN(anon_sym_ATsupport);
      END_STATE();
    case 131:
      ACCEPT_TOKEN(anon_sym_probe);
      END_STATE();
    case 132:
      ACCEPT_TOKEN(anon_sym_operation);
      END_STATE();
    case 133:
      ACCEPT_TOKEN(anon_sym_expectation);
      END_STATE();
    case 134:
      ACCEPT_TOKEN(anon_sym_supports);
      END_STATE();
    case 135:
      ACCEPT_TOKEN(anon_sym_LPAREN);
      END_STATE();
    case 136:
      ACCEPT_TOKEN(anon_sym_COMMA);
      END_STATE();
    case 137:
      ACCEPT_TOKEN(anon_sym_RPAREN);
      END_STATE();
    case 138:
      ACCEPT_TOKEN(anon_sym_not);
      END_STATE();
    case 139:
      ACCEPT_TOKEN(anon_sym_not);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 140:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == '-') ADVANCE(146);
      if (('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 141:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'a') ADVANCE(172);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('b' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 142:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'b') ADVANCE(140);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 143:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'c') ADVANCE(155);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 144:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'c') ADVANCE(149);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 145:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'c') ADVANCE(156);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 146:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'c') ADVANCE(166);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 147:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'd') ADVANCE(150);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 148:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'e') ADVANCE(151);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 149:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'e') ADVANCE(123);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 150:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'e') ADVANCE(160);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 151:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'g') ADVANCE(176);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 152:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'i') ADVANCE(147);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 153:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'i') ADVANCE(163);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 154:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'i') ADVANCE(165);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 155:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'l') ADVANCE(173);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 156:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'l') ADVANCE(174);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 157:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'n') ADVANCE(143);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 158:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'n') ADVANCE(129);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 159:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'n') ADVANCE(127);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 160:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'n') ADVANCE(144);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 161:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'n') ADVANCE(145);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 162:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'o') ADVANCE(157);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 163:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'o') ADVANCE(158);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 164:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'o') ADVANCE(171);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 165:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'o') ADVANCE(159);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 166:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'o') ADVANCE(161);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 167:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'r') ADVANCE(141);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 168:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 's') ADVANCE(153);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 169:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 's') ADVANCE(154);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 170:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 't') ADVANCE(167);
      if (lookahead == 'u') ADVANCE(142);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 171:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 't') ADVANCE(139);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 172:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 't') ADVANCE(148);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 173:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'u') ADVANCE(168);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 174:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'u') ADVANCE(169);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 175:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'v') ADVANCE(152);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 176:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == 'y') ADVANCE(125);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 177:
      ACCEPT_TOKEN(sym_ID);
      if (lookahead == '-' ||
          ('0' <= lookahead && lookahead <= '9') ||
          ('A' <= lookahead && lookahead <= 'Z') ||
          lookahead == '_' ||
          ('a' <= lookahead && lookahead <= 'z')) ADVANCE(177);
      END_STATE();
    case 178:
      ACCEPT_TOKEN(sym_STRING);
      END_STATE();
    case 179:
      ACCEPT_TOKEN(sym_INTEGER);
      if (('0' <= lookahead && lookahead <= '9')) ADVANCE(179);
      END_STATE();
    case 180:
      ACCEPT_TOKEN(anon_sym_or);
      END_STATE();
    case 181:
      ACCEPT_TOKEN(anon_sym_and);
      END_STATE();
    case 182:
      ACCEPT_TOKEN(anon_sym_EQ_EQ);
      END_STATE();
    case 183:
      ACCEPT_TOKEN(anon_sym_GT);
      if (lookahead == '=') ADVANCE(186);
      END_STATE();
    case 184:
      ACCEPT_TOKEN(anon_sym_LT);
      if (lookahead == '=') ADVANCE(185);
      END_STATE();
    case 185:
      ACCEPT_TOKEN(anon_sym_LT_EQ);
      END_STATE();
    case 186:
      ACCEPT_TOKEN(anon_sym_GT_EQ);
      END_STATE();
    default:
      return false;
  }
}

static const TSLexMode ts_lex_modes[STATE_COUNT] = {
  [0] = {.lex_state = 0},
  [1] = {.lex_state = 0},
  [2] = {.lex_state = 3},
  [3] = {.lex_state = 3},
  [4] = {.lex_state = 3},
  [5] = {.lex_state = 3},
  [6] = {.lex_state = 3},
  [7] = {.lex_state = 3},
  [8] = {.lex_state = 0},
  [9] = {.lex_state = 0},
  [10] = {.lex_state = 0},
  [11] = {.lex_state = 0},
  [12] = {.lex_state = 3},
  [13] = {.lex_state = 3},
  [14] = {.lex_state = 3},
  [15] = {.lex_state = 3},
  [16] = {.lex_state = 3},
  [17] = {.lex_state = 3},
  [18] = {.lex_state = 3},
  [19] = {.lex_state = 0},
  [20] = {.lex_state = 0},
  [21] = {.lex_state = 57},
  [22] = {.lex_state = 0},
  [23] = {.lex_state = 0},
  [24] = {.lex_state = 0},
  [25] = {.lex_state = 0},
  [26] = {.lex_state = 0},
  [27] = {.lex_state = 0},
  [28] = {.lex_state = 0},
  [29] = {.lex_state = 0},
  [30] = {.lex_state = 0},
  [31] = {.lex_state = 57},
  [32] = {.lex_state = 0},
  [33] = {.lex_state = 0},
  [34] = {.lex_state = 0},
  [35] = {.lex_state = 0},
  [36] = {.lex_state = 0},
  [37] = {.lex_state = 0},
  [38] = {.lex_state = 0},
  [39] = {.lex_state = 0},
  [40] = {.lex_state = 0},
  [41] = {.lex_state = 0},
  [42] = {.lex_state = 0},
  [43] = {.lex_state = 0},
  [44] = {.lex_state = 0},
  [45] = {.lex_state = 0},
  [46] = {.lex_state = 0},
  [47] = {.lex_state = 0},
  [48] = {.lex_state = 0},
  [49] = {.lex_state = 0},
  [50] = {.lex_state = 0},
  [51] = {.lex_state = 0},
  [52] = {.lex_state = 0},
  [53] = {.lex_state = 0},
  [54] = {.lex_state = 0},
  [55] = {.lex_state = 110},
  [56] = {.lex_state = 110},
  [57] = {.lex_state = 0},
  [58] = {.lex_state = 0},
  [59] = {.lex_state = 0},
  [60] = {.lex_state = 0},
  [61] = {.lex_state = 0},
  [62] = {.lex_state = 0},
  [63] = {.lex_state = 110},
  [64] = {.lex_state = 110},
  [65] = {.lex_state = 110},
  [66] = {.lex_state = 110},
  [67] = {.lex_state = 110},
  [68] = {.lex_state = 57},
  [69] = {.lex_state = 0},
  [70] = {.lex_state = 110},
  [71] = {.lex_state = 0},
  [72] = {.lex_state = 0},
  [73] = {.lex_state = 0},
  [74] = {.lex_state = 0},
  [75] = {.lex_state = 0},
  [76] = {.lex_state = 0},
  [77] = {.lex_state = 0},
  [78] = {.lex_state = 0},
  [79] = {.lex_state = 0},
  [80] = {.lex_state = 110},
  [81] = {.lex_state = 0},
  [82] = {.lex_state = 110},
  [83] = {.lex_state = 110},
  [84] = {.lex_state = 0},
  [85] = {.lex_state = 0},
  [86] = {.lex_state = 0},
  [87] = {.lex_state = 110},
  [88] = {.lex_state = 110},
  [89] = {.lex_state = 0},
  [90] = {.lex_state = 0},
  [91] = {.lex_state = 110},
  [92] = {.lex_state = 0},
  [93] = {.lex_state = 110},
};

static const uint16_t ts_parse_table[LARGE_STATE_COUNT][SYMBOL_COUNT] = {
  [0] = {
    [ts_builtin_sym_end] = ACTIONS(1),
    [anon_sym_justification] = ACTIONS(1),
    [anon_sym_implements] = ACTIONS(1),
    [anon_sym_LBRACE] = ACTIONS(1),
    [anon_sym_RBRACE] = ACTIONS(1),
    [anon_sym_pattern] = ACTIONS(1),
    [anon_sym_load] = ACTIONS(1),
    [anon_sym_implementation] = ACTIONS(1),
    [anon_sym_of] = ACTIONS(1),
    [anon_sym_is] = ACTIONS(1),
    [anon_sym_evidence] = ACTIONS(1),
    [anon_sym_strategy] = ACTIONS(1),
    [anon_sym_sub_DASHconclusion] = ACTIONS(1),
    [anon_sym_conclusion] = ACTIONS(1),
    [anon_sym_ATsupport] = ACTIONS(1),
    [anon_sym_probe] = ACTIONS(1),
    [anon_sym_operation] = ACTIONS(1),
    [anon_sym_expectation] = ACTIONS(1),
    [anon_sym_supports] = ACTIONS(1),
    [anon_sym_LPAREN] = ACTIONS(1),
    [anon_sym_COMMA] = ACTIONS(1),
    [anon_sym_RPAREN] = ACTIONS(1),
    [anon_sym_not] = ACTIONS(1),
    [sym_STRING] = ACTIONS(1),
    [sym_INTEGER] = ACTIONS(1),
    [anon_sym_or] = ACTIONS(1),
    [anon_sym_and] = ACTIONS(1),
    [anon_sym_EQ_EQ] = ACTIONS(1),
    [anon_sym_GT] = ACTIONS(1),
    [anon_sym_LT] = ACTIONS(1),
    [anon_sym_LT_EQ] = ACTIONS(1),
    [anon_sym_GT_EQ] = ACTIONS(1),
  },
  [1] = {
    [sym_source_file] = STATE(90),
    [sym_justification] = STATE(9),
    [sym_pattern] = STATE(9),
    [sym_load] = STATE(9),
    [sym_implementation] = STATE(9),
    [aux_sym_source_file_repeat1] = STATE(9),
    [ts_builtin_sym_end] = ACTIONS(3),
    [anon_sym_justification] = ACTIONS(5),
    [anon_sym_pattern] = ACTIONS(7),
    [anon_sym_load] = ACTIONS(9),
    [anon_sym_implementation] = ACTIONS(11),
  },
};

static const uint16_t ts_small_parse_table[] = {
  [0] = 8,
    ACTIONS(13), 1,
      anon_sym_RBRACE,
    ACTIONS(15), 1,
      anon_sym_evidence,
    ACTIONS(18), 1,
      anon_sym_strategy,
    ACTIONS(21), 1,
      anon_sym_sub_DASHconclusion,
    ACTIONS(24), 1,
      anon_sym_conclusion,
    ACTIONS(27), 1,
      anon_sym_ATsupport,
    ACTIONS(30), 1,
      sym_ID,
    STATE(2), 7,
      sym_evidence,
      sym_strategy,
      sym_sub_conclusion,
      sym_conclusion,
      sym_abs_support,
      sym_relation,
      aux_sym_pattern_body_repeat1,
  [31] = 8,
    ACTIONS(33), 1,
      anon_sym_RBRACE,
    ACTIONS(35), 1,
      anon_sym_evidence,
    ACTIONS(37), 1,
      anon_sym_strategy,
    ACTIONS(39), 1,
      anon_sym_sub_DASHconclusion,
    ACTIONS(41), 1,
      anon_sym_conclusion,
    ACTIONS(43), 1,
      anon_sym_ATsupport,
    ACTIONS(45), 1,
      sym_ID,
    STATE(4), 7,
      sym_evidence,
      sym_strategy,
      sym_sub_conclusion,
      sym_conclusion,
      sym_abs_support,
      sym_relation,
      aux_sym_pattern_body_repeat1,
  [62] = 8,
    ACTIONS(35), 1,
      anon_sym_evidence,
    ACTIONS(37), 1,
      anon_sym_strategy,
    ACTIONS(39), 1,
      anon_sym_sub_DASHconclusion,
    ACTIONS(41), 1,
      anon_sym_conclusion,
    ACTIONS(43), 1,
      anon_sym_ATsupport,
    ACTIONS(45), 1,
      sym_ID,
    ACTIONS(47), 1,
      anon_sym_RBRACE,
    STATE(2), 7,
      sym_evidence,
      sym_strategy,
      sym_sub_conclusion,
      sym_conclusion,
      sym_abs_support,
      sym_relation,
      aux_sym_pattern_body_repeat1,
  [93] = 7,
    ACTIONS(49), 1,
      anon_sym_RBRACE,
    ACTIONS(51), 1,
      anon_sym_evidence,
    ACTIONS(54), 1,
      anon_sym_strategy,
    ACTIONS(57), 1,
      anon_sym_sub_DASHconclusion,
    ACTIONS(60), 1,
      anon_sym_conclusion,
    ACTIONS(63), 1,
      sym_ID,
    STATE(5), 6,
      sym_evidence,
      sym_strategy,
      sym_sub_conclusion,
      sym_conclusion,
      sym_relation,
      aux_sym_justif_body_repeat1,
  [120] = 7,
    ACTIONS(35), 1,
      anon_sym_evidence,
    ACTIONS(37), 1,
      anon_sym_strategy,
    ACTIONS(39), 1,
      anon_sym_sub_DASHconclusion,
    ACTIONS(41), 1,
      anon_sym_conclusion,
    ACTIONS(45), 1,
      sym_ID,
    ACTIONS(66), 1,
      anon_sym_RBRACE,
    STATE(7), 6,
      sym_evidence,
      sym_strategy,
      sym_sub_conclusion,
      sym_conclusion,
      sym_relation,
      aux_sym_justif_body_repeat1,
  [147] = 7,
    ACTIONS(35), 1,
      anon_sym_evidence,
    ACTIONS(37), 1,
      anon_sym_strategy,
    ACTIONS(39), 1,
      anon_sym_sub_DASHconclusion,
    ACTIONS(41), 1,
      anon_sym_conclusion,
    ACTIONS(45), 1,
      sym_ID,
    ACTIONS(68), 1,
      anon_sym_RBRACE,
    STATE(5), 6,
      sym_evidence,
      sym_strategy,
      sym_sub_conclusion,
      sym_conclusion,
      sym_relation,
      aux_sym_justif_body_repeat1,
  [174] = 6,
    ACTIONS(73), 1,
      anon_sym_LPAREN,
    STATE(39), 1,
      sym_command_args,
    STATE(77), 1,
      sym_ARITH_OP,
    ACTIONS(77), 2,
      anon_sym_GT,
      anon_sym_LT,
    ACTIONS(70), 3,
      anon_sym_RBRACE,
      anon_sym_or,
      anon_sym_and,
    ACTIONS(75), 3,
      anon_sym_EQ_EQ,
      anon_sym_LT_EQ,
      anon_sym_GT_EQ,
  [198] = 6,
    ACTIONS(5), 1,
      anon_sym_justification,
    ACTIONS(7), 1,
      anon_sym_pattern,
    ACTIONS(9), 1,
      anon_sym_load,
    ACTIONS(11), 1,
      anon_sym_implementation,
    ACTIONS(79), 1,
      ts_builtin_sym_end,
    STATE(10), 5,
      sym_justification,
      sym_pattern,
      sym_load,
      sym_implementation,
      aux_sym_source_file_repeat1,
  [221] = 6,
    ACTIONS(81), 1,
      ts_builtin_sym_end,
    ACTIONS(83), 1,
      anon_sym_justification,
    ACTIONS(86), 1,
      anon_sym_pattern,
    ACTIONS(89), 1,
      anon_sym_load,
    ACTIONS(92), 1,
      anon_sym_implementation,
    STATE(10), 5,
      sym_justification,
      sym_pattern,
      sym_load,
      sym_implementation,
      aux_sym_source_file_repeat1,
  [244] = 4,
    STATE(84), 1,
      sym_ARITH_OP,
    ACTIONS(77), 2,
      anon_sym_GT,
      anon_sym_LT,
    ACTIONS(75), 3,
      anon_sym_EQ_EQ,
      anon_sym_LT_EQ,
      anon_sym_GT_EQ,
    ACTIONS(95), 3,
      anon_sym_RBRACE,
      anon_sym_or,
      anon_sym_and,
  [262] = 2,
    ACTIONS(97), 2,
      anon_sym_RBRACE,
      anon_sym_ATsupport,
    ACTIONS(99), 5,
      anon_sym_evidence,
      anon_sym_strategy,
      anon_sym_sub_DASHconclusion,
      anon_sym_conclusion,
      sym_ID,
  [274] = 2,
    ACTIONS(101), 2,
      anon_sym_RBRACE,
      anon_sym_ATsupport,
    ACTIONS(103), 5,
      anon_sym_evidence,
      anon_sym_strategy,
      anon_sym_sub_DASHconclusion,
      anon_sym_conclusion,
      sym_ID,
  [286] = 2,
    ACTIONS(105), 2,
      anon_sym_RBRACE,
      anon_sym_ATsupport,
    ACTIONS(107), 5,
      anon_sym_evidence,
      anon_sym_strategy,
      anon_sym_sub_DASHconclusion,
      anon_sym_conclusion,
      sym_ID,
  [298] = 2,
    ACTIONS(109), 2,
      anon_sym_RBRACE,
      anon_sym_ATsupport,
    ACTIONS(111), 5,
      anon_sym_evidence,
      anon_sym_strategy,
      anon_sym_sub_DASHconclusion,
      anon_sym_conclusion,
      sym_ID,
  [310] = 2,
    ACTIONS(113), 2,
      anon_sym_RBRACE,
      anon_sym_ATsupport,
    ACTIONS(115), 5,
      anon_sym_evidence,
      anon_sym_strategy,
      anon_sym_sub_DASHconclusion,
      anon_sym_conclusion,
      sym_ID,
  [322] = 2,
    ACTIONS(117), 2,
      anon_sym_RBRACE,
      anon_sym_ATsupport,
    ACTIONS(119), 5,
      anon_sym_evidence,
      anon_sym_strategy,
      anon_sym_sub_DASHconclusion,
      anon_sym_conclusion,
      sym_ID,
  [334] = 2,
    ACTIONS(121), 2,
      anon_sym_RBRACE,
      anon_sym_ATsupport,
    ACTIONS(123), 5,
      anon_sym_evidence,
      anon_sym_strategy,
      anon_sym_sub_DASHconclusion,
      anon_sym_conclusion,
      sym_ID,
  [346] = 1,
    ACTIONS(125), 5,
      ts_builtin_sym_end,
      anon_sym_justification,
      anon_sym_pattern,
      anon_sym_load,
      anon_sym_implementation,
  [354] = 1,
    ACTIONS(127), 5,
      ts_builtin_sym_end,
      anon_sym_justification,
      anon_sym_pattern,
      anon_sym_load,
      anon_sym_implementation,
  [362] = 4,
    ACTIONS(129), 1,
      anon_sym_not,
    ACTIONS(131), 1,
      sym_ID,
    STATE(51), 1,
      sym_expression,
    STATE(23), 2,
      sym_command,
      sym_boolean_expr,
  [376] = 1,
    ACTIONS(133), 5,
      ts_builtin_sym_end,
      anon_sym_justification,
      anon_sym_pattern,
      anon_sym_load,
      anon_sym_implementation,
  [384] = 4,
    ACTIONS(135), 1,
      anon_sym_RBRACE,
    STATE(21), 1,
      sym_BOOL_OP,
    STATE(35), 1,
      aux_sym_expression_repeat1,
    ACTIONS(137), 2,
      anon_sym_or,
      anon_sym_and,
  [398] = 1,
    ACTIONS(140), 5,
      ts_builtin_sym_end,
      anon_sym_justification,
      anon_sym_pattern,
      anon_sym_load,
      anon_sym_implementation,
  [406] = 1,
    ACTIONS(142), 5,
      ts_builtin_sym_end,
      anon_sym_justification,
      anon_sym_pattern,
      anon_sym_load,
      anon_sym_implementation,
  [414] = 4,
    ACTIONS(135), 1,
      anon_sym_RBRACE,
    STATE(21), 1,
      sym_BOOL_OP,
    STATE(27), 1,
      aux_sym_expression_repeat1,
    ACTIONS(144), 2,
      anon_sym_or,
      anon_sym_and,
  [428] = 4,
    ACTIONS(146), 1,
      anon_sym_RBRACE,
    STATE(21), 1,
      sym_BOOL_OP,
    STATE(36), 1,
      aux_sym_expression_repeat1,
    ACTIONS(144), 2,
      anon_sym_or,
      anon_sym_and,
  [442] = 1,
    ACTIONS(148), 5,
      ts_builtin_sym_end,
      anon_sym_justification,
      anon_sym_pattern,
      anon_sym_load,
      anon_sym_implementation,
  [450] = 1,
    ACTIONS(150), 5,
      ts_builtin_sym_end,
      anon_sym_justification,
      anon_sym_pattern,
      anon_sym_load,
      anon_sym_implementation,
  [458] = 1,
    ACTIONS(152), 5,
      ts_builtin_sym_end,
      anon_sym_justification,
      anon_sym_pattern,
      anon_sym_load,
      anon_sym_implementation,
  [466] = 4,
    ACTIONS(129), 1,
      anon_sym_not,
    ACTIONS(131), 1,
      sym_ID,
    STATE(73), 1,
      sym_expression,
    STATE(26), 2,
      sym_command,
      sym_boolean_expr,
  [480] = 1,
    ACTIONS(154), 5,
      ts_builtin_sym_end,
      anon_sym_justification,
      anon_sym_pattern,
      anon_sym_load,
      anon_sym_implementation,
  [488] = 1,
    ACTIONS(156), 5,
      ts_builtin_sym_end,
      anon_sym_justification,
      anon_sym_pattern,
      anon_sym_load,
      anon_sym_implementation,
  [496] = 1,
    ACTIONS(158), 5,
      ts_builtin_sym_end,
      anon_sym_justification,
      anon_sym_pattern,
      anon_sym_load,
      anon_sym_implementation,
  [504] = 4,
    ACTIONS(146), 1,
      anon_sym_RBRACE,
    STATE(21), 1,
      sym_BOOL_OP,
    STATE(36), 1,
      aux_sym_expression_repeat1,
    ACTIONS(160), 2,
      anon_sym_or,
      anon_sym_and,
  [518] = 4,
    ACTIONS(163), 1,
      anon_sym_RBRACE,
    STATE(21), 1,
      sym_BOOL_OP,
    STATE(36), 1,
      aux_sym_expression_repeat1,
    ACTIONS(165), 2,
      anon_sym_or,
      anon_sym_and,
  [532] = 1,
    ACTIONS(168), 4,
      anon_sym_RBRACE,
      anon_sym_expectation,
      anon_sym_or,
      anon_sym_and,
  [539] = 3,
    ACTIONS(73), 1,
      anon_sym_LPAREN,
    STATE(39), 1,
      sym_command_args,
    ACTIONS(170), 2,
      anon_sym_RBRACE,
      anon_sym_expectation,
  [550] = 1,
    ACTIONS(172), 4,
      anon_sym_RBRACE,
      anon_sym_expectation,
      anon_sym_or,
      anon_sym_and,
  [557] = 3,
    ACTIONS(174), 1,
      anon_sym_probe,
    ACTIONS(176), 1,
      anon_sym_operation,
    STATE(44), 2,
      sym_probe,
      sym_operation,
  [568] = 1,
    ACTIONS(178), 4,
      anon_sym_RBRACE,
      anon_sym_expectation,
      anon_sym_or,
      anon_sym_and,
  [575] = 3,
    ACTIONS(180), 1,
      anon_sym_COMMA,
    ACTIONS(182), 1,
      anon_sym_RPAREN,
    STATE(50), 1,
      aux_sym_command_args_repeat1,
  [585] = 1,
    ACTIONS(184), 3,
      anon_sym_RBRACE,
      anon_sym_or,
      anon_sym_and,
  [591] = 3,
    ACTIONS(186), 1,
      anon_sym_RBRACE,
    ACTIONS(188), 1,
      anon_sym_expectation,
    STATE(75), 1,
      sym_expectation,
  [601] = 3,
    ACTIONS(190), 1,
      anon_sym_implements,
    ACTIONS(192), 1,
      anon_sym_RBRACE,
    STATE(49), 1,
      aux_sym_impl_body_repeat1,
  [611] = 3,
    ACTIONS(194), 1,
      anon_sym_implements,
    ACTIONS(196), 1,
      anon_sym_LBRACE,
    STATE(32), 1,
      sym_justif_body,
  [621] = 1,
    ACTIONS(198), 3,
      anon_sym_RBRACE,
      anon_sym_or,
      anon_sym_and,
  [627] = 3,
    ACTIONS(180), 1,
      anon_sym_COMMA,
    ACTIONS(200), 1,
      anon_sym_RPAREN,
    STATE(42), 1,
      aux_sym_command_args_repeat1,
  [637] = 3,
    ACTIONS(190), 1,
      anon_sym_implements,
    ACTIONS(202), 1,
      anon_sym_RBRACE,
    STATE(52), 1,
      aux_sym_impl_body_repeat1,
  [647] = 3,
    ACTIONS(204), 1,
      anon_sym_COMMA,
    ACTIONS(207), 1,
      anon_sym_RPAREN,
    STATE(50), 1,
      aux_sym_command_args_repeat1,
  [657] = 1,
    ACTIONS(209), 3,
      anon_sym_RBRACE,
      anon_sym_or,
      anon_sym_and,
  [663] = 3,
    ACTIONS(211), 1,
      anon_sym_implements,
    ACTIONS(214), 1,
      anon_sym_RBRACE,
    STATE(52), 1,
      aux_sym_impl_body_repeat1,
  [673] = 2,
    ACTIONS(216), 1,
      anon_sym_LBRACE,
    STATE(20), 1,
      sym_impl_body,
  [680] = 2,
    ACTIONS(196), 1,
      anon_sym_LBRACE,
    STATE(33), 1,
      sym_justif_body,
  [687] = 2,
    ACTIONS(218), 1,
      sym_ID,
    STATE(59), 1,
      sym_command,
  [694] = 2,
    ACTIONS(218), 1,
      sym_ID,
    STATE(60), 1,
      sym_command,
  [701] = 1,
    ACTIONS(220), 2,
      anon_sym_implements,
      anon_sym_RBRACE,
  [706] = 2,
    ACTIONS(222), 1,
      anon_sym_LBRACE,
    STATE(19), 1,
      sym_pattern_body,
  [713] = 1,
    ACTIONS(224), 2,
      anon_sym_RBRACE,
      anon_sym_expectation,
  [718] = 1,
    ACTIONS(226), 2,
      anon_sym_RBRACE,
      anon_sym_expectation,
  [723] = 1,
    ACTIONS(207), 2,
      anon_sym_COMMA,
      anon_sym_RPAREN,
  [728] = 1,
    ACTIONS(228), 2,
      anon_sym_implements,
      anon_sym_RBRACE,
  [733] = 2,
    ACTIONS(230), 1,
      sym_ID,
    STATE(15), 1,
      sym_identified_element,
  [740] = 2,
    ACTIONS(230), 1,
      sym_ID,
    STATE(16), 1,
      sym_identified_element,
  [747] = 2,
    ACTIONS(230), 1,
      sym_ID,
    STATE(17), 1,
      sym_identified_element,
  [754] = 2,
    ACTIONS(230), 1,
      sym_ID,
    STATE(12), 1,
      sym_identified_element,
  [761] = 2,
    ACTIONS(230), 1,
      sym_ID,
    STATE(18), 1,
      sym_identified_element,
  [768] = 1,
    ACTIONS(232), 2,
      anon_sym_not,
      sym_ID,
  [773] = 1,
    ACTIONS(234), 1,
      sym_STRING,
  [777] = 1,
    ACTIONS(236), 1,
      sym_ID,
  [781] = 1,
    ACTIONS(238), 1,
      anon_sym_is,
  [785] = 1,
    ACTIONS(240), 1,
      anon_sym_LBRACE,
  [789] = 1,
    ACTIONS(242), 1,
      anon_sym_RBRACE,
  [793] = 1,
    ACTIONS(244), 1,
      anon_sym_is,
  [797] = 1,
    ACTIONS(246), 1,
      anon_sym_RBRACE,
  [801] = 1,
    ACTIONS(248), 1,
      sym_INTEGER,
  [805] = 1,
    ACTIONS(250), 1,
      sym_INTEGER,
  [809] = 1,
    ACTIONS(252), 1,
      sym_STRING,
  [813] = 1,
    ACTIONS(254), 1,
      anon_sym_is,
  [817] = 1,
    ACTIONS(256), 1,
      sym_ID,
  [821] = 1,
    ACTIONS(258), 1,
      sym_STRING,
  [825] = 1,
    ACTIONS(260), 1,
      sym_ID,
  [829] = 1,
    ACTIONS(262), 1,
      sym_ID,
  [833] = 1,
    ACTIONS(264), 1,
      sym_INTEGER,
  [837] = 1,
    ACTIONS(266), 1,
      anon_sym_is,
  [841] = 1,
    ACTIONS(268), 1,
      anon_sym_supports,
  [845] = 1,
    ACTIONS(270), 1,
      sym_ID,
  [849] = 1,
    ACTIONS(272), 1,
      sym_ID,
  [853] = 1,
    ACTIONS(274), 1,
      anon_sym_of,
  [857] = 1,
    ACTIONS(276), 1,
      ts_builtin_sym_end,
  [861] = 1,
    ACTIONS(278), 1,
      sym_ID,
  [865] = 1,
    ACTIONS(280), 1,
      sym_STRING,
  [869] = 1,
    ACTIONS(282), 1,
      sym_ID,
};

static const uint32_t ts_small_parse_table_map[] = {
  [SMALL_STATE(2)] = 0,
  [SMALL_STATE(3)] = 31,
  [SMALL_STATE(4)] = 62,
  [SMALL_STATE(5)] = 93,
  [SMALL_STATE(6)] = 120,
  [SMALL_STATE(7)] = 147,
  [SMALL_STATE(8)] = 174,
  [SMALL_STATE(9)] = 198,
  [SMALL_STATE(10)] = 221,
  [SMALL_STATE(11)] = 244,
  [SMALL_STATE(12)] = 262,
  [SMALL_STATE(13)] = 274,
  [SMALL_STATE(14)] = 286,
  [SMALL_STATE(15)] = 298,
  [SMALL_STATE(16)] = 310,
  [SMALL_STATE(17)] = 322,
  [SMALL_STATE(18)] = 334,
  [SMALL_STATE(19)] = 346,
  [SMALL_STATE(20)] = 354,
  [SMALL_STATE(21)] = 362,
  [SMALL_STATE(22)] = 376,
  [SMALL_STATE(23)] = 384,
  [SMALL_STATE(24)] = 398,
  [SMALL_STATE(25)] = 406,
  [SMALL_STATE(26)] = 414,
  [SMALL_STATE(27)] = 428,
  [SMALL_STATE(28)] = 442,
  [SMALL_STATE(29)] = 450,
  [SMALL_STATE(30)] = 458,
  [SMALL_STATE(31)] = 466,
  [SMALL_STATE(32)] = 480,
  [SMALL_STATE(33)] = 488,
  [SMALL_STATE(34)] = 496,
  [SMALL_STATE(35)] = 504,
  [SMALL_STATE(36)] = 518,
  [SMALL_STATE(37)] = 532,
  [SMALL_STATE(38)] = 539,
  [SMALL_STATE(39)] = 550,
  [SMALL_STATE(40)] = 557,
  [SMALL_STATE(41)] = 568,
  [SMALL_STATE(42)] = 575,
  [SMALL_STATE(43)] = 585,
  [SMALL_STATE(44)] = 591,
  [SMALL_STATE(45)] = 601,
  [SMALL_STATE(46)] = 611,
  [SMALL_STATE(47)] = 621,
  [SMALL_STATE(48)] = 627,
  [SMALL_STATE(49)] = 637,
  [SMALL_STATE(50)] = 647,
  [SMALL_STATE(51)] = 657,
  [SMALL_STATE(52)] = 663,
  [SMALL_STATE(53)] = 673,
  [SMALL_STATE(54)] = 680,
  [SMALL_STATE(55)] = 687,
  [SMALL_STATE(56)] = 694,
  [SMALL_STATE(57)] = 701,
  [SMALL_STATE(58)] = 706,
  [SMALL_STATE(59)] = 713,
  [SMALL_STATE(60)] = 718,
  [SMALL_STATE(61)] = 723,
  [SMALL_STATE(62)] = 728,
  [SMALL_STATE(63)] = 733,
  [SMALL_STATE(64)] = 740,
  [SMALL_STATE(65)] = 747,
  [SMALL_STATE(66)] = 754,
  [SMALL_STATE(67)] = 761,
  [SMALL_STATE(68)] = 768,
  [SMALL_STATE(69)] = 773,
  [SMALL_STATE(70)] = 777,
  [SMALL_STATE(71)] = 781,
  [SMALL_STATE(72)] = 785,
  [SMALL_STATE(73)] = 789,
  [SMALL_STATE(74)] = 793,
  [SMALL_STATE(75)] = 797,
  [SMALL_STATE(76)] = 801,
  [SMALL_STATE(77)] = 805,
  [SMALL_STATE(78)] = 809,
  [SMALL_STATE(79)] = 813,
  [SMALL_STATE(80)] = 817,
  [SMALL_STATE(81)] = 821,
  [SMALL_STATE(82)] = 825,
  [SMALL_STATE(83)] = 829,
  [SMALL_STATE(84)] = 833,
  [SMALL_STATE(85)] = 837,
  [SMALL_STATE(86)] = 841,
  [SMALL_STATE(87)] = 845,
  [SMALL_STATE(88)] = 849,
  [SMALL_STATE(89)] = 853,
  [SMALL_STATE(90)] = 857,
  [SMALL_STATE(91)] = 861,
  [SMALL_STATE(92)] = 865,
  [SMALL_STATE(93)] = 869,
};

static const TSParseActionEntry ts_parse_actions[] = {
  [0] = {.entry = {.count = 0, .reusable = false}},
  [1] = {.entry = {.count = 1, .reusable = false}}, RECOVER(),
  [3] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_source_file, 0),
  [5] = {.entry = {.count = 1, .reusable = true}}, SHIFT(80),
  [7] = {.entry = {.count = 1, .reusable = true}}, SHIFT(93),
  [9] = {.entry = {.count = 1, .reusable = true}}, SHIFT(92),
  [11] = {.entry = {.count = 1, .reusable = true}}, SHIFT(91),
  [13] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_pattern_body_repeat1, 2),
  [15] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_pattern_body_repeat1, 2), SHIFT_REPEAT(67),
  [18] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_pattern_body_repeat1, 2), SHIFT_REPEAT(66),
  [21] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_pattern_body_repeat1, 2), SHIFT_REPEAT(65),
  [24] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_pattern_body_repeat1, 2), SHIFT_REPEAT(64),
  [27] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_pattern_body_repeat1, 2), SHIFT_REPEAT(63),
  [30] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_pattern_body_repeat1, 2), SHIFT_REPEAT(86),
  [33] = {.entry = {.count = 1, .reusable = true}}, SHIFT(30),
  [35] = {.entry = {.count = 1, .reusable = false}}, SHIFT(67),
  [37] = {.entry = {.count = 1, .reusable = false}}, SHIFT(66),
  [39] = {.entry = {.count = 1, .reusable = false}}, SHIFT(65),
  [41] = {.entry = {.count = 1, .reusable = false}}, SHIFT(64),
  [43] = {.entry = {.count = 1, .reusable = true}}, SHIFT(63),
  [45] = {.entry = {.count = 1, .reusable = false}}, SHIFT(86),
  [47] = {.entry = {.count = 1, .reusable = true}}, SHIFT(28),
  [49] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_justif_body_repeat1, 2),
  [51] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_justif_body_repeat1, 2), SHIFT_REPEAT(67),
  [54] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_justif_body_repeat1, 2), SHIFT_REPEAT(66),
  [57] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_justif_body_repeat1, 2), SHIFT_REPEAT(65),
  [60] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_justif_body_repeat1, 2), SHIFT_REPEAT(64),
  [63] = {.entry = {.count = 2, .reusable = false}}, REDUCE(aux_sym_justif_body_repeat1, 2), SHIFT_REPEAT(86),
  [66] = {.entry = {.count = 1, .reusable = true}}, SHIFT(24),
  [68] = {.entry = {.count = 1, .reusable = true}}, SHIFT(34),
  [70] = {.entry = {.count = 2, .reusable = true}}, REDUCE(sym_command, 1, .production_id = 9), REDUCE(sym_boolean_expr, 1, .production_id = 9),
  [73] = {.entry = {.count = 1, .reusable = true}}, SHIFT(78),
  [75] = {.entry = {.count = 1, .reusable = true}}, SHIFT(76),
  [77] = {.entry = {.count = 1, .reusable = false}}, SHIFT(76),
  [79] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_source_file, 1),
  [81] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_source_file_repeat1, 2),
  [83] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_source_file_repeat1, 2), SHIFT_REPEAT(80),
  [86] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_source_file_repeat1, 2), SHIFT_REPEAT(93),
  [89] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_source_file_repeat1, 2), SHIFT_REPEAT(92),
  [92] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_source_file_repeat1, 2), SHIFT_REPEAT(91),
  [95] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_boolean_expr, 2, .production_id = 2),
  [97] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_strategy, 2),
  [99] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_strategy, 2),
  [101] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_identified_element, 3, .production_id = 6),
  [103] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_identified_element, 3, .production_id = 6),
  [105] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_relation, 3, .production_id = 5),
  [107] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_relation, 3, .production_id = 5),
  [109] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_abs_support, 2),
  [111] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_abs_support, 2),
  [113] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_conclusion, 2),
  [115] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_conclusion, 2),
  [117] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_sub_conclusion, 2),
  [119] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_sub_conclusion, 2),
  [121] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_evidence, 2),
  [123] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_evidence, 2),
  [125] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_pattern, 3, .production_id = 2),
  [127] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_implementation, 5, .production_id = 4),
  [129] = {.entry = {.count = 1, .reusable = false}}, SHIFT(70),
  [131] = {.entry = {.count = 1, .reusable = false}}, SHIFT(8),
  [133] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_impl_body, 3, .production_id = 7),
  [135] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_expression, 1),
  [137] = {.entry = {.count = 2, .reusable = true}}, REDUCE(sym_expression, 1), SHIFT(68),
  [140] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_justif_body, 2),
  [142] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_impl_body, 2),
  [144] = {.entry = {.count = 1, .reusable = true}}, SHIFT(68),
  [146] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_expression, 2, .production_id = 10),
  [148] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_pattern_body, 3),
  [150] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_load, 2, .production_id = 1),
  [152] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_pattern_body, 2),
  [154] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_justification, 3, .production_id = 2),
  [156] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_justification, 5, .production_id = 3),
  [158] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_justif_body, 3),
  [160] = {.entry = {.count = 2, .reusable = true}}, REDUCE(sym_expression, 2, .production_id = 10), SHIFT(68),
  [163] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_expression_repeat1, 2, .production_id = 13),
  [165] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_expression_repeat1, 2, .production_id = 13), SHIFT_REPEAT(68),
  [168] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_command_args, 3),
  [170] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_command, 1, .production_id = 9),
  [172] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_command, 2, .production_id = 9),
  [174] = {.entry = {.count = 1, .reusable = true}}, SHIFT(79),
  [176] = {.entry = {.count = 1, .reusable = true}}, SHIFT(71),
  [178] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_command_args, 4),
  [180] = {.entry = {.count = 1, .reusable = true}}, SHIFT(81),
  [182] = {.entry = {.count = 1, .reusable = true}}, SHIFT(41),
  [184] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_boolean_expr, 3, .production_id = 11),
  [186] = {.entry = {.count = 1, .reusable = true}}, SHIFT(57),
  [188] = {.entry = {.count = 1, .reusable = true}}, SHIFT(74),
  [190] = {.entry = {.count = 1, .reusable = true}}, SHIFT(82),
  [192] = {.entry = {.count = 1, .reusable = true}}, SHIFT(25),
  [194] = {.entry = {.count = 1, .reusable = true}}, SHIFT(88),
  [196] = {.entry = {.count = 1, .reusable = true}}, SHIFT(6),
  [198] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_boolean_expr, 4, .production_id = 14),
  [200] = {.entry = {.count = 1, .reusable = true}}, SHIFT(37),
  [202] = {.entry = {.count = 1, .reusable = true}}, SHIFT(22),
  [204] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_command_args_repeat1, 2), SHIFT_REPEAT(81),
  [207] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_command_args_repeat1, 2),
  [209] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_expression_repeat1, 2, .production_id = 12),
  [211] = {.entry = {.count = 2, .reusable = true}}, REDUCE(aux_sym_impl_body_repeat1, 2, .production_id = 8), SHIFT_REPEAT(82),
  [214] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_impl_body_repeat1, 2, .production_id = 8),
  [216] = {.entry = {.count = 1, .reusable = true}}, SHIFT(45),
  [218] = {.entry = {.count = 1, .reusable = true}}, SHIFT(38),
  [220] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_impl_body_repeat1, 5, .production_id = 2),
  [222] = {.entry = {.count = 1, .reusable = true}}, SHIFT(3),
  [224] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_probe, 3),
  [226] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_operation, 3),
  [228] = {.entry = {.count = 1, .reusable = true}}, REDUCE(aux_sym_impl_body_repeat1, 6, .production_id = 2),
  [230] = {.entry = {.count = 1, .reusable = true}}, SHIFT(85),
  [232] = {.entry = {.count = 1, .reusable = false}}, REDUCE(sym_BOOL_OP, 1),
  [234] = {.entry = {.count = 1, .reusable = true}}, SHIFT(13),
  [236] = {.entry = {.count = 1, .reusable = true}}, SHIFT(11),
  [238] = {.entry = {.count = 1, .reusable = true}}, SHIFT(56),
  [240] = {.entry = {.count = 1, .reusable = true}}, SHIFT(40),
  [242] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_expectation, 3),
  [244] = {.entry = {.count = 1, .reusable = true}}, SHIFT(31),
  [246] = {.entry = {.count = 1, .reusable = true}}, SHIFT(62),
  [248] = {.entry = {.count = 1, .reusable = true}}, REDUCE(sym_ARITH_OP, 1),
  [250] = {.entry = {.count = 1, .reusable = true}}, SHIFT(43),
  [252] = {.entry = {.count = 1, .reusable = true}}, SHIFT(48),
  [254] = {.entry = {.count = 1, .reusable = true}}, SHIFT(55),
  [256] = {.entry = {.count = 1, .reusable = true}}, SHIFT(46),
  [258] = {.entry = {.count = 1, .reusable = true}}, SHIFT(61),
  [260] = {.entry = {.count = 1, .reusable = true}}, SHIFT(72),
  [262] = {.entry = {.count = 1, .reusable = true}}, SHIFT(14),
  [264] = {.entry = {.count = 1, .reusable = true}}, SHIFT(47),
  [266] = {.entry = {.count = 1, .reusable = true}}, SHIFT(69),
  [268] = {.entry = {.count = 1, .reusable = true}}, SHIFT(83),
  [270] = {.entry = {.count = 1, .reusable = true}}, SHIFT(53),
  [272] = {.entry = {.count = 1, .reusable = true}}, SHIFT(54),
  [274] = {.entry = {.count = 1, .reusable = true}}, SHIFT(87),
  [276] = {.entry = {.count = 1, .reusable = true}},  ACCEPT_INPUT(),
  [278] = {.entry = {.count = 1, .reusable = true}}, SHIFT(89),
  [280] = {.entry = {.count = 1, .reusable = true}}, SHIFT(29),
  [282] = {.entry = {.count = 1, .reusable = true}}, SHIFT(58),
};

#ifdef __cplusplus
extern "C" {
#endif
#ifdef TREE_SITTER_HIDE_SYMBOLS
#define TS_PUBLIC
#elif defined(_WIN32)
#define TS_PUBLIC __declspec(dllexport)
#else
#define TS_PUBLIC __attribute__((visibility("default")))
#endif

TS_PUBLIC const TSLanguage *tree_sitter_jpipe() {
  static const TSLanguage language = {
    .version = LANGUAGE_VERSION,
    .symbol_count = SYMBOL_COUNT,
    .alias_count = ALIAS_COUNT,
    .token_count = TOKEN_COUNT,
    .external_token_count = EXTERNAL_TOKEN_COUNT,
    .state_count = STATE_COUNT,
    .large_state_count = LARGE_STATE_COUNT,
    .production_id_count = PRODUCTION_ID_COUNT,
    .field_count = FIELD_COUNT,
    .max_alias_sequence_length = MAX_ALIAS_SEQUENCE_LENGTH,
    .parse_table = &ts_parse_table[0][0],
    .small_parse_table = ts_small_parse_table,
    .small_parse_table_map = ts_small_parse_table_map,
    .parse_actions = ts_parse_actions,
    .symbol_names = ts_symbol_names,
    .field_names = ts_field_names,
    .field_map_slices = ts_field_map_slices,
    .field_map_entries = ts_field_map_entries,
    .symbol_metadata = ts_symbol_metadata,
    .public_symbol_map = ts_symbol_map,
    .alias_map = ts_non_terminal_alias_map,
    .alias_sequences = &ts_alias_sequences[0][0],
    .lex_modes = ts_lex_modes,
    .lex_fn = ts_lex,
    .primary_state_ids = ts_primary_state_ids,
  };
  return &language;
}
#ifdef __cplusplus
}
#endif
