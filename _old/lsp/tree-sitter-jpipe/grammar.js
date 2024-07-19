module.exports = grammar({
    name: 'jpipe',

    conflicts: $ => [
        [$.command, $.boolean_expr],
        [$.expression],
    ],

    rules: {
        source_file: $ => seq(repeat(choice($.justification, $.pattern, $.load, $.implementation))),

        justification: $ => seq('justification', field('id', $.ID), optional(seq('implements', field('parent', $.ID))), $.justif_body),
        justif_body: $ => seq(
            '{',
            repeat(choice($.evidence, $.sub_conclusion, $.strategy, $.relation, $.conclusion)),
            '}'
        ),

        pattern: $ => seq('pattern', field('id', $.ID), $.pattern_body),
        pattern_body: $ => seq(
            '{',
            repeat(choice($.abs_support, $.evidence, $.sub_conclusion, $.strategy, $.relation, $.conclusion)),
            '}'
        ),

        load: $ => seq('load', field('file', $.STRING)),

        implementation: $ => seq('implementation', field('id', $.ID), 'of', field('justification_id', $.ID), $.impl_body),
        impl_body: $ => seq(
            '{',
            repeat(seq('implements', field('id', $.ID), '{', choice($.probe, $.operation), optional($.expectation), '}')),
            '}'
        ),


        identified_element: $ => seq(field('id', $.ID), 'is', field('name', $.STRING)),
        evidence: $ => seq('evidence', $.identified_element),
        strategy: $ => seq('strategy', $.identified_element),
        sub_conclusion: $ => seq('sub-conclusion', $.identified_element),
        conclusion: $ => seq('conclusion', $.identified_element),
        abs_support: $ => seq('@support', $.identified_element),

        probe: $ => seq('probe', 'is', $.command),
        operation: $ => seq('operation', 'is', $.command),
        expectation: $ => seq('expectation', 'is', $.expression),

        relation: $ => seq(field('from', $.ID), 'supports', field('to', $.ID)),

        command: $ => seq(field('id', $.ID), optional($.command_args)),
        command_args: $ => seq('(', $.STRING, repeat(seq(',', $.STRING)), ')'),

        expression: $ => seq(choice($.boolean_expr, $.command), repeat(seq(field('op', $.BOOL_OP), $.expression))),
        boolean_expr: $ => seq(optional('not'), field('id', $.ID), optional(seq(field('op', $.ARITH_OP), $.INTEGER))),

        ID: $ => /[A-Za-z_\-][A-Za-z0-9_\-]*/,
        STRING: $ => /["']([^"']+)["']/,
        INTEGER: $ => /[0-9]+/,
        BOOL_OP: $ => choice('or', 'and'),
        ARITH_OP: $ => choice('==', '>', '<', '<=', '>='),
    }
});
