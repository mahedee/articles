# Integate Bot with Sequential Flow of Dialog

## Introduction

Without maintainig a sequential flow in chat bot, the conversation will not able to fullfil the goal of communication within clients. This article demonstrate how to ipmlement a sequntial conversation flow by using Dialog Components.

## Objectives

### In this article, you will learn how to

* Gather and save the bot states: conversation and user
* Upgrade simple bot into dialog bot
* Maintain sequential flow of dialogs

### Required Tools

Before proceeding with this article, please procced our [previous article from here](https://github.com/mahedee/Articles/blob/master/how-to-AI-Bot-With-LUIS.md) and complete the task for the implemention of a basic chat bot with LUIS.

* Visual Studio 2017 or later
* [Bot Framework Emulator](https://github.com/Microsoft/BotFramework-Emulator/releases/tag/v4.7.0)

## Step1: Creeating a Basic Bot with LUIS

* First go to our [previous article from here](https://github.com/mahedee/Articles/blob/master/how-to-AI-Bot-With-LUIS.md) and install the requred tools.
* From your Visual Studio, create a simple Bot from the template.
* Integrate it with LUIS and complete the required tasks described on the article.
* Test it on Emulator and ensure about sucessesful implementation.

## Step2: Registering Bot States using memory storage

* Open your project on Visual Studio and go to your startup file.
* Add an instance variable of Memory Storage.

```C#
var storage = new MemoryStorage();
```

* Create the User state and Conversation state passing in the storage layer.

```C#
var userState = new UserState(storage);
var conversationState = new ConversationState(storage);
```

* Register both in singleton for the dependency injection.

```C#
services.AddSingleton(userState);
services.AddSingleton(conversationState);
```

## Step3: Accessing Bot States dependency in Bot Activity

* Go to your C sharp bot file (generally it stands on project folder and named with your [ProjectName].cs).

[_Because you use ASP .NET Core as the tergeted framework of your application, you can transfer it now inside a directory named __Bots__. Please consider, if you transfer, please fix it's namespace as well as inside pre processing of the startup file. If you not interested, it's ok until you create another bot file._]

* Declare more two Bot State objects inside your Bot activity class:
  * one for Conversation state, and
  * another For User state.
* Initiate them in constructor of your bot activity class with other dependencies.

* Reply the message with something, now bot have no longer to response. But you still get a post 200 status response in activity logger of your emulator. That means the request was accepted.

## Step4: Installing Bot Builder Dialog from NuGet

Go to the Solution Explorer of Visual Studio. Right click on your project and hit Manage NuGet Packeges. From the NuGet Package Manager of your project, install the following packages:

__Microsoft.Bot.Builder.Dialogs__ which have dependency on LUIS Runtime.
 
![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT007.png "NuGet Packege Selector V2")
