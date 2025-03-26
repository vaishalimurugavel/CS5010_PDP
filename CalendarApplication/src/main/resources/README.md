### A. Instructions to run the program:

Command line argument:
1) java ControllerApp --mode interactive
2) java CalendarApp --mode headless \<Full absolute File path\>

Using Intellij:
1) Extract the contents of the code in the src/main/java folder of an Intellij project.
2) Go to Run/Debug configuration, click Edit configurations
3) Add New configurations -> Application
4) Enter any name, select the main class to be CalendarApp.
5) enter one of the two program arguments.
       i) --mode interactive
       ii) --mode headless \<Full absolute File path\> (Sample files given "valid_commands.txt" and "invalid_commands.txt")

i) For interactive, give commands line-by-line. To end the program enter "exit"
ii) the program will automatically terminate.

### B. Features:

##Calendar Management
1) Create group:
   i)create calendar --name <calName> --timezone area/location
   Example: create calendar --name WorkCalendar --timezone America/New_York

2) Edit group:
   i) edit calendar --name <name-of-calendar> --property <property-name> <new-property-value>
   Example: edit calendar --name WorkCalendar --property timezone America/Los_Angeles

3) Set group:
   i) use calendar --name <name-of-calendar>
   Example: use calendar --name WorkCalendar
   NOTE: Any calendar events can be after setting a calendar

4) Copy events between groups:
   i)copy event <eventName> on <dateStringTtimeString> --target <calendarName> to <dateStringTtimeString>
   ii)copy events on <dateString> --target <calendarName> to <dateString> - Not Working
   iii) copy events between <dateString> and <dateString> --target <calendarName> to <dateString> - Not working

##Event Management
1) Create: 
   All supported Commands for create:
   i) create event --autoDecline \<eventName\> from \<dateStringTtimeString\> to \<dateStringTtimeString\>
   ii) create event --autoDecline \<eventName\> from \<dateStringTtimeString\> to \<dateStringTtimeString\> repeats \<weekdays\> until \<dateStringTtimeString\>
   iii) create event --autoDecline \<eventName\> from \<dateStringTtimeString\> to \<dateStringTtimeString\> repeats \<weekdays\> for \<N\> times
   iv)create event --autoDecline \<eventName\> from \<dateStringTtimeString\> to \<dateStringTtimeString\> repeats \<weekdays\> until \<dateStringTtimeString\>
   v) create event --autoDecline \<eventName\> on \<dateStringTtimeString\>
   vi) create event \<eventName\> on \<dateString\> repeats \<weekdays\> for \<N\> times
   vii) create event \<eventName\> on \<dateString\> repeats \<weekdays\> until \<dateString\>
   To add location, description and is public or private:
   Append the structure to the end of the above commands:
   " location \<locationString\> description \<descriptionStrin\> (private|public)
   Example: create event --autoDecline Event1 from 2025-03-10 10:00 to 2025-03-11 10:00 location Shillman hall description Event1 at Shillman Hall private

2) Edit:
   All supported commands for edit:
   i) edit event \<property\> \<eventName\> from \<dateStringTtimeString\> to \<dateStringTtimeString\> with \<NewPropertyValue\>
   ii) edit events \<property\> \<eventName\> from \<dateStringTtimeString\> with \<NewPropertyValue\>
   iii) edit events \<property\> \<eventName\> \<NewPropertyValue\>
   Properties that can be edited are subject, location, start_datetime, end_datetime, description

3) Print:
   All supported commands for print:
   i) print events on \<dateString\>
   ii) print events from \<dateStringTtimeString\> to \<dateStringTtimeString\>

4) Show:
   All supported commands for show:
   show status on \<dateStringTtimeString\>

5) Export:
   All supported commands for export:
   export cal \<FileName\>.csv

NOTE: \<dateStringTtimeString\> is of the format: yyyy-MM-ddTHH:mm
\<dateString\> is of the format: yyyy-MM-dd

### C. Team Members contribution:
Vaishali - Event addition and editing module. Designed the overall design of the application. Redesigned the entire application
Aishwarya - Printing event, showing and export, Testing
