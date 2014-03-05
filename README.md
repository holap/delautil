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

A Map<String, JComponent> will be used to map templates key to corrisponding JCompoent.
The map for the above template could look this:

map.put("name.label", new JLabel("Name"));
map.put("name", new JTextField(contact.getName()));
map.put("surname.label", new JLabel("Surname"));
map.put("name", new JTextField(contact.getSurname());
map.put("tags", new JTextField(contact.getTags());

While you can create the map by yourself, Dalautis is most suited for the use with frameworks that creates automatically components inspecting the property to bind. On data driven application, where most of constraints and information are already defined on entity classes, this would simply development and follow the DRY principle. If you need to create the component on your own than javabuilders is more appropriate.

TODO:
- Complete GridBagLayout support (at the moment only weightx and weighty missing)
- Complete unit test and push them on git
- Add support to JGoodies FormLayout
- Add support to MigLayout

WARNING:
The project is not stable and is not ready for production use. Api will propably change significatively.
