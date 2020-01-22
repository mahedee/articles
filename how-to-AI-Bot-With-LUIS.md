# How to Create AI Bot with LUIS

## Introduction:
Every rated institutes or organization expect to serve instant customer greetings and basic interaction. As a virtual help assistant, a Chatbot might help on the spot of instant service. This article demonstrate how to create a simple Bot using Microsoft Bot Framework and LUIS which is stands for machine learning-based service to build natural language into apps, bots, and IoT devices.

## Creating Application with LUIS from scratch:
Currently Microsoft release a several packages that helps to build bots with various features. If you need more details please visit their [Bot Framework SDK](https://github.com/Microsoft/botbuilder-dotnet/) release notes and documentation on GitHub.
### In this article, you will learn how to:
*	Create a basic Chatbot 
*	Integrate Bot with LUIS
*	Run and interact with it

### Required Tools:
*	Visual Studio 2017 or later
*	[Bot Framework Emulator](https://github.com/Microsoft/BotFramework-Emulator/releases/tag/v4.7.0)
*	[LUISGen](https://github.com/microsoft/botbuilder-tools/blob/master/packages/LUISGen/src/npm/readme.md) in .NET, the Cognitive Model Generator  for LUIS 
## Step1: Installing Bot Framework SDK
* First go to Visual Studio marketplace and download: [Bot Framework v4 SDK Templates for Visual Studio](https://marketplace.visualstudio.com/items?itemName=BotBuilder.botbuilderv4) and install the templates on your visual Studio.
## Step2: Creating Project on Visual Studio
* Open your Visual Studio and choose create a new project, select C# as your language, all platforms and AI Bot as your project type. [Or search for Bot and then] select Empty Bot and hit next.

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT001.png "Project Selector")

* Give your project name and complete the dialog box. It initiate a project of ASP .Net Core application with minimal codes and dependency with Bot Builder Integration.
* Build the project and run it. for verifying this all works fine. Generally all bot applications run on port 3978 on the local machine. 
## Step3: Testing with Emulator
* If you haven't installed Bot Framework Emulator, [download it from here](https://github.com/Microsoft/BotFramework-Emulator/releases/tag/v4.7.0) and install. Go to your Bot Framework Emulator and open bot from: http://localhost:3978/api/messages 
If everything is ready, then bot will response with Hello World!
* Reply the message with something, now bot have no longer to response. But you still get a post 200 status response in activity logger of your emulator. That means the request was accepted. 
## Step4: Enabling Response on Message
* To get the response, back to Visual Studio and open your C sharp bot file. On empty project, generally it stands on project folder and named with your [ProjectName].cs, open it and find your class named ```EmptyBot``` who inherits bot activity Handler, ```ActivityHandler```. Here you find the overridden method of OnMembersAddedAsync who responsible for your Welcome message on first request. 
* If required, use refatoring tool to rename the class ```EmptyBot``` with desired bot name. Note: _if you follow this procedure, you have to resolve the dependancy registering service for your Bot inside your_ ```Startup.cs``` _file._
* On this bot activity class, override another method from ```ActivityHandler``` named ```OnMessageActivityAsync``` with samples goes here: 
 ```
protected override async Task OnMessageActivityAsync(ITurnContext<IMessageActivity> turnContext, CancellationToken cancellationToken)
{
    var replyText = $"Echo: {turnContext.Activity.Text}";
    await turnContext.SendActivityAsync(MessageFactory.Text(replyText, replyText), cancellationToken);
}
 ```
* This message activity turns your Emptybot into Echobot. Rebuild it and run. Hit something from emulator and Enjoy.
## Step5: Creating LUIS App and Exporting App in JSON
* Now we have to initiate LUIS. To Start with LUIS, please go to [LUIS.ai](https://www.luis.ai/), Sign Up with your Microsoft account. If you have any Azure subscription, use it, otherwise start for 3 month free trials. 
* Go to [LUIS Application menu](https://www.luis.ai/applications) and create new app or import your app in JSON Configuration format. 
* Create your entities, normalize with their synonyms. Then model your intents and configure them with Utterances. 
* Within your expected training data set, train your App and publish it for production.
* Get your Application Id from __Application Manage Tab -> Settings -> Application Information__
* Get your API Key from __Application Manage Tab -> Settings -> Azure Resources -> Primary key__
* Get your Application Host Name from __Application Manage Tab -> Settings -> Azure Resources -> Endpoint Url__ {only between __https://__ and __/LUIS__, i.e.: _westus.api.cognitive.microsoft.com_}
* To retrieve your application configuration again go to __Application Manage Tab -> Settings -> Versions__ and check your required version of your app, then hit export.

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT002.png "Version Selector for LUIS")

* Copy the response data and Store it in a new JSON Configuration File.
## Step6: Generating Cognitive Model
* Copy the JSON Configuration file and store it inside your Visual Studio project file.  Hence the main application stands for ASP .NET Core, I recommend about storing this file in a sub directory [named CognitiveModels/LUISModels] of main project. 
* Run DOT NET CLI on this directory and hit 

```LUISGen .\ [nameofapp].json -cs LUIS.[namewanted_as_cs] –o```

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT003.png "LUISGen in .NET CLI")

 
* For further documentation, please review [LUISGen Documentation](https://github.com/microsoft/botbuilder-tools/blob/master/packages/LUISGen/src/npm/readme.md). Now the cognitive model is ready for deploy. 
* Open Solution Explorer for your project in Visual Studio and check the new generated C# Cognitive Model. If you faced any error inside the C# model file, Use refactoring/rename it by following correct convention and replace all of it's refarances.
## Step7: Installing NuGet Packages
* Go to the Solution Explorer of Visual Studio. Right click on your project and hit Manage NuGet Packeges. From the NuGet Package Manager of your project, install these packages:

   	__Microsoft.Bot.Builder.AI.LUIS__ which have dependency on LUIS Runtime.
 
![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT004.png "NuGet Packege Selector 1")

* Hence we create project from Bot Framework Sdk template, so it already installed the Integration for ASP .Net Core. 
* If you need any of Data Type Recognizer within cognitive model, Please install them. 
For example: I have installed __Microsoft.Recognizers.Text.DataTypes.TimexExpression__ 

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT005.png "NuGet Packege Selector 2")
 

* Now verify your package references for confirming all required package are installed successfully.

## Step8: Configuring LUIS API
* From the solution explorer, Go to your appsettings.json file and include configuration for your LUIS app: 
 your __Application Id__, __API Key__ and __Application Host Name__ that you retrieved in Step5. Samples Goes here, Oviously you have to replace the AppId, APIKey and HostName.

 ```
{
  "MicrosoftAppId": "",
  "MicrosoftAppPassword": "",
  "LuisAppId": "555630a2-f219-4d9b-be45-a330f398955a",
  "LuisAPIKey": "dd1b7ab89b6e417d90080a07ce332073",
  "LuisAPIHostName": "westus.api.cognitive.microsoft.com"
}
 ```

## Step9: Designing Recognizer for LUIS Model
* Design a recognizer class for your LUIS model that inherits Microsoft.Bot.Builder.IRecognizer, Here you have to implements two missing members 
```Task<T> RecognizeAsync<T>()``` and 
```Task<RecognizerResult> RecognizeAsync()```. 
* Before implementing these missing members, create a readonly object for ```LuisRecognizer```.

```
private readonly LuisRecognizer _recognizer;
```

* Before using the LUISRecognizer object, create a constructor for your recognizer class with parameter of IConfiguration. Here you have to check LUIS configurations what you configure in appsettings.json. Load the configurations and initiate a LUIS application from it. 
* By this LUIS application inside your constructor, create a LUIS recognizer option and set prediction options with instance. Currently V3 prefer to V4. 
* With this LUIS recognizer option, initiate the readonly object of ```LuisRecognizer```.
 
```
public HealthAssistantRecognizer(IConfiguration configuration)
{
    var luisIsConfigured = !string.IsNullOrEmpty(configuration["LuisAppId"]) && !string.IsNullOrEmpty(configuration["LuisAPIKey"]) && !string.IsNullOrEmpty(configuration["LuisAPIHostName"]);
    if (luisIsConfigured)
    {
        var luisApplication = new LuisApplication(
            configuration["LuisAppId"],
            configuration["LuisAPIKey"],
            "https://" + configuration["LuisAPIHostName"]);
        var recognizerOptions = new LuisRecognizerOptionsV3(luisApplication)
        {
            PredictionOptions = new Microsoft.Bot.Builder.AI.LuisV3.LuisPredictionOptions
            {
                IncludeInstanceData = true,
            }
        };

        _recognizer = new LuisRecognizer(recognizerOptions);
    }

}
```

* Implement these missing members 
```Task<T> RecognizeAsync<T>()``` and 
```Task<RecognizerResult> RecognizeAsync()``` from the readonly object of ```LuisRecognizer``` asynchronously.
 
```
public async Task<RecognizerResult> RecognizeAsync(ITurnContext turnContext, CancellationToken cancellationToken)
    => await _recognizer.RecognizeAsync(turnContext, cancellationToken);

public async Task<T> RecognizeAsync<T>(ITurnContext turnContext, CancellationToken cancellationToken)
    where T : IRecognizerConvert, new()
    => await _recognizer.RecognizeAsync<T>(turnContext, cancellationToken);
```

* To confirm about successful initialization of LUISRecognizer object, refer a public Boolean value or a method for check, which can be used in another instances. 
```
public virtual bool IsConfigured => _recognizer != null;
```
## Step10: Registering Service for Recognizer Class Dependency
* Go to your __Startup__ file, and resolve the dependency injection in services. 
* Register your Recognizer class with a Singleton service. 
 
 ```
 public void ConfigureServices(IServiceCollection services)
{
    services.AddMvc().SetCompatibilityVersion(CompatibilityVersion.Version_2_1);
    services.AddSingleton<IBotFrameworkHttpAdapter, AdapterWithErrorHandler>();
    services.AddSingleton<HealthAssistantRecognizer>();
    services.AddTransient<IBot, HealthAssistantBot>();
}
 ```

## Step11: Creating Models for Entities
* If you haven't any sub directory for Models, create a sub directory named ```Models``` on your project.
* On your project's Models directory, let’s design your models for entities. These are used to fetch data from the cognitive model. For Example:

```
public class HealthAssistantDetails
{
    public string Age { get; set; }
    public string Drug { get; set; }
}
``` 

## Step12: Creating Constructor for Bot Activity with Dependency Injection
* Go to your C sharp bot file (generally it stands on project folder and named with your [ProjectName].cs) and. Declare following objects inside your Bot activity class: 
    * one of your recognizer, and 
    * another ILogger. 
* Initiate them with the constructor of your bot activity class.
```
private readonly HealthAssistantRecognizer _luisRecognizer;
protected readonly ILogger Logger;

public HealthAssistantBot(HealthAssistantRecognizer luisRecognizer, ILogger<HealthAssistantBot> logger)
{
    _luisRecognizer = luisRecognizer;
    Logger = logger;
}
```
 
## Step13: Modifying Bot Activity with Recognizer
* Inside your ```OnMessageActivityAsync``` of bot activity class, use the recognizer to recognize data from LUIS through the cognitive model genarated by LUISGen on the last of Step6. Store result inside a local variable.

```
var luisResult = await _luisRecognizer.RecognizeAsync<HealthAssistant>(turnContext, cancellationToken);
```

* Within the top intent of stored result, map values with your cognitive model. 

```
if (luisResult.TopIntent().intent == HealthAssistant.Intent.DiscoverDrugs)
{
    var healthAssistantDetails = new HealthAssistantDetails
    {
        Drug = luisResult.Entities?.Drugs?.FirstOrDefault()?.FirstOrDefault(),
        Age = luisResult.Entities?.age?.FirstOrDefault()?.Number.ToString()
    };

    //Do your Job From Here
}
```

* From the result, use your logic to present data. If required further processing more, do this asyncronusly. Or simply Re initiate your bot state.
For example: 

```
switch (luisResult.TopIntent().intent)
{
    case HealthAssistant.Intent.DiscoverDrugs:

        var healthAssistantDetails = new HealthAssistantDetails
        {
            Drug = luisResult.Entities?.Drugs?.FirstOrDefault()?.FirstOrDefault(),
            Age = luisResult.Entities?.age?.FirstOrDefault()?.Number.ToString()
        }; 
                    
        var ageRange = Convert.ToInt32(healthAssistantDetails.Age) >= 18 ? "Adult" : "Child";
        var messageText = $"I suggest you to take a dosage of {healthAssistantDetails.Drug} for {ageRange}";
        var message = MessageFactory.Text(messageText, messageText, InputHints.IgnoringInput);
        await turnContext.SendActivityAsync(message, cancellationToken);
                
        var promptMessage = "What else can I do for you?";
        var nextMsg = MessageFactory.Text(promptMessage, promptMessage, InputHints.AcceptingInput);
        await turnContext.SendActivityAsync(nextMsg, cancellationToken);
        break;

    default:
        var didntUnderstandMessageText = $"Sorry, I didn't get that. Please try asking in a different way " +
                                            $"(intent was {luisResult.TopIntent().intent})";
        var didntUnderstandMessage = MessageFactory.Text(didntUnderstandMessageText,
            didntUnderstandMessageText, InputHints.IgnoringInput);
        await turnContext.SendActivityAsync(didntUnderstandMessage, cancellationToken);
        break;
}
```

* From your ```OnMessageActivityAsync``` remove reply component and Use logger if required anythoing to acknowledgement.

## Step14: Re-Building Project and Testing on Emulator
* Now rebuild your project and run. 
* Test with your Bot Framework Emulator, enjoy.

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT006.png "Bot Framework Emulator")
