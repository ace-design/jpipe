package tree_sitter_jpipe_test

import (
	"testing"

	tree_sitter "github.com/smacker/go-tree-sitter"
	"github.com/tree-sitter/tree-sitter-jpipe"
)

func TestCanLoadGrammar(t *testing.T) {
	language := tree_sitter.NewLanguage(tree_sitter_jpipe.Language())
	if language == nil {
		t.Errorf("Error loading Jpipe grammar")
	}
}
