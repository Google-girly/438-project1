# "Are You a Fan?" Lyric Trivia Android App
This is a **Kotlin-based** Android app that is meant as a lyric trivia game, built using **Jetpack Compose**. 
Once users log in, they can choose between selecting or randomly generating an artist, and then test their music knowledge by choosing the song name based on a lyric snippet of their chosen artist.

The app dynamically fetches artists and related data (song names, lyrics, etc) using external APIs and provides an interactive UI built entirely through in Compose (no added .xml files). 

## Features
**Authentication**
Login screen is entry point, and users need to log in before accessing the game. 

**Modern UI**
Custom dropdown menu for artist names, scrollable dynamic artist lit
Responsive button states
Replayability functionality, or navigation back to home
Displays performance result screen (Success or Failure feedback)

**API Integration**
**iTunes Search API**
Dynamically fetches artists by genre (Pop, Hip-Hop, Rock).
Retrieves song metadata.

**Lyrics.ovh API**
Fetches real song lyrics for gameplay.
Internet permission enabled for API calls.



## Installation
Clone the repo: git clone https://github.com/Google-girly/438-project1.git
Open the project in Android Studio
Sync Gradle and run the app, either on Android Emulator or Physical Android Device


## Gameplay
Once the app is launched, follow these steps to play:

1. Login, either through an existing account or create a new one
2. Select an Artist, from dynamically loaded artist list, or using "**Randomly Generate Artist**" button. This will enable you to then select "Start Quiz"
3. Complete song choices. There are 10 Rounds.
4. After completing the quiz, users are taken to a **Results Screen** where fan status is determined. Performance status determined by: 
    6 or more correct answers → “Congrats, you are a fan!!!”
    Fewer than 6 correct answers → “Sorry, you are NOT a fan!”
Users can then choose to **Play Again** or **Go Home**. Playing again restarts the quiz for the same artist, and go home returns to the artist selection screen. 

   

   
