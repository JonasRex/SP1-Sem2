## Quick Start Project for the Chat - Server

Simple Maven Project which can be used for the Chat-CA 

Using this project as your start code will make deploying your server (the jar-file) to Digital Ocean a "no brainer" if you follow the instructions given here

https://docs.google.com/document/d/1aE1MlsTAAYksCPpI4YZu-I_uLYqZssoobsmA-GHmiHk/edit?usp=sharing 
# SP1-Sem2
#README-file, or in a document linked to from the readme-file):
#A short design description of the chosen design.
Vi fik til opgave at lave en chat-server. Vi har taget udgangspunkt i de sidste to ugers undervisning
Vi har bygget en echoserver som tager imod klienter fra en liste med specifikke brugernavne. 
En dispatcher fordeler beskederne mellem brugere ved hjælp af en blockingqueue. 
Derudover har vi bygget en 'serverhandler' som fungerer som en slags administrator på chat-serveren. 
Den kan ikke tilgås af brugere.
Vi har bygget en message klasse, som indeholder en modtager og en besked i form af en string. 
Dette har vi gjort for at gøre koden mere overskuelig.
Det lykkedes at deploye serveren så nu kan den tilgås fra flere forskellige computere. 