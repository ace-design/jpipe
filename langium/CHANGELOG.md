## Changelog
### Unreleased
- Leader: Cass Braun
  - Features:
    - Added GoToDefinition when clicking on SVG

### v0.2.8 (2025-03-05)
- Leader: Sébastien Mosser
  - Features:
    - Fixing path issues for Windows

### v0.2.7 (2025-02-24)
- Leader: Cass Braun
  - Features:
    - Added ability to activate and deactivate installation checks on startup for Java and GraphViz
    - Added instruction description on code completion prompt
    - Fixed error with conclusion validation when justification implements pattern
    - Added QuickFix to add blank @support statement to pattern
    - Added QuickFix to add blank conclusion into pattern
    - Added validation for conclusion in pattern
    - Added completion support for variables loaded from other files
    - Added ability to set java version in settings

### v0.2.6 (2025-01-29)
- Leader: Sébastien Mosser
  - Features:
    - Support file path coming from windows

### v0.2.5 (2025-01-29)
- Leader: Sébastien Mosser
  - Features:
    - Fix path issues when they contain space (bugfix issue #99)

### v0.2.4 (2025-01-20)
- Leader: Sébastien Mosser
  - Features:
    - Fix dependencies vulnerabilities
    - Update to latest version of Langium

### v0.2.3 (2025-01-20)

 
### Unreleased
- Leader: Cass Braun
  - Features:
    - Added ability to activate and deactivate installation checks on startup for Java and GraphViz
    - Added QuickFix to remove implemented element of justification, pattern, and composition when there is incorrect syntax
    - Added validation to throw error when there is no conclusion found in a justification diagram
    - Added QuickFix to add a conclusion to a justification diagram
    - Added GoToDefintion for load statements
 
### v0.2.2 (2024-08-13)
- Leader: Cass Braun
  - Features:
    - Added QuickFix to change justification to pattern if it includes an instruction labelled @support
    - Added QuickFix to change pattern to justification if it includes no instruction labelled @support
    - Added QuickFix to remove support statement line if it's instruction types do not match
    - Added QuickFix to add relative load statements for unresolved references
    - Added support for relative load statements
    - Added validation for declaration implementation rules

### v0.2.2 (2024-08-06)
- Leader: Cass Braun
  - Features:
    - Added patterns and compositions to grammar with basic language support
    - Added improved completion for justification diagrams and compositions
    - Added basic quick fix

### v0.2.0 (2024-07-19)

  - Leader: Cass Braun
  - Features:
    - New language server using Langium instead of LEVER
    - Downloading files with right click, as PNG or SVG
    - Configuration settings to select which JAR file to be used


### v0.1.0 (2024-04-20)

  - Leader: Nirmal Chaudhari
  - Features:
    - Language server for jPipe, using LEVER
    - VS Code extension with syntax highligthing
    - Preview of diagrams
