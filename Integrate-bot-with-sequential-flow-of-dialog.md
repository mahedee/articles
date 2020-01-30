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

## Step2: Installing Bot Builder Dialog from NuGet

* Go to the Solution Explorer of Visual Studio. Right click on your project and hit Manage NuGet Packeges. From the NuGet Package Manager of your project, install the following packages:

__Microsoft.Bot.Builder.Dialogs__ which have responsible for component dialogs.

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT007.png "NuGet Packege Selector V2")

## Step3: Creating Models for User Profile and Conversation Data

* Go to your Models directory. Create a new model class for accessing conversation data from the conversation state.

```C#
public class ConversationData
{
    public string Timestamp { get; set; }
    public string ChannelId { get; set; }
    public bool PromptedUserForName { get; set; } = false;
}
```

* For User Profile, you can use any the existing entity model
[_If you followed our previous article, you have already created a model for Bot details_] or create new one for this. Consider, at least there have one property with identifier 'Name'. Add other additional properties with this.

```C#
public class UserProfile
{
    public long Id { get; set; } = DateTime.Now.Ticks;
    public string Name { get; set; }
    public string DateOfBirth { get; set; }
}
```

## Step4: Creating class for Component Dialog

* Inside your project directory, create another sub directory named __Dialogs__. Create a new Class for Dialog who inherits ```ComponentDialog``` of Bot Builder Dialog.

* Declare an object of State property accessor for accessing user profile. You have to pass the User Profile model creted in previous step for this.
* If you use the language understunding (LUIS), declare another object for Recognizer.
* Initaite them with constructor for this Dialog class.

```C#
public class SequenceDialog : ComponentDialog
{
    private readonly IStatePropertyAccessor<UserProfile> _userProfileAccessor;

    private readonly HARecognizer _luisRecognizer;
    public SequenceDialog(UserState userState, HARecognizer luisRecognizer)
        : base(nameof(SequenceDialog))
    {
        _userProfileAccessor = userState.CreateProperty<UserProfile>("UserProfile");
        _luisRecognizer = luisRecognizer;
    }
}
```

## Step5: Accessing Bot States dependency in Bot Activity

* Go to your C sharp bot file (generally it stands on project folder and named with your [ProjectName].cs).

[_Because you use ASP .NET Core as the tergeted framework of your application, you can transfer it now inside a sub directory named __Bots__. Please consider, if you transfer, please fix it's namespace as well as inside pre processing of the startup file. If you not interested, it's ok until you create another bot file._]

* Change the class declaration of your bot activity class. Use generic type, where type is Dialog

```C#
public class HABotActivity<T> : ActivityHandler
where T: Dialog
{
    //Class  Components
}

```

* Declare more following objects inside your Bot activity class:
  * One Dialog for componet dialog
  * two Bot State:
    * one for Conversation state, and
    * another Bot State For User state.
  
```C#
private readonly BotState _conversationState;
private readonly BotState _userState;
private readonly Dialog _dialog;
```

* Initiate them in constructor of your bot activity class with other dependencies. [_Consider now we have to use the Language Understanding (LUIS) inside the component dialog, So we can remove it's declaration form Activity Class_]

```C#
public HABotActivity(T dialog, ILogger<HABotActivity<T>> logger, ConversationState conversationState, UserState userState)
{
    Logger = logger;
    _conversationState = conversationState;
    _userState = userState;
    _dialog = dialog;
}
```

## Step6: Registering Dialog and the Bot States using memory storage

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

* Modify and resolve the registered dependency for your Bot Activity. Also you have to add another dependency for your Dialog class

```C#
services.AddSingleton<SequenceDialog>();
services.AddTransient<IBot, HABotActivity<SequenceDialog>>();
```
