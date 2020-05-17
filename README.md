# Firebase-operations

This is a Simple Firebase operations demo app. 

## About this app

Specialy it is made for uploading image in the firebase from my Android mobile storage. Inserting Name, Description and Image in Firebase. 
Images are uploaded in the Firebase Storage. Image storage url, name and decription are inserted in the Realtime Database.
# Basic Features:
-**Data Inserting** 
-**Data Deleting**
-**Data Reading**

Here I am Loading all the datas in the RecyclerView. Loading the images using Picasso. 
On click an item a menu will appear. User can choose  

**Show** for see the full image.  

**Delete** for delete the clicked item. 

**Copy** for copy the image url. User can get the image url and user can use image url to see the image in browser or others. 
 One Float button for uploading new Datas.

### Prerequisites

Android Studio.
Gmail account.

# Installing
## Firebase setup in the project.
1. Create a new project in the Android Studio.
2. Now open the android studio and click on Tools in the upper left corner.
3. Now click on the Firebase option in the drop down menu.
4. A menu will appear on the right side of screen. It will show various services that Firebase offers. choose Realtime Database or Storage. 
5. Now Click on the Connect to Firebase option in the menu of desired service.
6. Add the dependencies of your service by clicking on the Add FCM to the app option.
7. Now Log in to your gmail account and go to https://console.firebase.google.com/u/0/  .
8. Create a project by clicking on create project in the firebase console.
9. Fill the details in the pop up window about the project. Edit the project ID if required.
10. Click on the Add firebase to your android app option.
11. Enter the package name of the app in Android studio (Example: com.anwar.firebaseoperation). Also, the SHA1 certificate, can be given, of the app by following steps: (Right side of the screen Gradle project). Go to android studio project> gradle> root folder> Tasks>Android>signingReport>copy paste SHA1 from console.
12. Now download the google-services.json file and place it in the root directory of the android app.
13. Now add the sdk in the PROJECT-LEVEL build.gradle 

buildscript { 
dependencies { 
	classpath 'com.google.gms:google-services:4.3.3' 
} 
} 
14. Add the following code to APP-LEVEL build.gradle of the app.

dependencies { 
  implementation 'com.google.firebase:firebase-database:11.8.0'
  implementation 'com.google.firebase:firebase-storage:11.8.0'
} 
... 
// Add to the bottom of the file 
apply plugin: 'com.google.gms.google-services' 

15. Now Sync the gradle by clicking on sync now and run the app to send the verification to the Firebase console.


# Deployment

*ViewActivity - This activity is for show the Datas in recyclerview from Firebase.

*UploadActivity- This activity is to upload and insert the Datas in the Firebase.

*DetailsActivity- This activity is to show the image in full screen. It will Receive data from ViewActivity via intent. Also it will Set my Recived datas to imageviews and load it by picasso.

*Image_model- It is responsible for managing the data of the application. It will manage name, description ,imageURl and key datas.

*RecyclerAdapter- It is to handle the data collection and bind it to the view.



## Built With

* Firebase
* Picasso


## Version Support:

minSdkVersion 23

targetSdkVersion 29






## Reference

* https://www.geeksforgeeks.org/adding-firebase-to-android-app/
* https://developer.android.com/guide/topics/ui/layout/recyclerview