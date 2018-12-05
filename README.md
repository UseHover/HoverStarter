# HoverStarter
HoverStarter is a very simple Android app built with the Hover SDK. Use it as a starting point for your own code, or to troubleshoot setup and installation issues.

## Prerequisites
 - A Hover account
 - Basic familiarity with GitHub and Android Studio

You can [sign up](https://www.usehover.com/signup/) for a Hover account for free if you don't already have one.

## How to use HoverStarter

### 1) Configure a simple action
From your Hover dashboard, [configure a simple test action](https://www.usehover.com/docs/actions). Check balance or check own phone number often work well. Save your action and note the action ID for use in step 3.

### 2) Create a new Android Studio project.
Clone this repository and import it into a new Android Studio project. In the app level build.gradle, change the `applicationId` from `com.usehover.hoverstarter` to a name of your choice, eg. `com.yourname.hoverstarter`.

Note: This will allow you to build the starter app quickly, but if you want to use this repository as the basis for a real app you should [refactor to change the package name](https://stackoverflow.com/questions/16804093/rename-package-in-android-studio).

### 3) Create an app
From your Hover dashboard, create an app and give it a name of your choice, eg. "Hover Starter". Use the `applicationId` from step 2 as the package name. Save the app and note the API token.

In AndroidManifest.xml, replace `"YOUR_API_KEY"` with the token you just created. Then open MainActivity.java and add your action ID from step 1 to the `onClickListener` for `action_button`.

### 4) Build and run
Build and run the app. Tap the "REQUEST PERMISSIONS" button first and follow the prompts. Finally, tap the "RUN ACTION" button to run the test action.
