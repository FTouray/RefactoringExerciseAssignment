The Zip file RefactoringExercise2025.zip contains a set of Java classes developed to simulate a personnel application.

The specification for the personnel application was as follows:

You should construct a system that will allow users to define a data structure representing a collection of records that can be displayed both by means of a dialog that can be scrolled through and by means of a table to give an overall view of the collection contents. 
The user should be able to carry out tasks such as adding records to the collection, modifying the contents of records, and deleting records from the collection, as well as being able to save and retrieve the contents of the collection to and from external random access files.
The records in the collection will represent a set of personnel records with the following fields:
-	EmployeeID (this will be an integer unique to a particular employee and will be automatically generated when a new record is created)
-	PPSNumber (this will be a string of six digits and a letter and should also be unique)
-	Surname (this will be a string of length 20)
- FirstName (this will be a string of length 20)
-	Gender (this will have two options - "M" and "F" - and can be selected from a drop down list when entering a record)
•	Department (this will have four possible options - "Administration", "Production", "Transport" and "Management" - and again will be selected from a drop down list when entering a record)
-	Salary (this will be a positive real number)
-	FullTime (this will be a boolean, but you may wish to implement it as an integer to make calculating record sizes easier)

The system should be menu-driven, with the following menu options:
Navigate: First, Last, Next, Previous, Find By ID (allows you to find a record by EmployeeID entered via a dialog box and display the selected record), Find By Surname (allows you to find a record by surname entered via a dialog box), List All (displays the contents of the collection as a dialog containing a JTable)
- Records: Create, Modify, Delete
- File: Open, Save, Save As (these should use JFileChooser dialogs)
- Exit Application (this should make sure that the collection is saved - or that the user is given the opportunity to save the collection - before the application closes)
  
When presenting the results in a JTable for the List All option, the records should be sortable on all fields, but not editable (changing the records or adding and deleting records can only be done through the main dialog).
For all menu options in your program, you should perform whatever validation, error-checking and exception-handling you consider to be necessary.
