delautil
========

Declarative layout utility for swing with simple String template.

The purpose of the utility is simplifying the creation of guis. It doesn't create components, like javabuilders does,
it adds components to container with supported layout manager (only GridBagLayout at the moment) with the proper
constraints.

Example of template (better use fixed lenght font, see as raw):

{name.label}    | {name}    | {picture}

{surname.label} | {surname} |     ^ 

{tags.label}    | {tags}    :       

This tamplate defines 3 columns with 3 rows.
The componente mapped by {picture} will span over 2 columns, while the one mapped by {tags} will span over 2 rows.

Most of GridBagLayout configurations can be defined by template.

TODO:
- Complete GridBagLayout support (at the moment only weightx and weighty missing)
- Complete unit test and push them on git
- Add support to JGoodies FormLayout
- Add support to MigLayout

WARNING:
The project is not stable and is not ready for production use. Api will propably change significatively.
