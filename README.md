# AndroidBeer2

Application based on RxJava2 and Retrofit2 frameworks to control asynchronous threads with callbacks properly.
When we work with IO or Internet connection - RxJava2 is absolutely necessary nowadays.

Used MVP pattern to separate model, code of presenter and UI view.
Logic split to Application, Activity and Fragment to make source code readable and stable
Jackson library has been selected for fast JSON parse.
I have used modern Google Room SQL database to store data.
SQL database is also helpful in OFFLINE mode.
Also I make some unit tests according to specification.

I have tested this Android application on 5 different smartphones and 2 tablets to avoid any possible crashes.

Implemented: sorting, favorite features, undo delete and full details with complex UI.

P.S. ImageView controll has been replaced to ImageButton to achieve more clickable area by 'fat fingers'.
Also I have added some transitions between fragment and some animation for icons button to make application more "live" behaviour...
