# How to Add Speech Service in Web ChatBot

## Introduction

Microsoft bot framework V4 offered you to use ASP .Net Core web app service for designing your bot. So the app service have to deploy in an hosting platform. To use azure services within the chat bot, you have to publish your app in azure service. By using azure direct line service for web chat, you can add speech service in your chat bot.

## Add Speech Service in Web ChatBot

There are two methods stands for deploying Bot Application in azure.

* First: create a web app bot directly from azure bot service and update / modify the application source code as you expected.
* Second: deploy your bot application as an web app service first, then register your web app within Azure Bot Service channel.

### In this article, you will learn how to

* Deploy A AI Bot to Azure App Service
* Enable Web Chat in a Chat bot for Client Side
* Enable speech service within web chat bot.

### Required Tools

Before proceeding with this article, please proceed our [previous article from here](https://github.com/mahedee/Articles/ai/blob/master/ai-ml.md) and complete the task for the implementation of a basic chat bot with LUIS and sequential flow of dialog.

* Visual Studio 2017 or later
* Microsoft Account with Subscription for [Azure Portal](https://portal.azure.com/)
* [Azure CLI](https://aka.ms/installazurecliwindows) or publishing tools.
* Speech recognition enabled Web Browser (preferred Google Chrome)

## Step1: Create a Web App Bot resource from Azure Bot Service

* First go to [azure portal](https://portal.azure.com) and login with your microsoft account.
* From the top left toggle, go to all service and find the service list AI + Machine Learning. Hit on Bot Service.

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT008.png "Bot Service Selector")

* Select __Web App Bot__ provided by microsoft and Hit Create (or Select Bot Channel Registration if previously you attached another web application as service and want to integrate it with Azure Bot Service).

* Fill the required information, give your Bot handle name, select your subscription. If you haven't any resource group, hit create new, otherwise select existing one. Select one suitable pricing tier available for your subscription. Give your application name.

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT009.png "Bot handle")

* Chose Echo Bot from the bot template.

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT010.png "Bot Template")

* Chose your app service plan/location or create a new one.

* In Microsoft App Id and Password Menu, Select Automatic Create App Id and Password, this is important. If you experienced, you can add a new app id by providing a GUID and Strong password manually.

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT011.png "Bot App id")

* If everything looks fine, Hit Create from the bottom left. Wait a few moments. A verification will running over your selection and you will be notified when your application get ready.

## Step2: Find/Download your source code of Bot Application

* Go to your Bot Service resource (named by your bot handle) from all resources. From the left side bar, go to __Build__ blade of Bot Management.

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT013.png "Project Selector")

* Now you find option to download your Bot Source Code. Click on Download Bot Source Code and allow to zip your source files including build tools.

* wait a few moments, save the zipped source files from your browser.

## Step3: Modify the application source code

* Unzip the zipped source code that you downloaded from azure Web App Bot Build blade.

* From the azure bot service provided source code, go to ```appsettings.json``` file. Ensure that here you have found the Microsoft App Id and Password that automatically created during deploying your bot service first. 

```json
"MicrosoftAppId": "8ec79ae5-1bd9-4c23-b02c-769b89fda978",
  "MicrosoftAppPassword": "$J{_yqZuoXhble)N]J=6OFJyp",
  "ScmType": "None",
  ```

* If you want to use this Bot service with custom source code, combine this App Id and Password to your targeted source code's ```appsettings.json``` file.
* If you haven't completed our previous articles and tasks, please recap it. The downloaded source code looks like step 4 of our first article [How to create AI Bot With LUIS and Microsoft Bot Framework](https://github.com/mahedee/Articles/blob/master/ai/how-to-AI-Bot-With-LUIS.md). So complete all next tasks on this application in visual studio.

* If you already completed our previous articles, please combine the tools provided with this source code to your completed Bot Application.

* Resolve your dependencies and service registration on ```startup.cs``` file. Remember by default the Activity handler named Echobot already registered in startup. So you have to change it if required. Don't change the project name even it remain ```Echobot.cproj```

## Step4: Build and Test the Application

* If all combination or modification have done, build your application again.
* Go to emulator and test it locally by providing your App Id and Password included in ```appsettings.json``` file.
* If test is succeed, clean your project in visual studio and build finally to deploy.

* Close your project from Visual Studio, It is important before deploying application using Azure CLI.
* Find ```.deployment``` file from the source code project directory and remove it.

## Step5: Re-Deploy Application to Azure Web App Sevice by using Azure CLI

* If you haven't install azure CLI please go to [Azure CLI](https://aka.ms/installazurecliwindows) and install it.
* Sign Up with your Microsoft account from your default browser.
* Go to powershell from your source code project directory and request to login into azure subscription

```ps1
az login
```

* Switch to your microsoft account from the opened login prompt in your default browser. Azure CLI uses this login to get your authentication and find your subscription.
* If login success, then back to powershell window. Wait a few moments, here Azure CLI load your subscription information's.
* Now use this script command to get your application ready to deploy.

```ps1
az bot prepare-deploy --lang Csharp --code-dir "." --proj-file-path "EchoBot.csproj";
```

* If succeed then it console to true and ```.deployment``` will be created inside the project directory.
* Now zip manually your application source code. remember you are in your project directory and save it by your application name.
* Use this script command to deploy your app in the azure web app service.

```ps1
az webapp deployment source config-zip --resource-group "YourResourceGroupName" --name "BotHandledApplicationName" --src "ApplicationName.zip";
```

* Wait a few minutes, If succeed then Azure CLI console gives the deployment information in json format.

## Step6: Connect your Web App Bot to Direct Line Channel

* Go to your azure portal from your browser and find your Web App Bot resource.
* Go to __Channels__ blade from the Bot Management.
* Here you find web chat running. Hit on Direct Line (It provides the accessibility and the base of some features such as cognitive features in web chat).
* If direct line channel connected, You can see the configuration wizard. Here you have find two secret keys. These keys are used to authenticate your Web Chat.

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT014.png "Direct Line")

* Copy these keys and store them secretly.
* You can also connect Cognitive Service Speech in channel. Within the time to writing this article, the speech to text service is deprecated in web chat.

## Step7: Create the Client UI for web chat

* Go to __Build__ blade from Bot Management. Click on Open online code editor link. It will open source code editor online for your application.

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT015.png "source code editor")

* Find static pages under __wwwroot__ directory. Create a new html page.

Load Sample codes that includes at least Reactive Extensions for BotChat and Cognitive Service provided by microsoft. You can get samples  [from here](https://github.com/microsoft/BotFramework-Samples/blob/master/docs-samples/web-chat-speech/index.html)

* From the BotChat.App, replace parameter for direct line secret with your direct line secret key that you get at step 6. Or you can use JWT authentication to get token through your secret key.

```js
BotChat.App({
        bot: bot,
        locale: params['locale'],
        resize: 'detect',
        // sendTyping: true,    // defaults to false. set to true to send 'typing' activities to bot (and other users) when user is typing
        speechOptions: speechOptions,
        user: user,

        directLine: {
          domain: params['domain'],
          secret: 'YourDirectLineSecretGoesHere',
          token: params['t'],
          webSocket: params['webSocket'] && params['webSocket'] === 'true' // defaults to true
        }
      }, document.getElementById('BotChatGoesHere'));
```

* Now run your application from online editor. Browse with HTTPS to your UI page from google chrome browser or chromium engine based browser who support BrowserSpeechRecognizer
  ```https://YourBotHandle.azurewebsites.net/YourUI.html```
* If everything is ok, your UI loads chat fields and show microphone tap to listen button. Now give permission to your browser and enjoy speech service with Web Chat Bot.

![alt text](https://github.com/mahedee/Articles/raw/master/img/BOT016.png "NuGet Package Selector 2")

### Note

On the [UI sample provided by Microsoft](https://github.com/microsoft/BotFramework-Samples/blob/master/docs-samples/web-chat-speech/index.html), there are more than 5 individual methods to use speech recognizer and synthesizer. Currently microsoft provided cognitive service for speech to text is deprecated in web chat, so we recommend to use BrowserSpeechRecognizer. You can test any other by providing speech api key. Keep track on updated release which will be resolved this issue.
