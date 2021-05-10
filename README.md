# ES-2Sem-2021-Grupo-5

Software Engineering, 2020/21, 2nd Semester
Grupo 5
André Garcia, n 87872
David Simões, n 87618
Diana Lopes, n 87524
Diogo Candeias, n 87377
Diogo Minhoto, n 73429
João Guerra, n 68635

Execution Requirements: jdk 15 or above

Problems:
Our metric extraction algorithm includes .java files outside of the "src" folder. This means there are 2 entries in the extracted .xlsx file that do not exist in the provided Code_Smells.xlsx file when analysing the jasml project.
The evaluated code smells aren't separated from the regular metrics in the UI. They are instead shown in the same table, using the structure found in the generated .xlsx file
In trello, when using the Plus! plugin, we often had issues with desynced spend/estimates, where the values shown were significantly different between users. This lead to a very large spike, as each of us kept readjusting the numbers because what they saw was wrong.

Note: The code smell evaluation reports for this project were added May 10th as we forgot to include them in the repository before the delivery date.
No cases of Type Checking or Feature Envy were found.
4 God Class instances were found by JDeodorant. 2 of them were part of the GUI and 1 of them is a unit test class. The final one is the class that manages our metric extracting Threads and writes the .xlsx file with the info they gather. As such, we decided it was fine to keep it larger than recommended.
Lots of Long Method instances were found. Almost every GUI and Unit test method was considered a long method. The methods used to extract metrics in most metric classes were also considered long methods. These perform a very specific function and as such we decided not to extract another method, as it would only make reading them harder, as there is not clear separation to be made.
The rule history management methods were considered long methods, but they are mostly composed of creating and closing File and Object streams, so no extraction was possible.
The Rule and Condition evaluation methods were also long methods, but the only suggested extraction was to remove a switch case block from the method, which we found incorrect.
The Metric handler class (that deals with coordenating the metric extracting threads and writing the gathered information in a file) had multiple instances of long_methods. These were often caused due to the necessity of dealing with edge cases when parsing .java files. Multiple methods were extracted during development, but the end results were still considered long methods. We decided not to extract any more methods, as all the logical separation points had been exhausted.