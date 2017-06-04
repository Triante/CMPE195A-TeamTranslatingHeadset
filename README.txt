||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
|||  Jorge Aguiniga, Jonathan Chen, Andrew Javier, Luis Otero  |||
|||                                                            |||
|||    Advisors: Badari (Ishie) Eswar, Mr. Chak Singamsetti    |||
||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
                                                                                                                             
 I. Table of Contents: 												           
 	A. Running the Code 													   
        1. The application												           
	   2. The settings                                                                                                         
 	B. Contents of Demo Video                                                                                                  
                                                                                                                               
                                                                                                                               
                                                                                                                               
 	A. Running the code:													   
																   
		1. The application												   
																   
		Before getting started, the code for the TranslaTa application requires use of an 
		Android phone 4.0.0 or higher with Bluetooth access if you are running the TranslaTa 
		application dependent on the Bluetooth devices. There are   
		two ways to install and run this application. They are explained below.						   
		 														   
		For running the TranslaTa version that does not depend on Bluetooth devices,
		go through method 1 steps 1-4, open the MainActivity class and uncomment the comments
		with "open this version to use without specific speaker and headset" and comment
		out the part with the comment that says "For use with specific	   
		speaker and headset. You can also follow method 2 but instead of downloading
		"TranslaTa.apk", download the "TranslaTa_v2.apk" file and follow the rest of the steps.							   
																   
		Method 1: Through Android Studio										   
																   
			1. On a web browser, get access to the code repository on Github with the 
			following link:		   
                													           
				https://github.com/Triante/CMPE195A-TeamTranslatingHeadset					   
		   														   
		   	You can also copy the following link to clone the project to your android 
			studio projects folder:	   
																   
				https://github.com/Triante/CMPE195A-TeamTranslatingHeadset.git					   
																   
			2. Clone the repository if you have not done so and open it on the 
			Android Studio IDE			   
				2.1 Add the file api_keys.xml to				   				   
					<TranslaTa project root folder>\app\src\main\res\values			   		   
			3. Once open, plug in the Android device you would like to use to run 
			   the application			   
			4. Make sure to have developer options activated on your phone.						   
			5. Click on the run button on Android Studio and select your mobile 
			   phone from the phone options	   
			6. Wait for the process to finish installing the apk on your device					   
			7. Once it is done, the application will automatically open on your mobile phone			   
			8. Before running the application, make sure to connect the RN52 headset 
			   to the call channel on your phone and the speakers to the media channel
			   on the Bluetooth options. Connect the speakers first and then the 
			   headset.
			9. If both devices are connected as specified in step 8, go to the 
			   application and click on the "Connect" button.							  
			10. Make sure that both the speaker and headset images are highlighted 
			    blue and the "Connect" button now says "Translate". If this is not the case,
			    one or both of the devices are not connected or they are    
			    connected to the wrong Bluetooth channel. Connect the devices to the
			    correct channels and retry step 9.	
			11. Click on the "Translate" button. The button should change to "Translating"
			    and an "Off" button should appear near the top left corner of the screen.
			12. Speak on the microphone and wait for the program to translate for you.
			    Speak closer to translate your speech. Talk further away to translate
			    what someone else would say to you
			13. To turn off the system, press the "Off" button. The headset and speaker
			    images should stop lighting up and the "Translating" button 
			    should say "Connect"		   
			14. If you wish to change the language of the user/party, press on the 
			    settings wheel			   
			15. The settings wheel will take you to the language settings as well as 
			    the amplitude threshold settings  
			16. If you want to learn how to change the settings, scroll down to the
			    settings portion of this readme    
																   
		Method 2: With the APK												   
																   
			1. On the code repository on Github based on the first link provided on
			   step 1 of method 1, you will see a file named "TranslaTa.apk".
			   Download the file and transfer it to your Android phone if you are not
			   already downloading it directly into your phone.							   
			2. Press on the "TranslaTa.apk" file on your phone and let it install.					   
			3. Once installed, open the application									   
			4. Follow steps 8 - 16 of method 1 to continue								   
																   
																   
		2. The settings													   
																   
			There are a couple of options in the settings. Language settings,
			amplitude threshold settings, and the profanity filter.								   													   
			Language Settings											   
				1. In the settings, there are two options: My Language and Party Language			   
				2. Press on the dropdowns for a list of languages						   
				3. The dropdown below the language dropdowns is for choosing the
				   voices available for each language
				4. Make sure to click on the save button at the bottom to save your
				   new language options	   
																   
			Amplitude Threshold Settings										   
				1. In the settings, press on "Amplitude Settings"						   
				2. The amplitude threshold is for the system to know who is speaking.
				   The louder you speak, the more likely the system will recognize 
				   that it is the user speaking. 				   
				3. The "Record Amplitude Threshold" option lets you record your voice
				   in order to determine the amplitude automatically based on the
				   strength of your voice on the recording			   
				4. The "Configure Amplitude Threshold" option lets you choose the
				   amplitude manually. Click on	the "Configure Amplitude Threshold" 
				   and move one of the sliders at the bottom of the screen	   
				5. The top slider is for setting the threshold and the bottom slider
				   is for setting the maximum possible strenth for the system to recognize.					   
																   
			Profanity Filter											   
				1. Currently, the applicatio nonly supports profanity filters for English. 			   
				2. In the settings, press on the profanity filter switch. When the circle
				   is on the left, the filter is off. Otherwise, the filter is on							   
																   
	B. Contents of Demo Video
	
		https://drive.google.com/open?id=0B0VRY9eFTJOgUkJFdGVEeE9fTmc
		
		The demo video demonstrates how TranslaTa is works. It includes an overview of the
		domain (oral communication), the problem (language barriers),
		an initial solution (interpreters), state of the art (The Pilot), a demo of our   
		solution, the components of the system, and the conclusion.							   
