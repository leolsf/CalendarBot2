# CalendarBot2


## Background

Some new tool apps are creating more attractive and interesting user experience. For example, the app named Daodao Book-keeping for keeping accounts is a good case of chat bot. To be more specific, users can enter price with different tags like lunch, shirts and so on in the chatting interface, then the online chat bot replies text and pictures according to what users input, and the information is also kept in database and displayed on calendar.

At the same time, we found that creating events on calendar app or Post-it notes is tedious, most approach are one way and users always need to fill in blanks or forms mechanically. To make it more interesting, we decide to develop an Android app for users to record events and plans by communicating with a chat bot.


## Introduction

The App is mainly composed of the tasks calendar, edit interface and chat bot. 

The calendar fragment is to display the tasks recorded in the database. The calendar view will highlight the date of today and all the dates with tasks. When a date is selected, this date will be highlighted, and a popup window will show the list of tasks on this date. The task in the list only shows its time and content. If an item in the list is clicked, it will direct to the edit activity. In the editing page, user can modify all the information of a task and choose to either save the change or delete the record. 

For chat bot fragment, the layout includes chat interface and decoration items. The chat interface is composed of the message edit text and send button based on linear layout. To refresh message on screen frequently and immediately, we implement the widget of recycler view to form a basic layout of message list. Decoration items contains text view and image view in linear layout. We employ a pair of 9-patch bubble images for text background, and simulate the chat interface by setting message visible or not in terms of different types in the recycler view adapter.

After launching the app, the bot will send several initial messages to guide user how to create a new event or plan. User should input new task, location, date and time with the keyword respectively so that the bot can correctly distinguish information. For instance, if user enter task+presentation and click send button, then bot will receive it and ask the location, and user can continue to send location+zoom, date+2020,5,1 and time+15:30:00 step by step in the interaction with bot. When user finish sending four key information of new event, they are instantly stored in database and visible on calendar. If user input event information in incorrect format, bot will remind user and show an example.

## Demo
<div align=center>
<img src="https://github.com/leolsf/CalendarBot2/blob/master/public/image/Screenshot_1588774919.jpg" width=40% height=40%/>
<img src="https://github.com/leolsf/CalendarBot2/blob/master/public/image/Screenshot_1588776810.jpg" width=40% height=40%/>
<img src="https://github.com/leolsf/CalendarBot2/blob/master/public/image/Screenshot_1588776665.jpg" width=40% height=40%/>
<img src="https://github.com/leolsf/CalendarBot2/blob/master/public/image/Screenshot_1588776953.jpg" width=40% height=40%/>

