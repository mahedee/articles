# Integate Bot with Sequential Flow of Dialog

## Introduction

Without maintainig a sequential flow in chat bot, the conversation will not able to fullfil the goal of communication within clients. This article demonstrate how to ipmlement a sequntial conversation flow by using Dialog Components.

## Objectives

### In this article, you will learn how to

* Gather and save the bot states: conversation and user
* Upgrade simple bot into Dialog bot
* Maintain sequential flow of Dialogs

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

__Microsoft.Bot.Builder.Dialogs__ which have responsible for component Dialogs.

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
  * One Dialog for componet Dialog
  * two Bot State:
    * one for Conversation state, and
    * another Bot State For User state.
  
```C#
private readonly BotState _conversationState;
private readonly BotState _userState;
private readonly Dialog _Dialog;
```

* Initiate them in constructor of your bot activity class with other dependencies. [_Consider now we have to use the Language Understanding (LUIS) inside the component Dialog, So we can remove it's declaration form Activity Class_]

```C#
public HABotActivity(T Dialog, ILogger<HABotActivity<T>> logger, ConversationState conversationState, UserState userState)
{
    Logger = logger;
    _conversationState = conversationState;
    _userState = userState;
    _Dialog = Dialog;
}
```

* Override asynchronously another method named ```OnTurnAsync()``` from ```ActivityHandeler```, where you have to save the changes occurs in User State and Conversation State within your message activity.

```C#
public override async Task OnTurnAsync(ITurnContext turnContext, CancellationToken cancellationToken = default(CancellationToken))
{
    await base.OnTurnAsync(turnContext, cancellationToken);

    await _conversationState.SaveChangesAsync(turnContext, false, cancellationToken);
    await _userState.SaveChangesAsync(turnContext, false, cancellationToken);
}
```

* Within your overriden ```OnMessageActivityAsync()``` method modify the activity and await the RunAsync method of Dialog. Use Dialog state accessor with the name of Dialog state class. If required, Log your activity within your logger.

```C#
protected override async Task OnMessageActivityAsync(ITurnContext<IMessageActivity> turnContext, CancellationToken cancellationToken)
{
    Logger.LogInformation("Running Dialog with Message Activity.");

    await _Dialog.RunAsync(turnContext,
    _conversationState.CreateProperty<DialogState>(nameof(DialogState)),
    cancellationToken);
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

## Step7: Designing Dialog handles for the sequential flow of Dialog

* Go to constructor of your componet Dialog class which created in step 4. There are available various type of Dialog flow. The simplest flow is considered as WaterfallDialog. Here we are going to implement waterfall Dialog.
* Firstly create a list/array [Must be accessable with IEnumerable] of waterfall step

```C#
IList<WaterfallStep> waterfallSteps = new List<WaterfallStep>();
```

* Now we have to save the names for various type of Dialog which we have to use in prompt option for collectiong user message. Because we are using LUIS, so only text prompt is enough. But, for accessing flexibility of confirm prompt, choice prompt etc. we have to save these name. Before all, we save the name of Waterfall Dialog and after saving these names, we set initial Dialog id with name of waterfall to run the initial child Dialog.

```C#
AddDialog(new WaterfallDialog(nameof(WaterfallDialog), waterfallSteps));
AddDialog(new TextPrompt(nameof(TextPrompt)));
AddDialog(new DateTimePrompt(nameof(DateTimePrompt)));
AddDialog(new NumberPrompt<int>(nameof(NumberPrompt<int>)));
AddDialog(new ChoicePrompt(nameof(ChoicePrompt)));
AddDialog(new ConfirmPrompt(nameof(ConfirmPrompt)));
AddDialog(new AttachmentPrompt(nameof(AttachmentPrompt)));
InitialDialogId = nameof(WaterfallDialog);
```

## Step7: Designing Dialog activities for the sequential flow of Dialog

* Go to your componet Dialog class and create an asynchronous method who will be first element of the dialog. Because this activity will run when user send request for message on first time, so without any data driven operation, send prompt for user input.

```C#
private static async Task<DialogTurnResult> AppointStepAsync(WaterfallStepContext stepContext, CancellationToken cancellationToken)
{
    IList<Choice> choices = new List<Choice>
    {
        new Choice("Doctor Booking"),
        new Choice("Renewal Prescription"),
        new Choice("Nurse Booking")
    };
    return await stepContext.PromptAsync(nameof(TextPrompt),
        new PromptOptions
        {
            Prompt = (Activity) ChoiceFactory.ForChannel(stepContext.Context.Activity.ChannelId,choices,"Please tell me how can I help you today?","Please tell me how can I help you today?", default )
        }, cancellationToken);
}
```

* Within Data driven operation, Design other activities asynchronously. Here you can use LUIS to recognizig intents or expected entities from received data. Use ```stepContext.Context.SendActivity``` to send non prompt messages. Use ```stepContext.Value["keyName"]``` to store the step data.

```C#
var luisResult = await _luisRecognizer.RecognizeAsync<Luis.HABot>(stepContext.Context, cancellationToken);
if (luisResult?.Entities?.appointOptions?.FirstOrDefault() != null)
{
    await stepContext.Context.SendActivityAsync(MessageFactory.Text($"Definitely I can help you with that."), cancellationToken);

    stepContext.Values["Appoint"] = luisResult?.Entities?.appointOptions?.FirstOrDefault()?.FirstOrDefault();
}
```

* Here two activity factors are used widely. ```ChoiceFactory``` and ```MessageFactory```. Use Messsagefactory when you send only text send for text prompt.

```C#
return await stepContext.PromptAsync(nameof(TextPrompt), new PromptOptions
{
    Prompt = MessageFactory.Text("Can you just confirm your name?", inputHint: InputHints.ExpectingInput)
}, cancellationToken);
  ```

* Use ChoiceFactory when suggest user to selcet from a list for choice prompt. You have to use FoundChoice for retrieving data on the next step.

```C#
return await stepContext.PromptAsync(nameof(ChoicePrompt),
new PromptOptions
{
    Prompt = MessageFactory.Text("Please enter your mode of transport."),
    Choices = ChoiceFactory.ToChoices(new List<string> { "Car", "Bus", "Bicycle" }),
}, cancellationToken);
```

* To use ChoiceFactory as option of text prompt, such as accept both from your text and choice, Use ```ChoiceFactory.ForChannel``` and pass parameter as channel id from ```stepContext.Context.Activity.ChannelId```. You have to cast MessageActivity into Activity. For speaking features, use same text to perameter of speak. Also set prompt option to default.

```C#
return await stepContext.PromptAsync(nameof(TextPrompt),
new PromptOptions
{
    Prompt  = (Activity) ChoiceFactory.ForChannel(stepContext.Context.Activity.ChannelId,new List<Choice>
    {
        new Choice("Deen Mohammad"),
        new Choice("Ranajit Kumar"),
        new Choice("Bakhtiar Al Mamun"),
        new Choice("Sadia Suravi")
    }, $"Please select from the available doctors.",
    $"Please select from the available doctors.",default),
}, cancellationToken);
```

* Use confirm prompt where you have to use ony aggrement of YES or NO. Use Input Hints to control automatic enabling the microphone/text input cursor.

```C#
return await stepContext.PromptAsync(nameof(ConfirmPrompt), new PromptOptions
{
    Prompt = MessageFactory.Text("Did you take any pill?", inputHint: InputHints.ExpectingInput)
}, cancellationToken);
```

* Remember, for retrieving data of previous step's prompt in current step, Use appropriate csating such as, bool for Confirm prompt, string for Text prompt and FoundChoice For Choice prompt. Atherwise you have to thrown in exception of Activity Adapter.

* On the last step, aggregate all step data from the dictionaries ```stepContext.Value``` and set it to User Profile. Use your user profile accessor object to access user profile.

```C#
var userProfile = await _userProfileAccessor.GetAsync(stepContext.Context, () => new UserProfile(), cancellationToken);

if (userProfile != null)
{
    userProfile.Name = (string)stepContext.Values["Name"];
    userProfile.DateOfBirth = (string)stepContext.Values["DateOfBirth"];
}
```

* After all data driven operation, return await the end of current Dialog from the last step

```C#
return await stepContext.EndDialogAsync(cancellationToken: cancellationToken);
```

## Step8: Creating sequence of flow for Dialog

* Go to constructor of your componet Dialog class again. Now Add sequntially the dialog activities to the list of waterfall step that initiated in step 7 of this article.
* If requird to access step index in any activities, use ```stepContext.ActiveDialog.State["stepIndex"]```.

## Step9: Rebuilding project and run

* Now review your code and build the project again. For sample source code please go to [microsoft samples](https://github.com/microsoft/BotBuilder-Samples/tree/master/samples/csharp_dotnetcore/05.multi-turn-prompt) and browse dialogs directory.
* Test with your Bot Framework Emulator, enjoy.
