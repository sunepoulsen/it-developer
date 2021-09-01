# Data Model

The Data Model describes how the database are organized and its entity types

## Registration Types

A registration type is the type that is associated to each time registration. It describes how the time registration
should be handled by the application. 

The registration type describes:

1. If the registration is normal work

   Time registrations of this type will need to be splitted into the actual 
   projects that the user has worked on during the time.

2. If the registration is absence from work - sickness, paid absence, vacation
3. Each registration type can have one or more reasons that should be selected for the registration.
 
   A reason is a fixed value that the user can associate with the registration. For instance the sickness registration 
   type will have reasons for the illnesses. The paid absence registration type will have reasons like civil 
   representative, etc.
 
4. A registration type will also have a setting that tells if it's an all day registration.

### Entities

From the requirements above we get the following entities:

- `registration_types`
- `reasons`

There will be a one-to-many relation between `registration_types` and `reasons`
